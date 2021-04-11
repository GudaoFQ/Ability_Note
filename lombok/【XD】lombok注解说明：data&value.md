## Lombok注解说明：Data & Value

### 源码
#### Data
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Data {
	// 使用静态构造方法，私有实体构造器，通过 staticConstructor 配置静态构造方法的名称
	String staticConstructor() default "";
}
```
#### Value
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Value {
	// 使用静态构造方法，私有实体构造器，通过 staticConstructor 配置静态构造方法的名称
	String staticConstructor() default "";
}
```

### Data说明与注意点
* 用在实体类上
* 包含了：@Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor
* 生成所有字段的getter，所有非final和非static字段的setter
* 实现相应类字段的toString，equals，和hashCode
* 构造方法将初始化所有没有初始化值的final字段，和所有使用@NonNull标记却没有初始化值的非final字段，从而确保字段永远不为空
* 在生成构造器时，会生成带有这样两种参数的构造器
    * 实体类有未被初始化的final字段
    * 实体类中标有@NonNull注解的字段
* 可以通过`@Data(staticConstructor = mothedName)`来生成的静态方法，等号后面的methodName就是名称
  ```java
  @Data(staticConstructor = "testName")
  public class TestModel {
    private Integer id;
    @NonNull
    private Integer password;
    private final String name = "gudao";
    private final Integer age;
    private transient String email;
    private static String adderss;
  }
  
  // 编译后会多生成一个静态方法
  // ...省略其余代码
  public static TestModel testName(@NonNull final Integer password, final Integer age) {
      return new TestModel(password, age);
  }
  ```
    
#### 实例演示
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@Data
public class TestModel {
    private Integer id;
    @NonNull
    private Integer password;
    private final String name = "gudao";
    private final Integer age;
    private transient String email;
    private static String adderss;
}
```
#### 编译后文件
```java
public class TestModel {
    private Integer id;
    @NonNull
    private Integer password;
    private final String name = "gudao";
    private final Integer age;
    private transient String email;
    private static String adderss;

    // 在生成构造器时，会生成带有这样两种参数的构造器：实体类有未被初始化的final字段 实体类中标有@NonNull注解的字段
    public TestModel(@NonNull final Integer password, final Integer age) {
        if (password == null) {
            throw new NullPointerException("password is marked non-null but is null");
        } else {
            this.password = password;
            this.age = age;
        }
    }

    public Integer getId() {
        return this.id;
    }

    @NonNull
    public Integer getPassword() {
        return this.password;
    }

    public String getName() {
        this.getClass();
        return "gudao";
    }

    public Integer getAge() {
        return this.age;
    }

    public String getEmail() {
        return this.email;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setPassword(@NonNull final Integer password) {
        if (password == null) {
            throw new NullPointerException("password is marked non-null but is null");
        } else {
            this.password = password;
        }
    }

    public void setEmail(final String email) {
        this.email = email;
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
                label59: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label59;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label59;
                    }

                    return false;
                }

                Object this$password = this.getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }

                Object this$age = this.getAge();
                Object other$age = other.getAge();
                if (this$age == null) {
                    if (other$age != null) {
                        return false;
                    }
                } else if (!this$age.equals(other$age)) {
                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
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
        Object $id = this.getId();
        int result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        Object $age = this.getAge();
        result = result * 59 + ($age == null ? 43 : $age.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "TestModel(id=" + this.getId() + ", password=" + this.getPassword() + ", name=" + this.getName() + ", age=" + this.getAge() + ", email=" + this.getEmail() + ")";
    }
}
```

### Value说明与注意点
* 作用在类上，相当于是Data的不可变形式
* 编译后字段都被修饰为private和final，默认的情况下不会生成settter
* 编译后类本身也是final的，不能被继承
* 静态的字段还是不会做任何处理

#### 实例演示
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@Value
public class TestModel {
    private Integer id;
    @NonNull
    private Integer password;
    private final String name = "gudao";
    private final Integer age;
    private transient String email;
    private static String adderss;
}
```
#### 编译后文件
```java
public final class TestModel {
    private final Integer id;
    @NonNull
    private final Integer password;
    private final String name = "gudao";
    private final Integer age;
    private final transient String email;
    private static String adderss;

    public TestModel(final Integer id, @NonNull final Integer password, final Integer age, final String email) {
        if (password == null) {
            throw new NullPointerException("password is marked non-null but is null");
        } else {
            this.id = id;
            this.password = password;
            this.age = age;
            this.email = email;
        }
    }

    public Integer getId() {
        return this.id;
    }

    @NonNull
    public Integer getPassword() {
        return this.password;
    }

    public String getName() {
        this.getClass();
        return "gudao";
    }

    public Integer getAge() {
        return this.age;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TestModel)) {
            return false;
        } else {
            TestModel other;
            label56: {
                other = (TestModel)o;
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id == null) {
                        break label56;
                    }
                } else if (this$id.equals(other$id)) {
                    break label56;
                }

                return false;
            }

            label49: {
                Object this$password = this.getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password == null) {
                        break label49;
                    }
                } else if (this$password.equals(other$password)) {
                    break label49;
                }

                return false;
            }

            Object this$age = this.getAge();
            Object other$age = other.getAge();
            if (this$age == null) {
                if (other$age != null) {
                    return false;
                }
            } else if (!this$age.equals(other$age)) {
                return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
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

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $id = this.getId();
        int result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        Object $age = this.getAge();
        result = result * 59 + ($age == null ? 43 : $age.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "TestModel(id=" + this.getId() + ", password=" + this.getPassword() + ", name=" + this.getName() + ", age=" + this.getAge() + ", email=" + this.getEmail() + ")";
    }
}
```