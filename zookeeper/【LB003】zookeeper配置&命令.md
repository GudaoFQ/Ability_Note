## 配置&命令

### 常规配置文件说明：

```shell
# zookeeper时间配置中的基本单位 (毫秒)
tickTime=2000
# 允许follower初始化连接到leader最大时长，它表示tickTime时间倍数 即:initLimit*tickTime
initLimit=10
# 允许follower与leader数据同步最大时长,它表示tickTime时间倍数 
syncLimit=5
#zookeper 数据存储目录
dataDir=/tmp/zookeeper
#对客户端提供的端口号
clientPort=2181
#单个客户端与zookeeper最大并发连接数
maxClientCnxns=60
# 保存的数据快照数量，之外的将会被清除
autopurge.snapRetainCount=3
#自动触发清除任务时间间隔，小时为单位。默认为0，表示不自动清除。
autopurge.purgeInterval=1
```

### 客户端命令：

#### 基本命令列表

**close**<br>
 关闭当前会话<br>
**connect host:port**<br>
重新连接指定Zookeeper服务<br>
**create [-s] [-e] [-c] [-t ttl] path [data] [acl]**<br>
创建节点<br>
**delete [-v version] path**<br>
删除节点，(不能存在子节点）<br>
**deleteall path**<br>
 删除路径及所有子节点<br>
**setquota -n|-b val path**<br>
设置节点限额 -n 子节点数 -b 字节数<br>
 **listquota path**<br>
查看节点限额<br>
**delquota [-n|-b] path**<br>
删除节点限额<br>
**get [-s] [-w] path**<br>
查看节点数据 -s 包含节点状态 -w 添加监听 <br>
getAcl [-s] path<br>
**ls [-s] [-w] [-R] path**<br>
列出子节点 -s状态 -R 递归查看所有子节点 -w 添加监听<br>
**printwatches on|off**<br>
是否打印监听事件<br>
**quit**<br>
退出客户端<br>
 **history**<br>
查看执行的历史记录<br>
**redo cmdno**<br>
重复 执行命令，history 中命令编号确定<br>
removewatches path [-c|-d|-a] [-l]<br>
**删除指定监听**<br>
set [-s] [-v version] path data<br>
**设置值**<br>
**setAcl [-s] [-v version] [-R] path acl**<br>
为节点设置ACL权限<br>
**stat [-w] path**<br>
查看节点状态 -w 添加监听<br>
**sync path**<br>
强制同步节点<br>

**node数据的增删改查**

```shell
# 列出子节点 
ls /
#创建节点
create /luban "luban is good man"
# 查看节点
get /luban
# 创建子节点 
create /luban/sex "man"
# 删除节点
delete /luban/sex
# 删除所有节点 包括子节点
deleteall /luban
```

