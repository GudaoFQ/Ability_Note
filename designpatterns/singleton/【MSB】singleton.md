## 介绍
> 单例设计模式（Singleton Design Pattern）理解起来非常简单。一个类只允许创建一个对象（或者实例），那这个类就是一个单例类，这种设计模式就叫作单例设计模式，简称单例模式。

###　饿汉模式

> 类加载到内存后，就实例化一个单例，JVM保证能程安全<br>
> 商单实用，推荐对！<br>
> 唯一来点：不管用到与否，如装我时就完成实向化<br>
> （话说你不朋的，你装我它干啥）
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

### 懒汉模式

> 静态内部类的方式实现懒汉加载<br>
> 线程安全是通过JVM内部自己实现的
```java
public class SingletonBestInternalLazy {
    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestInternalLazy() {
    }

    //静态内部类是所在类被加载的时候才会创建静态内部类
    public static class internalClass {
        private final static SingletonBestInternalLazy singletonBestInternalLazy = new SingletonBestInternalLazy();
    }

    public static SingletonBestInternalLazy instance() {
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

> 懒汉模式的完美单例<br>
> 通过双重检查与双重锁来实现代码的优化
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