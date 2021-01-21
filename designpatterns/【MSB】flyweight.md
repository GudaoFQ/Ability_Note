##　享元模式
> 运用共享技术有效地支持大量细粒度对象的复用。系统只使用少量的对象，而这些对象都很相似，状态变化很小，可以实现对象的多次复用。由于享元模式要求能够共享的对象必须是细粒度对象，因此它又称为轻量级模式，它是一种对象结构型模式。享元模式结构较为复杂，一般结合工厂模式一起使

#### 使用细节
* Flyweight（抽象享元类）
> 通常是一个接口或抽象类，在抽象享元类中声明了具体享元类公共的方法，这些方法可以向外界提供享元对象的内部数据（内部状态），同时也可以通过这些方法来设置外部数据（外部状态）。
* ConcreteFlyweight（具体享元类）
> 它实现了抽象享元类，其实例称为享元对象；在具体享元类中为内部状态提供了存储空间。通常我们可以结合单例模式来设计具体享元类，为每一个具体享元类提供唯一的享元对象。
* UnsharedConcreteFlyweight（非共享具体享元类）
> 并不是所有的抽象享元类的子类都需要被共享，不能被共享的子类可设计为非共享具体享元类；当需要一个非共享具体享元类的对象时可以直接通过实例化创建。【是不可以共享的外部状态，它以参数的形式注入具体享元的相关方法中。】
* FlyweightFactory（享元工厂类）
> 享元工厂类用于创建并管理享元对象，它针对抽象享元类编程，将各种类型的具体享元对象存储在一个享元池中，享元池一般设计为一个存储“键值对”的集合（也可以是其他类型的集合），可以结合工厂模式进行设计；当用户请求一个具体享元对象时，享元工厂提供一个存储在享元池中已创建的实例或者创建一个新的实例（如果不存在的话），返回新创建的实例并将其存储在享元池中。

#### 优缺点
> 优点
1. 相同对象只要保存一份，这降低了系统中对象的数量，从而降低了系统中细粒度对象给内存带来的压力。

> 缺点
1. 为了使对象可以共享，需要将一些不能共享的状态外部化，这将增加程序的复杂性。
2. 读取享元模式的外部状态会使得运行时间稍微变长。

#### 经典使用场景
1. 一个系统有大量相同或者相似的对象，造成内存的大量耗费。
2. 对象的大部分状态都可以外部化，可以将这些外部状态传入对象中。
3. 在使用享元模式时需要维护一个存储享元对象的享元池，而这需要耗费一定的系统资源，因此，应当在需要多次重复使用享元对象时才值得使用享元模式。

### 单纯享元模式
> 在单纯享元模式中，所有的具体享元类都是可以共享的，不存在非共享具体享元类。
### 复合享元模式
> 将一些单纯享元对象使用组合模式加以组合，还可以形成复合享元对象，这样的复合享元对象本身不能共享，但是它们可以分解成单纯享元对象，而后者则可以共享

### Flyweight（抽象享元类）
```java
/**
 * 抽象共享类
 * Author : GuDao
 * 2020-10-26
 */
public abstract class Flyweight {
    abstract void operation(UnSpecoficFlyweight unSpecoficFlyweight);
}
```

### ConcreteFlyweight（具体享元类）
```java
/**
 * 共享实例
 * Author : GuDao
 * 2020-10-26
 */
public class SpecificFlyweight extends Flyweight {
    private String key;

    //提供共享类实例方式（为了方便测试，就用了有参构造）
    public SpecificFlyweight(String name) {
        this.key = name;
        System.out.println("创建了对象："+name);
    }
    /**
     * 给共享类加入非共享部分【非必要的部分】
     *
     * @param unSpecoficFlyweight 非共享部分
     */
    @Override
    void operation(UnSpecoficFlyweight unSpecoficFlyweight) {
        System.out.println(key+"的非共享信息为："+unSpecoficFlyweight.getInfo());
    }
}
```

### UnsharedConcreteFlyweight（非共享具体享元类）
```java
/**
 * 非共享元素，个人认为就是一次新展示的信息，在指定的共享类中展示
 * Author : GuDao
 * 2020-10-26
 */
public class UnSpecoficFlyweight {
    private String info;
    public UnSpecoficFlyweight(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
    public UnSpecoficFlyweight setInfo(String info) {
        this.info = info;
        return this;
    }
}
```

###　FlyweightFactory（享元工厂类）
```java
/**
 * 共享工厂
 * Author : GuDao
 * 2020-10-26
 */
public class FlyweightFactory {
    private HashMap<String, Flyweight> flyweihts = new HashMap<>();
    private Flyweight flyweight;
    public Flyweight getFlyweight(String key){
        if(flyweihts.containsKey(key)){
            flyweight = flyweihts.get(key);
            System.out.println("factory调用了对象："+key);
        }else {
            flyweight = new SpecificFlyweight(key);
            flyweihts.put(key, flyweight);
        }
        return flyweight;
    }
}
```

### 测试类
```java
/**
 * Author : GuDao
 * 2020-10-26
 */
public class Main {
    public static void main(String[] args) {
        FlyweightFactory fOne = new FlyweightFactory();
        Flyweight one = fOne.getFlyweight("a");
        Flyweight two = fOne.getFlyweight("a");

        Flyweight three = fOne.getFlyweight("b");
        Flyweight four = fOne.getFlyweight("a");
        Flyweight five = fOne.getFlyweight("b");

        one.operation(new UnSpecoficFlyweight("测试001"));
        two.operation(new UnSpecoficFlyweight("测试002"));
        five.operation(new UnSpecoficFlyweight("测试003"));
        
        System.out.println(one == two);
    }
}
```

### 执行结果
```shell
创建了对象：a
factory调用了对象：a
创建了对象：b
factory调用了对象：a
factory调用了对象：b
a的非共享信息为：测试001
a的非共享信息为：测试002
b的非共享信息为：测试003
true
```