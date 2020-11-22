## IdentityHashMap
> IdentityHashMap类实现了AbstractMap类。它类似于HashMap，但是它在**比较元素时使用引用相等**。

dentityHashMap的用法和HashMap的用法差不多，他们之间最大的区别就是**IdentityHashMap**判断两个key是否相等，是通过严格相等即`(key1==key2)`来判读的，而**HashMap**通过`equals()`方法和`hashCode()`这两个方法来判断key是否相等的

| 方法                                 | 描述                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| IdentityHashMap()                    | 此构造函数构造一个新的空标识哈希映射，它具有默认的预期最大大小(21) |
| IdentityHashMap(int expectedMaxSize) | 此构造函数使用指定的预期最大大小(`expectedMaxSize`)来构造一个新的空`IdentityHashMap` |
| IdentityHashMap(Map m)               | 此构造函数构造一个新的标识哈希映射，其中包含指定映射中的键-值映射 |

#### 提供的方法

| 方法                                 | 描述                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| void clear()                         | 从此映射中删除所有映射                                       |
| Object clone()                       | 返回此标识哈希映射的浅表副本：未克隆键和值本身               |
| boolean containsKey(Object key)      | 测试指定的对象引用是否是此标识哈希映射中的键                 |
| boolean containsValue(Object value)  | 测试指定的对象引用是否是此标识哈希映射中的值                 |
| Set entrySet()                       | 返回此映射中包含的映射的集合视图                             |
| boolean equals(Object o)             | 将指定对象与此映射进行相等性比较                             |
| Object get(Object key)               | 返回指定键在此标识哈希映射中映射到的值，如果映射不包含此键的映射，则返回`null` |
| int hashCode()                       | 返回此映射的哈希码值                                         |
| boolean isEmpty()                    | 如果此标识哈希映射不包含键-值映射，则返回`true`              |
| Set keySet()                         | 返回此映射中包含的键的基于标识的集合视图                     |
| Object put(Object key, Object value) | 将指定的值与此标识哈希映射中的指定键相关联                   |
| void putAll(Map t)                   | 将指定映射中的所有映射复制到此映射。这些映射将替换此映射对当前位于指定映射中键的映射 |
| Object remove(Object key)            | 从此映射中删除此键的映射(如果存在)                           |
| int size()                           | 返回此标识哈希映射中键-值映射的数量                          |
| Collection values()                  | 返回此映射中包含的值的集合视图                               |
