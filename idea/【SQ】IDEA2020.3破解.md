## 激活教程

#### 2020.3的激活码路径如下图
![2020.3下载说明](../resource/idea/idea-2020.3下载说明.jpg)

#### 软件下载
![2020.3官网下载](../resource/idea/idea-2020.3官网下载.jpg)

#### 试用进入软件
![2020.3试用版安装完后进入](../resource/idea/idea-2020.3试用版安装完后进入.jpg)
* 如果没有这个提示，可以使用资源包中的工具进行重置
    * 根据你的操作系统选择执行
        ![2020.3安装重置](../resource/idea/idea-2020.3安装重置.jpg)
    * 如果你之前是使用的jetbrains-agent-latest或者其他插件进行激活的，需要先移除原有激活的方式；否则可能导致软件无法启动
    
#### 移除原先的激活方式
> 如果你之前是使用的jetbrains-agent-latest或者其他插件进行激活的，需要先移除原有激活的方式；否则可能导致软件无法启动
1. 路径：help > Edit Custom VM Options
![2020.3移除激活1](../resource/idea/idea-2020.3移除激活1.jpg)

2. 删掉（或注释）最下面的一行，其他激活插件相关的jar包路径
![2020.3移除激活2](../resource/idea/idea-2020.3移除激活2.jpg)

#### 激活
1. File -> Settings -> Manage Plugins Reposito【打开软件中心】
![2020.3配置插件中心](../resource/idea/idea-2020.3配置插件中心.jpg)

2. 设置仓库地址
    ```shell
    https://repo.idechajian.com
    如果有https://plugins.zhile.io的地址，请移除掉...
    ```
    ![2020.3插件仓库地址](../resource/idea/idea-2020.3插件仓库地址.jpg)

3. 搜索 关键字“BetterIntellij” 安装并应用
![2020.3安装插件better](../resource/idea/idea-2020.3安装插件better.jpg)
![2020.3插件应用](../resource/idea/idea-2020.3插件应用.jpg)
4. 装完之后，会在help > Edit Custom VM Options配置文件中多如下配置；可自行确认是否安装成功。
    ```shell
    -javaagent:C:\Users\Public\.BetterIntelliJ\BetterIntelliJ-1.15.jar
    ```
    ![2020.3配置查看](../resource/idea/idea-2020.3配置查看.jpg)
5. 重启软件；重要！重要！重要！
    > 一定要先重启！先重启！先重启！再输入激活码；否则会提示激活码无效；如果开了多个窗口，请全部关闭，关闭单个窗口无效。
    ![2020.3重启](../resource/idea/idea-2020.3重启.jpg)
6. 打开  help --> Register
    > 输入资源包中的激活码，这里的激活码只有一个；全选粘贴就好；路径如下图
    ![2020.3激活码配置](../resource/idea/idea-2020.3激活码配置.jpg)
7. 参数设置
![2020.3参数配置](../resource/idea/idea-2020.3参数配置.jpg)

    


