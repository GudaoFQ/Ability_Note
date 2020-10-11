package desgindemo.factory.methodfactory;

/**
 * Author : GuDao
 * 2020-10-09
 */

public class Car implements MoveAble {
    @Override
    public void go() {
        System.out.println("car moving faster");
    }
}
