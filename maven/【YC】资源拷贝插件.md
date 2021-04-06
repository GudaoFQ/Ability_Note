## 资源拷贝插件
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