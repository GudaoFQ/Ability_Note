## Compose安装
[官网地址](https://docs.docker.com/compose)
[部署说明](https://docs.docker.com/compose/install/other/)
[Github版本地址](https://github.com/docker/compose/releases/)

### 环境说明
* 系统：Linux version 3.10.0-1160.80.1.el7.x86_64
* docker版本：20.10.22
* compose版本：1.25.0

### 安装
#### 下载Compose工具
> 下面的方法是针对github中的`1.29.2`使用的命令，github上最新的compose已经不能使用下面的命令下载；
> $(uname -s)是系统名称；
> $(uname -m)是系统架构；
> 所有新版本需要通过wget 准确的地址
> 例如此刻最新的：wget https://github.com/docker/compose/releases/download/v2.15.1/docker-compose-linux-aarch64
##### github下载
```shell
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```
##### 国内镜像下载
```shell
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

#### 修改文件夹权限
```shell
chmod +x /usr/local/bin/docker-compose
```

#### 建立软连接
```shell
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

#### 验证
```shell
docker-compose --version
```