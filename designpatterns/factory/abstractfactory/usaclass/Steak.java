package desgindemo.factory.abstractfactory.usaclass;

import desgindemo.factory.abstractfactory.abstractclass.Food;

/**
 * USA食物实体类
 * Author : GuDao
 * 2020-10-11
 */
public class Steak extends Food {
    @Override
    public void eating() {
        System.out.println("USA吃牛排");
    }
}
