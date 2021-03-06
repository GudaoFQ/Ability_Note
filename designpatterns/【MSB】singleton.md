## 单例模式介绍
> 单例模式（Singleton Design Pattern）理解起来非常简单。一个类只允许创建一个对象（或者实例），那这个类就是一个单例类，这种设计模式就叫作单例设计模式，简称单例模式。
1. 单例类只有一个实例对象；
2. 该单例对象必须由单例类自行创建；
3. 单例类对外提供一个访问该单例的全局访问点。

#### 使用细节
* 构造函数需要是 private 访问权限的，这样才能避免外部通过 new 创建实例；
* 考虑对象创建时的线程安全问题；
* 考虑是否支持延迟加载；
* 考虑 getInstance() 性能是否高（是否加锁）

#### 优缺点
> 优点
1. 单例模式可以保证内存里只有一个实例，减少了内存的开销。
2. 可以避免对资源的多重占用。
3. 单例模式设置全局访问点，可以优化和共享资源的访问。

> 缺点
1. 单例模式一般没有接口，扩展困难。如果要扩展，则除了修改原来的代码，没有第二种途径，违背开闭原则。
2. 在并发测试中，单例模式不利于代码调试。在调试过程中，如果单例中的代码没有执行完，也不能模拟生成一个新的对象。
3. 单例模式的功能代码通常写在一个类中，如果功能设计不合理，则很容易违背单一职责原则。

#### 经典应用场景
1. 需要频繁创建的一些类，使用单例可以降低系统的内存压力，减少 GC。
2. 某类只要求生成一个对象的时候，如一个班中的班长、每个人的身份证号等。
3. 某些类创建实例时占用资源较多，或实例化耗时较长，且经常使用。
4. 某类需要频繁实例化，而创建的对象又频繁被销毁的时候，如多线程的线程池、网络连接池等。
5. 频繁访问数据库或文件的对象。
6. 对于一些控制硬件级别的操作，或者从系统上来讲应当是单一控制逻辑的操作，如果有多个实例，则系统会完全乱套。
7. 当对象需要被共享的场合。由于单例模式只允许创建一个对象，共享该对象可以节省内存，并加快对象访问速度。如 Web 中的配置对象、数据库的连接池等。

###　饿汉模式
> 类加载到内存后，就实例化一个单例，JVM保证能程安全<br>
> 简单实用，推荐！<br>
> 在类加载的时候，instance 静态实例就已经创建并初始化好了，所以，instance 实例的创建过程是线程安全的。不过，这样的实现方式不支持延迟加载<br>
> （话说你不用的，你装我它干啥）
```java
public class SingletonBestHungry {
    private static SingletonBestHungry singletonBase = new SingletonBestHungry();

    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestHungry() {
    }

    public static SingletonBestHungry instance() {
        return singletonBase;
    }

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(SingletonBestHungry.instance().hashCode());
                }
            }).start();
        }
    }
}
```

### 懒汉模式【不推荐使用】
> 我们给 getInstance() 这个方法加了一把大锁（synchronzed），导致这个函数的并发度很低量化一下的话，并发度是 1，也就相当于串行操作了。而这个函数是在单例使用期间，一直会被调用。如果这个单例类偶尔会被用到，那这种实现方式还可以接受。但是，如果频繁地用到，那频繁加锁、释放锁及并发度低等问题，会导致性能瓶颈，这种实现方式就不可取了。
```java
public class SingletonGenerator {
    private static SingletonGenerator instance;
    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonGenerator(){
    }
    
    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new SingletonGenerator();
        }
        return instance;
    }
}
```

### 静态内部类
> 静态内部类的方式实现懒汉加载<br>
> 线程安全是通过JVM内部自己实现的
```java
public class SingletonBestInternalLazy {
    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestInternalLazy() {
    }

    //静态内部类是所在类被加载的时候才会创建静态内部类
    public static class internalClass {
        //通过privite特性【构造方法私有化】，实例化SingletonInternalLazy实体并通过final static让其实体不能被修改
        private final static SingletonBestInternalLazy singletonBestInternalLazy = new SingletonBestInternalLazy();
    }

    public static SingletonBestInternalLazy instance() {
        //调用内部类的方法来得到实体
        return internalClass.singletonBestInternalLazy;
    }

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(SingletonBestInternalLazy.instance().hashCode());
                }
            }).start();
        }
    }
}
```

### 双重检测
> 懒汉模式的完美单例<br>
> 通过双重检查与双重锁来实现代码的优化<br>

*网上有人说，这种实现方式有些问题。因为指令重排序，可能会导致 IdGenerator 对象被new 出来，并且赋值给 instance 之后，还没来得及初始化（执行构造函数中的代码逻辑），就被另一个线程使用了。要解决这个问题，我们需要给 instance 成员变量加上 volatile 关键字，禁止指令重排序才行。实际上，只有很低版本的 Java 才会有这个问题。我们现在用的高版本的 Java 已经在JDK 内部实现中解决了这个问题（解决的方法很简单，只要把对象 new 操作和初始化操作设计为原子操作，就自然能禁止重排序）*
```java
public class SingletonBestLazy {
    //volatile是防止class代码在JVM中指令重排的
    private static volatile SingletonBestLazy singletonBastLazy;

    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestLazy() {
    }

    public static SingletonBestLazy instance() {
        //双重检索
        if (null == singletonBastLazy) {
            synchronized (SingletonBestLazy.class) {
                if (null == singletonBastLazy) {
                    singletonBastLazy = new SingletonBestLazy();
                }
            }
        }
        return singletonBastLazy;
    }

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(SingletonBestLazy.instance().hashCode());
                }
            }).start();
        }
    }
}
```

### 枚举单例
> 解决了线程同步与反序列化<br>
> java枚举类是没有构造方法的，所有JVM就不能通过class文件反射来序列化【个人理解】
```java
public enum SingletonBestEnum {
    INSTANCE;

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(SingletonBestEnum.INSTANCE.hashCode());
                }
            }
        }).start();
    }
}
```