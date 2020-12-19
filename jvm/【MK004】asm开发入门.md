## ASM

### ASM概述
* ASM是一个Java字节码操纵框架,它能被用来动态生成类或者增强既有类的功能
* ASM可以直接产生二进制class文件,也可以在类被加载入虚机之态改变行为,ASM从类文件中)入信息后能够改变类行为,分析类信息,甚至能根据要求生成新类
* 目前许多框架如cglib, Hibernate, Spring都直接或间接地使用ASM操作字节码

### ASM编程模型
* Core API:提供了基于事件形式的编程模型。该模型不需要一次性将整个类的结构读取到内存中,因此这种方式更快,需要更少的内存,但这种编程方式难度较大
* Tree API :提供了基于树形的编程模型。该模型需要一次性将一个类的完整结构全部读取到内存当中,所以这种方法需要更多的内存,这种编程方式较简单

### Core API
* ASM Core API中操纵字节码的功能基于ClassVisitor接口。这个接口中的每个方法对应了class文件中的每一项
* ASM提供了三个基于ClassVisitor接口的类来实现class文件的生成和转换
    * ClassReader:ClassReader解析一个类的class字节码
    * ClassAdapter:ClassAdapter是ClassVisitor的实现类,实现要变化的功能
    * ClassWriter:ClassWriter也是ClassVisitor的实现类,可以用来输出变化后的字节码
    
### 工具推荐
ASM给我们提供了ASMifier工具来帮助开发,可使用ASMifier工具生成ASM结构来对比
```xml
<dependency>
    <groupId>org.ow2.asm</groupId>
    <artifactId>asm</artifactId>
    <version>8.0.1</version>
</dependency>
<dependency>
    <groupId>org.ow2.asm</groupId>
    <artifactId>asm-util</artifactId>
    <version>8.0.1</version>
</dependency>
```
