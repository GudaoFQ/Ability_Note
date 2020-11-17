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
##### get()方法
```java
public T get() {
    // 获取当前线程
    Thread t = Thread.currentThread();
    // 获取线程里的map
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```
ThreadLocal 中的get()方法，关键的 map 是在 Thread 类中的threadLocals变量，让我们继续看看 ThreadLocalMap 的源代码：
```java
ThreadLocal.ThreadLocalMap threadLocals = null;

static class ThreadLocalMap {
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            // 使用ThreadLocal作为key，并且是弱引用
            super(k);
            value = v;
        }
    }

    // 省略代码
}
```
此处的Entry它继承的是WeakReference，是弱引用，说明没有对象指向它，Entity就会被回收   
* ThreadLocalMap 的 Entry 对 ThreadLocal 的引用为弱引用。
* ThreadLocal 本身并不存储值，具体的 value 依旧在各个线程中。

#### 注意
* 使用弱引用的原因在于，当没有强引用指向 ThreadLocal 变量时，它可被回收，从而避免上文所述 ThreadLocal 不能被回收而造成的内存泄漏的问题。
* 但是，这里又可能出现另外一种内存泄漏的问题。ThreadLocalMap 维护 ThreadLocal 变量与具体实例的映射，当 ThreadLocal 变量被回收后，该映射的键变为 null，该 Entry 无法被移除。从而使得实例被该 Entry 引用而无法被回收造成**内存泄漏**。

#### 防止内存泄漏
> 对于已经不再被使用且已被回收的 ThreadLocal 对象，它在每个线程内对应的实例由于被线程的 ThreadLocalMap 的 Entry 强引用，无法被回收，可能会造成内存泄漏。
针对该问题，ThreadLocalMap 的 set 方法中，通过 replaceStaleEntry 方法将所有键为 null 的 Entry 的值设置为 null，从而使得该值可被回收。另外，会在 rehash 方法中通过 expungeStaleEntry 方法将键和值为 null 的 Entry 设置为 null 从而使得该 Entry 可被回收。通过这种方式，ThreadLocal 可防止内存泄漏。
```java
private void set(ThreadLocal<?> key, Object value) {
  Entry[] tab = table;
  int len = tab.length;
  int i = key.threadLocalHashCode & (len-1);

  for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
    ThreadLocal<?> k = e.get();
    if (k == key) {
      e.value = value;
      return;
    }
    if (k == null) {
      replaceStaleEntry(key, value, i);
      return;
    }
  }
  tab[i] = new Entry(key, value);
  int sz = ++size;
  if (!cleanSomeSlots(i, sz) && sz >= threshold)
    rehash();
}
```

#### ThreadLocal用途 
> 声明式事务,保证同一个Connection