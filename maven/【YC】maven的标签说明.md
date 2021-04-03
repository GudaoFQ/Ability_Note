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
  
### 资源拷贝插件
> 配置了之后就能在build之后在target中查看到；在打包之后也能将xml配置加载进jar包之中
```xml
<!-- 资源拷贝路径插件，不配置的话，默认是去resources目录下查找xml文件 -->
<resources>
    <resource>
        <!-- 如果mybatis中的xml映射文件不是在resources目录下，那么就要让springboot去指定的文件目录下去查找xml文件 -->
        <directory>src/java/com</directory>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <!-- 指定了让springboot去查找文件的位置之后，就不会再去resources目录下去查找，所以要将resources目录添加进来让springboot再次扫描 -->
        <directory>src/java/resource</directory>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
</resources>
```