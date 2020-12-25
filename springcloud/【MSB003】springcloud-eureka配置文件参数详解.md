## Spring Cloud Eureka配置文件详解

#### eureka.server配置信息
| 参数                                        | 描述                         | 备注       |
| ------------------------------------------- | ---------------------------- | ---------- |
| eureka.server.eviction-interval-timer-in-ms | server清理无效节点的时间间隔 | 默认60秒   |
| eureka.server.enable-self-preservation      | 是否开启自我保护，默认true   | true false |
| eureka.server.renewal-percent-threshold     | 开启自我保护的系数           | 默认：0.85 |

#### eureka.client配置信息
| 参数                                                   | 描述                           | 备注          |
| ------------------------------------------------------ | ------------------------------ | ------------- |
| eureka.client.enabled                                  | 是否开启client，默认true       | true false    |
| eureka.client.register-with-eureka                     | 设置是否将自己作为客户端注册到注册中心 | 默认true      |
| eureka.client.fetch-registry                           | 设置是否从注册中心获取注册信息 | 默认true    |
| eureka.client.serviceUrl.defaultZone                   | 默认服务注册中心地址           | 多个用","隔开 |
| eureka.client.eureka-server-connect-timeout-seconds    | 连接server服务器超时时间       | 默认5秒       |
| eureka.client.eureka-connection-idle-timeout-seconds   | 连接server的连接空闲时长       | 默认30秒      |
| eureka.client.eureka-server-read-timeout-seconds       | 连接server读取数据超时时间     | 默认8秒       |
| eureka.client.eureka-server-total-connections          | 连接server的最大连接数         | 默认200       |
| eureka.client.eureka-server-total-connections-per-host | 对单个server的最大连接数       | 默认50        |
| eureka.client.eureka-service-url-poll-interval-seconds | 获取集群中最新的server节点数据 | 默认0         |
| eureka.client.heartbeat-executor-thread-pool-size      | client维持与server的心跳线程数 | 默认2         |
| eureka.client.registry-fetch-interval-seconds          | client本地缓存清单更新间隔，默认30秒 | client每隔30秒，向server请求可用服务清单。对于API网关类应用，可以适当降低时间间隔 |
| eureka.client.service-url                              | 列出所有可用注册中心的地址     |               |

#### eureka.instance配置信息
| 参数                                                 | 描述                                 | 备注                                                         |
| ---------------------------------------------------- | ------------------------------------ | ------------------------------------------------------------ |
| eureka.instance.lease-renewal-interval-in-seconds    | 心跳：服务续约任务调用间隔时间，默认30秒   | client每隔30秒向server上报自己状态，避免被server剔除         |
| eureka.instance.lease-expiration-duration-in-seconds | 续约：服务时效时间，默认90秒               | 当server 90秒内没有收到client的注册信息时，会将该节点剔除    |
| eureka.instance.prefer-ip-address                    | 注册服务时是否使用IP注册，默认false  | true false                                                   |
| eureka.instance.ip-address                           | server端的ip地址                     |                                                              |
| eureka.instance.hostname                             | server端的hostname                   | 默认localhost                                                |
| eureka.instance.instance-id                          | 注册到server的实例                   |                                                              |

#### 真实项目中的配置
* server
```yaml
app:
  config:
    eureka-server-host: eureka1.com
    eureka-server-port: 32000
spring:
  application:
    name: eureka-server
server:
  # Tomcat项目端口号
  port: ${app.config.eureka-server-port}
# eureka配置信息
eureka:
  instance:
    # server端的hostname
    hostname: ${app.config.eureka-server-host}
    # 注册服务时是否使用IP注册，默认false
    prefer-ip-address: true
    # 心跳：服务续约任务调用间隔时间，即服务续约间隔时间，默认30秒
    lease-renewal-interval-in-seconds: 3
    # 续约：服务时效时间，即服务续约到期时间，默认90秒
    lease-expiration-duration-in-seconds: 10
    # 注册到server的实例：ipAddress为当前服务器的ip地址
    instance-id: ${ipAddress}:${spring.application.name}:${server.port}:'@project.version@':'@time@'
    metadata-map.time: '@time@'
    metadata-map.version: '@project.version@'
  client:
    # 开启健康检查（依赖spring-boot-starter-actuator）
    healthcheck:
      enabled: true
    # 设置注册表获取间隔秒数【client本地缓存清单更新间隔，默认30秒】
    registry-fetch-interval-seconds: 5
    # 设置是否将自己作为客户端注册到注册中心（缺省true），单机配置的话可以为false
    registerWithEureka: true
    # 设置是否从注册中心获取注册信息（缺省true），单机配置的话可以为false
    fetchRegistry: true
    serviceUrl:
      # 列出所有可用注册中心的地址
      defaultZone: http://${app.config.eureka-server-host}:${app.config.eureka-server-port}/eureka/
  server:
    # 关闭自我保护模式（缺省为打开）
    enable-self-preservation: true
    # server清理无效节点的时间间隔
    eviction-interval-timer-in-ms: 180000
```