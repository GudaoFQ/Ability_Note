## ThreadPoolExecutor
![multithreading-threadpool-线程池抽象概念.jpg](../resource/multithreading/multithreading-threadpool-线程池抽象概念.jpg)

#### 线程池拒绝策略
Abort:抛异常
Discard:扔掉,不抛异常D
iscardoldest:扔掉排队时间最久的
CallerRuns:调用者处理任务