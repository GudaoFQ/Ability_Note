## Optional
* Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象
* Optional 是个容器：它可以保存类型T的值，或者仅仅保存null。Optional提供很多有用的方法，这样我们就不用显式进行空值检测
* Optional 类的引入很好的解决空指针异常

#### 方法API

| 方法                                                                         | 说明                                                                                                                         |
| ---------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| static <T> Optional<T> empty()                                               | 返回空的 Optional 实例 |
| boolean equals(Object obj)                                                   | 判断其他对象是否等于 Optional |
| Optional<T> filter(Predicate<? super <T> predicate)                          | 如果值存在，并且这个值匹配给定的 predicate，返回一个Optional用以描述这个值，否则返回一个空的Optional |
| <U> Optional<U> flatMap(Function<? super T,Optional<U>> mapper)              | 如果值存在，返回基于Optional包含的映射方法的值，否则返回一个空的Optiona |
| T get()                                                                      | 如果在这个Optional中包含这个值，返回值，否则抛出异常：NoSuchElementExceptio |
| int hashCode()                                                               | 返回存在值的哈希码，如果值不存在 返回 0 |
| void ifPresent(Consumer<? super T> consumer)                                 | 如果值存在则使用该值调用 consumer , 否则不做任何事情 |
| boolean isPresent()                                                          | 如果值存在则方法会返回true，否则返回 false |
| <U>Optional<U> map(Function<? super T,? extends U> mapper)                   | 如果有值，则对其执行调用映射函数得到返回值。如果返回值不为 null，则创建包含映射返回值的Optional作为map方法返回值，否则返回空Optional |
| static <T> Optional<T> of(T value)                                           | 返回一个指定非null值的Optional |
| static <T> Optional<T> ofNullable(T value)                                   | 如果为非空，返回 Optional 描述的指定值，否则返回空的 Optional |
| T orElse(T other)                                                            | 如果存在该值，返回值， 否则返回 other |
| T orElseGet(Supplier<? extends T> other)                                     | 如果存在该值，返回值， 否则触发 other，并返回 other 调用的结果 |
| <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) | 如果存在该值，返回包含的值，否则抛出由 Supplier 继承的异 |
| String toString()                                                            | 返回一个Optional的非空字符串，用来调试 |

#### 测试方法
```java
/**
 * Author : GuDao
 * 2020-12-03
 */
public class OptionalAdvanced {
    public static void main(String[] args) {
        final String text = "Hallo world!";
        System.out.println(text.length());
        Optional.ofNullable(text)//显示创建一个Optional壳
                .map(OptionalAdvanced::print)
                .map(OptionalAdvanced::print)// ""
                .ifPresent(System.out::println);// true

        System.out.println("----------------");

        Optional.ofNullable(text)
                .map(s ->{
                    System.out.println(s);
                    return s.substring(6);// world!
                })
                .map(s -> null)//返回 null
                .ifPresent(System.out::println);

    }
    // 打印并截取str[5]之后的字符串
    private static String print(String str) {
        System.out.println(str);
        String substring = str.substring(6);
        System.out.println("截取后：" + substring);
        return substring;
    }
}

/**
 * Author : GuDao
 * 2020-12-03
 */
public class OptionalDemo {
    public static void main(String[] args) {

        User user = null;
        // Optional.ofNullable - 允许传递为 null 参数
        Optional<User> optionalUser = Optional.ofNullable(user);
        System.out.println("判断user是否为空：" + optionalUser);// Optional.empty

        // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
        //Optional<User> user1 = Optional.of(user);

        User userInfo = new User("", 11);
        // 参数判空，只能判断参数是否为null，不能判断参数是否是""
        boolean present = Optional.ofNullable(userInfo.getName()).isPresent();
        System.out.println("判读userInfo中的name是否为null：" + present);

        Integer sum = sum(Optional.ofNullable(null), Optional.ofNullable(8));
        System.out.println("sum：" + sum);
    }

    public static Integer sum(Optional<Integer> a, Optional<Integer> b) {

        // Optional.isPresent - 判断值是否存在

        System.out.println("第一个参数值存在: " + a.isPresent());
        System.out.println("第二个参数值存在: " + b.isPresent());

        // Optional.orElse - 如果值存在，返回它，否则返回默认值
        Integer value1 = a.orElse(new Integer(0));

        //Optional.get - 获取值，值需要存在
        Integer value2 = b.get();
        return value1 + value2;
    }
}

class User {
    private String name;
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }
}
```