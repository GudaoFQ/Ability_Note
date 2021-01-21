package designdemo.flyweight;

/**
 * 非共享元素，个人认为就是一次新展示的信息，在指定的共享类中展示
 * Author : GuDao
 * 2020-10-26
 */

public class UnSpecoficFlyweight {
    private String info;

    public UnSpecoficFlyweight(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public UnSpecoficFlyweight setInfo(String info) {
        this.info = info;
        return this;
    }
}
