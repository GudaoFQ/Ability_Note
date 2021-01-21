package desgindemo.responsibilitychain.simpleresponsibilitychain;

/**
 * Author : GuDao
 * 2020-10-16
 */

public class Main {
    public static void main(String[] args) {
        Handler textHandler = new TextHandler();
        Handler uriHandler = new UriHandler();
        Handler commaHandler = new CommaHandler();
        Handler periodHandler = new PeriodHandler();
        //将uriHandler设置为textHandler的下一个责任链成员
        textHandler.setHandler(uriHandler);
        //将commaHandler设置为uriHandler的下一个责任链成员
        uriHandler.setHandler(commaHandler);
        //将periodHandler设置为commaHandler的下一个责任链成员
        commaHandler.setHandler(periodHandler);

        String s = textHandler.handlerMsg("Hello,I am gudao,my page is 'gudao.ink'");
        System.out.println(s);
    }
}
