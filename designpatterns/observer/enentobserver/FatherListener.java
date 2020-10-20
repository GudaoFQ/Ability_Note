package designdemo.observer.enentobserver;

/**
 * Author : GuDao
 * 2020-10-20
 */

public class FatherListener implements Listerner {
    @Override
    public void process(ActionEvent event) {
        if("morning".equals(event.getTimePeriod())) System.out.println("父亲哄孩子");
        else if("dinner".equals(event.getTimePeriod())) System.out.println("父亲带孩子散步");
        else System.out.println("父亲换尿布");
    }
}
