package desgindemo.factory.abstractfactory.usaclass;

import desgindemo.factory.abstractfactory.abstractclass.Transportion;

/**
 * USA使用的交通工具实体类
 * Author : GuDao
 * 2020-10-11
 */
public class Car extends Transportion {
    @Override
    public void moving() {
        System.out.println("Car moving faster");
    }
}
