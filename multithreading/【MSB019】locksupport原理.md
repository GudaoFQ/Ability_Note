## LockSupport底层代码实现
通过阅读源代码我们可以发现，LockSupport中关于线程的阻塞和唤醒，主要调用的是sun.misc.Unsafe 中的park(boolean isAbsolute, long time)与unpark(Object thread)方法，也就是如下代码：
```java
private static final jdk.internal.misc.Unsafe theInternalUnsafe =   
      jdk.internal.misc.Unsafe.getUnsafe();
      
	public void park(boolean isAbsolute, long time) {
        theInternalUnsafe.park(isAbsolute, time);
    }
    public void unpark(Object thread) {
        theInternalUnsafe.unpark(thread);
    }
```
复制代码查看sun.misc.包下的Unsafe.java文件我们可以看出，内部其实调用的是jdk.internal.misc.Unsafe中的方法。继续查看jdk.internal.misc.中的Unsafe.java中对应的方法：
```java
 @HotSpotIntrinsicCandidate
    public native void unpark(Object thread);

    @HotSpotIntrinsicCandidate
    public native void park(boolean isAbsolute, long time);
```
复制代码通过查看方法，我们可以得出最终调用的是JVM中的方法，也就是会调用hotspot.share.parims包下的unsafe.cpp中的方法。继续跟踪。
```shell
UNSAFE_ENTRY(void, Unsafe_Park(JNIEnv *env, jobject unsafe, jboolean isAbsolute, jlong time)) {
  //省略部分代码
  thread->parker()->park(isAbsolute != 0, time);
  //省略部分代码
} UNSAFE_END

UNSAFE_ENTRY(void, Unsafe_Unpark(JNIEnv *env, jobject unsafe, jobject jthread)) {
  Parker* p = NULL;
  //省略部分代码
  if (p != NULL) {
    HOTSPOT_THREAD_UNPARK((uintptr_t) p);
    p->unpark();
  }
} UNSAFE_END
```
复制代码通过观察代码我们发现，线程的阻塞和唤醒其实是与hotspot.share.runtime中的Parker类相关。我们继续查看：
```java
class Parker : public os::PlatformParker {
private:
  volatile int _counter ;//该变量非常重要，下文我们会具体描述
	 //省略部分代码
protected:
  ~Parker() { ShouldNotReachHere(); }
public:
  // For simplicity of interface with Java, all forms of park (indefinite,
  // relative, and absolute) are multiplexed into one call.
  void park(bool isAbsolute, jlong time);
  void unpark();
  //省略部分代码
}
```
复制代码在上述代码中，volatile int _counter该字段的值非常重要，一定要注意其用volatile修饰（在下文中会具体描述，接着当我们通过SourceInsight工具(推荐大家阅读代码时，使用该工具)点击其park与unpark方法时，我们会得到如下界面：
![multithreading-locksupport源码说明.jpg](../resource/multithreading/multithreading-locksupport源码说明.jpg)<br>
从图中红色矩形中我们可也看出，针对线程的阻塞和唤醒，不同操作系统有着不同的实现。众所周知Java是跨平台的。针对不同的平台，做出不同的处理。也是非常理解的。因为作者对windows与solaris操作系统不是特别了解。所以这里我选择对Linux下的平台下进行分析。也就是选择hotspot.os.posix包下的os_posix.cpp文件进行分析。