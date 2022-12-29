## FTP服务搭建

### 安装环境
* Windows10 专业版

### 关闭防火墙
![关闭防火墙](../resource/windows/windows-关闭防火墙.png)

### 开启ftp、iis功能
![开启ftp、iis功能](../resource/windows/windows-开启ftp、iis功能.png)

### 创建用户，给创建的ftp使用（可以不创建，直接允许匿名访问就可以不用帐号密码访问ftp文件夹）
#### 打开计算机管理
![打开计算机管理](../resource/windows/windows-打开计算机管理.png)
#### 创建用户（没有绑定组和角色，如果需要自己添加）
![创建新用户](../resource/windows/windows-创建新用户.png)

### 通过IIS创建ftp节点
![打开iis管理器](../resource/windows/windows-打开iis管理器.png)
#### 创建ftp站点
![创建ftp节点](../resource/windows/windows-创建ftp节点.png)
#### 创建步骤
![ftp添加节点步骤](../resource/windows/windows-ftp添加节点步骤.png)

### 连接
#### ftp命令连接
```shell
ftp://ip:端口
```
![通过命令连接ftp](../resource/windows/windows-通过命令连接ftp.png)
#### ftp登录
![ftp登录](../resource/windows/windows-ftp登录.png)
#### 进入文件
![ftp访问文件](../resource/windows/windows-ftp访问文件.png)


### 如果登录不成功，修改`ftp授权规则`，重新添加用户或者添加新用户
![ftp添加用户流程](../resource/windows/windows-ftp添加用户流程.png)

