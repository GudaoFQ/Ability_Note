## GO语言Windows安装说明

### 下载GO语言安装包
[官网地址：需出国](https://golang.org/dl/)

### GOPATH和GOROOT说明
* GOROOT：是go安装位置 
* GOPATH：所有安装的第三方库，会自动安装在GOPATH的第一个路径下。
* 项目路径: 项目路径与GOPATH没有必要的关系。不管项目路径在哪，当在项目中导入第三方库的时候，系统会自动搜索GOPATH中是否存在已安装的该包
#### 个人设置
```shell
GOROOT：GO安装路径
GOPATH：指定任意位置，但最好不要放在GOROOT配置的路径下
项目路径：可以为GOPATH，也可以使用其它路径，引用第三方依赖的时候，go会默认到GOPATH中查找
```

### 安装完成后查看安装是否成功
* cmd窗口命令`go version`
![go安装版本查看](../resource/go/go-go安装版本查看.png)

### GOPATH工作空间配置
> GOPATH 适合处理大量 Go语言源码、多个包组合而成的复杂工程。
> 项目中使用到第三方依赖或者自己写的依赖都是存放在GOPATH下的src下的，如：go get github... 下载的包都是在这个路径下
* 个人使用的工作空间位置为D:\Go_Config\workspace【在桌面上右键单击“我的电脑”图标，在弹出的菜单中单击“属性”，然后单击“高级系统设置”；在“系统属性”对话框中单击“环境变量”按钮，然后添加GOPATH变量即可】
![go工作空间配置](../resource/go/go-go工作空间配置.png)
* 注意：我的电脑中在系统环境中配置GOPATH不生效，后才在用户变量中又配置了一份；各位可以根据自己的电脑，系统变量不生效了再配置用户变量

### 查看环境信息
* cmd窗口命令`go env [环境名称]`
![go环境信息](../resource/go/go-go环境信息.png)

