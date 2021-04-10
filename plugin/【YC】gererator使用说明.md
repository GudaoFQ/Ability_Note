## Generator配置说明
[GitHub地址](https://github.com/GudaoFQ/Generator_Demo)

### 核心Maven坐标配置
> 版本最好跟着本项目中来
```xml
<!-- generator必备：mysql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
<!-- generator必备：MyBatis Generator -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.7</version>
</dependency>
```

### 配置说明
> 主要是为了方便后期的修改，用户直接修改参数配置generatorConfig.properties中的数据即可，不需要再修改核心配置中的信息了
### `mybatis-generator-config.xml`核心配置说明【配合参数配置文件`generatorConfig.properties`使用】
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!-- todo 修改：配置参数位置 -->
    <properties resource="generator/generatorConfig.properties"/>

    <!-- 配置table表信息内容体，targetRuntime指定采用MyBatis3的版本 -->
    <context id="tables" targetRuntime="MyBatis3Simple">
        <!-- 生成的 Java 文件的编码 -->
        <property name="javaFileEncoding" value="UTF-8"/>
        <!-- 格式化 Java 代码 -->
        <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
        <!-- 格式化 XML 代码 -->
        <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>

        <!-- 生成的pojo，将implements Serializable：为模型生成序列化方法 -->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <!-- 生成实体添加toString方法 -->
        <!--<plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>-->
        <!--生成mapper.xml时覆盖原文件-->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin" />
        <!-- todo 修改：集成lombok，将type修改为自定义的lombok配置实体位置[不集成lombok直接将下面的配置注释] -->
        <plugin type="com.gudao.generator.generatorconfig.comment.LombokPlugin">
            <!-- 添加lombok中的jar包，留作在配置注释方法中使用 -->
            <property name="lombokDataAnnotationPackage" value="lombok.Data" />
            <property name="lombokToStringAnnotationPackage" value="lombok.ToString" />
        </plugin>

        <!-- todo 修改：集成swagger，将type修改为自定义的swagger配置实体位置[不集成swagger直接将下面的配置注释] -->
        <plugin type="com.gudao.generator.generatorconfig.comment.SwaggerPlugin">
            <!-- 添加swagger中的jar包，留作在配置注释方法中使用 -->
            <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
            <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
        </plugin>

        <!-- todo 修改：抑制生成注释，由于生成的注释都是英文的，可以不让它生成，将type修改为自己定义的注释配置实体位置；不需要按照自己的配置信息生成实体字段注解就将type="com.wedding.project.configuration.generator.comment.MySQLCommentGenerator"删除 -->
        <commentGenerator type="com.gudao.generator.generatorconfig.comment.MySQLCommentGenerator">
            <!-- suppressAllComments:阻止生成注释，默认为false -->
            <property name="suppressAllComments" value="false"/>
            <!-- suppressDate:阻止生成的注释包含时间戳，默认为false -->
            <property name="suppressDate" value="true"/>
            <!-- 生成创建人 -->
            <property name="Author" value="Programmer"/>
            <!-- 生成创建日期 -->
            <property name="DateFormat" value="yyyy/MM/dd"/>
        </commentGenerator>

        <!-- 配置数据库连接信息 -->
        <jdbcConnection driverClass="${jdbc.driver}"
                        connectionURL="${jdbc.url}"
                        userId="${jdbc.username}"
                        password="${jdbc.password}">
            <!-- 防止xml中生成重复sql语句 -->
            <property name="nullCatalogMeansCurrent" value="true"/>
            <!-- 防止thint转换为boolean类型 -->
            <property name="tinyInt1isBit" value="false"/>
        </jdbcConnection>

        <!--
            默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer
            true，把JDBC DECIMAL 和 NUMERIC 类型解析为java.math.BigDecimal
        -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- todo 修改：生成model类，targetPackage指定model类的包名， targetProject指定生成的model放在项目的哪个工程下面【分布式中会把实体类放在common模块下，这样就要修改targetProject】 -->
        <javaModelGenerator targetPackage="${entity.targetPackage}" targetProject="src/main/java">
            <property name="enableSubPackages" value="false" />
            <property name="trimStrings" value="false" />
        </javaModelGenerator>

        <!-- todo 修改：生成MyBatis的Mapper.xml文件，targetPackage指定mapper.xml文件的包名， targetProject指定生成的mapper.xml放在项目的哪个工程下面 -->
        <sqlMapGenerator targetPackage="${xml.targetPackage}" targetProject="src/main/resources">
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>

        <!-- todo 修改：生成MyBatis的Mapper接口类文件,targetPackage指定Mapper接口类的包名， targetProject指定生成的Mapper接口xml放在项目的哪个工程下面 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="${dao.targetPackage}" targetProject="src/main/java">
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>

        <!-- todo 修改：下面的enable...注释调就会产生一个example类，可以做sql修改 -->
        <table tableName="${table.tableName}"
               domainObjectName="${table.domainObjectName}"
               mapperName="${table.domainObjectName}Mapper"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
               selectByExampleQueryId="false">
            <!--
                uuid使用配置：select uuid() from dual
                手动自增配置：SELECT LAST_INSERT_ID()
                若主键是自动生成的，那么使用以下配置；否则注释掉以下配置
            -->
            <!--<generatedKey column="${table.idColumn}" sqlStatement="SELECT LAST_INSERT_ID()"/>-->
            <!-- 将指定字段中的thint类型映射为Integer类型 -->
            <!--<columnOverride column="status" javaType="Integer"/>-->
        </table>
    </context>
</generatorConfiguration>
```
#### generatorConfig.properties配置说明
```properties
# jdbc settings
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
jdbc.username=root
jdbc.password=19980525

# 使用时候修改到自己的实体类
entity.targetPackage=com.gudao.generator.model

# mapper 使用时候修改到自己的映射层
dao.targetPackage=com.gudao.generator.dao

# xml 使用的时候配置自己的sql映射xml文件
xml.targetPackage=mappers

# 使用时候修改到自己的数据库表名
table.tableName=user
# 使用时候修改到自己的数据库主键
table.idColumn=id
# 使用时候修改到自己的实体类名
table.domainObjectName=User
```

###Main方法实现项目执行
```java
public class CommentStart {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>(2);
        ConfigurationParser cp = new ConfigurationParser(warnings);

        // todo 修改：变为自己的配置文件的位置
        File configFile = new File("src/main/resources/generator/mybatis-generator-config.xml");
        Configuration config = cp.parseConfiguration(configFile);

        DefaultShellCallback callback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
```

### 集成插件配置
#### 自定义中文注解（xml文件中的commentGenerator配置+java代码实现配置详情）
```java
/**
 * @Auther: Gudao
 * @Date: 2021/03/29
 * @Description: 实体类逆向工程配置过渡
 */
public class EmptyCommentGenerator implements CommentGenerator {

    @Override
    public void addConfigurationProperties(Properties properties) {

    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean b) {

    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

    }

    @Override
    public void addComment(XmlElement xmlElement) {

    }

    @Override
    public void addRootComment(XmlElement xmlElement) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }
}

/**
 * @Auther: Gudao
 * @Date: 2021/04/09
 * @Description: 用于 Mybatis generator 生成代码时从数据库表注释生成实体类注释
 */
public class MySQLCommentGenerator extends EmptyCommentGenerator {
    // 属性，即配置在 commentGenerator 标签之内的 Property 标签
    private final Properties properties;

    public MySQLCommentGenerator() {
        properties = new Properties();
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // 获取自定义的 properties
        this.properties.putAll(properties);
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String author = properties.getProperty("Author");
        String dateFormat = properties.getProperty("DateFormat", "yyyy-MM-dd");
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

        // 获取表注释
        String remarks = introspectedTable.getRemarks();

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * @Author: " + author);
        topLevelClass.addJavaDocLine(" * @DateFormat: " + dateFormatter.format(new Date()));
        topLevelClass.addJavaDocLine(" * @Description: " + remarks);
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
    }
}
```
### lombok插件（xml文件中的plugin配置+java代码实现配置详情）
```java
/**
 * @Auther: Gudao
 * @Date: 2021/3/26 14:57
 * @Description: Lombok集成入实体
 */
public class LombokPlugin extends PluginAdapter {
    // [方法二可以将此处注释]属性，即配置在 plugin 标签之内的 Property 标签
    private final Properties properties = super.getProperties();

    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 模型上添加注解
     *
     * @param topLevelClass     顶级类
     * @param introspectedTable 进行自检表
     * @return boolean
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 导入需要的jar包方法一（需要再配置文件中指定所需的jar包地址）
        String lombokDataAnnotationPackage;
        String lombokToStringAnnotationPackage;
        if(null != (lombokDataAnnotationPackage = properties.getProperty("lombokDataAnnotationPackage"))){
            topLevelClass.addImportedType(lombokDataAnnotationPackage);
            topLevelClass.addAnnotation("@Data");
        }
        if(null != (lombokToStringAnnotationPackage = properties.getProperty("lombokToStringAnnotationPackage"))){
            topLevelClass.addImportedType(lombokToStringAnnotationPackage);
            topLevelClass.addAnnotation("@ToString");
        }

        /*// 导入需要的jar包方法二（不需要再配置文件中指定）
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.ToString");

        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@ToString");*/

        return true;
    }

    /**
     * 模型setter方法生成
     *
     * @param method             方法
     * @param topLevelClass      顶级类
     * @param introspectedColumn 进行自检列
     * @param introspectedTable  进行自检表
     * @param modelClassType     模型类类型
     * @return boolean false不生成，true生成
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * 模型生成getter方法
     *
     * @param method             方法
     * @param topLevelClass      顶级类
     * @param introspectedColumn 进行自检列
     * @param introspectedTable  进行自检表
     * @param modelClassType     模型类类型
     * @return boolean false不生成，true生成
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }
}
```
### swagger插件（xml文件中的plugin配置+java代码实现配置详情）
```java
/**
 * @Auther: Gudao
 * @Date: 2021/3/26 14:57
 * @Description: Lombok集成入实体
 * 特殊说明: 该类可以不写，可以集成进MySQLCommentGenerator进行配置，也可以集成进LombokPlugin，两种方法都可以（就是将<property>标签信息移动的问题，我这样配置只是为了让其看起来更清晰）
 */
public class SwaggerPlugin extends PluginAdapter {
    // 属性，即配置在 plugin 标签之内的 Property 标签
    private final Properties properties = super.getProperties();

    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 模型上添加注解【添加@ApiModel(value="表名")】
     *
     * @param topLevelClass     顶级类
     * @param introspectedTable 进行自检表
     * @return boolean
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 导入需要的jar包方法一（需要再配置文件中指定所需的jar包地址）
        String apiModelAnnotationPackage;
        String apiModelPropertyAnnotationPackage;
        // 表说明
        String tableRemarks = introspectedTable.getRemarks();
        if(null != (apiModelPropertyAnnotationPackage = properties.getProperty("apiModelPropertyAnnotationPackage"))){
            topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
        }
        if(null != (apiModelAnnotationPackage = properties.getProperty("apiModelAnnotationPackage"))){
            // 添加import信息
            topLevelClass.addImportedType(apiModelAnnotationPackage);
            // 添加swagger注解
            String annotatedInfo = "@ApiModel(value=\"" + (tableRemarks==null || "".equals(tableRemarks)  ? topLevelClass.getType().getShortName() : tableRemarks) + "\")";
            topLevelClass.addAnnotation(annotatedInfo);
        }

        return true;
    }

    /**
     * 模型字段上加注解【@ApiModelProperty(value="字段名称", name="id",dataType="Integer")】
     *
     * @param field              场
     * @param topLevelClass      顶级类
     * @param introspectedColumn 进行自检列
     * @param introspectedTable  进行自检表
     * @param modelClassType     模型类类型
     * @return boolean
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String fieldRemarks = introspectedColumn.getRemarks();
        String apiModelPropertyAnnotationPackage;
        if(null != (apiModelPropertyAnnotationPackage = properties.getProperty("apiModelPropertyAnnotationPackage"))){
            topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
            if(null != fieldRemarks){
                field.addAnnotation("@ApiModelProperty(value=\"" + fieldRemarks + "\", name=\""+introspectedColumn.getJavaProperty() + "\",dataType=\""+introspectedColumn.getFullyQualifiedJavaType().getShortName() + "\")");
            }
        }

        return true;
    }
}
```

## 使用说明

## 修改配置文件
### 启动SpringBoot修改
* application-dev.properties中的数据库配置修改为自己的

### Generator配置修改
#### 不需要实体类字段添加中文注解
* 将pom中的plugin注释掉的内容打开
* 将mybatis-generator-config.xml中的commentGenerator标签中的type删除
  ```xml
    <!-- todo 修改：抑制生成注释，由于生成的注释都是英文的，可以不让它生成，将type修改为自己定义的注释配置实体位置；不需要按照自己的配置信息生成实体字段注解就将type="com.wedding.project.configuration.generator.comment.MySQLCommentGenerator"删除 -->
    <commentGenerator>
        <!-- suppressAllComments:阻止生成注释，默认为false -->
        <property name="suppressAllComments" value="false"/>
        <!-- suppressDate:阻止生成的注释包含时间戳，默认为false -->
        <property name="suppressDate" value="true"/>
        <!-- 生成创建人 -->
        <property name="Author" value="Programmer"/>
        <!-- 生成创建日期 -->
        <property name="DateFormat" value="yyyy/MM/dd"/>
    </commentGenerator>
  ```
* 将mybatis-generator-config.xml中的自定义plugin标签删除
  ```xml
    <!-- 将自定义的SwaggerPlugin和LombokPlugin直接删除 -->
    <plugin type="com.gudao.generator.generatorconfig.comment.SwaggerPlugin">
        <!-- 添加swagger中的jar包，留作在配置注释方法中使用 -->
        <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
        <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
    </plugin>
    <plugin type="com.gudao.generator.generatorconfig.comment.LombokPlugin">
        <!-- 添加lombok中的jar包，留作在配置注释方法中使用 -->
        <property name="lombokDataAnnotationPackage" value="lombok.Data" />
        <property name="lombokToStringAnnotationPackage" value="lombok.ToString" />
    </plugin>
  ```
* 根据修改配置文件`mybatis-generator-config.xml` `generatorConfig.properties`（内部有修改的注释，根据注释修改）；maven运行generator插件

#### 需要实体类字段添加中文注解
* 根据修改配置文件`mybatis-generator-config.xml` `generatorConfig.properties`（内部有修改的注释，根据注释修改）
* 运行`CommentStart`实体类中的main方法