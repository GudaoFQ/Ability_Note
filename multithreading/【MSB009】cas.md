## CAS【无锁优化、自旋】
![multithreading-cas比较实现原理.jpg](../resource/multithreading/multithreading-cas比较实现原理.jpg)
* V:要改的值
* Excepted:期望的值【当前线程认为原来应该有的那个值】
* NewValue:需要设定的新值
* cas是CPU指令级别的操作，中间不能被打断