## ConcurrentLinkQueue
> 内部的取的方法都是使用CAS来实现的原子行操作

#### Queue和List区别
Queue提供了很多对线程友好的API offer peek poll
BlockingQueue put take->阻塞 