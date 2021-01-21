package designdemo.observer.simpleobserver;

/**
 * 使用观察者模式中的汇率变化来判断AmericanCompany、ChineseCompany两家公司是盈利还是亏损
 * Author : GuDao
 * 2020-10-19
 */

public class Main {
    public static void main(String[] args) {
        Subject companySubject = new CompanySubject();
        Company americanCompany = new AmericanCompany();
        Company chineseCompany = new ChineseCompany();
        companySubject.add(americanCompany)
                .add(chineseCompany);

        //此时是1 USA = 11 CNY
        companySubject.implementation(11);
    }
}
