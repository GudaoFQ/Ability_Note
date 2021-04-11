## Lombok注解说明：Log

### 源码
#### Log
```java
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Log {
	// 指定Logger在上下文的名称，默认名称是：类的全包名
	String topic() default "";
}
```

### Log说明与注意点
* topic：指定Logger在上下文的名称，默认名称是：类的全包名
  ```java
  // 默认名称是：类的全包名，com...LogExample
  @Log(topic = "gudao")
  public class LogExample {
    public static void main(String[] args) {
      System.out.println(log.getName());
      log.severe("this is @Log testing.");
    }
  }

  // 编译后
  public class LogExample {
      private static final Logger log = Logger.getLogger("className");
      //默认：private static final Logger log = Logger.getLogger(LogExample.class.getName());
      public LogExample() {}
      public static void main(String[] args) {
          System.out.println(log.getName());
          log.severe("this is @Log testing.");
      }
  }
  // 输出内容
  gudao
  一月 08, 2019 1:10:23 下午 com.gitee.jdkong.lombok.log.LogExample main
  严重: this is @Log testing.
  ```
* maven坐标引入：不添加只能使用@Log使用java.util.logging的日志方法
    ```xml
    <!-- 使用slf4j的打印日志需要此jar包 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.25</version>
    </dependency>
    <!-- 使用commonsLog的打印日志需要此jar包 -->
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.2</version>
    </dependency>
    ```
#### lombok中的体系说明
##### @CommonsLog
```java
private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LogExample.class);
```
##### @Flogger
```java
private static final com.google.common.flogger.FluentLogger log = com.google.common.flogger.FluentLogger.forEnclosingClass();
```
##### @JBossLog
```java
private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(LogExample.class);
```
##### @CommonsLog
```java
private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LogExample.class);
```
##### @Log
```java
private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogExample.class.getName());
```
##### @Log4j
```java
private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogExample.class);
```
##### @Log4j2
```java
private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LogExample.class);
```
##### @Slf4j
```java
private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
```
##### @XSlf4j
```java
private static final org.slf4j.ext.XLogger log = org.slf4j.ext.XLoggerFactory.getXLogger(LogExample.class);
```

#### 实例演示
```java
@Log
class LogExample {
    public static void main(String[] args) {
        log.severe("this is @Log testing.");
    }
}

@Slf4j
class Slf4jExample {
    public static void main(String[] args) {
        log.info("this is @Slf4j testing.");
    }
}

@CommonsLog
class CommonsLogExample{
    public static void main(String[] args) {
        log.info("this is @CommonsLog testing.");
    }
}
```
#### 编译后文件
```java
import java.util.logging.Logger;
class LogExample {
    private static final Logger log = Logger.getLogger(LogExample.class.getName());

    LogExample() { }

    public static void main(String[] args) { log.severe("this is @Log testing."); }
}


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
class Slf4jExample {
  private static final Logger log = LoggerFactory.getLogger(Slf4jExample.class);

  Slf4jExample() { }

  public static void main(String[] args) { log.info("this is @Slf4j testing."); }
}

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
class CommonsLogExample {
  private static final Log log = LogFactory.getLog(CommonsLogExample.class);

  CommonsLogExample() { }

  public static void main(String[] args) { log.info("this is @CommonsLog testing."); }
}
```