## 节点介绍

### 知识点：

1. 节点类型
2. 节点的监听(watch)
3. 节点属性说明(stat)
4. 权限设置(acl)

zookeeper中节点叫znode存储结构上跟文件系统类似，以树级结构进行存储。不同之处在于znode没有目录的概念，不能执行类似cd之类的命令。znode结构包含如下：

* **path**:唯一路径 
* **childNode**：子节点
* **stat**:状态属性
* **type**:节点类型

### 节点类型

| 类型                  | 描述                           |
| :-------------------- | :----------------------------- |
| PERSISTENT            | 持久节点                       |
| PERSISTENT_SEQUENTIAL | 持久序号节点                   |
| EPHEMERAL             | 临时节点(不可在拥有子节点)     |
| EPHEMERAL_SEQUENTIAL  | 临时序号节点(不可在拥有子节点) |

#### PERSISTENT（持久节点）

持久化保存的节点，也是默认创建的

```
#默认创建的就是持久节点
create /test
```

#### PERSISTENT_SEQUENTIAL(持久序号节点)

创建时zookeeper 会在路径上加上序号作为后缀，。非常适合用于分布式锁、分布式选举等场景。创建时添加 -s 参数即可。

```
#创建序号节点
create -s /test
#返回创建的实际路径
Created /test0000000001
create -s /test
#返回创建的实际路径2
Created /test0000000002
```

#### EPHEMERAL（临时节点）

临时节点会在客户端会话断开后自动删除。适用于心跳，服务发现等场景。创建时添加参数-e 即可。

```
#创建临时节点， 断开会话 在连接将会自动删除
create -e /temp
```

#### EPHEMERAL_SEQUENTIAL（临时序号节点）

与持久序号节点类似，不同之处在于EPHEMERAL_SEQUENTIAL是临时的会在会话断开后删除。创建时添加 -e -s 

```
create -e -s /gudao/test
```

### 节点属性

```
# 查看节点属性
stat /gudao
```

#### 其属性说明如下表：

```
#创建节点的事物ID
cZxid = 0x385
#创建时间
ctime = Tue Sep 24 17:26:28 CST 2019
#修改节点的事务ID
mZxid = 0x385
#最后修改时间
mtime = Tue Sep 24 17:26:28 CST 2019
#子节点变更的事务ID【子节点的数据修改，改值不会变更】
pZxid = 0x385
#这表示对此znode的子节点进行的更改次数（不包括子节点）
cversion = 0
#数据版本，变更次数【节点中数据修改时会变更】
dataVersion = 0
#权限版本，变更次数【acess control list 只有当权限变更时才会变更】
aclVersion = 0
#临时节点所属会话ID
ephemeralOwner = 0x0【0x0表示当前属性为空，则这个节点就不是临时节点】
#数据长度
dataLength = 17
#子节点数(不包括子子节点)
numChildren = 0
```

### 节点的监听：

客户添加 -w 参数可实时监听节点与子节点的变化，并且实时收到通知。非常适用保障分布式情况下的数据一至性。其使用方式如下：

| 命令                 | 描述                                 |
| :------------------- | :----------------------------------- |
| ls -w path           | 监听子节点的变化（增，删）           |
| get -w path          | 监听节点数据的变化                   |
| stat -w path         | 监听节点属性的变化                   |
| printwatches on\off | 触发监听后，是否打印监听事件(默认on) |


### acl权限设置

ACL全称为Access Control List（访问控制列表），用于控制资源的访问权限。ZooKeeper使用ACL来控制对其znode的防问。基于scheme:id:permission的方式进行权限控制。scheme表示授权模式、id模式对应值、permission即具体的增删改权限位。

**scheme:认证模型**

| 方案   | 描述                                                         |
| :----- | :----------------------------------------------------------- |
| world  | 开放模式，world表示全世界都可以访问（这是默认设置）          |
| ip     | ip模式，限定客户端IP防问                                     |
| auth   | 用户密码认证模式，只有在会话中添加了认证才可以防问           |
| digest | 与auth类似，区别在于auth用明文密码，而digest 用sha-1+base64加密后的密码。在实际使用中digest 更常见。 |

**permission权限位**

| 权限位 | 权限   | 描述                             |
| :----- | :----- | :------------------------------- |
| c      | CREATE | 可以创建子节点                   |
| d      | DELETE | 可以删除子节点（仅下一级节点）   |
| r      | READ   | 可以读取节点数据及显示子节点列表 |
| w      | WRITE  | 可以设置节点数据                 |
| a      | ADMIN  | 可以设置节点访问控制列表权限     |

**acl 相关命令：**

| 命令    | 使用方式                | 描述         |
| :------ | :---------------------- | :----------- |
| getAcl  | getAcl <path>           | 读取ACL权限  |
| setAcl  | setAcl <path> <acl>     | 设置ACL权限  |
| addauth | addauth <scheme> <auth> | 添加认证用户 |

**world权限示例**
**语法**： <br>
`setAcl <path> world:anyone:<权限位>`<br>
注：world模式中anyone是唯一的值,表示所有人

#### 查看默认节点权限：

```
#创建一个节点
create -e /testAcl
#查看节点权限
getAcl /testAcl
#返回的默认权限表示 ，所有人拥有所有权限。
'world,'anyone: cdrwa
```

#### 修改默认权限为：读写

```
#设置为rw权限 
setAcl /testAcl world:anyone:rw
# 可以正常读
get /testAcl
# 无法正常创建子节点
create -e /testAcl/t "hi"
# 返回没有权限的异常
Authentication is not valid : /testAcl/t
```

**IP权限示例：**
语法： 

1. `setAcl <path> ip:<ip地址|地址段>:<权限位>`

**auth模式示例:**
语法： 

1. `setAcl <path> auth:<用户名>:<密码>:<权限位>`
2. `addauth digest <用户名>:<密码>`

**digest 权限示例：**
语法： 

1. `setAcl <path> digest :<用户名>:<密钥>:<权限位>`
2. `addauth digest <用户名>:<密码>`

注1：密钥 通过sha1与base64组合加密码生成，可通过以下命令生成

```
echo -n <用户名>:<密码> | openssl dgst -binary -sha1 | openssl base64
```

注2：为节点设置digest 权限后，访问前必须执行addauth，当前会话才可以访问。

1. 设置digest 权限

```
#先 sha1 加密，然后base64加密
echo -n gudao:123456 | openssl dgst -binary -sha1 | openssl base64
#返回密钥
2Rz3ZtRZEs5RILjmwuXW/wT13Tk=
#设置digest权限
setAcl /gudao digest:gudao:2Rz3ZtRZEs5RILjmwuXW/wT13Tk=:cdrw
```

1. 查看节点将显示没有权限

```
#查看节点
get /gudao
#显示没有权限访问
org.apache.zookeeper.KeeperException$NoAuthException: KeeperErrorCode = NoAuth for /gudao
```

1. 给当前会话添加认证后在次查看

```
#给当前会话添加权限帐户
addauth digest gudao:123456
#在次查看
get /gudao
#获得返回结果
gudao test
```

#### ACL的特殊说明：
权限仅对当前节点有效，不会让子节点继承。如限制了IP防问A节点，但不妨碍该IP防问A的子节点 /A/B。