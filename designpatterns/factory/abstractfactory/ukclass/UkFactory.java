package desgindemo.factory.abstractfactory.ukclass;

import desgindemo.factory.abstractfactory.abstractclass.AbstractFactory;
import desgindemo.factory.abstractfactory.abstractclass.Food;
import desgindemo.factory.abstractfactory.abstractclass.Gun;
import desgindemo.factory.abstractfactory.abstractclass.Transportion;
import desgindemo.factory.abstractfactory.ukclass.Bus;
import desgindemo.factory.abstractfactory.ukclass.Noodles;
import desgindemo.factory.abstractfactory.ukclass.Uzi;

/**
 * uk的同族工厂实体
 * Author : GuDao
 * 2020-10-11
 */
public class UkFactory extends AbstractFactory {

    @Override
    public Food createFood() {
        return new Noodles();
    }

    @Override
    public Gun createGun() {
        return new Uzi();
    }

    @Override
    public Transportion createTransportion() {
        return new Bus();
    }
}
