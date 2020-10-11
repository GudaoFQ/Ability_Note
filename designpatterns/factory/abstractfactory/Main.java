package desgindemo.factory.abstractfactory;

import desgindemo.factory.abstractfactory.abstractclass.AbstractFactory;
import desgindemo.factory.abstractfactory.abstractclass.Food;
import desgindemo.factory.abstractfactory.abstractclass.Gun;
import desgindemo.factory.abstractfactory.abstractclass.Transportion;
import desgindemo.factory.abstractfactory.ukclass.UkFactory;
import desgindemo.factory.abstractfactory.usaclass.UsaFactory;

/**
 * Author : GuDao
 * 2020-10-11
 */
public class Main {
    public static void main(String[] args) {
        //当前使用Uas的族类工厂
        AbstractFactory factory = new UsaFactory();
        //想变成Uk的只需要修改工厂实体就行
        //AbstractFactory factory = new UkFactory();

        //下面的代码不会修改
        Food food = factory.createFood();
        food.eating();
        Transportion transportion = factory.createTransportion();
        transportion.moving();
        Gun gun = factory.createGun();
        gun.usering();
    }
}
