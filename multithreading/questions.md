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