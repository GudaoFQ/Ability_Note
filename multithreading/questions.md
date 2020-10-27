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

### 面试题：CAS（自旋锁）一定比系统锁的效率高吗？
> 答：不一定，分具体情况：执行时间短（加锁的代码），线程数少，用自旋；执行时间长，线程数多，用系统锁。