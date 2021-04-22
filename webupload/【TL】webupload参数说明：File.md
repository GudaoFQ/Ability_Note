## WebUpload参数配置说明
[官网](https://fex.baidu.com/webuploader)

### File
  * new File( source ) ⇒ File
* 参数:
  * source {Lib.File}lib.File
      > 实例, 此source对象是带有Runtime信息的。
> 构造函数

#### 事件说明
| 事件名         | 参数说明 | 描述         |
| :------------- | :------- | :----------- |
| `statuschange` |          | 文件状态变化 |

#### name
> 文件名，包括扩展名（后缀）

#### size
> 文件体积（字节）

#### type
> 文件MIMETYPE类型，与文件类型的对应关系请参考http://t.cn/z8ZnFny

#### lastModifiedDate
> 文件最后修改日期

#### id
> 文件ID，每个对象具有唯一ID，与文件名无关

#### ext
> 文件扩展名，通过文件名获取，例如test.png的扩展名为png

#### statusText
> 状态文字说明。在不同的status语境下有不同的用途。

#### setStatus
  * setStatus( status[, statusText] );
* 参数:
  * status {File.Status, String}
      > 文件状态值
  * statusText {String} [可选] [默认值: ''] 
      > 状态说明，常在error时使用，用http, abort,server等来标记是由于什么原因导致文件错误。
> 设置状态，状态变化时会触发change事件。

#### File.Status
> 文件状态值，具体包括以下几种类型：
```js
inited 初始状态
queued 已经进入队列, 等待上传
progress 上传中
complete 上传完成。
error 上传出错，可重试
interrupt 上传中断，可续传。
invalid 文件不合格，不能重试上传。会自动从队列中移除。
cancelled 文件被移除。
```