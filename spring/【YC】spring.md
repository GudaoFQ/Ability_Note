## Spring中的单例实现

### 注解方式

```java
//向spring容器中注册实体类【默认为单例】
@Component
public class Bean {
    private Integer id;
    private String name;
    private String sex;
    private String age;
}
```
```java
//此时通过@Autowired来调用到spring容器中的单例对象
public void test(){
    @Autowired
    private Bean bean;
}
```

### 配置文件方式

```xml
<!-- spring配置中的信息 -->
<bean id="hi" class="com.test.Hi" init-method="init" scope="singleton">
```
```java
//调用
ApplicationContext context = new FileSystemXmlApplicationContext("applicationContext.xml");
    Hi testO = (Hi) context.getBean("hi");
    Hi testT = (Hi) context.getBean("hi");
    System.out.println(testO);
    System.out.println(testT);
```