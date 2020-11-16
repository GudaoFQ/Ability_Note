## ReentrantLock
<https://github.com/GudaoFQ/Multithreading/tree/main/src/main/java/com/gudao/m010_reentrantlock_demo>
> 锁是用来控制多个线程访问共享资源的方式，通过锁可以防止多个线程同时访问共享资源。在 Java1.5之前实现锁只能使用 synchronized关键字实现，但是synchronized隐式获取释放锁，在 1.5之后官方新增了 lock 接口也是用来实现锁的功能，，它具备与synchronized关键字类似的同步功能，显式的获取和释放锁。lock拥有了锁获取与释放的可操作性、可中断的获取锁以及超时获取锁等多种synchronized关键字所不具备的同步特性。

#### 特性
* 公平性选择
> 支持非公平（默认）和公平的锁获取模式，吞吐量还是非公平优于公平
* 重入性
> 该锁支持重入锁，以读写线程为例：读线程在获取读锁之后，能够再次读取读锁，而写线程在获取写锁之后可以同时再次获取读锁和写锁
* 锁降级
> 遵循获取写锁，获取读锁再释放写锁的次序，写锁能够降级为读锁

#### 方法说明
* void lock():获取锁，调用该方法当前线程将会获取锁，当锁获得后，从该方法返回
* void lockInterruptibly() throws InterruptedException:可中断地获取锁，和 lock方法地不同之处在于该方法会响应中断，即在锁的获取中可以中断当前线程
* boolean tryLock(): 尝试非阻塞地获取锁，调用该方法后立刻返回，如果能够获取则返回 true 否则 返回false
* boolean tryLock(long time, TimeUnit unit):超时地获取锁，当前线程在以下 3 种情况下会返回：
    * 当前线程在超时时间内获得了锁
    * 当前线程在超时时间被中断
    * 超时时间结束后，返回 false
* void unlock(): 释放锁
* Condition newCondition():获取锁等待通知组件，该组件和当前的锁绑定，当前线程只有获得了锁，才能调用该组件的 wait() 方法，而调用后，当前线程将释放锁。

#### 方法讲解
* ReentrantLock() 
> 无参 ReentrantLock 使用的非公平锁。
* ReentrantLock(boolean fair)
> ReentrantLock 可以初始化设置是公平锁锁，还是非公平锁。
* getHoldCount()
> 查询当前线程在某个 Lock上的数量，如果当前线程成功获取了 Lock，那么该值大于等于 1；如果没有获取到 Lock 的线程调用该方法，则返回值为 0 。
* isHeldByCurrentThread()
> 判断当前线程是否持有某个 Lock，由于 Lock 的排他性，因此在某个时刻只有一个线程调用该方法返回 true。
* isLocked()
> 判断Lock是否已经被线程持有。
* isFair()
> 创建的ReentrantLock是否为公平锁。
* hasQueuedThreads()
> 在多个线程试图获取Lock的时候，只有一个线程能够正常获得，其他线程可能（如果使用 tryLock()方法失败则不会进入阻塞）会进入阻塞，该方法的作用就是查询是否有线程正在等待获取锁。
* hasQueuedThread(Thread thread)
> 在等待获取锁的线程中是否包含某个指定的线程。
* getQueueLength()
> 返回当前有多少个线程正在等待获取锁。

#### 公平锁与非公平锁的实现
* 公平锁 `ReentrantLock lock = new ReentrantLock(true)`
> 是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁。
* 非公平锁 `ReentrantLock lock = new ReentrantLock(false)`
> 如果构造函数不传递参数，则默认是非公平锁。

#### Condition newCondition()
```java
Lock lock = new ReentrantLock();
Condition conditionT1 = lock.newCondition();
Condition conditionT2 = lock.newCondition();

//使用Condition必须要在lock.lock()下使用
lock.lock();

//唤醒conditionT2
conditionT2.signal();
//阻塞conditionT1
conditionT1.await();
```

#### ReentrantLock与Synchronized的区别
1. 锁的实现：synchronized是依赖于jvm实现的，ReentrantLock是依赖jdk实现的。
2. 性能的区别：在sync优化以后，两者性能差不多，都可用时更建议使用sync，因为它的写法更容易。
3. 功能的区别：锁的灵活性ReentrantLock优于sync。
4. ReentrantLock可以指定是公平锁还是非公平锁，而sync只能是非公平锁。
5. ReentrantLock提供了一个Condition类，可以分组唤醒需要的线程。sync只能随机唤醒一个线程或者唤醒全部线程。
6. ReentrantLock提供了能够中断等待锁的线程的机制。
