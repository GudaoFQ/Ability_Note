## Meta-Annotation

#### 说明
* 在Java中，注解分为两种，元注解和自定义注解。
* 一般我们把元注解理解为描述注解的注解，元数据理解为描述数据的数据，元类理解为描述类的类
* 所以，在Java中，除了有限的几个固定的”描述注解的注解”以外，所有的注解都是自定义注解。
* 在JDK中提供了4个标准的用来对注解类型进行注解的注解类（元注解），他们分别是:
```markdown
@Target
@Retention
@Documented
@Inherited
```

#### @Target
> @Target 表示该注解用于什么地方，可能的值在枚举类 ElemenetType 中，包括
```markdown
ElemenetType.CONSTRUCTOR：构造器声明 
ElemenetType.FIELD：域声明（包括 enum 实例） 
ElemenetType.LOCAL_VARIABLE：局部变量声明 
ElemenetType.METHOD：方法声明 
ElemenetType.PACKAGE：包声明 
ElemenetType.PARAMETER：参数声明 
ElemenetType.TYPE：类，接口（包括注解类型）或enum声明 

ElemenetType.ANNOTATION_TYPE：注释类型声明
ElemenetType.TYPE_PARAMETER：参数类型声明
ElemenetType.TYPE_USE：使用类型
```

#### @Retention
> @Retention 表示在什么级别保存该注解信息。可选的参数值在枚举类型 RetentionPolicy 中，包括：
```markdown
RetentionPolicy.SOURCE：注解将被编译器丢弃 
RetentionPolicy.CLASS：注解在class文件中可用，但会被VM丢弃 
RetentionPolicy.RUNTIME：VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息。
```

#### @Decumented
> @Documented 将此注解包含在 javadoc 中 ，它代表着此注解会被javadoc工具提取成文档。在doc文档中的内容会因为此注解的信息内容不同而不同。相当与@see,@param等。

#### @Inherited
> @Inherited 允许子类继承父类中的注解。
