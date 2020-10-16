package desgindemo.responsibilitychain.realfilterresponsibilitychain;

/**
 * Author : GuDao
 * 2020-10-16
 */

public class Main {
    public static void main(String[] args) {
        RealFilterChain chain = new RealFilterChain();
        StringBuffer request = new StringBuffer();
        StringBuffer response = new StringBuffer();
        //向责任链中添加Filter
        chain.add(new FirstFilter()).add(new TwiceFilter());
        chain.doFilter(request, response);
        System.out.println(request);
        System.out.println(response);
    }
}
