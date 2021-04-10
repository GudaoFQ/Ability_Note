## Lombok注解说明：Getter&Setter

### Get&Set
* 作用类上，生成**所有成员变量**的getter/setter方法
* 作用于成员变量上，生成**该成员变量**的getter/setter方法

### 注意
* final修饰的字段不会生成set方法
* static修饰的方法不会生成set&get方法

#### 注解作用在类上
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@Setter
@Getter
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    private static String adderss;
}
```
#### 编译后的效果`mv compile`：不能使用的话看看有没有配置maven的环境变量
```java
public class TestModel {
    private Integer id;
    private final String name = "";
    private static String adderss;

    public TestModel() {
    }

    public void setId(final Integer id) { this.id = id; }

    public Integer getId() { return this.id; }

    public String getName() { 
        this.getClass();
        return "gudao";
    }
}
```

#### 注解作用在属性上
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
public class TestModel {
    @Getter
    @Setter
    private Integer id;
    private String name;
    private String adderss;
}
```
#### 编译后的效果
```java
public class TestModel {
    private Integer id;
    private String name;
    private String adderss;

    public TestModel() { }

    public Integer getId() { return this.id; }

    public void setId(final Integer id) { this.id = id; }
}
```

### 方法控制访问级别
* 注解中添加`AccessLevel.访问级别`来控制属性的访问级别
```java
// 该注解就是说明让lombok在生成get、set方法的时候不生成该属性的get方法
@Getter(AccessLevel.NONE)
private String name;
```
#### 控制级别详解
* PUBLIC：将被该注解加上的属性生成pubilc方法
* MODULE：将被该注解加上的属性不会生成访问级别，截止到写该文章和package没啥区别，如：`void setId(final Integer id) { this.id = id; }`
* PROTECTED：将被该注解加上的属性生成protected方法
* PACKAGE：将被该注解加上的属性不会生成访问级别，截止到写该文章和module没啥区别，如：`void setId(final Integer id) { this.id = id; }`
* PRIVATE：将被该注解加上的属性生成private方法
* NONE：被加上该注解的属性不会生成方法