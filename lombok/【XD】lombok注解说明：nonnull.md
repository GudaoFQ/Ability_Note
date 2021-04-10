## Lombok注解说明：NonNull

### NonNull
* 用在成员方法或者构造方法的参数前面
* 用在属性上面

#### 属性上面配置
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */

public class TestModel {
    @Setter
    @Getter
    @NonNull
    private Integer id;
}
```
#### 编译后文件
```java
public class TestModel {
    @NonNull
    private Integer id;

    public TestModel() {
    }

    public void setId(@NonNull final Integer id) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        } else {
            this.id = id;
        }
    }

    @NonNull
    public Integer getId() {
        return this.id;
    }
}
```
#### 方法上面配置
```java
/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */

public class TestModel {
    private Integer id;

    public void setTestId(@NonNull Integer id){
        this.id = id;
    }
}
```
#### 编译后文件
```java
public class TestModel {
    private Integer id;

    public TestModel() {
    }

    public void setTestId(@NonNull Integer id) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        } else {
            this.id = id;
        }
    }
}
```