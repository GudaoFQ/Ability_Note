package desgindemo.factory.abstractfactory.usaclass;

import desgindemo.factory.abstractfactory.abstractclass.AbstractFactory;
import desgindemo.factory.abstractfactory.abstractclass.Food;
import desgindemo.factory.abstractfactory.abstractclass.Gun;
import desgindemo.factory.abstractfactory.abstractclass.Transportion;
import desgindemo.factory.abstractfactory.usaclass.Ak;
import desgindemo.factory.abstractfactory.usaclass.Car;
import desgindemo.factory.abstractfactory.usaclass.Steak;

/**
 * usa的同族工厂实体
 * Author : GuDao
 * 2020-10-11
 */
public class UsaFactory extends AbstractFactory {
    @Override
    public Food createFood() {
        return new Steak();
    }

    @Override
    public Gun createGun() {
        return new Ak();
    }

    @Override
    public Transportion createTransportion() {
        return new Car();
    }
}
