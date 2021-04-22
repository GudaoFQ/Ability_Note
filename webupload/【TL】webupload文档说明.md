## WebUpload文档说明
[官网](https://fex.baidu.com/webuploader)

### 接口说明  
> Web Uploader的所有代码都在一个内部闭包中，对外暴露了唯一的一个变量WebUploader，所以完全不用担心此框架会与其他框架冲突。

#### 内部所有的类和功能都暴露在WebUploader名字空间下面。
> Demo中使用的是WebUploader.create方法来初始化的，实际上可直接访问WebUploader.Uploader。
```js
var uploader = new WebUploader.Uploader({
    swf: 'path_of_swf/Uploader.swf'

    // 其他配置项
});
```

### 事件
> Uploader实例具有Backbone同样的事件API：on，off，once，trigger。
```js
uploader.on( 'fileQueued', function( file ) {
  // do some things.
});
```
* 除了通过on绑定事件外，Uploader实例还有一个更便捷的添加事件方式。
```js
uploader.onFileQueued = function( file ) {
  // do some things.
};
```
> 如同Document Element中的onEvent一样，他的执行比on添加的handler的要晚。如果那些handler里面，有一个return false了，此onEvent里面是不会执行到的。

### Hook
> Uploader里面的功能被拆分成了好几个widget，由command机制来通信合作。
* 如下，filepicker在用户选择文件后，直接把结果request出去，然后负责队列的queue widget，监听命令，根据配置项中的accept来决定是否加入队列。
```js
// in file picker
picker.on( 'select', function( files ) {
    me.owner.request( 'add-file', [ files ]);
});

// in queue picker
Uploader.register({
    'add-file': 'addFiles'

    // xxxx
}, {

    addFiles: function( files ) {

        // 遍历files中的文件, 过滤掉不满足规则的。
    }
});
```
* Uploader.regeister方法用来说明，该widget要响应哪些命令，并指定由什么方法来响应。上面的例子，当add-file命令派送时，内部的addFiles成员方法将被执行到，同一个命令，可以指定多次handler, 各个handler会按添加顺序依次执行，且后续的handler，不能被前面的handler截断。
* handler里面可以是同步过程，也可以是异步过程。是异步过程时，只需要返回一个promise对象即可。存在异步可能的request调用者会等待此过程结束后才继续。举个例子，webuploader运行在flash模式下时，需要等待flash加载完毕后才能算ready了，此过程为一个异步过程，目前的做法是如下：
```js
// uploader在初始化的时候
me.request( 'init', opts, function() {
    me.state = 'ready';
    me.trigger('ready');
});

// filepicker `widget`中的初始化过程。
Uploader.register({
    'init': 'init'
}, {
    init: function( opts ) {

        var deferred = Base.Deferred();

        // 加载flash
        // 当flash ready执行deferred.resolve方法。

        return deferred.promise();
    }
});
```
#### 目前webuploader内部有很多种command，在此列出比较重要的几个。
| 名称               | 参数                        | 说明                                                         |
| :----------------- | :-------------------------- | :----------------------------------------------------------- |
| `add-file`         | files: File对象或者File数组 | 用来向队列中添加文件。                                       |
| `before-send-file` | file: File对象              | 在文件发送之前request，此时还没有分片（如果配置了分片的话），可以用来做文件整体md5验证。 |
| `before-send`      | block: 分片对象             | 在分片发送之前request，可以用来做分片验证，如果此分片已经上传成功了，可返回一个rejected promise来跳过此分片上传 |
| `after-send-file`  | file: File对象              | 在所有分片都上传完毕后，且没有错误后request，用来做分片验证，此时如果promise被reject，当前文件上传会触发错误。 |

#### 文件组织
> webuploader由很多独立的小文件组成。每个文件都是以AMD规范组织的，方便类似与RequireJS之类的库直接使用。
* 如lib/file.js
```js
/**
 * @fileOverview File
 */
define([
    '../base',
    './blob'
], function( Base, Blob ) {

    var uid = 0,
        rExt = /\.([^.]+)$/;

    function File( ruid, file ) {
        var ext;

        Blob.apply( this, arguments );
        this.name = file.name || ('untitled' + uid++);

        if ( !this.type ) {
            ext = rExt.exec( file.name ) ? RegExp.$1.toLowerCase() : '';
            if ( ~'jpg,jpeg,png,gif,bmp'.indexOf( ext ) ) {
                this.type = 'image/' + ext;
            }
        }

        this.ext = ext;
        this.lastModifiedDate = file.lastModifiedDate ||
                (new Date()).toLocaleString();
    }

    return Base.inherits( Blob, File );
});
```

### 目录结构及说明
```js
├── base.js   实现一些常用的帮助类方法，如inherits, log等等。
├── file.js    文件类，Queue中存放的数据类。
├── jq-bridge.js    jQuery的替代品，只实现webuploader所需的，当然，如果已经有jQuery了，此文件不用打包。
├── lib
│   ├── blob.js  带ruid（为了兼容flash抽象出来的，ruid为运行时id）的Blob类
│   ├── dnd.js    文件拖拽
│   ├── file.js   带ruid的文件类，blob的子类。
│   ├── filepaste.js  负责图片粘贴。
│   ├── filepicker.js    文件选择器
│   ├── image.js    图片处理类，生成缩略图和图片压缩。
│   └── transport.js    文件传送。
├── mediator.js   Event类
├── promise.js    同jq-bridge, 在没有jQuery的时候才需要。用来实现Deferred。
├── queue.js    队列
├── runtime
│   ├── client.js   连接器
│   ├── compbase.js    component的基类。
│   ├── flash
│   │   ├── xxx lib中flash的具体实现。
│   ├── html5
│   │   ├── xxx lib中html5的具体实现。
│   └── runtime.js
├── uploader.js    Uploader类。
└── widgets
    ├── filednd.js   文件拖拽应用在Uploader
    ├── filepaste.js   图片粘贴应用在Uploader
    ├── filepicker.js   文件上传应用在Uploader中。
    ├── image.js     图片文件在对应的时机做图片压缩和预览
    ├── queue.js     队列管理
    ├── runtime.js    添加runtime信息给Uploader
    ├── upload.js      负责具体上传逻辑
    ├── validator.js    各种验证器
    └── widget.js    实现command机制
```