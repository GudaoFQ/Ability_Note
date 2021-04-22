## WebUpload参数配置说明
[官网](https://fex.baidu.com/webuploader)

### Queue
> 文件队列, 用来存储各个状态中的文件。

#### stats
* 统计文件数。
  * numOfQueue 队列中的文件数。
  * numOfSuccess 上传成功的文件数
  * numOfCancel 被取消的文件数
  * numOfProgress 正在上传中的文件数
  * numOfUploadFailed 上传错误的文件数。
  * numOfInvalid 无效的文件数。
  * numofDeleted 被移除的文件数。

#### append
* 参数:
  * file {File}
      > 文件对象
> 将新文件加入对队列尾部

#### repend
* 参数:
  * file {File}
      > 文件对象
> 将新文件加入对队列头部

#### getFile
* 参数:
  * fileId {String}
      > 文件ID
* 返回值:
  * {File}
> 获取文件对象

#### fetch
  * fetch( status ) ⇒ File
* 参数:
  * status {String}
      > 文件状态值
* 返回值:
  * {File}File
> 从队列中取出一个指定状态的文件。

#### sort
  * sort( fn ) ⇒ undefined
* 参数:
  * fn {Function}
    > 排序方法
> 对队列进行排序，能够控制文件上传顺序。

#### getFiles
  * getFiles( [status1[, status2 ...]] ) ⇒ Array
* 参数:
  * status {String} [可选]
      > 文件状态值
> 获取指定类型的文件列表, 列表中每一个成员为File对象。

#### removeFile
  * removeFile( file ) ⇒ Array
* 参数:
  * {File}
      > 文件对象。
> 在队列中删除文件。