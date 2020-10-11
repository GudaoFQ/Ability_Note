package desgindemo.factory.abstractfactory.ukclass;

import desgindemo.factory.abstractfactory.abstractclass.Transportion;

/**
 * UK使用的交通工具实体类
 * Author : GuDao
 * 2020-10-11
 */
public class Bus extends Transportion {
    @Override
    public void moving() {
        System.out.println("Bus moving slower");
    }
}
