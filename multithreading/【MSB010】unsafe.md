## UnSafe

#### 基本说明
> 1.7版本
![multithreading-unsafe基本说明.jpg](../resource/multithreading/multithreading-unsafe基本说明.jpg)
* 1.8能通过反射使用
* 后期版本好像不能使用了
* 底层使用CPU原语实现
* 所有的atomic底层都是用这个类中的CampareAndSwap...来实现的
* 变相的让java语言拥有了像c语言中的指针等操作，操作内存