## Docker安装-CentOS-8
[Docker官方安装地址](https://docs.docker.com/engine/install/centos/)

### 官网rpm包下载安装（国内目前无法访问官网，推荐使用下面配置阿里Yum源安装）

#### 进入官方网址下载对应CentOS版本的Docker安装包
> `https://download.docker.com/linux/centos/` 并选择您的CentOS版本。然后浏览`x86_64/stable/Packages/` 并下载`.rpm`要安装的Docker版本的文件。

#### 查看对应的CentOS版本信息
```shell
cat /etc/redhat-release
```

#### 卸载旧版本
```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
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
sudo docker run hello-world
```
![运行测试](../resource/docker/docker-运行测试.jpg)

### 使用Yum存储库安装（国内推荐使用）

#### 卸载旧版本
```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

#### 安装必要的依赖（lvm2和device-mapper-persistent-data安装失败可以不装）
```shell
sudo yum install -y yum-utils \
    device-mapper-persistent-data \
    lvm2
```

#### 设置docker仓库（官方）或者阿里的镜像源
```shell
# 官方镜像源（国内无法使用）
sudo yum-config-manager \
      --add-repo \
      https://download.docker.com/linux/centos/docker-ce.repo
      
# 阿里镜像（国内使用）
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

#### 配置阿里云加速器
![阿里云容器](../resource/docker/docker-阿里云容器.png)
![阿里云容器加速](../resource/docker/docker-阿里云容器加速.png)

#### 安装docker
```shell
sudo yum install -y docker-ce docker-ce-cli containerd.io
```

#### 启动docker
```shell
sudo systemctl start docker
```

#### 测试docker安装是否成功
```shell
sudo docker run hello-world
```

#### 本次使用的是`https://get.docker.com`提供的脚本安装docker
> 下面的脚本是https://github.com/docker/docker-install/blob/master/install.sh中提供的
```shell
curl -fsSL https://get.docker.com/ | sh
```
![通过脚本安装docker](../resource/docker/docker-通过脚本安装docker.png)