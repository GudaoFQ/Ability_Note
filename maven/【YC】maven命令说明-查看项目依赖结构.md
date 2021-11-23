## Maven命令查看项目依赖树

### 通过命令查看jar包中间接引用详情
* 命令
```shell
mvn dependency:tree
```
* 信息输出到文件中
```shell
mvn dependency:tree >tree.txt // 将内容输出到当前项目的tree.txt中
```
* 通过指定jar名称，搜索具体jar包间接引入
![项目依赖文件查看](../resource/maven/maven-项目依赖文件查看.png)