## 本地yum源修改为阿里（Centos7）

### 说明
* 在中国无法访问外网，下载一些国外的工具，会报错，需要配置阿里yum源解决
* Centos的yum源存放路径：/etc/yum.repos.d/
* 阿里yum源文件仓库地址 [阿里yum源](http://mirrors.aliyun.com/repo/)

### 备份CentOS-Base.repo
```shell
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak
```

### 下载新的阿里`CentOS-Base.repo`到`/etc/yum.repos.d/`
```shell
#centos 6
curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo
#centos 7
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
#centos 8
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-8.repo
```

### 把服务器的包信息下载到本地电脑缓存起来，makecache建立一个缓存
```shell
yum makecache
```

### 运行安装rpel源命令，安装完成之后你就可以直接使用yum来安装额外的软件包，也就是epel源
```shell
yum install epel-release 
```