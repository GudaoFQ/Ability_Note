## Lombok注解说明：ToString & EqualsAndHashCode
### 源码说明
#### ToString
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ToString {
	// 输出是否带字段名称
	boolean includeFieldNames() default true;
	// 排除字段，以 String[] 格式设置
	String[] exclude() default {};
	// 指定输出哪些字段，不过将来会被标注@deprecated，使用 @ToString.Include 替代
	String[] of() default {};
	// 是否调用父类的 toString() 方法
	boolean callSuper() default false;
	// 不使用 getter 方法获取字段值
	boolean doNotUseGetters() default false;
	// 是否只输出被 @ToString.Include 备注的属性，默认情况输出所有非静态字段
	boolean onlyExplicitlyIncluded() default false;
	
	// 标注不被输出的字段
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Exclude {}
	
	// 标注被输出的字段 @ToString.Include
	@Target({ElementType.FIELD, ElementType.METHOD})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Include {
		// 输出字段的顺序，值越高越先被输出
		int rank() default 0;
		// 默认为带注释的成员的字段/方法名称。可以使用此方式使用新的名称
		String name() default "";
	}
}
```
#### EqualsAndHashCode
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface EqualsAndHashCode {
	// 排除指定字段
	String[] exclude() default {};
	// 包含指定字段，将来会不再使用，使用 @EqualsAndHashCode.Include 代替
	String[] of() default {};
	// 是否在计算之前调用父类的实现
	boolean callSuper() default false;
	// 是否使用 getter
	boolean doNotUseGetters() default false;
	// 在生成的 equals 和 hashCode 的入参添加注解。e.g. @NonNull
    // up to JDK7: @EqualsAndHashCode(onParam=@__({@AnnotationsGoHere}))
    // from JDK8: @EqualsAndHashCode(onParam_={@AnnotationsGohere})
	AnyAnnotation[] onParam() default {};
	// 是否只使用 @EqualsAndHashCode.Include 标注的字段：可以通过使用@EqualsAndHashCode.Include并使用@EqualsAndHashCode(onlyExplicitlyIncluded = true)标记它们来准确指定希望使用的字段或方法
	boolean onlyExplicitlyIncluded() default false;

	@Deprecated
	@Retention(RetentionPolicy.SOURCE)
	@Target({})
	@interface AnyAnnotation {}
    
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Exclude {}

	@Target({ElementType.FIELD, ElementType.METHOD})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Include {
		String replaces() default "";
	}
}
```

### ToString说明与注意
> 生成ToString方法
* transient和static修饰的字段不会被添加到tostring方法中去

#### 实例说明
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@ToString
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    // 不会被加进tostring中去
    private transient String email;
    // 不会被加进tostring中去
    private static String adderss;
}
```
#### 编译后文件
```java
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    private static String adderss;
    private transient String email;

    public TestModel() {
    }

    public String toString() {
        StringBuilder var10000 = (new StringBuilder()).append("TestModel(id=").append(this.id).append(", name=");
        this.getClass();
        return var10000.append("gudao").append(")").toString();
    }
}
```
#### @ToString中的属性配置说明
* exclude：通过`@ToString(exclude = {"name","id"})`将exclud等号后面的属性排除出去，不在tostring中生成对应的属性
* of：通过`@ToString(of = {"name"})`让其只生成of等号后面属性对应的tostring方法
* callSuper：是否引用父类的toString方法；默认为false；true为引用，编译后：`public String toString() { return "User(super=" + super.toString() + "...)"; }`
* onlyExplicitlyIncluded：是否显示字段名
  ```java
  @ToString(includeFieldNames = false)
  @Getter @Setter
  public class User {
    private String username;
    private String password;
    private boolean isAdult;
  }
  
  // 编译后：
  public String toString() {
      return "User(" + this.getUsername() + ", " + this.getPassword() + ", " + this.isAdult() + ")";
  }
  // 输出信息：User(name,password,false)
  ```
* doNotUseGetters：是否使用getter获取值；false时`this.getUsername()`，true时`this.password`
  ```java
  @ToString(doNotUseGetters = true)
  @Getter @Setter
  public class User {
    private String username;
    private String password;
    private boolean isAdult;
  }
  
  // 编译后：
  public String toString() {
    return "User(username=" + this.username + ", password=" + this.password + ", isAdult=" + this.isAdult + ")";
  }
  ```
* @ToString.Include：只输出指定字段
  ```java
  @ToString(
          doNotUseGetters = true,
          onlyExplicitlyIncluded = true
  )
  @Getter @Setter
  public class User {
      // 指定字段
      @ToString.Include
      private String username;
      // 指定字段
      @ToString.Include
      private String password;
      private boolean isAdult;
      private boolean isAdult;
  }
  
  // 编译后：
  public String toString() {
      return "User(username=" + this.username + ", password=" + this.password + ")";
  }
  ```
* @ToString.Include(rank = 1,name = "")：**指定输出字段进行排序，以及设置别名**；rank值越大，排的越靠前
  ```java
  @ToString
  @Getter @Setter
  public class User {
      @ToString.Include(rank = 1)
      private String username;
      @ToString.Include(rank = 2,name = "pwd")
      private String password;
      private boolean isAdult;
  }
  
  // 编译后：
  public String toString() {
      return "User(pwd=" + this.password + ", username=" + this.username + ")";
  }
  ```

### EqualsAndHashCode说明与注意
> 重写equals和hashcode方法
* transient和static修饰的字段不会被添加到equals和hashcode方法中去

#### 实例演示
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@EqualsAndHashCode
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    // 不会被加进equals和hashcode中去
    private transient String email;
    // 不会被加进equals和hashcode中去
    private static String adderss;
}
```
#### 编译后的文件
```java
public class TestModel {
    private Integer id;
    private final String name = "gudao";
    private transient String email;
    private static String adderss;

    public TestModel() {
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TestModel)) {
            return false;
        } else {
            TestModel other = (TestModel)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.id;
                Object other$id = other.id;
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                this.getClass();
                Object this$name = "gudao";
                other.getClass();
                Object other$name = "gudao";
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestModel;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $id = this.id;
        int result = result * 59 + ($id == null ? 43 : $id.hashCode());
        this.getClass();
        Object $name = "gudao";
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }
}
```
#### @EqualsAndHashCode中的属性配置说明
* exclude：通过`@ToString(exclude = {"name","id"})`将exclud等号后面的属性排除出去，不在tostring中生成对应的属性【准备弃用了，使用EqualsAndHashCode.Exclude和EqualsAndHashCode.Include代替】
* of：通过`@ToString(of = {"name"})`让其只生成of等号后面属性对应的tostring方法【准备弃用了，使用EqualsAndHashCode.Exclude和EqualsAndHashCode.Include代替】
* callSuper：默认false，表示是否将父类的equals和hashCode方法加到该子类的equals和hashCode方法中；通过callSuper=true让其生成的方法中调用父类的方法【使用前提是当前类要继承一个类，不然编译报错】
    ```java
    // 使用后对比发现就多了这个比较
    if (!super.equals(o)) {
    return false;
    }
    ```
* doNotUseGetters：是否使用getter访问成员变量；
* onParam：为equals()和hashCode()的入参加注解（up to JDK7：@EqualsAndHashCode(onParam=@__({@AnnotationsGoHere}))； from JDK8：@EqualsAndHashCode(onParam_={@AnnotationsGohere}) // note the underscore after onParam.）
  ```java
  @EqualsAndHashCode(onParam_ = {@NotNull})
  public class User {
      private String username;
      private String password;
      private boolean isAdult;
  }
  
  // 编译后：
  public class User {
    // 其他省略
    public boolean equals(@NotNull Object o) {
    }
  
    protected boolean canEqual(@NotNull Object other) {
      return other instanceof User;
    }
  
    public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $username = this.getUsername();
      int result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      result = result * 59 + (this.isAdult() ? 79 : 97);
      return result;
    }
  }
  ```
* onlyExplicitlyIncluded：为false时，所有的非静态和非瞬态的字段都会被包含进equals和hashCode方法中；为true时，只有在字段上明确使用了EqualsAndHashCode.Include注解才会被包含进equals和hashCode方法中
 