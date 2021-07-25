package desgindemo.singleton;

/**
 * 懒汉模式的完美单例 DCL
 * 通过双重检查与双重锁来实现代码的优化
 * Author : GuDao
 * 2020/8/13
 */

public class SingletonBestLazy {
    //volatile是防止class代码在JVM中指令重排的
    private static volatile SingletonBestLazy singletonBastLazy;

    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestLazy() {
    }

    public static SingletonBestLazy instance() {
        //双重检索
        if (null == singletonBastLazy) {
            synchronized (SingletonBestLazy.class) {
                if (null == singletonBastLazy) {
                    singletonBastLazy = new SingletonBestLazy();
                }
            }
        }
        return singletonBastLazy;
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
                    System.out.println(SingletonBestLazy.instance().hashCode());
                }
            }).start();
        }
    }
}
