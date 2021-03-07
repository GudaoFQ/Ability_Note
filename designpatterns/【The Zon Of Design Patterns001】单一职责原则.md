## Single Responsibility Principle
> 一个类，应该有且仅有一个原因引起类的变更

#### 好处
* 类的复杂性降低，实现什么职责都有清晰明确的定义
* 可读性提高，复杂性降低，那当然可读性提高了
* 可维护性提高，可读性提高，那当然更容易维护了
* 变更引起的风险降低，变更是必不可少的，如果接口的单一职责做得好，一个接口修改只对相应的实现类有影响，对其他的接口无影响，这对系统的扩展性、维护性都有非常大的帮助

#### 说明
单一职责适用于接口、类，同时也适用于方法，什么意思呢？一个方法尽可能做一件事情，比如一个方法修改用户密码，不要把这个方法放到“修改用户信息”方法中，这个方法的颗粒度很粗

#### 建议
对于单一职责原则，建议是接口一定要做到单一职责，类的设计尽量做到只有一个原因引起变化。

#### 没有使用单一职责原则开发
```java
/**
 *
 * 该接口完全没有套用单一职责原则开发，因为接口把用户信息和业务处理放到了一起
 *
 * Author : GuDao
 * 2020-12-21
 */
public interface NonSingleResponsibility_IUserInfo {
    void setName();
    String getName();
    void setId(String id);
    String getId();
    void setAderss(String aderss);
    String getAdress();

    boolean changeName(String name);
    boolean delUser();
}
```
#### 修改为单一职责原则的接口
```java
/**
 *
 * 把NonSingleResponsibility_IUserInfo业务对象），把行为抽取成一个Biz（Business Logic，业务逻辑）
 *
 * Author : GuDao
 * 2020-12-21
 */
public interface SingleResponsibility_IUserInfo_BusinessLogic {
    boolean changeName(String name);
    boolean delUser();
}

/**
 *
 * 把NonSingleResponsibility_IUserInfo用户的信息抽取成一个BO（Business Object）
 *
 * Author : GuDao
 * 2020-12-21
 */
public interface SingleResponsibility_IUserInfo_BusinessObject {
    void setName();
    String getName();
    void setId(String id);
    String getId();
    void setAderss(String aderss);
    String getAdress();
}
```
