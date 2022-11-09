## Linux中的Nginx安装详情，非源码模式部署【CentOS 8】

### 安装说明
> 在Centos下，yum源不提供nginx的安装，可以通过直接下载安装包的方法（前面已经说明）；也通过切换yum源的方法获取安装，以下命令均需root权限执行：
* Nginx软件包可在EPEL存储库中获得。在安装Nginx之前我们先启用EPEL仓库，使用yum。然后再使用yum安装nginx
* 如果这是您首次从EPEL存储库安装软件包，yum可能会提示您导入EPEL GPG密钥。如果你是这种情况，请键入y并单击Enter

### 安装Nginx
```shell
sudo yum install epel-release
sudo yum install nginx
# 查看nginx状态
systemctl status nginx
```

#### 安装epel-release
![安装epel-release](../resource/nginx/nginx-安装epel-release.png)

#### 安装nginx
![安装nginx](../resource/nginx/nginx-安装nginx.png)

#### 查看nginx状态
![通过systemctl查询nginx状态](../resource/nginx/nginx-通过systemctl查询nginx状态.png)


### 打开80和443和的端口
> FirewallD是Centos 7上的默认防火墙解决方案。在安装过程中，Nginx使用预定义规则创建防火墙服务文件。打开HTTP协议的80端口和HTTPS协议443端口，允许来自这两个端口的连接。使用以下命令永久打开80和443和的端口
```shell
sudo firewall-cmd --permanent --zone=public --add-service=http
sudo firewall-cmd --permanent --zone=public --add-service=https
sudo firewall-cmd --reload
```

### Nginx配置文件的结构
> 所有Nginx配置文件都位于/etc/nginx/目录中。主要的Nginx配置文件为/etc/nginx/nginx.conf

![nginx主要配置文件位置](../resource/nginx/nginx-nginx主要配置文件位置.png)

### 配置使用建议
* 建议为每个域创建单独的配置文件。Nginx的虚拟主机配置文件必须以.conf结尾，并存储在/etc/nginx/conf.d目录中。您可以根据需要拥有任意数量的虚拟主机配置文件。
* 遵循标准命名约定是一种好的做法。例如，如果域名为mydomain.com，则配置文件应命名为mydomain.com.conf。
* 如果您在虚拟主机配置文件中使用可重复的配置片段，则最好将这些片段存放在单独的文件中，然后使用include指令包含它。
* Nginx日志文件access.log和error.log位于/var/log/nginx/目录中。建议为每个虚拟主机使用不同的access和error日志文件。
* 您可以将web站点根目录设置为所需的任何位置。Webroot的最常见位置包括：/home/<user_name>/<site_name>，/var/www/<site_name>，
* /var/www/html/<site_name>，/opt/<site_name>，/usr/share/nginx/html。

### 配置修改完成后，启动或重新nginx
```shell
# 启动nginx
systemctl start nginx
# 重启nginx
systemctl restart nginx
```