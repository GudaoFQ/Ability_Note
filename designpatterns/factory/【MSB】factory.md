## 介绍
> 一般情况下，工厂模式分为三种更加细分的类型：简单工厂、工厂方法和抽象工厂。不过，在 GoF 的《设计模式》一书中，它将简单工厂模式看作是工厂方法模式的一种特例，所以工厂模式只被分成了工厂方法和抽象工厂两类。

#### 使用细节

### 简单工厂一（Simple Factory）【可扩展性不好】

> 简单工厂模式并不是23种常用的设计模式之一，它只算工厂模式的一个特殊实现。简单工厂模式在实际中的应用相对于其他2个工厂模式用的还是相对少得多，因为它只适应很多简单的情况。最重要的是它违背了我们在概述中说的 开放-封闭原则 （虽然可以通过反射的机制来避免，后面我们会介绍到） 。因为每次你要新添加一个功能，都需要在生switch-case 语句（或者if-else 语句）中去修改代码，添加分支条件。

> 示例：根据配置文件的后缀（json、xml、yaml、properties），选择不同的解析器（JsonRuleConfigParser、XmlRuleConfigParser……），将存储在文件中的配置解析成内存对象 RuleConfig。
```java
public class RuleConfigSource {
    public RuleConfig load(String ruleConfigFilePath) {
        String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);
        //调用RuleConfigParserFactory中的方法创建对象实体
        IRuleConfigParser parser = RuleConfigParserFactory.createParser(ruleConfigFileExtension);
    if(parser == null) {
        throw new InvalidRuleConfigException("Rule config file format is not supported: " + ruleConfigFilePath);
    }
    String configText = "";
        //从ruleConfigFilePath文件中读取配置文本到configText中
        RuleConfig ruleConfig = parser.parse(configText);
        return ruleConfig;
    }
    private String getFileExtension(String filePath) {
        //...解析文件名获取扩展名，比如rule.json，返回json
        return "json";
    }
}
public class RuleConfigParserFactory {
    public static IRuleConfigParser createParser(String configFormat) {
        IRuleConfigParser parser = null;
        if ("json".equalsIgnoreCase(configFormat)) {
            parser = new JsonRuleConfigParser();
        } else if ("xml".equalsIgnoreCase(configFormat)) {
            parser = new XmlRuleConfigParser();
        } else if ("yaml".equalsIgnoreCase(configFormat)) {
            parser = new YamlRuleConfigParser();
        } else if ("properties".equalsIgnoreCase(configFormat)) {
            parser = new PropertiesRuleConfigParser();
        }
        return parser;
    }
}
```

### 简单工厂二（Simple Factory）

> 使用接口实现
```java
/*接口类*/
/**
 * 公共功能接口
 * Author : GuDao
 * 2020-10-09
 */
public interface MoveAble {
    void go();
}

/*Car实体类类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Car implements MoveAble {
    @Override
    public void go() {
        System.out.println("car moving faster");
    }
}

/*Bus实体类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Bus implements MoveAble {
    @Override
    public void go() {
        System.out.println("bus moving slower");
    }
}

/*简单工厂类*/
/**
 * 通过反射创建出实体类
 * Author : GuDao
 * 2020-10-09
 */
public class EasyFactory {
    /**
     * 直接获取实体类
     *
     * @param clazz clazz
     * @return {@link Object}
     */
    public static Object getBean(Class<? extends Shape> clazz){
        Object obj = null;
        try {
            obj = Class.forName(clazz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * 获取实体前可以进行前置操作
     *
     * @return {@link Car}
     */
    public static Car getCar(){
        //前置操作：如打印日志等信息
        return new Car();
    }
    /**
     * 获取实体前可以进行前置操作
     *
     * @return {@link Car}
     */
    public static Bus getBus(){
        //前置操作：如打印日志等信息
        return new Bus();
    }
}

/*测试方法*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Main {
    public static void main(String[] args) {
        EasyFactory factory = new EasyFactory();
        //两者二选一；网上推荐使用第一种，将类的路径写在配置文件中，但我测试第二种也能使用，所以此处就不将第一种详细使用方法过多说明了
        //MoveAble car = (Car) factory.getBean(desgindemo.factory.easy.Car.class);
        MoveAble car = (Car) factory.getBean(Car.class);//多态
        car.go();
    }
}
```

### 工厂方法（Factory Method）【一个实体一个工厂】

> 工厂方法模式比起简单工厂模式更加符合开闭原则<br>
> 开闭原则规定“软件中的对象（类，模块，函数等等）应该对于扩展是开放的，但是对于修改是封闭的”，这意味着一个实体是允许在不改变它的源代码的前提下变更它的行为。

```java
/*接口类*/
/**
 * 公共功能接口
 * Author : GuDao
 * 2020-10-09
 */
public interface MoveAble {
    void go();
}

/*Car实体类类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Car implements MoveAble {
    @Override
    public void go() {
        System.out.println("car moving faster");
    }
}

/*Bus实体类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Bus implements MoveAble {
    @Override
    public void go() {
        System.out.println("bus moving slower");
    }
}

/*Bus工厂类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class BusFactory {
    public static Bus getBus(){
        //可以针对业务进行些前置操作
        System.out.println("Bus is Creating");
        return new Bus();
    }
}

/*Car工厂类*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class CarFactory {
    public static Car getCar(){
        //可以针对业务进行些前置操作
        System.out.println("Car is Creating");
        return new Car();
    }
}

/*测试方法*/
/**
 * Author : GuDao
 * 2020-10-09
 */
public class Main {
    public static void main(String[] args) {
        MoveAble car = new CarFactory().getCar();
        car.go();
    }
}
```

### 抽象工厂（Abstract Factory）