## ThreadLocal
> 【ThreadLocal是一个关于创建线程局部变量的类。】ThreadLocal 提供了线程本地的实例。它与普通变量的区别在于，每个使用该变量的线程都会初始化一个完全独立的实例副本。ThreadLocal 变量通常被`private static`修饰。当一个线程结束时，它所使用的所有 ThreadLocal 相对的实例副本都可被回收。


通常情况下，我们创建的变量是可以被任何一个线程访问并修改的。而使用ThreadLocal创建的变量**只能被当前线程访问**，其他线程则无法访问和修改。

#### 常用方法
* ThreadLocal<String> threadLocalDemo = new ThreadLocal<>();
> 创建
* threadLocalDemo.set("value");
> 注参
* threadLocalDemo.get();
> 获取值

#### 理解
ThreadLoal 变量，它的基本原理是，同一个 ThreadLocal 所包含的对象（对ThreadLocal< String >而言即为 String 类型变量），在不同的 Thread 中有不同的副本（实际是不同的实例，后文会详细阐述）。这里有几点需要注意
* 因为每个 Thread 内有自己的实例副本，且该副本只能由当前 Thread 使用。这是也是 ThreadLocal 命名的由来
* 既然每个 Thread 有自己的实例副本，且其它 Thread 不可访问，那就不存在多线程间共享的问题
* 既无共享，何来同步问题，又何来解决同步问题一说？

#### 源码
set Thread.currentThread.map(ThreadLocal, person) 
> 设到了当前线程的map中 

#### ThreadLocal用途 
> 声明式事务,保证同一个Connection