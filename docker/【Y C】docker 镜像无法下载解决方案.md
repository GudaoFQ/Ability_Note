## 镜像无法下载解决方案-截止24-08-09还能使用

### 说明
* 国内大面积禁用Docker官方地址和国内镜像源地址
* 目前这个方案如果映射地址失效，那么需要自行寻找新的Docker仓库映射地址（百度或者公众号）

### 配置镜像加速（默认已经安装完Docker工具）
```shell
# 如果/etc/docker/daemon.json已经存在，可以通过vi命令添加"registry-mirrors": ["https://do.nark.eu.org"]
# 文件若不存在通过下面的tee命令直接创建并将内容添加进去
sudo tee /etc/docker/daemon.json <<EOF
{
    "registry-mirrors": ["https://do.nark.eu.org"]
}
EOF
```

### 重新加载Docker配置
```shell
sudo systemctl daemon-reload
```

### 重启Docker
> 如果重新加载配置后，还是无法pull，可以试下重启Docker；如果重启后还是不能pull下来，大概率是网址失效率（使用ping、curl或者直接访问下地址：https://do.nark.eu.org）

```shell
sudo systemctl restart docker
```