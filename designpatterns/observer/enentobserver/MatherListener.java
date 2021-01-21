package designdemo.observer.enentobserver;

/**
 * Author : GuDao
 * 2020-10-20
 */

public class MatherListener implements Listerner {
    @Override
    public void process(ActionEvent event) {
        if("morning".equals(event.getTimePeriod())) System.out.println("母亲喂奶");
        else if("dinner".equals(event.getTimePeriod())) System.out.println("母亲喂午饭");
        else System.out.println("母亲哄孩子睡觉");
    }
}
