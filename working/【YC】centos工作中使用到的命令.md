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

### ssh工具连接，编译较久，使用守护进程执行
```shell
# make V=s -j$(nproc)是所需要执行的命令
nohup make V=s -j$(nproc) 2>&1 >> log.txt &

# 输入ssh密码模式，需要安装sshpass命令
yum install sshpass
nohup sshpass -p "目标服务器用户密码" scp 传输文件名 目标服务器用户名称@目标服务器IP:目标服务器文件保存路径 2>&1 >> log.txt &
```

### 查看当前文件夹下**每个文件夹**的大小
```shell
# 通过find命令查询
find . -maxdepth 1 -type d -exec du -sh {} \;

# 通过du命令直接查询
du -h --max-depth=1
```

### 查看当前文件夹下**每个文件**的大小
```shell
find . -maxdepth 1 -type f -exec du -sh {} \;
```

### 查询当前文件夹下，某个时间段的数量与大小
```shell
# 查询当前文件夹下2024-05-22 14:30:00~2024-05-22 20:00:00时间段产生的文件数量
find . -maxdepth 1 -type d -newermt "2024-05-22 14:30:00" ! -newermt "2024-05-22 20:00:00" | wc -l

# 查询当前文件夹下2024-05-22 14:30:00~2024-05-22 20:00:00时间段产生的文件总大小（以G为单位）
find . -maxdepth 1 -type d -newermt "2024-05-22 14:30:00" ! -newermt "2024-05-22 20:00:00" -exec du -sb {} + | awk '{total += $1} END {print total/1024/1024/1024}'
```

### 测试磁盘读写IO
```shell
# 测试磁盘写入性能
dd if=/dev/zero of=/tmp/test bs=1M count=1000 oflag=direct
# 测试磁盘读取性能
dd if=/dev/sda of=/dev/null bs=1M count=1000 iflag=direct
```
* if=/dev/sda 指定要测试的磁盘
* of=/dev/null 表示读取的数据直接丢弃
* iflag=direct 使用直接 I/O,绕过系统缓存
* bs=1M count=1000 表示读取 1GB 数据