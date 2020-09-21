#### zk中文件介绍

```shell
#bin文件夹
一些主要的运行命令【以 .cmd 结结尾的命令，是指的是在windows环境上运行使用的命令，在win环境上，直接双击 .cmd 运行就好了。我们这里用的是linux环境，使用 .sh 命令】
#zkService.sh
zk启动项
#zkCli.sh
zk客户端

#conf文件夹
存放配置文件，其中我们需要修改zk.cfg【在运行zk之前，必须配置 .cfg。文件中有一个默认的zoo_sample.cfg文件。】

#contrib文件夹
附加的一些功能

#dist-maven文件夹
mvn编译后的目录【里面有一些打包后的jar包啊，pom文件啊，maven相关的东西什么的。】

#docs文件夹
一些分布式的软件里面都会有这种帮助文档目录【比如说里面的index.html文件，打开是hadoop的官网，可以基于本地访问】

#lib文件夹
需要依赖的jar包【这些jar包会在后面开发java api客户端时用到。】

#recipes文件夹
案例 demo的代码

#src文件夹
源码
```