# OceanBase - 单点部署说明



## 资源准备

| 节点IP       | 配置       | 操作系统   |
| ------------ | ---------- | ---------- |
| 192.168.0.13 | x86 8C 16G | CentOS 7.9 |

## 操作步骤

### 创建SSH免密

```diff
+[root@localhost ob]# ssh-keygen -t rsa -b 2048
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa):
Created directory '/root/.ssh'.
Enter passphrase (empty for no passphrase):
Enter same passphrase again:
Your identification has been saved in /root/.ssh/id_rsa.
Your public key has been saved in /root/.ssh/id_rsa.pub.
The key fingerprint is:
SHA256:FUVBKcdnedY2SAX5kiiLCuOLKh6AkBlw8WC52RS5xm0 root@localhost.localdomain
The key's randomart image is:
+---[RSA 2048]----+
|+ =o.o    .=*==o.|
| *.oo     ..+o+.=|
|+  *.o    .o.oo+.|
|o o = E  o . o . |
|o  . .  S o   .  |
|.  o   . .       |
| .. o .          |
|. o. .           |
|=o ..            |
+----[SHA256]-----+
+[root@localhost ob]# ssh-copy-id root@192.168.0.13
/usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "/root/.ssh/id_rsa.pub"
The authenticity of host '192.168.0.13 (192.168.0.13)' can't be established.
ECDSA key fingerprint is SHA256:OPLdLEux5u6VPWbeIQujL7qr7GOC9VrjZEXsU9UuKb8.
ECDSA key fingerprint is MD5:9d:cd:18:25:e1:d4:d2:0c:53:84:ea:c0:c7:30:d2:53.
Are you sure you want to continue connecting (yes/no)? yes
/usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
/usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
root@192.168.0.13's password:
+[root@localhost ob]# ssh-copy-id root@192.168.0.13
/usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "/root/.ssh/id_rsa.pub"
/usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
/usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
root@192.168.0.13's password:

Number of key(s) added: 1

Now try logging into the machine, with:   "ssh 'root@192.168.0.13'"
and check to make sure that only the key(s) you wanted were added.

+[root@localhost ob]# cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
+[root@localhost ob]# chmod 600 ~/.ssh/authorized_keys
+[root@localhost ob]# chmod 700 ~/.ssh
+[root@localhost ob]# ssh root@192.168.0.13
Last login: Sun Jan 18 22:44:52 2026 from 192.168.111.45
+[root@localhost ~]# exit
logout
Connection to 192.168.0.13 closed.
```

### 使用 OBD 自动配置系统参数

```shell
# 以 root 用户执行以下命令

# 修复 open files 限制
echo -e "* soft nofile 655350\n* hard nofile 655350" >> /etc/security/limits.d/nofile.conf

# 确保 SSH 配置正确
grep "UsePAM" /etc/ssh/sshd_config
# 如果显示 "UsePAM no"，需要改为 yes：
# sed -i 's/UsePAM no/UsePAM yes/g' /etc/ssh/sshd_config
# systemctl restart sshd

# 修复内核参数
cat >> /etc/sysctl.conf << EOF
vm.max_map_count=655360
fs.file-max=6573688
fs.aio-max-nr=1048576
EOF

# 使内核参数生效
sysctl -p

# 退出当前终端
exit

# 重新登录（让 ulimit 配置生效）

# 检查 open files
ulimit -n
# 应该显示 655350

# 检查内核参数
sysctl vm.max_map_count
sysctl fs.file-max
sysctl fs.aio-max-nr
```

### 创建OB单节点配置

```shell
# vi mini-single.yaml


oceanbase-ce:
  servers:
    - 192.168.0.13   # 修改为服务器自己的IP
  global:
    devname: ens192   # 修改为自己服务器网卡信息
    mysql_port: 2881
    rpc_port: 2882
    home_path: /root/observer
    data_dir: /data/oceanbase
    redo_dir: /data/oceanbase
    memory_limit: 8G
    system_memory: 2G
    datafile_size: 20G   # 数据盘大小
    log_disk_size: 20G
  192.168.0.13:
    zone: zone1
```

### 执行部署

```shell
# 1. 部署
obd cluster deploy ob-sca -c mini-single.yaml

# 2. 自动初始化环境
obd cluster init4env ob-sca

# 3. 启动
obd cluster start ob-sca

# 4. 查看状态
obd cluster display ob-sca

# 日志
[root@localhost ob]# obd cluster deploy ob-sca -c mini-single.yaml
Are you sure you want to deploy the database as the root user? [y/n] [Default: n]: y
+------------------------------------------------------------------------------------------------+
|                                            Packages                                            |
+--------------+-------------+------------------------+------------------------------------------+
| Repository   | Version/Tag | Release                | Hash                                     |
+--------------+-------------+------------------------+------------------------------------------+
| oceanbase-ce | 4.5.0.0     | 100000012025112711.el7 | ea2a91329c00190d20c01dd73da81b2f081abd4e |
+--------------+-------------+------------------------+------------------------------------------+
Repository integrity check ok
Load param plugin ok
Open ssh connection ok
Initializes observer work home ok
Parameter check ok
Remote oceanbase-ce-4.5.0.0-100000012025112711.el7-ea2a91329c00190d20c01dd73da81b2f081abd4e repository install ok
Remote oceanbase-ce-4.5.0.0-100000012025112711.el7-ea2a91329c00190d20c01dd73da81b2f081abd4e repository lib check ok
ob-sca deployed
Please execute ` obd cluster start ob-sca ` to start
Trace ID: b92334ae-f4ea-11f0-a95a-000c296ef78b
If you want to view detailed obd logs, please run: obd display-trace b92334ae-f4ea-11f0-a95a-000c296ef78b
[root@localhost ob]# obd cluster init4env ob-sca
Get local repositories ok
Open ssh connection ok
get system config ok
No need to change system parameters
Trace ID: bfb4a64a-f4ea-11f0-b98d-000c296ef78b
If you want to view detailed obd logs, please run: obd display-trace bfb4a64a-f4ea-11f0-b98d-000c296ef78b
[root@localhost ob]# obd cluster start ob-sca
Get local repositories ok
Load cluster param plugin ok
Open ssh connection ok
[WARN] OBD-1007: (192.168.0.13) The recommended number of core file size is unlimited (Current value: 0)
[WARN] OBD-1007: (192.168.0.13) The recommended number of stack size is unlimited (Current value: 8192)
[WARN] OBD-1012: (192.168.0.13) clog and data use the same disk (/)
cluster scenario: htap
Start observer ok
observer program health check ok
Connect to observer 192.168.0.13:2881 ok
oceanbase bootstrap ok
obshell start ok
obshell program health check ok
obshell bootstrap ok
Connect to observer 192.168.0.13:2881 ok
Wait for observer init ok
+------------------------------------------------+
|                  oceanbase-ce                  |
+--------------+---------+------+-------+--------+
| ip           | version | port | zone  | status |
+--------------+---------+------+-------+--------+
| 192.168.0.13 | 4.5.0.0 | 2881 | zone1 | ACTIVE |
+--------------+---------+------+-------+--------+
obclient -h192.168.0.13 -P2881 -uroot@sys -p'zuRw27zaecjzvJ2ZAWTz' -Doceanbase -A

cluster unique id: 989c1da3-8632-5e0d-86f9-5ae3db49f75d-19bd465602b-00000504

obshell program health check ok
display obshell dashboard ok
+-------------------------------------------------------------------+
|                         obshell Dashboard                         |
+--------------------------+------+------------------------+--------+
| url                      | user | password               | status |
+--------------------------+------+------------------------+--------+
| http://192.168.0.13:2886 | root | 'zuRw27zaecjzvJ2ZAWTz' | active |
+--------------------------+------+------------------------+--------+

ob-sca running
Trace ID: c2b7190e-f4ea-11f0-8413-000c296ef78b
If you want to view detailed obd logs, please run: obd display-trace c2b7190e-f4ea-11f0-8413-000c296ef78b
```

### 验证部署

```shell
# 查看集群状态
obd cluster display ob-sca

# 连接数据库测试
mysql -h127.0.0.1 -P2881 -uroot -p
```

### 销毁

```shell
# 销毁之前的部署
obd cluster destroy ob-sca -f
```

