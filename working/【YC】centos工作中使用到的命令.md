## 记录工作中能使用到，但是又不好记的centos命令

### 添加防火墙端口
```shell
firewall-cmd --add-port=18443/tcp --permanent

# 重新加载防火墙规则
firewall-cmd --reload
```

### 查看开启的防火墙端口列表
```shell
firewall-cmd --zone=public --list-ports
```

### 删除脚本中看不见的空格和换行符号
```shell
sed -i -e 's/\r$//' 脚本
```

### 查看80/tcp的连接数量
```shell
netstat -nat | grep -i "80" | wc -l
```

### 删除centos中的历史命令
```shell
# 历史操作命令存储位置
/root/.bash_history
# 清除命令 -c是清除历史记录
history -c
# 可以通过echo写入空内容到/root/.bash_history
echo >/root/.bash_history
```

### 查看文件具体时间，精确到毫秒
```shell
ll --full-time
```