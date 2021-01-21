package designdemo.observer.simpleobserver;

/**
 * Author : GuDao
 * 2020-10-19
 */

public class CompanySubject extends Subject{
    @Override
    public void implementation(float parities) {
        for(Company co : observers){
            co.change(parities);
        }
    }
}
