package designdemo.flyweight;

/**
 * 共享实例
 * Author : GuDao
 * 2020-10-26
 */

public class SpecificFlyweight extends Flyweight {
    private String key;

    //提供共享类实例方式（为了方便测试，就用了有参构造）
    public SpecificFlyweight(String name) {
        this.key = name;
        System.out.println("创建了对象："+name);
    }

    /**
     * 给共享类加入非共享部分【非必要的部分】
     *
     * @param unSpecoficFlyweight 非共享部分
     */
    @Override
    void operation(UnSpecoficFlyweight unSpecoficFlyweight) {
        System.out.println(key+"的非共享信息为："+unSpecoficFlyweight.getInfo());
    }
}
