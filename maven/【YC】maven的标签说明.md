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