package desgindemo.singleton;

/**
 * 静态内部类的方式实现懒汉加载
 * 线程安全是通过JVM内部自己实现的
 * Author : GuDao
 * 2020/8/13
 */

public class SingletonBestInternalLazy {
    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestInternalLazy() {
    }

    //静态内部类是所在类被加载的时候才会创建静态内部类
    public static class internalClass {
        private final static SingletonBestInternalLazy singletonBestInternalLazy = new SingletonBestInternalLazy();
    }

    public static SingletonBestInternalLazy instance() {
        return internalClass.singletonBestInternalLazy;
    }

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(SingletonBestInternalLazy.instance().hashCode());
                }
            }).start();
        }
    }
}
