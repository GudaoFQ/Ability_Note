package desgindemo.singleton;

/**
 * 解决了线程同步与反序列化
 * java枚举类是没有构造方法的，所有JVM就不能通过class文件反射来序列化【个人理解】
 * Author : GuDao
 * 2020/8/14
 */

public enum SingletonBestEnum {
    INSTANCE;

    /*public void methods(){
        System.out.println("业务代码");
    }*/

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(SingletonBestEnum.INSTANCE.hashCode());
                }
            }
        }).start();
    }
}
