## Linux中的Nginx安装详情【CentOS 8】

### 安装说明
> 在Centos下，yum源不提供nginx的安装，可以通过切换yum源的方法获取安装。也可以通过直接下载安装包的方法，以下命令均需root权限执行：
* 首先安装必要的库（nginx中gzip模块需要zlib库，rewrite模块需要pcre库，ssl功能需要openssl库）。
* 选定/usr/local为安装目录，以下具体版本号根据实际改变。

### 安装
#### 安装对应的工具包
```shell
yum install -y gcc gcc-c++
```

#### 安装 PCRE 库
```shell
cd /usr/local/ 
# 下载不了自行到网上查找
wget ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/pcre-8.36.tar.gz 
# 解压
tar -zxvf pcre-8.36.tar.gz 
# 进入解压后文件
cd pcre-8.36 
# 配置
./configure 
# 编译
make && make install
```

#### 安装 zlib 库
```shell
cd /usr/local/ 
wget http://zlib.net/zlib-1.2.8.tar.gz 
tar -zxvf zlib-1.2.8.tar.gz 
cd lib-1.2.8 
./configure 
make && make install
```

#### 安装 ssl
```shell
cd /usr/local/ 
wget http://www.openssl.org/source/openssl-1.0.1j.tar.gz 
tar -zxvf openssl-1.0.1j.tar.gz 
./config 
make && make install
```

#### 安装 nginx
```shell
cd /usr/local/ 
wget http://nginx.org/download/nginx-1.8.0.tar.gz
tar -zxvf nginx-1.8.0.tar.gz 
cd nginx-1.8.0 
./configure --prefix=/usr/local/nginx 
make && make install
```