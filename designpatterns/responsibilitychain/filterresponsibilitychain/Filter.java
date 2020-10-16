package desgindemo.responsibilitychain.filterresponsibilitychain;

/**
 * 责任执行接口
 * Author : GuDao
 * 2020-10-16
 */
public interface Filter {
    boolean doFilter(StringBuffer request, StringBuffer response, FilterChain chain);
}
