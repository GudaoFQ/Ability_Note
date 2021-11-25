## SpringBoot中配置本地静态文件夹
> 只实践了配置文件，代码实现的此处不说明

### application.yml中配置
```yaml
spring:
	resources:
		#访问系统外部资源，将该目录下的文件映射到系统下（
		#file是本地需要映射到项目中的路径
		#classpath是项目中的路径
		#为什么添加两个：因为static-locations默认可以添加多个，而且如果只写一个，会默认将SpringBoot下的静态文件全替换为唯一路径下的内容（此时就需要将项目中所有的静态文件全部存储到指定的映射文件夹下，不然项目会找不到资源）
		#为什么file在classpath前面：这个看个人使用，因为我们项目中可能会替换一些图片，但又不想替换项目中的，所以将file映射路径写在前面，这样如果有重名的文件，会被file路径下的文件覆盖掉，这样也就实现了替换项目中图片的效果
		#）
		static-locations: file:D://Download//images,classpath:/static
	mvc:
        #这表示只有静态资源的访问路径为/static/**时，才会处理请求
		static-path-pattern: /static/**
```
#### spring.mvc.static-path-pattern配置说明【重要】
> SpringBoot项目中的静态资源文件存放在static文件下面，当通过浏览器访问这些静态文件时，发现必须要添加static作为前缀才能访问，这个前缀跟`spring.mvc.static-path-pattern`这个配置项有关
* 配置文件中，存在`spring.mvc.static-path-pattern=/static/**`配置项时，访问静态资源文件要加static才行，相对应的框架拦截器也要对这个路径进行放行
* 当把这个配置项除掉时，不用加static作为前缀亦可进行正常访问同时配置上下文路径为/,如果存在上下文则需要在链接上添加上下文路径`server.servlet.context-path=/`

### 注意：
* 如果项目中使用了swagger或者knife4j，界面会出现404的问题，此时需要去使用的jar包中找到界面的路径，然后将路径通过classpath加载到项目中
```yaml
# knif4j的配置路径
spring: 
    resources:
      static-locations: file:D://Download//image,classpath:/static,classpath:/META-INF/resources
```

