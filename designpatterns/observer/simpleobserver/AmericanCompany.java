package designdemo.observer.simpleobserver;

/**
 * Author : GuDao
 * 2020-10-19
 */

public class AmericanCompany extends Company{
    @Override
    public void change(float parities) {
        if(0.0 > parities-6.9168) System.out.println("美元与人民币汇率降低 AmericanCompany要亏损");
        else if(0.0 < parities-6.9168) System.out.println("美元与人民币汇率升高 AmericanCompany盈利了");
    }
}
