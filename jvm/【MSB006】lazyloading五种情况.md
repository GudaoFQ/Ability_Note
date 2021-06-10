## LazyLoading的五种情况

### 简要说明
* new getstatic putstatic invokestatic指令，访问final变量除外
* 动态语言支持java.lang.invoke.MethodHandle解析的结果为REF getstatic REF putstatic REF invokestatic的方法句柄时，该类必须初始化
* java.lang.reflect对类进行反射调用时
* 初始化子类的时候，父类首先初始化
* 虚拟机启动的时候，被执行的之类必须初始化