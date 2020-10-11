package desgindemo.factory.methodfactory;

/**
 * Author : GuDao
 * 2020-10-09
 */
public class BusFactory {
    public static Bus getBus(){
        //可以针对业务进行些前置操作
        System.out.println("Bus is Creating");
        return new Bus();
    }
}
