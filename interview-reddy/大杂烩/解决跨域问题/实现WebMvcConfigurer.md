```java
@Configuration
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 拦截所有的请求
                .allowedOrigins("http://www.abc.com")  // 可跨域的域名，可以为 *
                .allowCredentials(true)
                .allowedMethods("*")   // 允许跨域的方法，可以单独配置
                .allowedHeaders("*");  // 允许跨域的请求头，可以单独配置
    }
}
```