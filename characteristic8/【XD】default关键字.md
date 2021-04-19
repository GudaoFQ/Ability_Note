## 接口中的Default关键字

### 说明
* 在jdk1.8以前接⼝⾥⾯是只能有抽象⽅法，不能有任何⽅法的实现的
* jdk1.8⾥⾯打破了这个规定，引⼊了新的关键字default，使⽤default修饰⽅法，可以在接⼝⾥⾯ 定义具体的⽅法实现

### 案例说明
* 默认⽅法： 接⼝⾥⾯定义⼀个默认⽅法，这个接⼝的**实现类实现了这个接⼝之后，不⽤管这个default修饰的⽅法就可以直接调⽤**，即接⼝⽅法的默认实现
```java
public interface Animal {
    void run();

    void eat();

    default void breath() {
        System.out.println("使⽤氧⽓呼吸");
    }
}
```
* 静态⽅法: 接⼝名.静态⽅法来访问接⼝中的静态⽅法
```java
public interface Animal {
    void run();

    void eat();

    static void test() {
        System.out.println("这是静态⽅法");
    }
}
```