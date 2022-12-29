## Windows安装WebLogic环境

### 安装说明
* [官网下载地址](https://www.oracle.com/middleware/technologies/weblogic-server-downloads.html)
* JDK1.8
* 此次使用版本12.2.1.4

### 官网下载
![下载安装包](../resource/weblogic/weblogic-下载安装包.png)

### 解压安装包
![安装包解压](../resource/weblogic/weblogic-安装包解压.png)

### 管理员启动dos窗口
![管理员启动命令行](../resource/weblogic/weblogic-管理员启动命令行.png)

### 执行jar文件
```shell
cd 下载包解压出来的jar包位置

# java -jar 需要部署的weblogic的jar
java -jar fmw_12.2.1.4.0_wls_quick.jar

# 安装完成标识
完成百分比:70
完成百分比:80
完成百分比:90
完成百分比:100

Oracle Fusion Middleware 12c WebLogic 和 Coherence Developer 12.2.1.4.0 的 安装 已成功完成
日志已成功复制到...
```
![部署](../resource/weblogic/weblogic-部署.png)

### 安装完成，启动
#### 安装完成后，会在jar包的同级目录下创建一个文件夹
![安装完成生成部署文件夹](../resource/weblogic/weblogic-安装完成生成部署文件夹.png)
#### 配置环境变量`MW_HOME`
![配置环境变量](../resource/weblogic/weblogic-配置环境变量.png)

### 在生成的文件夹中（wls版本号）创建domain
> 进入`运行jar包文件夹位置\执行jar包生成的文件夹中\oracle_common\common\bin`找到config.cmd，运行此文件
* 点击后，会出现配置向导界面，一直下一步，到向导3界面，需要配置帐号密码，如图
  ![配置向导](../resource/weblogic/weblogic-配置向导.png)

### 启动自己的配置的WebLogic
> 进入`运行jar包文件夹位置\执行jar包生成的文件夹中\user_projects\domains\base_domain`找到startWebLogic.cmd，运行此文件
![执行weblogic启动脚本](../resource/weblogic/weblogic-执行weblogic启动脚本.png)
* 出现Server state changed to RUNNING 字样，说明启动成功
  ![项目启动日志](../resource/weblogic/weblogic-项目启动日志.png)

### 访问
![登录](../resource/weblogic/weblogic-登录.png)

### 创建java的webapp项目
#### 点击install
![创建webapp项目](../resource/weblogic/weblogic-创建webapp项目.png)
#### 选择war包位置，然后一路`下一步`
![指定部署包](../resource/weblogic/weblogic-指定部署包.png)
#### 访问
> 通过`http://localhost:7001/`+`上下文根`来访问
![项目启动后界面](../resource/weblogic/weblogic-项目启动后界面.png)

![访问项目](../resource/weblogic/weblogic-访问项目.png)


