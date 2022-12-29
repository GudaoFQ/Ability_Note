## 记录工作中能使用到，但是又不好记的centos命令

### 添加防火墙端口
```shell
firewall-cmd --add-port=18443/tcp --permanent
```

### 查看开启的防火墙端口列表
```shell
firewall-cmd --zone=public --list-ports
```