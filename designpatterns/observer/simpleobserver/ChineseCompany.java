package designdemo.observer.simpleobserver;

/**
 * 现在美元与人民币汇率是6.9168，汇率涨的话，人民币就贬值了【就是1美元=6.9168人民币】【涨：1美元=8人民币】【降：1美元=5人民币】
 * Author : GuDao
 * 2020-10-19
 */

public class ChineseCompany extends Company{
    @Override
    public void change(float parities) {
        if(0.0 > parities-6.9168) System.out.println("美元与人民币汇率降低 ChineseCompny盈利了");
        else if(0.0 < parities-6.9168) System.out.println("美元与人民币汇率升高 ChineseCompny要亏损");
    }
}
