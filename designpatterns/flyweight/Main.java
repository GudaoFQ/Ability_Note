package designdemo.flyweight;

/**
 * Author : GuDao
 * 2020-10-26
 */

public class Main {
    public static void main(String[] args) {
        FlyweightFactory fOne = new FlyweightFactory();
        Flyweight one = fOne.getFlyweight("a");
        Flyweight two = fOne.getFlyweight("a");

        Flyweight three = fOne.getFlyweight("b");
        Flyweight four = fOne.getFlyweight("a");
        Flyweight five = fOne.getFlyweight("b");

        one.operation(new UnSpecoficFlyweight("测试001"));
        two.operation(new UnSpecoficFlyweight("测试002"));
        five.operation(new UnSpecoficFlyweight("测试003"));

        System.out.println(one == two);
    }
}
