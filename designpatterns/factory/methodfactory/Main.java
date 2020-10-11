package desgindemo.factory.methodfactory;

/**
 * Author : GuDao
 * 2020-10-09
 */
public class Main {
    public static void main(String[] args) {
        MoveAble car = new CarFactory().getCar();
        car.go();
    }
}
