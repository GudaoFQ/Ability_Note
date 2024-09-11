在Spring Boot应用程序中，如果你想要根据不同的数据库类型（例如：达梦、TDSQL、高斯和PGSQL）来加载不同类型的MyBatis映射文件(mapper files)，可以通过配置多个数据源并基于配置信息选择正确的数据源进行操作。以下是一个基本的实现方案：

1. 数据库驱动的配置：
   首先，你需要为每种数据库配置一个数据源，并根据配置信息选择使用哪个数据源。

2. 创建数据源工厂：
   在你的Spring Boot应用程序中，创建一个数据源工厂类，它可以根据不同的配置来创建相应类型的数据源。例如：

```java
@Component
public class DynamicDataSourceFactory {

    @Value("${db.type}")
    private String dbType;

    // 根据数据库类型返回对应的数据源

    public DataSource getDataSource() {
        if (dbType.equals("dameng")) {
            return damengDataSource();
        } else if (dbType.equals("tdsql")) {
            return tdsqlDataSource();
        } else if (dbType.equals("gaus")) { // 假设这里是高斯数据库
            return gausDataSource();
        } else if (dbType.equals("pgsql")) {
            return pgsqlDataSource();
        }
        throw new UnsupportedOperationException("Unsupported database type: " + dbType);
    }

    private DataSource damengDataSource() {
        // 返回达梦数据源对象
    }

    private DataSource tdsqlDataSource() {
        // 返回TDSQL数据源对象
    }

    private DataSource gausDataSource() {
        // 返回高斯数据源对象
    }

    private DataSource pgsqlDataSource() {
        // 返回PGSQL数据源对象
    }
}
```

3. 配置文件：
   在你的配置文件（application.properties或application.yml）中，你需要添加一个数据库类型的属性来指示使用哪种数据库。

```properties
db.type=dameng # 或者 tdsql, gaus, pgsql 等
```

4. 数据源选择器：
   你可以创建一个数据源选择器类，它会根据配置信息和动态数据源工厂来选择正确的数据源。

```java
public class DynamicDataSourceSelector {

    private final DynamicDataSourceFactory dataSourceFactory;

    public DynamicDataSourceSelector(DynamicDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public DataSource getDataSource() {
        // 读取数据库类型配置信息
        String dbType = ConfigurationPropertiesUtil.getProperty("db.type");

        return dataSourceFactory.getDataSource();
    }
}
```

5. 使用数据源选择器：
   在你的Spring Boot应用程序中，注入动态数据源选择器，并在MyBatis的配置中使用它。

```java
@Configuration
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        // 配置多个映射文件的路径和名称
        String damengMapperPath = "mappers/dameng/mapper.xml";
        String tdsqlMapperPath = "mappers/tdsql/mapper.xml";
        String gausMapperPath = "mappers/gaus/mapper.xml";
        String pgsqlMapperPath = "mappers/pgsql/mapper.xml";

        // 读取数据库类型
        String dbType = ConfigurationPropertiesUtil.getProperty("db.type");

        // 根据数据库类型选择映射文件
        String mapperPath;
        if (dbType.equals("dameng")) {
            mapperPath = damengMapperPath;
        } else if (dbType.equals("tdsql")) {
            mapperPath = tdsqlMapperPath;
        } else if (dbType.equals("gaus")) {
            mapperPath = gausMapperPath;
        } else if (dbType.equals("pgsql")) {
            mapperPath = pgsqlMapperPath;
        } else {
            throw new UnsupportedOperationException("Unsupported database type: " + dbType);
        }

        // 创建配置
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSourceSelector.getDataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("your.package.with.mappers");
        sqlSessionFactoryBean.setMapperLocations(SimplePathMatcherFactory.class.getResource(mapperPath));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public DynamicDataSourceSelector dynamicDataSourceSelector() {
        return new DynamicDataSourceSelector(dynamicDataSourceFactory());
    }
}
```

这个方案假设你已经配置了所有的数据库驱动类，并且它们都被添加到了Spring Boot应用程序的classpath中。你还需要根据实际情况适当修改上述代码。

请注意，这只是一个基本的实现，可能需要根据你的具体要求进行调整。在实际应用中，你可能还需要考虑事务管理、SQL映射文件的加载和解析等其他细节。