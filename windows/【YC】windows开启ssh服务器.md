## Windows开启OpenSSH服务器（Windows会默认开启OpenSSH的客户端）（scp / ssh等）

### 环境
Windows 10

### 通过`win + s`打开设置窗口
![打开设置窗口](../resource/windows/windows-打开设置窗口.png)

### 安装OpenSSH服务器
![开启openssh服务器](../resource/windows/windows-开启openssh服务器.png)

### 安装完成
![ssh安装完成](../resource/windows/windows-ssh安装完成.png)

### 右击开始按钮，以管理员运行cmd
![以管理员运行cmd](../resource/windows/windows-以管理员运行cmd.png)

### cmd开启OpenSSH
```shell
# 开启命令
net start sshd

# 停止命令（此处不需要执行）

net stop sshd
```
![cmd开启openssh](../resource/windows/windows-cmd开启openssh.png)

### 远程连接测试
```shell
# 命令
ssh 用户名@用户ip

# 演示
ssh username@127.0.0.1
```
![ssh远程连接测试](../resource/windows/windows-ssh远程连接测试.png)

### 注意：如果不知道当前连接当前计算机的账号和密码，请看【【YC】windows本地用户创建.md】
![本地用户查看](../resource/windows/windows-本地用户查看.png)
