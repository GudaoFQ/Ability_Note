package desgindemo.responsibilitychain.filterresponsibilitychain;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链中的链条
 * Author : GuDao
 * 2020-10-16
 */
public class FilterChain implements Filter{
    //Filter存储容器
    private List<Filter> filters = new ArrayList<>();
    //Filter执行标识，辨别当前的chain执行到了第几个Filter
    int index = 0;

    /**
     * 向执行链中添加Filter
     *
     * @param filter 过滤器
     * @return {@link FilterChain}
     */
    public FilterChain add(Filter filter){
        this.filters.add(filter);
        return this;
    }

    /**
     * 做的过滤器
     * 执行request顺序为：filter1->filter2->filter；执行response顺序为：filter3->filter2->filter1
     *
     * @param request  请求
     * @param response 响应
     * @param chain    链
     * @return boolean
     */
    @Override
    public boolean doFilter(StringBuffer request, StringBuffer response, FilterChain chain) {
        //判断当前责任链中是否执行到了责任链末尾【后面没有执行的Filter就返回false】
        if(index == filters.size()) return false;
        //获取当前的执行器
        Filter filter = filters.get(index);
        //标识加一，告诉chain执行到哪儿了
        index++;
        //执行Filter中的逻辑
        return filter.doFilter(request, response, chain);
    }

//    /**
//     * 实现某个Filter校验为false这执行结束
//     *
//     * @param request  请求
//     * @param response 响应
//     * @param chain    链
//     * @return boolean
//     */
//    @Override
//    public boolean doFilter(StringBuffer request, StringBuffer response, FilterChain chain) {
//        for (Filter filter:filters) {
//            if(!filter.doFilter(request, response, chain))return false;
//        }
//        return true;
//    }
}
