## 在`--privileged`启动的CentOS 7容器中报错`Failed to get D-Bus connection: Operation not permitted`
> 大部分原因是使用特权启动容器后，还是使用的`/bin/bash`，所以就出现了上面的问题

### 说明
* docker version: Docker version 1.13.1, build 7d71120/1.13.1
* docker centos image: 
  * name/tag: docker.io/centos:7 
  * id:eeb6ee3f44bd

### 启动命令
```shell
docker run -d --privileged --name test centos:7 /bin/bash -c "while true;do echo hello docker;sleep 1;done"
```

### 解决方法一
> 修改启动命令将`/bin/bash`修改为`/usr/sbin/init`或者`/sbin/init`
```shell
docker run -d --privileged --name test centos:7 /usr/sbin/init -c "while true;do echo hello docker;sleep 1;done"
```

### 解决方法二
> 更换systemctl文件

#### 备份systemctl文件
```shell
mv /usr/bin/systemctl /usr/bin/systemctl.old
```
#### 获取新文件
```shell
curl https://raw.githubusercontent.com/gdraheim/docker-systemctl-replacement/master/files/docker/systemctl.py > /usr/bin/systemctl

## systemctl内容，可以将此文件上传，然后改名成systemctl替换
systemctl-bak
```
#### 设置可执行
```shell
chmod +x /usr/bin/systemctl
```
