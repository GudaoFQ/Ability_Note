## 观察者模式介绍【事件处理模型】
> 指多个对象间存在一对多的依赖关系,当一个对象的状态发生改变时,所有依赖于它的对象都得到通知并被自动更新。这种模式有时又称作发布-订阅模式、模型-视图模式,它是对象行为型模式。

#### 责任链与观察者的区别【个人理解】
* 责任链是选择哪个执行对象，当责任链走到某个处理对象中执行结束后，这个责任链就结束了
* 观察者是只要被添加后，都会被执行

#### 观察者模式优缺点
> 优点：

1. 降低了目标与观察者之间的耦合关系,两者之间是抽象耦合关系。
2. 目标与观察者之间建立了一套触发机制。

> 缺点：

1. 目标与观察者之间的依赖关系并没有完全解除,而且有可能出现循环引用。
2. 当观察者对象很多时,通知的发布会花费很多时间,影响程序的效率。

#### 使用细节
* 抽象主题(Subject)角色
> 也叫抽象目标类,它提供了一个用于保存观察者对象的聚集类和增加、删除观察者对象的方法,以及通知所有观察者的抽象方法。
* 具体主题(ConcreteSubject)角色
> 也叫具体目标类,它实现抽象目标中的通知方法，当具体主题的内部状态发生改变时，通知所有注册过的观察者对象。
* 抽象观察者(Observer)角色
> 它是一个抽象类或接口,它包含了一个更新自己的抽象方法,当接到具体主题的更改通知时被调用。
* 具体观察者(ConcreteObserver)角色
> 实现抽象观察者中定义的抽象方法,以便在得到目标的更改通知时更新自身的状态。

### 抽象主题(Subject)角色
```java
/**
 * Author : GuDao
 * 2020-10-20
 */
public class Child {
    //监听者集合
    List<Listerner> listers = new ArrayList<>();
    public Child add(Listerner lister){
        listers.add(lister);
        return this;
    }
    public void childProcess(ActionEvent actionEvent){
        for(Listerner lister : listers){
            lister.process(actionEvent);
        }
    }
}
```
### 抽象观察者(Observer)角色
```java
/**
 * 监听接口
 * Author : GuDao
 * 2020-10-20
 */
public interface Listerner {
    public void process(ActionEvent event);
}
```
### 事件
```java
/**
 * Author : GuDao
 * 2020-10-20
 */
public class ActionEvent {
    /**
     * 时段
     */
    private String timePeriod;

    /**
     * 监听源
     */
    private Object source;
    public ActionEvent(String timePeriod, Object source) {
        this.timePeriod = timePeriod;
        this.source = source;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
    public Object getSource() {
        return source;
    }
}
```
### 具体监听者
```java
/**
 * Author : GuDao
 * 2020-10-20
 */
public class FatherListener implements Listerner {
    @Override
    public void process(ActionEvent event) {
        if("morning".equals(event.getTimePeriod())) System.out.println("父亲哄孩子");
        else if("dinner".equals(event.getTimePeriod())) System.out.println("父亲带孩子散步");
        else System.out.println("父亲换尿布");
    }
}
/**
 * Author : GuDao
 * 2020-10-20
 */
public class MatherListener implements Listerner {
    @Override
    public void process(ActionEvent event) {
        if("morning".equals(event.getTimePeriod())) System.out.println("母亲喂奶");
        else if("dinner".equals(event.getTimePeriod())) System.out.println("母亲喂午饭");
        else System.out.println("母亲哄孩子睡觉");
    }
}
```
### 测试
```java
/**
 * Author : GuDao
 * 2020-10-20
 */
public class Main {
    public static void main(String[] args) {
        Child child = new Child();
        ActionEvent event = new ActionEvent("morning", child);
        Listerner fatherLister = new FatherListener();
        Listerner matherLister = new MatherListener();
        child.add(fatherLister).add(matherLister);
        child.childProcess(event);
    }
}
```