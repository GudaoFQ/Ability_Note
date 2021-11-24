## pom文件下的plugin标签下的jar包报错

### 问题描述
> 项目中使用xjar-maven-plugin插件报错，也不能通过该插件进行打包
![maven项目pom文件下plugin插件报错](../resource/bug/bug-maven项目pom文件下plugin插件报错.png)

### 问题解决
#### maven问题
* maven的settings文件是否正确，如果不确定，直接进maven[官网](https://maven.apache.org/download.cgi)重新下一个
  ![maven官网下载maven](../resource/bug/bug-maven官网下载maven.png)
* idea配置maven
  ![idea配置maven](../resource/bug/bug-idea配置maven.png)

#### 本地仓库没有下载过这个jar包
> 因为项目中使用的打包插件，没有在`<dependencies>`标签下进行声明，所以maven不会去下载（我这边是这样），但是我又在maven下的`<plugins>`中进行使用，导致maven去自己本地仓库找不到，所以报红了
* 在`<dependencies>`声明下使用的jar包
  ![解决plugin下jar包报错问题](../resource/bug/bug-解决plugin下jar包报错问题.png)
* 最后把`<dependencies>`下声明的信息删除就行了，此时留不留都没啥问题了

#### 注意：
* 解决问题的过程中如果出现刷新maven没反应，直接重启项目就可以了
  ![idea清除缓存重启](../resource/bug/bug-idea清除缓存重启.png)
