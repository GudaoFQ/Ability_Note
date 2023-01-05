## Linux中自定义Nginx的yum源【CentOS 7】
> 这个是不使用阿里云yum源和centos自带的yum源的情况下使用下面的方法；如果不想使用此方法，可以将centos的yum源修改为阿里的yum源也能安装nginx

### 安装说明
> yum安装nginx时报错：No package nginx available. Error: Nothing to do
> 同时你也不想通过阿里云提供的系统镜像来安装nginx，就可以使用下面这种方法

### 操作说明
> 进入yum源文件夹，手动创建nginx的源文件
#### 进入/etc/yum.repos.d
```shell
cd /etc/yum.repos.d/
```
#### 添加nginx的源nginx.repo
```shell
vi nginx.repo

# 添加信息
[nginx]
name=nginx repo
baseurl=http://nginx.org/packages/centos/$releasever/$basearch/
gpgcheck=0
enabled=1
```
#### 执行安装
```shell
yum install nginx -y
```
