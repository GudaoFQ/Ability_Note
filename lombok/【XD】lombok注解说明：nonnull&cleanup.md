## Lombok注解说明：NonNull & Cleanup

### 源码
#### Cleanup
```java
@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.SOURCE)
public @interface Cleanup {
	// 调用的方法的名称，默认是使用 close() 方法。
    // 看到方法名是字符串，就应该知道是反射实现的。
	String value() default "close";
}
```

### NonNull说明与注意点
> 当前参数不能为空
* 用在成员方法或者构造方法的参数前面，也可以用在属性上面
* 编译后的class文件中，所有的Getter都在方法上添加了@NonNull，在所有Setter方法和带参构造方法的所有入参添加@NonNull注解
* 参数上使用lombok自己的@lombok.NonNull会导致在您自己的方法或构造函数中插入null-check语句
    ```java
    if (id == null) {
        throw new NullPointerException("id is marked non-null but is null");
    } else {
        this.id = id;
    }
    ```

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

### Cleanup说明与注意点
* Cleanup确保在代码执行后，在退出当前作用域之前自动清除给定资源
* 通过使用@Cleanup来注释任何局部变量声明`@Cleanup InputStream in = new FileInputStream("/../file");`
* 编译后会通过try/finally代码块在局部变量作用范围的末尾，调用in.close()
* Cleanup 指定清理方法`@Cleanup("mothedName")`；要清理的对象类型没有close()方法，而是其他一些无参数方法，则可以指定此方法的名称
```java
@Cleanup("dispose")org.eclipse.swt.widgets.CoolBar bar = new CoolBar(parent,0);
```

#### 实例演示
```java
public class CleanUpTest {
    public static void main(String[] args) throws IOException {
        File file = new File("file");
        File file1 = new File("file1");
        @Cleanup InputStream inputStream = new FileInputStream(file);
        @Cleanup OutputStream outputStream = new FileOutputStream(file1);
        byte[] bytes = new byte[1024];
        int len = -1;
        while (true) {
            len = inputStream.read(bytes);
            if (len == -1) {
                break;
            }
            outputStream.write(bytes,0,len);
        }
    }
}
```
#### 编译后文件
```java
public class CleanUpTest {
    public static void main(String[] args) throws IOException {
        File file = new File("file");
        File file1 = new File("file1");
        FileInputStream inputStream = new FileInputStream(file);

        try {
            FileOutputStream outputStream = new FileOutputStream(file1);

            try {
                byte[] bytes = new byte[1024];
                boolean var6 = true;

                while(true) {
                    int len = inputStream.read(bytes);
                    if (len == -1) {
                        return;
                    }

                    outputStream.write(bytes, 0, len);
                }
            } finally {
                if (Collections.singletonList(outputStream).get(0) != null) {
                    outputStream.close();
                }

            }
        } finally {
            if (Collections.singletonList(inputStream).get(0) != null) {
                inputStream.close();
            }

        }
    }
}
```