## Lombok注解说明：NoArgsConstructor & AllArgsConstructor & RequiredArgsConstructor
### 源码说明
#### NoArgsConstructor
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface NoArgsConstructor {
	String staticName() default "";
    // 在构造方法的入参上添加注解。e.g. @NonNull
    // up to JDK7: @EqualsAndHashCode(onParam=@__({@AnnotationsGoHere}))
    // from JDK8: @EqualsAndHashCode(onParam_={@AnnotationsGohere})
	AnyAnnotation[] onConstructor() default {};
	// 设置构造方法的访问权限，默认是 public
	AccessLevel access() default lombok.AccessLevel.PUBLIC;
	// 是否强制对未赋值的final字段初始化值。
	boolean force() default false;
	
	@Deprecated
	@Retention(RetentionPolicy.SOURCE)
	@Target({})
	@interface AnyAnnotation {}
}
```
#### NoArgsConstructor
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AllArgsConstructor {
	// 是否使用静态构造器，并制定静态构造器的名称
	String staticName() default "";
	// 构造器入参添加注解
	AnyAnnotation[] onConstructor() default {};
	// 设置构造器的访问权限
	AccessLevel access() default lombok.AccessLevel.PUBLIC;

	@Deprecated
	@Retention(RetentionPolicy.SOURCE)
	@Target({})
	@interface AnyAnnotation {}
}
```
#### RequiredArgsConstructor
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RequiredArgsConstructor {
	// 是否使用静态构造器，并制定静态构造器的名称
	String staticName() default "";
	// 构造器入参添加注解
	AnyAnnotation[] onConstructor() default {};

	@Deprecated
	@Retention(RetentionPolicy.SOURCE)
	@Target({})
	@interface AnyAnnotation {}
}
```

### NoArgsConstructor&AllArgsConstructor有参与无参构造函数注解说明与注意点
* 默认情况下编译后的文件是会自动添加无参构造函数的
* 当使用`AllArgsConstructor`修饰类的时候，final修饰的字段和static修饰的字段不会将参数添加到有参构造方法中去
* 如果类中含有final修饰的成员变量并**未给这个属性赋值**，是无法使用@NoArgsConstructor注解的
* 当使用了`AllArgsConstructor`之后最好将`NoArgsConstructor`也添加到类注解中
* 可用@NoArgsConstructor(force = true)为没有初始化的final字段设置默认值 0 / false / null, 这样编译器就不会报错
    ```java
    @NoArgsConstructor(force = true)
    public class User {
        private final String gender;
        private String username;
        private String password;
    }
    // 编译后：
    public class User {
        private final String gender = null;
        private String username;
        private String password;
    
        public User() { }
    }
    ```

#### 实例演示
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    private static String adderss;
}
```
#### 编译后文件
```java
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    private static String adderss;

    public TestModel() {
    }

    public TestModel(final Integer id) {
        this.id = id;
    }
}
```

### RequiredArgsConstructor说明与注意
> 使用类中所有带有@NonNull注解的或者带有final修饰的**未被初始化**成员变量生成对应的构造方法
* 有final修饰的字段时，RequiredArgsConstructor不能和NoArgsConstructor一起使用；没有final修饰的字段则没有问题
* 不需要给final属性提前赋值
* static修饰的字段不会将参数添加到构造函数中去
* 通过`RequiredArgsConstructor(staticName = methodName)`修饰时，此时会**生成所有final修饰的有参构造函数**外，还会生成一个指定名称的**静态方法**（methodName就是生成的方法名），返回一个调用相应的**构造方法产生的对象**
* **当所有字段都是final修饰的情况下，RequiredArgsConstructor和AllArgsConstructor只能使用其中一个**

#### 实例演示
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@RequiredArgsConstructor(staticName = "test")
public class TestModel {
    // 没有被final修饰或者添加@NonNull注解也不会生成
    private Integer id;
    // static属性不会通过RequiredArgsConstructor生成
    private static String adderss;
    // 虽然被final修饰了，但是该属性已经被初始化，所以不会通过@RequiredArgsConstructor生成
    private final Integer age = 11;
    @NonNull
    private String email;
    private final String name;
}
```
#### 编译后文件
```java
public class TestModel {
    private Integer id;
    private static String adderss;
    private final Integer age = 11;
    @NonNull
    private String email;
    private final String name;
    
    // 将所有的final未被赋值/NonNull修饰的字段生成一个有参构造函数
    private TestModel(final String name, @NonNull final String email) {
        if (email == null) {
            throw new NullPointerException("email is marked non-null but is null");
        } else {
            this.name = name;
            this.email = email;
        }
    }
    // 通过@RequiredArgsConstructor生成的一个静态方法，形参都是final修饰的字段，并返回当前实体对象
    public static TestModel test(final String name, @NonNull final String email) {
        return new TestModel(name, email);
    }
}
```