package desgindemo.responsibilitychain.filterresponsibilitychain;

/**
 * Author : GuDao
 * 2020-10-16
 */

public class FirstFilter implements Filter {
    @Override
    public boolean doFilter(StringBuffer request, StringBuffer response, FilterChain chain) {
        //处理request业务
        request.append("FirstFilter ");
        //调用chain，通过chain来执行下一个Filter中的request业务
        //TODO 执行“实现某个Filter校验为false这执行结束”是要将下面一行注释
        chain.doFilter(request, response, chain);
        //处理Response
        response.append("FirstFilter ");
        return true;
    }
}
