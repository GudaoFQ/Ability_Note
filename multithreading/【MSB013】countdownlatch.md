## CountDownLatch
> countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。
> 是通过一个计数器来实现的，计数器的初始值是线程的数量。每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了。

#### 背景：
> countDownLatch是在java1.5被引入，跟它一起被引入的工具类还有CyclicBarrier、Semaphore、concurrentHashMap和BlockingQueue。
> 存在于java.util.cucurrent包下。


#### 源码：
countDownLatch类中只提供了一个构造器
```java
//参数count为计数值
public CountDownLatch(int count) { };
```

类中有三个方法是最重要的
```shell
# 调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
public void await() throws InterruptedException { }; //相当于将门栓栓起，让线程不往下执行  
# 和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };  
# 将count值减1
public void countDown() { };  
```

#### 使用示例：

##### 等待示例：
```JAVA
public class Main {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行…… ……");
        //第一个子线程执行
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        es1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("子线程："+Thread.currentThread().getName()+"执行");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //将count值减1
                latch.countDown();
            }
        });
        es1.shutdown();

        //第二个子线程执行
        ExecutorService es2 = Executors.newSingleThreadExecutor();
        es2.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程："+Thread.currentThread().getName()+"执行");
                //将count值减1
                latch.countDown();
            }
        });
        es2.shutdown();
        System.out.println("等待两个线程执行完毕…… ……");
        
        try {
            //等待count值为0【此时就是等待两个线程执行完成】
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }
}
```
##### 执行结果：
```shell
主线程开始执行…… ……
等待两个线程执行完毕…… ……
子线程：pool-1-thread-1执行
子线程：pool-2-thread-1执行
两个子线程都执行完毕，继续执行主线程
```

#####　并发示例：
```java
public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(100);
        //创建出100个CountRunnable对象，CountRunnable中创建100个线程
        for (int i = 0; i < 100; i++) {
            CountRunnable runnable = new CountRunnable(cdl);
            pool.execute(runnable);
        }
    }
}

class CountRunnable implements Runnable {
    private CountDownLatch countDownLatch;

    public CountRunnable(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            synchronized (countDownLatch) {
                /*** 每次减少一个容量*/
                countDownLatch.countDown();
                System.out.println("thread counts = " + (countDownLatch.getCount()));
            }
            //等待count为0，只有当所有线程都执行结束后才会继续往下执行
            countDownLatch.await();
            System.out.println("concurrency counts = " + (100 - countDownLatch.getCount()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
##### 执行结果：
```shell
#打印了100条[创建100个线程]
thread counts = 99
...
thread counts = 3
thread counts = 2
thread counts = 1
thread counts = 0
#打印了100条
concurrency counts = 100
...
concurrency counts = 100
```

#### CountDownLatch和CyclicBarrier区别：
- CountDownLatch是一个计数器，线程完成一个记录一个，计数器递减，只能只用一次
- CyclicBarrier的计数器更像一个阀门，需要所有线程都到达，然后继续执行，计数器递增，提供reset功能，可以多次使用

#### 基础代码
```java
//创建时，就需要指定参与的parties个数  
int parties = 12;  
CountDownLatch latch = new CountDownLatch(parties);  
//线程池中同步task  
ExecutorService executor = Executors.newFixedThreadPool(parties);  
for(int i = 0; i < parties; i++) {  
    executor.execute(new Runnable() {  
        @Override  
        public void run() {  
            try {  
                //可以在任务执行开始时执行,表示所有的任务都启动后，主线程的await即可解除  
                //latch.countDown();  
                //run  
                //..  
                Thread.sleep(3000);  
  
            } catch (Exception e) {  
  
            }  
            finally {  
                //任务执行完毕后：到达  
                //表示所有的任务都结束，主线程才能继续  
                latch.countDown();  
            }  
        }  
    });  
}  
latch.await();//主线程阻塞，直到所有的parties到达  
//latch上所有的parties都达到后，再次执行await将不会有效，  
//即barrier是不可重用的  
executor.shutdown();  
```
