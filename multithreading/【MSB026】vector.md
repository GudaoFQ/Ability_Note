## Vector【自带锁】
> Java1.0就有了，太老了，设计有缺陷，基本不用

> Vector是JDK1.0引入的，比整个集合还要早，它的很多实现方法都加入了同步语句，因此是线程安全的（其实也只是相对安全，有些时候还是要加入同步语句来保证线程的安全），可以用于多线程环境。Vector实现了Serializable接口，支持序列化，实现了Cloneable接口，能被克隆，实现了RandomAccess接口，支持快速随机访问

#### 总结
* Vector有四个不同的构造方法。无参构造方法的容量为默认值10，仅包含容量的构造方法则将容量增长量设置为0.
* 注意扩充容量的方法ensureCapacityHelper。与ArrayList相同，Vector在每次增加元素（1个或者1组）时，都要调用该方法来确保足够的容量。当容量不足以容纳当前的元素个数时，就先看构造方法中传入的容量增长量参数CapacityIncrement是否为0，如果不为0，就设置新的容量为旧容量加上容量增长量；如果为0，就设置新的容量为旧的容量的2倍，如果设置后的新容量还不够，则直接新容量设置为传入参数（即所需容量），然后同样用Arrays.copyOf()方法将元素拷贝到新的数组。
* 很多方法都加入了synchronized同步语句，来保证线程安全。
* 同样在查找给定元素索引值等方法时，源码都将元素的值分为null和不为null两种情况进行处理，Vector中允许元素为null。
* 其他很多地方都与ArrayList实现大同小异，Vector现在已经基本不再使用

#### Vector与ArrrayList的区别
* ArrayList在内存不够时默认是扩展为1.5倍 + 1个，Vector是默认扩展为2倍
* Vector提供indexOf(obj, start)接口，ArrayList没有
* Vector属于线程安全级别的，但是大多数情况下不使用Vector，因为线程安全需要更大的系统开销  

#### Vector使用
```java
/**
 * Author : GuDao
 * 2020-11-20
 */
public class VectorDemo {
    public static void main(String[] args) {
        // 默认容器大小为10
        Vector<String> vector = new Vector<>();

        // 指定容器大小，此时的默认扩展因子为0
        //Vector<String> vector = new Vector<>(15);

        // 指定容器大小与扩展因子
        //Vector<String> vector = new Vector<>(15, 5);

        vector.add("1");
        vector.add("2");
        vector.add("3");
        vector.add("4");
        vector.add("5");

        // 设置索引为9的值
        vector.set(1, "gudao");
        // 索引越界，通过索引进行设值得时候只能设置当前vector的size内的值
        //System.out.println(vector.size());
        //vector.set(6, "gudao");
        System.out.println(vector);

        // 插入索引为2的值[后面的值回向后进行移动一位索引]
        vector.add(3, "test");
        // 这边也同上
        //vector.add(9,"test");
        System.out.println(vector);

        // (顺序查找)获取 gudao 的索引
        System.out.println("顺序查找gudao：" + vector.indexOf("gudao"));
        // (倒序查找)获取100的索引
        System.out.println("倒序查找gudao：" + vector.lastIndexOf("gudao"));
        // 获取第一个元素
        System.out.println("获取第一个元素：" + vector.firstElement());
        // 获取第三个元素
        System.out.println("获取第3个元素：" + vector.elementAt(2));
        // 获取最后一个元素
        System.out.println("获取最后一个元素：" + vector.lastElement());

        // 获取Vector的大小
        System.out.println("size：" + vector.size());
        // 获取Vector的总的容量
        System.out.println("获取Vector的总的容量：" + vector.capacity());

        // 获取vector的“第2”到“第4”个元素
        System.out.println("Vector 2 to 4:" + vector.subList(2, 5));

        // 通过Enumeration遍历Vector
        Enumeration<String> elements = vector.elements();
        while (elements.hasMoreElements()) {
            System.out.print(elements.nextElement() + "\t");
        }

        System.out.println("");

        Vector retainVector = new Vector();
        retainVector.add("test");
        retainVector.add("300");
        // 获取“vector”中包含在“retainVector中的元素”的集合
        System.out.println("vector.retain():" + vector.retainAll(retainVector));
        System.out.println("vector:" + vector);

        // 获取vector对应的String数组
        String[] array = (String[]) vector.toArray(new String[0]);
        for (String str : array) {
            System.out.println("str:"+str);
        }

        // 清空Vector.clear()和removeAllElements()一样！
        //vector.clear();
        vector.removeAllElements();
        System.out.println(vector);

        // 判断Vector是否为空
        System.out.println(vector.isEmpty());
    }
}
```
