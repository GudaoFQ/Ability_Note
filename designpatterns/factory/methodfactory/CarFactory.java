package desgindemo.factory.methodfactory;

/**
 * Author : GuDao
 * 2020-10-09
 */
public class CarFactory {
    public static Car getCar(){
        //可以针对业务进行些前置操作
        System.out.println("Car is Creating");
        return new Car();
    }
}
