# Rocky Linux初次部署完成后Root用户无法登录解决

### 说明
* 版本：Rocky Linux 9.4 x86_64

### Rocky 9.4下载提示
* Minimal版本大小为1.7GB，顾名思义这是Rocky Linux的最小版本，进入安装界面之后还需要借助网络完成安装；
* DVD版本大小为10.17GB，这是一个完整版本，包含所有的模块；
* Boot版本大小为949MB，这个镜像用于启用Rocky Linux 9的安装，进入安装界面之后还需要借助网络完成安装。

**注意：** Minimal版、Boot版都需要网络，没有网络连接的情况下都无法进入安装界面

### 问题说明
使用ssh工具连接 Rocky Linux 服务器，安装时只配置了root账户，通过ssh连接一直提示：**Access denied**

### 解决
#### 修改ssh配置文件
```shell
vi /etc/ssh/sshd_config
```

#### 添加允许使用 root 账户直接通过 SSH 远程登录系统配置项
```shell
PermitRootLogin yes
```
![ssh远程root登录配置](../resource/rockylinux/rocky-ssh远程root登录配置.png)

* Rocky Linux默认是**PermitRootLogin prohibit-password**：允许 root 使用密钥认证，但禁止密码登录；因此，若想要通过密码登录只能将其修改为**yes**
##### 其余配置说明
* PermitRootLogin no：完全禁止 root 登录
* PermitRootLogin prohibit-password：允许 root 使用密钥认证，但禁止密码登录
* PermitRootLogin forced-commands-only：仅允许 root 执行预定义的命令

#### 重启ssh服务或重启服务器
```shell
systemctl restart sshd
或
reboot（重启命令，二选一即可）
```