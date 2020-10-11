package desgindemo.singleton;

/**
 * 饿汉模式
 * 类加载到内存后，就实例化一个单例，JVM保证能程安全
 * 商单实用，推荐对！
 * 唯一来点：不管用到与否，如装我时就完成实向化
 * （话说你不朋的，你装我它干啥）
 * <p>
 * Author : GuDao
 * 2020/8/13
 */

public class SingletonBestHungry {
    private static SingletonBestHungry singletonBase = new SingletonBestHungry();

    //构造方法私有化，让调用者无法通过new创建新的单例对象
    private SingletonBestHungry() {
    }

    public static SingletonBestHungry instance() {
        return singletonBase;
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
                    System.out.println(SingletonBestHungry.instance().hashCode());
                }
            }).start();
        }
    }
}
