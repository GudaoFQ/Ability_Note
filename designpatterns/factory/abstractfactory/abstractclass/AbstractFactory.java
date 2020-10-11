package desgindemo.factory.abstractfactory.abstractclass;

/**
 * 抽象工厂类
 * 抽象工厂中生产抽象产品
 * Author : GuDao
 * 2020-10-11
 */
public abstract class AbstractFactory {
    public abstract Food createFood();
    public abstract Gun createGun();
    public abstract Transportion createTransportion();
}
