## 线程中的方法
* sleep
* yield
* join

### sleep
> sleep方法是属于Thread类中的，sleep过程中线程不会释放锁，只会阻塞线程，让出cpu给其他线程，但是他的监控状态依然保持着，当指定的时间到了又会自动恢复运行状态，可中断，sleep给其他线程运行机会时不考虑线程的优先级，因此会给低优先级的线程以运行的机会

### yield
> yield和sleep一样都是Thread类的方法，都是暂停当前正在执行的线程对象，不会释放资源锁，和sleep不同的是yield方法并不会让线程进入阻塞状态，而是让**线程重回就绪状态**，它只需要等待重新获取CPU执行时间，所以执行yield()的线程有可能在进入到可执行状态后马上又被执行。还有一点和sleep不同的是yield方法只能使同优先级或更高优先级的线程有执行的机会

### join
> 等待调用join方法的线程结束之后，程序再继续执行，一般用于等待异步线程执行完结果之后才能继续运行的场景。例如：主线程创建并启动了子线程，如果子线程中药进行大量耗时运算计算某个数据值，而主线程要取得这个数据值才能运行，这时就要用到join方法了

### 示例代码
```java
/**
 * Author : GuDao
 * 2020-10-18
 */
public class ThreadMothed {
    public static void main(String[] args) {
        //sleepTest();
        //System.out.println("main");

        //yieldTest();

        joinTest();
    }

    //本线程休息500毫秒，CPU让给其他线程去运行
    static void sleepTest(){
        new Thread(() -> {
            System.out.println("sleepTest start");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleepTest end");
        }).start();
    }

    //调用yield后的线程将会进入等待队列中【就绪状态】，当CPU空闲后它又会继续执行【本质就是让出一下CPU】
    static void yieldTest(){
        new Thread(() -> {
            for(int i=0;i<100;i++){
                System.out.println("yieldA : " + i);
                if(i%10 == 0){
                    Thread.yield();
                }
            }
        }).start();

        new Thread(() -> {
            for(int i=0;i<100;i++){
                System.out.println("yieldB : " + i);
                if(i%10 == 0){
                    Thread.yield();
                }
            }
        }).start();
    }

    //当执行到join部分是，会让join的线程先执行完成，再执行自己本身的内容
    static void joinTest(){
        Thread threadA = new Thread(() -> {
            //只是为了让A在B的后面执行【让效果更明显】
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<100;i++){
                System.out.println("threadA : " + i);
            }
        });

        Thread threadB = new Thread(() -> {
            System.out.println("threadB start");
            try {
                threadA.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("threadB End");
        });

        threadA.start();
        threadB.start();
    }
}
```
#### sleep 执行结果
```shell
main
sleepTest start
sleepTest end
```
#### yield 执行结果
```shell
yieldA : 0
yieldA : 1
yieldA : 2
yieldA : 3
yieldA : 4
yieldA : 5
yieldA : 6
yieldA : 7
yieldA : 8
yieldA : 9
yieldA : 10
yieldB : 0
yieldA : 11
yieldA : 12
yieldA : 13
yieldA : 14
yieldA : 15
yieldA : 16
yieldA : 17
yieldA : 18
yieldA : 19
yieldA : 20
yieldB : 1
yieldB : 2
yieldB : 3
yieldB : 4
yieldB : 5
yieldB : 6
yieldB : 7
yieldB : 8
yieldB : 9
yieldB : 10
yieldA : 21
yieldA : 22
yieldA : 23
yieldA : 24
yieldA : 25
yieldA : 26
yieldA : 27
yieldA : 28
yieldA : 29
yieldA : 30
yieldA : 31
yieldA : 32
yieldA : 33
yieldA : 34
yieldB : 11
yieldB : 12
yieldB : 13
yieldB : 14
yieldB : 15
yieldB : 16
yieldB : 17
yieldB : 18
yieldB : 19
yieldB : 20
yieldA : 35
yieldA : 36
yieldA : 37
yieldA : 38
yieldA : 39
yieldA : 40
yieldB : 21
yieldB : 22
yieldA : 41
yieldA : 42
yieldA : 43
yieldA : 44
yieldA : 45
yieldA : 46
yieldA : 47
yieldA : 48
yieldA : 49
yieldA : 50
yieldB : 23
yieldB : 24
yieldB : 25
yieldB : 26
yieldB : 27
yieldB : 28
yieldB : 29
yieldB : 30
yieldA : 51
yieldA : 52
yieldA : 53
yieldA : 54
yieldA : 55
yieldA : 56
yieldA : 57
yieldA : 58
yieldA : 59
yieldA : 60
yieldB : 31
yieldB : 32
yieldA : 61
yieldA : 62
yieldA : 63
yieldA : 64
yieldA : 65
yieldA : 66
yieldA : 67
yieldA : 68
yieldA : 69
yieldA : 70
yieldB : 33
yieldB : 34
yieldB : 35
yieldB : 36
yieldB : 37
yieldB : 38
yieldB : 39
yieldB : 40
yieldA : 71
yieldA : 72
yieldA : 73
yieldA : 74
yieldA : 75
yieldB : 41
yieldB : 42
yieldB : 43
yieldB : 44
yieldB : 45
yieldB : 46
yieldB : 47
yieldB : 48
yieldB : 49
yieldB : 50
yieldA : 76
yieldA : 77
yieldA : 78
yieldA : 79
yieldA : 80
yieldB : 51
yieldB : 52
yieldB : 53
yieldB : 54
yieldB : 55
yieldB : 56
yieldB : 57
yieldB : 58
yieldB : 59
yieldB : 60
yieldA : 81
yieldA : 82
yieldA : 83
yieldA : 84
yieldA : 85
yieldA : 86
yieldA : 87
yieldA : 88
yieldA : 89
yieldA : 90
yieldB : 61
yieldB : 62
yieldB : 63
yieldB : 64
yieldB : 65
yieldB : 66
yieldB : 67
yieldB : 68
yieldB : 69
yieldB : 70
yieldA : 91
yieldA : 92
yieldA : 93
yieldA : 94
yieldA : 95
yieldA : 96
yieldA : 97
yieldA : 98
yieldA : 99
yieldB : 71
yieldB : 72
yieldB : 73
yieldB : 74
yieldB : 75
yieldB : 76
yieldB : 77
yieldB : 78
yieldB : 79
yieldB : 80
yieldB : 81
yieldB : 82
yieldB : 83
yieldB : 84
yieldB : 85
yieldB : 86
yieldB : 87
yieldB : 88
yieldB : 89
yieldB : 90
yieldB : 91
yieldB : 92
yieldB : 93
yieldB : 94
yieldB : 95
yieldB : 96
yieldB : 97
yieldB : 98
yieldB : 99
```
#### join 执行结果
```shell 
threadB start
threadA : 0
threadA : 1
threadA : 2
threadA : 3
threadA : 4
threadA : 5
threadA : 6
threadA : 7
threadA : 8
threadA : 9
threadA : 10
threadA : 11
threadA : 12
threadA : 13
threadA : 14
threadA : 15
threadA : 16
threadA : 17
threadA : 18
threadA : 19
threadA : 20
threadA : 21
threadA : 22
threadA : 23
threadA : 24
threadA : 25
threadA : 26
threadA : 27
threadA : 28
threadA : 29
threadA : 30
threadA : 31
threadA : 32
threadA : 33
threadA : 34
threadA : 35
threadA : 36
threadA : 37
threadA : 38
threadA : 39
threadA : 40
threadA : 41
threadA : 42
threadA : 43
threadA : 44
threadA : 45
threadA : 46
threadA : 47
threadA : 48
threadA : 49
threadA : 50
threadA : 51
threadA : 52
threadA : 53
threadA : 54
threadA : 55
threadA : 56
threadA : 57
threadA : 58
threadA : 59
threadA : 60
threadA : 61
threadA : 62
threadA : 63
threadA : 64
threadA : 65
threadA : 66
threadA : 67
threadA : 68
threadA : 69
threadA : 70
threadA : 71
threadA : 72
threadA : 73
threadA : 74
threadA : 75
threadA : 76
threadA : 77
threadA : 78
threadA : 79
threadA : 80
threadA : 81
threadA : 82
threadA : 83
threadA : 84
threadA : 85
threadA : 86
threadA : 87
threadA : 88
threadA : 89
threadA : 90
threadA : 91
threadA : 92
threadA : 93
threadA : 94
threadA : 95
threadA : 96
threadA : 97
threadA : 98
threadA : 99
threadB End
```