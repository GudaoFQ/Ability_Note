## Phaser 同音【facer】
> Java 7 引入了一个全新灵活的线程同步机制，名为 Phaser 。 如果你需要等待线程结束然后继续执行其他任务，那么 Phaser 是一个好的选择【像阶段锁，遗传算法中可能用到】

#### Phaser与CountDownLatch区别
Phaser是在线程动态数需要继续执行之前等待的屏障。在CountDownLatch中，该数字无法动态配置，需要在创建实例时提供。

#### 注册机制
> 与其他barrier不同的是，Phaser中的“注册的同步者（parties）”会随时间而变化，Phaser可以通过构造器初始化parties个数，也可以在Phaser运行期间随时加入（register）新的parties，以及在运行期间注销（deregister）parties。运行时可以随时加入、注销parties，只会影响Phaser内部的计数器，它建立任何内部的bookkeeping（账本），因此task不能查询自己是否已经注册了，当然你可以通过实现子类来达成这一设计要求。
```shell
//伪代码  
Phaser phaser = new Phaser();  
phaser.register();//parties count: 1  
....  
phaser.arriveAndDeregister()://count : 0;  
....  
```
#### 同步机制
> 类似于CyclicBarrier，Phaser也可以awaited多次，它的arrivedAndAwaitAdvance()方法的效果类似于CyclicBarrier的await()。Phaser的每个周期（generation）都有一个phase数字，phase 从0开始，当所有的已注册的parties都到达后（arrive）将会导致此phase数字自增（advance），当达到Integer.MAX_VALUE后继续从0开始。这个phase数字用于表示当前parties所处于的“阶段周期”，它既可以标记和控制parties的wait行为、唤醒等待的时机。
1. Arrival：Phaser中的arrive()、arriveAndDeregister()方法，这两个方法不会阻塞（block），但是会返回相应的phase数字，当此phase中最后一个party也arrive以后，phase数字将会增加，即phase进入下一个周期，同时触发（onAdvance）那些阻塞在上一phase的线程。这一点类似于CyclicBarrier的barrier到达机制；更灵活的是，我们可以通过重写onAdvance方法来实现更多的触发行为。
2. Waiting：Phaser中的awaitAdvance()方法，需要指定一个phase数字，表示此Thread阻塞直到phase推进到此周期，arriveAndAwaitAdvance()方法阻塞到下一周期开始（或者当前phase结束）。不像CyclicBarrier，即使等待Thread已经interrupted，awaitAdvance方法会继续等待。Phaser提供了Interruptible和Timout的阻塞机制，不过当线程Interrupted或者timout之后将会抛出异常，而不会修改Phaser的内部状态。如果必要的话，你可以在遇到此类异常时，进行相应的恢复操作，通常是在调用forceTermination()方法之后。
3. Phaser通常在ForJoinPool中执行tasks，它可以在有task阻塞等待advance时，确保其他tasks的充分并行能力。

#### 中断（终止）
> Phaser可以进入Termination状态，可以通过isTermination()方法判断；当Phaser被终止后，所有的同步方法将会立即返回（解除阻塞），不需要等到advance（即advance也会解除阻塞），且这些阻塞方法将会返回一个负值的phase值（awaitAdvance方法、arriveAndAwaitAdvance方法）。当然，向一个termination状态的Phaser注册party将不会有效；此时onAdvance()方法也将会返回true（默认实现），即所有的parties都会被deregister，即register个数为0。

#### Tiering（分层）
> Phaser可以“分层”，以tree的方式构建Phaser来降低“竞争”。如果一个Phaser中有大量parties，这会导致严重的同步竞争，所以我们可以将它们分组并共享一个parent Phaser，这样可以提高吞吐能力；Phaser中注册和注销parties都会有Child 和parent Phaser自动管理。当Child Phaser中中注册的parties变为非0时（在构造函数Phaser(Phaser parent,int parties)，或者register()方法），Child Phaser将会注册到其Parent上；当Child Phaser中的parties变为0时（比如由arrivedAndDegister()方法），那么此时Child Phaser也将从其parent中注销出去。

#### 监控
> 同步的方法只会被register操作调用，对于当前state的监控方法可以在任何时候调用，比如getRegisteredParties()获取已经注册的parties个数，getPhase()获取当前phase周期数等；因为这些方法并非同步，所以只能反映当时的瞬间状态。

#### 基础代码
```java
//创建时，就需要指定参与的parties个数  
int parties = 12;  
//可以在创建时不指定parties  
// 而是在运行时，随时注册和注销新的parties  
Phaser phaser = new Phaser();  
//主线程先注册一个  
//对应下文中，主线程可以等待所有的parties到达后再解除阻塞（类似与CountDownLatch）  
phaser.register();  
ExecutorService executor = Executors.newFixedThreadPool(parties);  
for(int i = 0; i < parties; i++) {  
    phaser.register();//每创建一个task，我们就注册一个party  
    executor.execute(new Runnable() {  
        @Override  
        public void run() {  
            try {  
                int i = 0;  
                while (i < 3 && !phaser.isTerminated()) {  
                    System.out.println("Generation:" + phaser.getPhase());  
                    Thread.sleep(3000);  
                    //等待同一周期内，其他Task到达  
                    //然后进入新的周期，并继续同步进行  
                    phaser.arriveAndAwaitAdvance();  
                    i++;//我们假定，运行三个周期即可  
                }  
            } catch (Exception e) {  
  
            }  
            finally {  
                phaser.arriveAndDeregister();  
            }  
        }  
    });  
}  
//主线程到达，且注销自己  
//此后线程池中的线程即可开始按照周期，同步执行。  
phaser.arriveAndDeregister();  
```

#### API说明
    1、Phaser()：构造函数，创建一个Phaser；默认parties个数为0。此后我们可以通过register()、bulkRegister()方法来注册新的parties。每个Phaser实例内部，都持有几个状态数据：termination状态、已经注册的parties个数（registeredParties）、当前phase下已到达的parties个数（arrivedParties）、当前phase周期数，还有2个同步阻塞队列Queue。Queue中保存了所有的waiter，即因为advance而等待的线程信息；这两个Queue分别为evenQ和oddQ，这两个Queue在实现上没有任何区别，Queue的元素为QNode，每个QNode保存一个waiter的信息，比如Thread引用、阻塞的phase、超时的deadline、是否支持interrupted响应等。两个Queue，其中一个保存当前phase中正在使用的waiter，另一个备用，当phase为奇数时使用evenQ、oddQ备用，偶数时相反，即两个Queue轮换使用。当advance事件触发期间，新register的parties将会被放在备用的Queue中，advance只需要响应另一个Queue中的waiters即可，避免出现混乱。

    2、Phaser(int parties)：构造函数，初始一定数量的parties；相当于直接regsiter此数量的parties。

    3、arrive()：到达，阻塞，等到当前phase下其他parties到达。如果没有register（即已register数量为0），调用此方法将会抛出异常，此方法返回当前phase周期数，如果Phaser已经终止，则返回负数。

    4、arriveAndDeregister()：到达，并注销一个parties数量，非阻塞方法。注销，将会导致Phaser内部的parties个数减一（只影响当前phase），即下一个phase需要等待arrive的parties数量将减一。异常机制和返回值，与arrive方法一致。

    5、arriveAndAwaitAdvance()：到达，且阻塞直到其他parties都到达，且advance。此方法等同于awaitAdvance(arrive())。如果你希望阻塞机制支持timeout、interrupted响应，可以使用类似的其他方法（参见下文）。如果你希望到达后且注销，而且阻塞等到当前phase下其他的parties到达，可以使用awaitAdvance(arriveAndDeregister())方法组合。此方法的异常机制和返回值同arrive()。

    6、awaitAdvance(int phase)：阻塞方法，等待phase周期数下其他所有的parties都到达。如果指定的phase与Phaser当前的phase不一致，则立即返回。

    7、awaitAdvanceInterruptibly(int phase)：阻塞方法，同awaitAdvance，只是支持interrupted响应，即waiter线程如果被外部中断，则此方法立即返回，并抛出InterrutedException。

    8、awaitAdvanceInterruptibly(int phase,long timeout,TimeUnit unit)：阻塞方法，同awaitAdvance，支持timeout类型的interrupted响应，即当前线程阻塞等待约定的时长，超时后以TimeoutException异常方式返回。

    9、forceTermination()：强制终止，此后Phaser对象将不可用，即register等将不再有效。此方法将会导致Queue中所有的waiter线程被唤醒。

    10、register()：新注册一个party，导致Phaser内部registerPaties数量加1；如果此时onAdvance方法正在执行，此方法将会等待它执行完毕后才会返回。此方法返回当前的phase周期数，如果Phaser已经中断，将会返回负数。

    11、bulkRegister(int parties)：批量注册多个parties数组，规则同10、。

    12、getArrivedParties()：获取已经到达的parties个数。

    13、getPhase()：获取当前phase周期数。如果Phaser已经中断，则返回负值。

    14、getRegisteredParties()：获取已经注册的parties个数。

    15、getUnarrivedParties()：获取尚未到达的parties个数。

    16、onAdvance(int phase,int registeredParties)：这个方法比较特殊，表示当进入下一个phase时可以进行的事件处理，如果返回true表示此Phaser应该终止（此后将会把Phaser的状态为termination，即isTermination()将返回true。），否则可以继续进行。phase参数表示当前周期数，registeredParties表示当前已经注册的parties个数。

    默认实现为：return registeredParties == 0；在很多情况下，开发者可以通过重写此方法，来实现自定义的advance时间处理机制。

 
    内部原理，比较简单（简述）：

    1）两个计数器：分别表示parties个数和当前phase。register和deregister会触发parties变更（CAS），全部parties到达（arrive）会触发phase变更。

    2）一个主要的阻塞队列：非AQS实现，对于arriveAndWait的线程，会被添加到队列中并被park阻塞，知道当前phase中最后一个party到达后触发唤醒。