## Lombok注解说明：ToString & EqualsAndHashCode

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
 