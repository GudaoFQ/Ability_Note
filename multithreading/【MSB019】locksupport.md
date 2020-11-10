## LockSupport
> LockSupport是一个线程阻塞工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，当然阻塞之后肯定得有唤醒的方法。
> LockSupport 和 CAS 是Java并发包中很多并发工具控制机制的基础，它们底层其实都是依赖Unsafe实现

#### 常用的方法
> 主要有两类方法：park和unpark。

    public static void park(Object blocker); // 暂停当前线程
    public static void parkNanos(Object blocker, long nanos); // 暂停当前线程，不过有超时时间的限制
    public static void parkUntil(Object blocker, long deadline); // 暂停当前线程，直到某个时间
    public static void park(); // 无期限暂停当前线程
    public static void parkNanos(long nanos); // 暂停当前线程，不过有超时时间的限制
    public static void parkUntil(long deadline); // 暂停当前线程，直到某个时间
    public static void unpark(Thread thread); // 恢复当前线程
    public static Object getBlocker(Thread t);
    
LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。LockSupport 提供park()和unpark()方法实现阻塞线程和解除线程阻塞，LockSupport和每个使用它的线程都与一个许可(permit)关联。permit相当于1，0的开关，默认是0，调用一次unpark就加1变成1，调用一次park会消费permit, 也就是将1变成0，同时park立即返回。再次调用park会变成block（因为permit为0了，会阻塞在这里，直到permit变为1）, 这时调用unpark会把permit置为1。每个线程都有一个相关的permit, permit最多只有一个，重复调用unpark也不会积累。

park()和unpark()不会有 “Thread.suspend和Thread.resume所可能引发的死锁” 问题，由于许可的存在，调用 park 的线程和另一个试图将其 unpark 的线程之间的竞争将保持活性。

如果调用线程被中断，则park方法会返回。同时park也拥有可以设置超时时间的版本。

需要特别注意的一点：park 方法还可以在其他任何时间“毫无理由”地返回，因此通常必须在重新检查返回条件的循环里调用此方法。从这个意义上说，park 是“忙碌等待”的一种优化，它不会浪费这么多的时间进行自旋，但是必须将它与 unpark 配对使用才更高效。

三种形式的 park 还各自支持一个 blocker 对象参数。此对象在线程受阻塞时被记录，以允许监视工具和诊断工具确定线程受阻塞的原因。（这样的工具可以使用方法 getBlocker(java.lang.Thread) 访问 blocker。）建议最好使用这些形式，而不是不带此参数的原始形式。在锁实现中提供的作为 blocker 的普通参数是 this。
    
#### 总结
1. park和unpark可以实现类似wait和notify的功能，但是并不和wait和notify交叉，也就是说unpark不会对wait起作用，notify也不会对park起作用。
2. park和unpark的使用不会出现死锁的情况
3. blocker的作用是在dump线程的时候看到阻塞对象的信息
