跨域：ip地址，端口，请求方式（http/https）中任何一个不同都属于跨域问题

* 使用Filter过滤器来过滤服务请求，向请求端设置Response Header(响应头部)的Access-Control-Allow-Origin属性声明允许跨域访问。
```java
@WebFilter
public class CorsFilter implements Filter {  

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {  
        HttpServletResponse response = (HttpServletResponse) res;  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        response.setHeader("Access-Control-Allow-Methods", "*");  
        response.setHeader("Access-Control-Max-Age", "3600");  
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, res);  
    }  
}
```