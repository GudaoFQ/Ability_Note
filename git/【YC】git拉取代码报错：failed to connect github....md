## Git Clone GitHub 代码报错Failed to connect to github.com port 443:connection timed out

### 说明
* 解决日期：2023 06 17
* 截至目前是能使用此方法，但使用的修改DNS服务器，如果目前使用的DNS服务器也被禁，那么此方法可能会不能使用

### 报错截图
![clone报错连接超时](../resource/git/git-clone报错连接超时.png)

### 修改电脑中的DNS服务器
![clone报错连接超时](../resource/git/git-修改dns解决git连接超时.png)

### 也可以把8.8.8.8的DNS添加到备用


### 通过设置代理的方式解决（需要个人有科学上网工具，开启允许局域网访问）
```shell
# 后面的ip端口改成自己工具的
git config --global http.proxy http://127.0.0.1:1080
git config --global https.proxy http://127.0.0.1:1080

# 取消代理（用完了可以取消，看个人情况）
git config --global --unset http.proxy
git config --global --unset https.proxy
```
