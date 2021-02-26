```yaml
spring: 
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            # 允许跨域的源(网站域名/ip)，设置*为全部
            # 允许跨域请求里的head字段，设置*为全部
            # 允许跨域的method， 默认为GET和OPTIONS，设置*为全部
            allow-credentials: true
            allowed-origins:
              - "http://xb.abc.com"
              - "http://sf.xx.com"
            allowed-headers: "*"
            allowed-methods:
              - OPTIONS
              - GET
              - POST
              - DELETE
              - PUT
              - PATCH
            max-age: 3600
```
注意： 通过gateway 转发的其他项目，不要进行配置跨域配置

有时即使配置了也不会起作用，这时你可以根据浏览器控制的错误输出来查看问题，如果提示是 response 中 header 出现了重复的 Access-Control-* 请求头，可以进行如下操作
```java
import java.util.ArrayList;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("corsResponseHeaderFilter")
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

  @Override
  public int getOrder() {
    // 指定此过滤器位于NettyWriteResponseFilter之后
    // 即待处理完响应体后接着处理响应头
    return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return chain.filter(exchange).then(Mono.defer(() -> {
      exchange.getResponse().getHeaders().entrySet().stream()
          .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
          .filter(kv -> (
              kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                  || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
                  || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS)
                  || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)
                  || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_MAX_AGE)))
          .forEach(kv -> {
            kv.setValue(new ArrayList<String>() {{
              add(kv.getValue().get(0));
            }});
          });
      return chain.filter(exchange);
    }));
  }
}
```