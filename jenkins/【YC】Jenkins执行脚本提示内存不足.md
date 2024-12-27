# Jenkins执行 java -jar 命令提示系统无法分配足够的文件描述符

## 说明
* 服务器版本：统信（Union OS Server 20）
* Jenkins版本：2.346.3-2-lts
* JDK：1.8.0_301

## 问题
```shell
library initialization failed - unable to allocate file descriptor table - out of memory./remotely.sh: line 12:  1151 Aborted 
(core dumped) $(pwd)/java/jre1.8.0_301/bin/java -jar remotely-1.0.0.jar --xxx=$1 --xxx=$2
```

### 临时增加限制
```shell
# 进入jenkins容器
docker exec -it jenkins bash
# 临时增加限制（比如设置为65535）
ulimit -n 65535

# （待验证）永久修改，编辑 /etc/security/limits.conf 添加：
* soft nofile 65535
* hard nofile 65535
jenkins soft nofile 65535
jenkins hard nofile 65535
```
* 因为项目中使用 remotely.sh 脚本，将 ulimit -n 65535 放在报错行即：$(pwd)/java/jre1.8.0_301/bin/java -jar remotely-1.0.0.jar --xxx=$1 --xxx=$2 上一行即可解决