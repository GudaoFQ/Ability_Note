## Linux环境下卸载RabbitMQ
> RabbitMQ运行在erlang的环境中，所以需要卸载erlang

### 卸载RabbitMQ相关
```shell
# 停止rabiitmq服务[停止不成工就进入安装目录下，再执行下面的命令，或kill进程]
service rabbitmq-server stop
# 查看rabbitmq相关列表
yum list | grep rabbitmq
# 卸载rabbitmq相关安装内容
yum -y remove rabbitmq-server.noarch
```

### 卸载erlang
```shell
# 查看erlang安装相关列表
yum list | grep erlang
# 卸载erlang已安装的相关内容
yum -y remove erlang-*
yum remove erlang.x86_64
```
