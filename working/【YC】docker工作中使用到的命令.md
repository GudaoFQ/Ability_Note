## 记录工作中能使用到，但是又不好记的docker命令

### 在不进入docker容器的情况下，对docker容器中的文件进行删除
```shell
# docker exec -it 容器名 rm -rf 需要删除的文件或文件夹的路径（容器中的）
docker exec -it sca rm -r data/code/2022-11-14/
```

### shell脚本中判断某个镜像是否存在
```shell
if [[ "$(docker images -q [镜像名]:[镜像tag] 2> /dev/null)" != "" ]]; then
  xxx
fi
```
* /dev/null是一个特殊的设备文件，这个文件接收到任何数据都会被丢弃；因此，null这个设备通常也被称为位桶（bit bucket）或黑洞
* 2> /dev/null的意思就是将标准错误删掉
* 0：标准输入（stdin）
* 1：标准输出（stdout）
* 2：标准错误（stderr）

### shell脚本中判断某个容器是否存在
```shell
if [[ "$(docker inspect [容器名] 2> /dev/null | grep '"Name": "/[容器名]"')" != "" ]]; then
  xxx
fi
```
* /dev/null是一个特殊的设备文件，这个文件接收到任何数据都会被丢弃；因此，null这个设备通常也被称为位桶（bit bucket）或黑洞
* 2> /dev/null的意思就是将标准错误删掉

### run命令使用说明
```shell
# 公司使用到的mysql启动
docker run -d \
--restart=always \
--network=host \
--name=mysql_deploy \
-v $work_path/mysql/my.cnf:/etc/mysql/my.cnf \
-v $work_path/mysql/mysql/:/var/lib/mysql \
-e TZ=Asia/Shanghai \
mysql_deploy
```
* -d：后台启动，如果写`-d`就会将容器的日志打印到控制台，通过`ctrl -z`退出日志打印
* --restart：设置docker重启后，此容器自动重启
* --name：容器名称
* -v：容器与服务器的映射关系
* -e：容器中变量设置
* 上面命令的最后一行是镜像名称
* --network：设置成`host`后，-p命令将不生效，可以通过`docker network ls`查看docker的网络

### docker通过镜像名称删除所有与此镜像有关的容器
```shell
# 通过镜像名称停止所有的容器
docker stop $(docker ps -q --filter ancestor=镜像名称)
# 通过镜像名称删除所有的容器（此处一定要写`-aq`，不然所有关闭的镜像都不会被查出来）
docker rm $(docker ps -aq --filter ancestor=镜像名称)
```
* -a：列出所有容器，包括停止运行的；如果没有这个选项，则默认只列出在运行的容器
* -q：列出容器的数字ID，而不是容器的所有信息

### `docker exec -it`报错`the input device is not a TTY`
```shell
# 去掉`-it`中的`t`即可
docker exec -i
```
* -t：是指分配一个伪终端；如果让脚本在后台运行，就没有可交互的终端