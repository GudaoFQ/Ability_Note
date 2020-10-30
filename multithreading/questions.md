## 多线程中的问题

### 线程启动的三种方式
* 继承Thread类创建线程
* 实现Runnable接口创建线程
* 使用线程池例如用Executor框架来启动【Execitor.newCachedThread()】

### 如何关闭线程
* 关闭线程直接让线程执行完毕就会把线程关闭
* JDK中有stop()方法也能对线程进行关闭，该方法太过粗暴，可能会产生状态的不一致

### wait/notify与sleep的比较.
* 相同点
1. 都会让线程进入阻塞的状态. 都会响应中断.
* 不同点
1. wait/notify 必须在同步方法中去执行(线程更加安全, 防止死锁和永久等待), 而sleep不需要
2. wait/notify 会释放锁, sleep不会释放锁
3. sleep方法必须传递参数,设置休眠时间. 而wait方法可以不传递时间参数, 如果不传递,直到被唤醒
4. wait/notify 所属的Object类(Java对象中,每一个类都是一把锁), sleep属于Thread.

### 多线程与高并发
1. synchronized 当多个线程同时访问同一个资源的时候需要对这个资源上锁。
2. synchronized 既保证原子性，也保证线程间的可见性

## Synchronized

### synchronized保证原子性与线程间可见
```java
public class Thread_009 {
    private int count  = 100000;

    public /*synchronized*/ void run(){
        count--;
        System.out.println(Thread.currentThread().getName()+" count = "+count);
    }

    public static void main(String[] args) {
        Thread_009 t = new Thread_009();
        for (int i = 100000; i > 0 ; i--) {
            new Thread(t::run,"Thread "+i+" ").start();
        }
    }
}
```

```shell
不加sychronized执行结果,我们可以看到多个线程输出了同一个值，最终结果不是我们预期的0
......
Thread 38223  count = 38230
Thread 38166  count = 38229
Thread 38226  count = 38229
Thread 38169  count = 38229
Thread 38284  count = 38230
Thread 38168  count = 38228
......

当我们加上sychronized时，会解决这个问题
Thread 4  count = 3
Thread 5  count = 2
Thread 2  count = 1
Thread 1  count = 0
```
### 面试题：模拟银行账户，对业务写方法加锁，对业务读方法不加锁，可以吗？
> 答：不可以，容易产生脏读现象；具体看代码，解决方法就是把读方法也加锁

```java
public class Thread_010 {
    String name;
    double balance;

    public /*synchronized*/ void set(String name,double balance){
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public synchronized double getBalance(){
        return this.balance;
    }

    public static void main(String[] args) {
        Thread_010 account = new Thread_010();
        new Thread(()->account.set("柯南",300.0)).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前账户余额："+account.getBalance());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("当前账户余额："+account.getBalance());
    }
}
```
```shell
不加锁执行结果：
当前账户余额：0.0
当前账户余额：300.0
加锁执行结果：
当前账户余额：300.0
当前账户余额：300.0
```

### synchronized 锁重入|异常锁
* 锁重入
> 一个同步方法内可以调用另一个同步方法，及一个类中两个方法都加了锁，那么他们锁定的都是同一个对象，当我们在m1中调用m2时，会发现他们呢是同一个线程的申请这把锁，允许执行m2，这就叫锁重入。
* 异常锁
> 程序执行过程中，如果某个环节出现异常，默认锁会被释放，外部等待的程序就会冲进来，程序乱入，可能会访问到异常时产生的数据；一般用try-catch解决，保证流程是不会被异常中断的。

### synchronized 锁升级
这里强烈安利 《我是厕所所长一、二》-马士兵 马老师的文章生动的讲解了锁升级的一个过程，通俗易懂！

##CAS

### 面试题：CAS（自旋锁）一定比系统锁的效率高吗？
> 答：不一定，分具体情况：执行时间短（加锁的代码），线程数少，用自旋；执行时间长，线程数多，用系统锁。

### 什么是 CAS？
> CAS（Compare And Swap）指比较并交换。CAS算法CAS(V, E, N)包含 3 个参数，V 表示要更新的变量，E 表示预期的值，N 表示新值。在且仅在 V 值等于 E值时，才会将 V 值设为 N，如果 V 值和 E 值不同，则说明已经有其他线程做了更新，当前线程什么都不做。最后，CAS 返回当前 V 的真实值。Concurrent包下所有类底层都是依靠CAS操作来实现，而sun.misc.Unsafe为我们提供了一系列的CAS操作。

### CAS 有什么缺点？ 
> ABA问题 自旋问题 范围不能灵活控制

### 对 CAS 中的 ABA 产生有解决方案吗？
#### 什么是 ABA 问题呢？
* 代码示例看【MSB009】cas中ABA问题解决.md
> 多线程环境下。线程 1 从内存的V位置取出 A ，线程 2 也从内存中取出 A，并将 V 位置的数据首先修改为 B，接着又将 V 位置的数据修改为 A，线程 1 在进行CAS操作时会发现在内存中仍然是 A，线程 1 操作成功。尽管从线程 1 的角度来说，CAS操作是成功的，但在该过程中其实 V 位置的数据发生了变化，线程 1 没有感知到罢了，这在某些应用场景下可能出现过程数据不一致的问题。
#### 解决
> 可以版本号（version）来解决 ABA 问题的，在 atomic 包中提供了 AtomicStampedReference 这个类，它是专门用来解决 ABA 问题的，通过getStampe()方法获取当前版本戳，通过compareAndSet()方法进行修改，方法中包含：引用预期值、引用新值、版本戳预期值、版本戳新值

### CAS 自旋导致的问题？ 
> 由于单次 CAS 不一定能执行成功，所以 CAS 往往是配合着循环来实现的，有的时候甚至是死循环，不停地进行重试，直到线程竞争不激烈的时候，才能修改成功。  CPU 资源也是一直在被消耗的，这会对性能产生很大的影响。所以这就要求我们，要根据实际情况来选择是否使用 CAS，在高并发的场景下，通常 CAS 的效率是不高的。

### CAS 范围不能灵活控制 
> 不能灵活控制线程安全的范围。只能针对某一个，而不是多个共享变量的，不能针对多个共享变量同时进行 CAS 操作，因为这多个变量之间是独立的，简单的把原子操作组合到一起，并不具备原子性。

### 什么是 AQS？
> AbstractQueuedSynchronizer抽象同步队列简称AQS，它是实现同步器的基础组件，并发包中锁的底层就是使用AQS实现的。AQS定义了一套多线程访问共享资源的同步框架，许多同步类的实现都依赖于它，例如常用的Synchronized、ReentrantLock、ReentrantReadWriteLock、Semaphore、CountDownLatch等。该框架下的锁会先尝试以CAS乐观锁去获取锁，如果获取不到，则会转为悲观锁（如RetreenLock）

### 了解 AQS 共享资源的方式吗？
1. 独占式：只有一个线程能执行，具体的Java实现有ReentrantLock。
2. 共享式：多个线程可同时执行，具体的Java实现有Semaphore和CountDownLatch。

## Atomic

### Atomic 原子更新 
> Java 从 JDK1.5 开始提供了 java.util.concurrent.atomic 包，方便程序员在多线程环 境下，无锁的进行原子操作。在 Atomic 包里一共有 12 个类，四种原子更新方式，分别是原子更新基本类型，原子更新数组，原子更新引用和原子更新字段。在 JDK 1.8 之后又新增几个原子类。如下如：
![multithreading-atomic脑图.jpg](../resource/multithreading/multithreading-atomic脑图.jpg)

### 列举几个AtomicLong 的常用方法
1. long getAndIncrement() ：以原子方式将当前值加1，注意，返回的是旧值。（i++)
2. long incrementAndGet() ：以原子方式将当前值加1，注意，返回的是新值。（++i）
3. long getAndDecrement() ：以原子方式将当前值减 1，注意，返回的是旧值 。(i--)
4. long decrementAndGet() ：以原子方式将当前值减 1，注意，返回的是新值 。(--i)
5. long addAndGet（int delta） ：以原子方式将输入的数值与实例中的值（AtomicLong里的value）相加，并返回结果

### 说说 AtomicInteger 和 synchronized 的异同点？
#### 相同点
* 都是线程安全
#### 不同点
* 背后原理
> synchronized 背后的 monitor 锁。在执行同步代码之前，需要首先获取到 monitor 锁，执行完毕后，再释放锁。原子类，线程安全的原理是利用了 CAS 操作。
* 使用范围
> 原子类使用范围是比较局限的,一个原子类仅仅是一个对象，不够灵活。而 synchronized 的使用范围要广泛得多。比如说 synchronized 既可以修饰一个方法，又可以修饰一段代码，相当于可以根据我们的需要，非常灵活地去控制它的应用范围
* 粒度
> 原子变量的粒度是比较小的，它可以把竞争范围缩小到变量级别。通常情况下，synchronized 锁的粒度都要大于原子变量的粒度。
* 性能
> synchronized 是一种典型的悲观锁，而原子类恰恰相反，它利用的是乐观锁。

### 原子类和 volatile 有什么异同？
* volatile 可见性问题
* 解决原子性问题

### AtomicLong 可否被 LongAdder 替代？
* 有了更高效的 LongAdder，那 AtomicLong 可否不使用了呢？是否凡是用到 AtomicLong 的地方，都可以用 LongAdder 替换掉呢？答案是不是的，这需要区分场景。
* LongAdder 只提供了 add、increment 等简单的方法，适合的是统计求和计数的场景，场景比较单一，而 AtomicLong 还具有 compareAndSet 等高级方法，可以应对除了加减之外的更复杂的需要 CAS 的场景。
> 结论：如果我们的场景仅仅是需要用到加和减操作的话，那么可以直接使用更高效的 LongAdder，但如果我们需要利用 CAS 比如compareAndSet 等操作的话，就需要使用 AtomicLong 来完成

## 并发工具

### CountDownLatch
> 基于线程计数器来实现并发访问控制，主要用于主线程等待其他子线程都执行完毕后执行相关操作。其使用过程为：在主线程中定义CountDownLatch，并将线程计数器的初始值设置为子线程的个数，多个子线程并发执行，每个子线程在执行完毕后都会调用countDown函数将计数器的值减1，直到线程计数器为0，表示所有的子线程任务都已执行完毕，此时在CountDownLatch上等待的主线程将被唤醒并继续执行。

### CyclicBarrier
> CyclicBarrier（循环屏障）是一个同步工具，可以实现让一组线程等待至某个状态之后再全部同时执行。在所有等待线程都被释放之后，CyclicBarrier可以被重用。CyclicBarrier的运行状态叫作Barrier状态，在调用await方法后，线程就处于Barrier状态。
#### CyclicBarrier中最重要的方法是await方法，它有两种实现。
1. public int await()：挂起当前线程直到所有线程都为Barrier状态再同时执行后续的任务。
2. public int await(long timeout, TimeUnit unit)：设置一个超时时间，在超时时间过后，如果还有线程未达到Barrier状态，则不再等待，让达到Barrier状态的线程继续执行后续的任务。

### Semaphore
> Semaphore指信号量，用于控制同时访问某些资源的线程个数，具体做法为通过调用acquire()获取一个许可，如果没有许可，则等待，在许可使用完毕后通过release()释放该许可，以便其他线程使用。

### CyclicBarrier 和 CountdownLatch 有什么异同？
#### 相同点
都能阻塞一个或一组线程，直到某个预设的条件达成发生，再统一出发。
#### 不同点，具体如下。
* 作用对象不同
> CyclicBarrier 要等固定数量的线程都到达了栅栏位置才能继续执行，而 CountDownLatch 只需等待数字倒数到 0，也就是说 CountDownLatch 作用于事件，但 CyclicBarrier 作用于线程；CountDownLatch 是在调用了 countDown 方法之后把数字倒数减 1，而 CyclicBarrier 是在某线程开始等待后把计数减 1。
* 可重用性不同
> CountDownLatch 在倒数到 0 并且触发门闩打开后，就不能再次使用了，除非新建一个新的实例；而 CyclicBarrier 可以重复使用。CyclicBarrier 还可以随时调用 reset 方法进行重置，如果重置时有线程已经调用了 await 方法并开始等待，那么这些线程则会抛出 BrokenBarrierException 异常。
* 执行动作不同
> CyclicBarrier 有执行动作 barrierAction，而 CountDownLatch 没这个功能。

### CountDownLatch、CyclicBarrier、Semaphore的区别如下。
1. CountDownLatch和CyclicBarrier都用于实现多线程之间的相互等待，但二者的关注点不同。CountDownLatch主要用于主线程等待其他子线程任务均执行完毕后再执行接下来的业务逻辑单元，而CyclicBarrier主要用于一组线程互相等待大家都达到某个状态后，再同时执行接下来的业务逻辑单元。此外，CountDownLatch是不可以重用的，而CyclicBarrier是可以重用的。
2. Semaphore和Java中的锁功能类似，主要用于控制资源的并发访问。

## Locks

### 公平锁与非公平锁
> ReentrantLock支持公平锁和非公平锁两种方式。公平锁指锁的分配和竞争机制是公平的，即遵循先到先得原则。非公平锁指JVM遵循随机、就近原则分配锁的机制。ReentrantLock通过在构造函数ReentrantLock(boolean fair)中传递不同的参数来定义不同类型的锁，默认的实现是非公平锁。这是因为，非公平锁虽然放弃了锁的公平性，但是执行效率明显高于公平锁。如果系统没有特殊的要求，一般情况下建议使用非公平锁。