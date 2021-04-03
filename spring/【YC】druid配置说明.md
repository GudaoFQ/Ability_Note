## Druid配置信息说明
```shell
## 数据源配置，缺少druid配置
#spring.datasource.url=jdbc:mysql://localhost:3306/springboot?useUnicode=true&characterEncoding=utf-8&useSSL=false
#spring.datasource.username=root
#spring.datasource.password=root
#spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# 这4个参数key里不带druid也可以，即可以还用上面的这个4个参数
spring.datasource.druid.url=jdbc:mysql://localhost:3306/springboot?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.druid.username=root
spring.datasource.druid.password=root
spring.datasource.druid.driver-class-name=com.mysql.jdbc.Driver

# 初始化时建立物理连接的个数
spring.datasource.druid.initial-size=5
# 最大连接池数量
spring.datasource.druid.max-active=30
# 最小连接池数量
spring.datasource.druid.min-idle=5
# 获取连接时最大等待时间，单位毫秒
spring.datasource.druid.max-wait=60000
# 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
spring.datasource.druid.time-between-eviction-runs-millis=60000
# 连接保持空闲而不被驱逐的最小时间
spring.datasource.druid.min-evictable-idle-time-millis=300000
# 用来检测连接是否有效的sql，要求是一个查询语句
spring.datasource.druid.validation-query=SELECT 1 FROM DUAL
# 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
spring.datasource.druid.test-while-idle=true
# 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
spring.datasource.druid.test-on-borrow=false
# 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
spring.datasource.druid.test-on-return=false
# 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
spring.datasource.druid.pool-prepared-statements=true
# 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
spring.datasource.druid.max-pool-prepared-statement-per-connection-size=50
# 配置监控统计拦截的filters，去掉后监控界面sql无法统计
spring.datasource.druid.filters=stat,wall
# 通过connectProperties属性来打开mergeSql功能；慢SQL记录
spring.datasource.druid.connection-properties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
# 合并多个DruidDataSource的监控数据
spring.datasource.druid.use-global-data-source-stat=true
# 数据源前端界面监控开启：http://localhost:8080/druid/index.html访问 http://localhost:8080/druid/login.html登录
spring.datasource.druid.stat-view-servlet.enabled=true
```