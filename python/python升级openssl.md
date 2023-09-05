###


### 环境依赖下载
```shell
yum install -y gcc gcc-c++ autoconf automake zlib zlib-devel pcre-devel libffi-devel 
```

### openssl部署安装
```shell
# 下载部署包
wget https://www.openssl.org/source/openssl-1.1.1u.tar.gz --no-check-certificate

# 解压
tar zxvf openssl-1.1.1u.tar.gz

# 进入解压包
cd openssl-1.1.1u

# 创建安装目录
mkdir /usr/local/openssl1.1.1

# 配置安装目录
./config --prefix=/usr/local/openssl1.1.1

# 编译安装
make && make install
```

### 升级python的openssl
> python安装参考[【HMCXY001】python安装-linux.md]；其中配置安装目录中需要使用下面的配置
```shell
# 指定openssl版本，需要先下载openssl编译后才能这个指定
./configure --prefix=/usr/local/python3 --with-openssl=/usr/local/openssl1.1.1 --with-openssl-rpath=auto
# --with-openssl=/usr/local/openssl1.1.1是openssl的安装位置
```