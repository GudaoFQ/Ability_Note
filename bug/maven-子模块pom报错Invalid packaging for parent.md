## 子模块在继承父类pom的时候，project标红，并报Invalid packaging for parent POM ink.gudao.blogspace:1.0, must be “pom” but is "jar"

#### 问题描述
![Invalid packaging for parent POM](../resource/bug/bug-Invalid packaging for parent POM.png)

#### 解决方法
> 说明父POM的打包方式不对，添加`<packaging>pom</packaging>`
![pom文件添加packing](../resource/bug/bug-pom文件添加packing.png)

