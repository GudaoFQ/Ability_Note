## WebUpload参数配置说明
[官网](https://fex.baidu.com/webuploader)

### Base
> 基础类，提供一些简单常用的方法

#### create
  * Base.create( opts ) ⇒ Uploader
> 创建Uploader实例，等同于new Uploader( opts );

#### version
> 当前版本号。

#### $
> 引用依赖的jQuery或者Zepto对象。[【TL】webupload方法说明.md中不引用jquery控制台报错的原因]

#### browser
> 简单的浏览器检查结果。
* webkit 
    > webkit版本号，如果浏览器为非webkit内核，此属性为undefined。
* chrome 
    > chrome浏览器版本号，如果浏览器为chrome，此属性为undefined。
* ie 
    > ie浏览器版本号，如果浏览器为非ie，此属性为undefined。暂不支持ie10+
* firefox 
    > firefox浏览器版本号，如果浏览器为非firefox，此属性为undefined。
* safari 
    > safari浏览器版本号，如果浏览器为非safari，此属性为undefined。
* opera 
    > opera浏览器版本号，如果浏览器为非opera，此属性为undefined。

#### os
> 操作系统检查结果。
* android 
    > 如果在android浏览器环境下，此值为对应的android版本号，否则为undefined。
* ios 
    > 如果在ios浏览器环境下，此值为对应的ios版本号，否则为undefined。

#### inherits
  * Base.inherits( super ) ⇒ child
  * Base.inherits( super, protos ) ⇒ child
  * Base.inherits( super, protos, statics ) ⇒ child
* 参数:
  * super {Class}
      > 父类
  * protos {Object, Function} [可选]
      > 子类或者对象。如果对象中包含constructor，子类将是用此属性值。
  * constructor {Function} [可选]
      > 子类构造器，不指定的话将创建个临时的直接执行父类构造器的方法。
  * statics {Object} [可选]
      > 静态属性或方法。
* 返回值:
  * {Class}
      > 返回子类。
* 实现类与类之间的继承。
  ```js
  function Person() {
    console.log( 'Super' );
  }
  Person.prototype.hello = function() {
    console.log( 'hello' );
  };
  
  var Manager = Base.inherits( Person, {
      world: function() {
      console.log( 'World' );
    }
  });
  
  // 因为没有指定构造器，父类的构造器将会执行。
  var instance = new Manager();    // => Super
  
  // 继承子父类的方法
  instance.hello();    // => hello
  instance.world();    // => World
  
  // 子类的__super__属性指向父类
  console.log( Manager.__super__ === Person );    // => true
  ```

#### noop
> 一个不做任何事情的方法。可以用来赋值给默认的callback.

#### bindFn
  * Base.bindFn( fn, context ) ⇒ Function
> 返回一个新的方法，此方法将已指定的context来执行。
  ```js
  var doSomething = function() {
      console.log( this.name );
  },
  obj = {
      name: 'Object Name'
  },
  aliasFn = Base.bind( doSomething, obj );
  
  aliasFn();    // => Object Name
  ```

#### log
  * Base.log( args... ) ⇒ undefined
> 引用Console.log如果存在的话，否则引用一个空函数noop。

#### slice
  * Base.slice( target, start[, end] ) ⇒ Array
> 被uncurrythis的数组slice方法。 将用来将非数组对象转化成数组对象。
  ```js
  function doSomthing() {
    var args = Base.slice( arguments, 1 );
    console.log( args );
  }
  
  doSomthing( 'ignored', 'arg2', 'arg3' );    // => Array ["arg2", "arg3"]
  ```

#### guid
  * Base.guid() ⇒ String
  * Base.guid( prefx ) ⇒ String
> 生成唯一的ID

#### formatSize
  * Base.formatSize( size ) ⇒ String
  * Base.formatSize( size, pointLength ) ⇒ String
  * Base.formatSize( size, pointLength, units ) ⇒ String
* 参数:
  * size {Number}文件大小
  * pointLength {Number} [可选] [默认值: 2] 精确到的小数点数。
  * [units=[ {Array}'B', 'K', 'M', 'G', 'TB' ]] 单位数组。从字节，到千字节，一直往上指定。如果单位数组里面只指定了到了K(千字节)，同时文件大小大于M, 此方法的输出将还是显示成多少K.
* 格式化文件大小, 输出成带单位的字符串
```js
console.log( Base.formatSize( 100 ) );    // => 100B
console.log( Base.formatSize( 1024 ) );    // => 1.00K
console.log( Base.formatSize( 1024, 0 ) );    // => 1K
console.log( Base.formatSize( 1024 * 1024 ) );    // => 1.00M
console.log( Base.formatSize( 1024 * 1024 * 1024 ) );    // => 1.00G
console.log( Base.formatSize( 1024 * 1024 * 1024, 0, ['B', 'KB', 'MB'] ) );    // => 1024MB
```

#### Deferred
  * Base.Deferred() ⇒ Deferred
> 创建一个Deferred对象。 详细的Deferred用法说明，请参照jQuery的API文档。
* Deferred对象在钩子回掉函数中经常要用到，用来处理需要等待的异步操作。
```js
// 在文件开始发送前做些异步操作。
// WebUploader会等待此异步操作完成后，开始发送文件。
Uploader.register({
'before-send-file': 'doSomthingAsync'
}, {

    doSomthingAsync: function() {
        var deferred = Base.Deferred();

        // 模拟一次异步操作。
        setTimeout(deferred.resolve, 2000);

        return deferred.promise();
    }
});
```

#### isPromise
  * Base.isPromise( anything ) ⇒ Boolean
* 参数:
  * anything {*}检测对象。
* 返回值:
  * {Boolean}
* 判断传入的参数是否为一个promise对象。
```js
console.log( Base.isPromise() );    // => false
console.log( Base.isPromise({ key: '123' }) );    // => false
console.log( Base.isPromise( Base.Deferred().promise() ) );    // => true

// Deferred也是一个Promise
console.log( Base.isPromise( Base.Deferred() ) );    // => true
```

#### when
  * Base.when( promise1[, promise2[, promise3...]] ) ⇒ Promise 
> 返回一个promise，此promise在所有传入的promise都完成了后完成。 详细请查看这里。
