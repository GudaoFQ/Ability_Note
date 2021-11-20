## Pom中的maven标签说明

### optional
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
* optional=true：当与java中的关键字final ，意思是只在本项目中使用，不传递给子孙项目。子孙项目都需要强制引入才行。
    > 假如你的Project A的某个依赖D添加了<optional>true</optional>，当别人通过pom依赖Project A的时候，D不会被传递依赖进来。当你依赖某各工程很庞大或很可能与其他工程的jar包冲突的时候建议加上该选项，可以节省开销，同时减少依赖冲突
  
### scope
```xml
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.16</version>
  <scope>provided</scope>
</dependency>
```
* scope=provided，说明它只在编译阶段生效，不需要打入包中, Lombok在编译期将带Lombok注解的Java文件正确编译为完整的Class文件

### dependencyManagement
* 父模块
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.44</version>
    </dependency>
  </dependencies>
</dependencyManagement> 
```
* 子模块
```xml
 <dependencies>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
  </dependency>
</dependencies>
```
#### 说明
* dependencyManagement可以统一管理项目的版本号，确保应用的各个项目的依赖和版本一致，不用每个模块项目都弄一个版本号，不利于管理，当需要变更版本号的时候只需要在父类容器里更新，不需要任何一个子项目的修改
* 如果某个子项目需要另外一个特殊的版本号时，只需要在自己的模块dependencies中声明一个版本号即可。子类就会使用子类声明的版本号，不继承于父类版本号

#### 与dependencies区别
* dependencies
  * dependencies相对于dependencyManagement，所有在dependencies里的依赖都会自动引入，并默认被所有的子项目继承
* dependencyManagement
  * dependencyManagement里只是声明依赖，并不自动实现引入，因此子项目需要**显示的声明需要用的依赖**
  * 如果不在子项目中声明依赖，是不会从父项目中继承下来的；
  * 只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom;
  * 另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本