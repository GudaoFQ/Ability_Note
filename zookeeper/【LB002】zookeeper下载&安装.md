## 下载&安装

zookeeper 基于JAVA开发，下载后只要有对应JVM环境即可运行。其默认的`端口号：2181`运行前得保证其不冲突。

### 版本说明：

2019年5月20日发行的3.5.5是3.5分支的第一个稳定版本。此版本被认为是3.4稳定分支的后续版本，可以用于生产。基于3.4它包含以下新功能

* 动态重新配置
* 本地会议
* 新节点类型：容器，TTL
* 原子广播协议的SSL支持
* 删除观察者的能力
* 多线程提交处理器
* 升级到Netty 4.1
* Maven构建

另请注意：建议的最低JDK版本为1.8

### 下载地址：

```shell
#普通下载地址
https://zookeeper.apache.org/releases.html#download
#zk归档地址
http://archive.apache.org/dist/zookeeper/
#文件说明：
apache-zookeeper-xxx-tar.gz 代表源代码
apache-zookeeper-xxx-bin.tar.gz 运行版本【包含windows与linux】
```

#### 具体部署流程：

```shell
#下载【下载其它版本只需要修改版本号即可】
wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/current/apache-zookeeper-3.5.5-bin.tar.gz

#解压
#当前文件夹下解压
tar -zxvf apache-zookeeper-3.5.5-bin.tar.gz
#将tgz文件解压到指定目录
tar -zxvf apache-zookeeper-3.5.5-bin.tar.gz -C 指定目录

#拷贝默认配置
cd  {zookeeper_home}/conf 
cp zoo_sample.cfg zoo.cfg

#启动【可以切换到bin目录下再执行】
#直接启动【默认执行配置文件zoo.cfg】
./{zookeeper_home}/bin/zkServer.sh start
#指定启动执行的配置文件
./{zookeeper_home}/bin/zkServer.sh start ./conf/zoo_sample.cfg start
#查看是否启动成功
./zkServer.sh status
```

