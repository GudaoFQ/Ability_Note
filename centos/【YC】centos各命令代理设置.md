# Centos中各命令设置本地代理

### 永久设置环境变量
```shell
vi /etc/profile

# 文件末尾添加
export http_proxy="http://代理节点ip:代理节点端口"
export https_proxy="http://代理节点ip:代理节点端口"
export no_proxy="localhost,127.0.0.1,localaddress,.localdomain.com"

# 重新加载环境变量
source /etc/profile
```

### git命令
```shell
git config --global --edit

# 添加以下内容
[http]
    proxy = http://代理节点ip:代理节点端口
[https]
    proxy = https://代理节点ip:代理节点端口
    
# 验证代理是否生效
git config --global --get http.proxy
git config --global --get https.proxy
```

### yum命令

```shell
# 修改yum配置文件
nano /etc/yum.conf

# 添加本地代理
proxy=http://代理节点ip:代理节点端口
#proxy_username=user  # 如果需要认证
#proxy_password=password  # 如果需要认证
```

### crul命令

```shell
curl -x https://代理节点ip:代理节点端口 -L "https://target-url.com"
```

### docker命令

```shell
mkdir -p /etc/systemd/system/docker.service.d
vi /etc/systemd/system/docker.service.d/http-proxy.conf

# 在文件中添加以下内容
[Service]
Environment="HTTP_PROXY=http://代理节点ip:代理节点端口"
Environment="HTTPS_PROXY=http://代理节点ip:代理节点端口"
Environment="NO_PROXY=localhost,127.0.0.1"

# 重新加载配置并重启
sudo systemctl daemon-reload
sudo systemctl restart docker
```

