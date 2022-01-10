## Pip3安装教程

### 说明
* 安装版本：3
* CentOS：8

### 安装依赖（非必要）
```shell
sudo yum install openssl-devel -y 
sudo yum install zlib-devel -y
```

### 安装setuptools
```shell
# 下载安装文件
wget --no-check-certificate https://pypi.python.org/packages/source/s/setuptools/setuptools-19.6.tar.gz#md5=c607dd118eae682c44ed146367a17e26

# 解压
tar -zxvf setuptools-19.6.tar.gz 
cd setuptools-19.6

# 执行安装
sudo python3 setup.py build 
sudo python3 setup.py install
```

### 安装pip3
```shell
# 下载安装文件
wget --no-check-certificate https://pypi.python.org/packages/source/p/pip/pip-20.2.2.tar.gz#md5=3a73c4188f8dbad6a1e6f6d44d117eeb
 
# 解压
tar -zxvf pip-20.2.2.tar.gz 
cd pip-20.2.2

# 执行安装
python3 setup.py build 
sudo python3 setup.py install
```

### 测试：输入pip3 -V，打印
```shell
pip 21.3.1 from /usr/local/lib/python3.6/site-packages/pip (python 3.6)
```