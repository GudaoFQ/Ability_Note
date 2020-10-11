package desgindemo.factory.easyfactory;

/**
 * Author : GuDao
 * 2020-10-09
 */

public class Bus implements MoveAble {
    @Override
    public void go() {
        System.out.println("bus moving slower");
    }
}
