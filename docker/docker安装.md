## Docker安装-ContOS-8

### 官网rpm包下载安装

#### 进入官方网址下载对应ContOS版本的Docker安装包
> `https://download.docker.com/linux/centos/` 并选择您的CentOS版本。然后浏览`x86_64/stable/Packages/` 并下载`.rpm`要安装的Docker版本的文件。

#### 查看对应的ContOS版本信息
```shell
cat /etc/redhat-release
```

#### 下载对应的安装包
```shell
wget https://download.docker.com/linux/centos/8/x86_64/stable/Packages/docker-ce-19.03.15-3.el8.x86_64.rpm
```

#### 安装Docker容器
```shell
# 安装之前需要转到下载的rpm包的目录下面，然后安装
yum install docker-ce-19.03.15-3.el8.x86_64.rpm
```
![安装](../resource/docker/docker-安装.png)

#### 启动容器
```shell
sudo systemctl start docker
```

#### 测试容器是否能正常运行
```shell
udo docker run hello-world
```
![运行测试](../resource/docker/docker-运行测试.jpg)
