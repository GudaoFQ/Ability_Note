## CentOS创建脚本定时任务

### 环境说明
* 内网环境搭建的两台CentOS服务器
* 系统版本：两台都为CentOS Linux release 7.7.1908 (Core)
* 脚本使用scp命令定时向另一台备份数据
  * 两台服务器网络互通，且有scp命令

### 使用说明
#### 创建备份脚本
##### backup.sh脚本
> 通过tar打备份包，通过scp传输到备份服务器，通过find删除上一次昨天的备份数据
```shell
#!/bin/bash
# 备份文件名称
file_name=codebak$(date "+%Y%m%d%H%M%S")
# 备份文件存储位置，与脚本同一位置
file_path=/cronTask
# 备份文件存储天数
save_date=+1
# 备份服务器目标路径
backup_server_path=/test

# 将需要的文件夹打成gz包放到指定存储位置
tar czf $file_path/$file_name.tar.gz /data/mysql

# 备份服务器ip
cp_ip=192.168.0.100
# 备份服务器帐号
cp_user=root
# 备份服务器密码
cp_pass=123456
# 备份服务器目标路径
cp_path=$backup_server_path

# 通过scp命令传输文件
$file_path/scpfile.sh $cp_path/ $cp_user $cp_ip $file_path/$file_name.tar.gz $cp_pass >> $file_path/$file_name.log
# 删除昨天备份的数据
find $file_path -mtime $save_date -name "*.*" -exec rm -Rf {} \ >> $file_path/$file_name.log
```
##### scpfile.sh脚本：自动输入帐号密码脚本
```shell
# 先安装expect
yum install expect

# 脚本
#!/usr/bin/expect -f
set timeout -1
set server_path [lindex $argv 0]
set server_user [lindex $argv 1]
set server_ip [lindex $argv 2]
set local_path [lindex $argv 3]
set server_password [lindex $argv 4]

spawn scp $local_path $server_user@$server_ip:$server_path 
expect {
  "*assword:*" {
      send "$server_password\r"
      exp_continue
    }
  "*yes/no)?*" {
    send "yes\r"
  }
}
```

#### 创建定时任务
```shell
# 通过crontab -e 添加定时任务
crontab -e

# 每天0点执行脚本
0 0 * * * /bin/sh 脚本位置
```

#### 每天定时任务信息
```shell
# 查看脚本的执行日志（只能查看上一次的执行日志，以前的不会存在）
cat /var/spool/mail/root
```