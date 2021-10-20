### 答题技巧
* 总分的描述方式
  * 总：当前问题回答的是那些具体的点
  * 分：1、2、3、4的方式分细节去描述相关的知识点，如果哪些点不清楚，直接忽略过去
  * 突出一些技术名词：核心概念、接口、类、关键方法

<details>
<summary>1.谈谈对Spring IOC的理解，实现与原理</summary>

- 总
  - 主要点：控制反转、容器
  1. 控制反转：理论思想，原来的对象是由使用者进行控制，有了Spring之后，可以把对象交给Spring来帮我们进行管理
     - DI：依赖注入，把对应属性的值注入到具体的对象中去，如：@Autowired，populateBean完成属性值的注入 
  2. 容器：存储对象，使用map结构存储，在Spring中存在三级缓存，singletonObject存放完整的Bean对象，整个bean的生命周期，从创建->使用->销毁的整个过程都是由容器来管理（引伸Bean的生命周期）

- 分
  1. 一般聊IOC容器的时候要涉及到容器的创建过程（BeanFactory、DefaultListableBeanFactory）
     - 容器有个最上层的根接口`BeanFactory`，它只是个接口，没有提供对应的子类实现，我们在实际调用过程中，最普遍的就是DefaultListableBeanFactory；包括我们在使用的时候，我们会优先创建当前的BeanFactory，创建完BeanFactory，向Beanfactory中设置一些参数（BeanPostProcessor，Aware接口的子类）等等属性
  2. 加载解析Bean对象，准备要创建的Bean对象的定义对象`beanDefinition`(期间会涉及到xml或者注解的解析过程)
  3. BeanFactoryPostProcessor的处理（此处是扩展点：修改bean的定义） 
     - PlaceHolderConfigurerSupport，ConfigurationClassPostProcessor

</details>

<details>
<summary>2.谈一下Spring IOC的底层实现</summary>

-

</details>

<details>
<summary>3.描述一下bean的生命周期</summary>

-

</details>

<details>
<summary>4.Spring是如何解决循环依赖的</summary>

-

</details>

<details>
<summary>5.Bean Factory与FactoryBean有什么区别</summary>

-

</details>

<details>
<summary>6.Spring中用到的设计模式</summary>

-

</details>

<details>
<summary>7.Spring的AOP的底层实现原理</summary>

-

</details>

<details>
<summary>8.Spring的事务是如何回滚的</summary>

-

</details>

<details>
<summary>9.谈一下Spring的事务传播</summary>

-

</details>