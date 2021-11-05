## 字体颜色修改

#### 修改Linux中的字体显示颜色
![command-修改字体颜色.jpg](../resource/linux/command-修改字体颜色.jpg)
* 修改etc下的profile文件，命令：`vim /etc/profile`
* 添加信息：`export PS1="[\[\e[34;1m\]\u@\[\e[0m\]\[\e[32;1m\]\H\[\e[0m\] \[\e[31;1m\]\w\[\e[0m\]]\\$"`
![command-字体颜色添加命令.jpg](../resource/linux/command-字体颜色添加命令.jpg)
