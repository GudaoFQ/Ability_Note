## Java8 基于CAS的ConcurrentHashMap
* Java 7为实现并行访问，引入了Segment这一结构，实现了分段锁，理论上最大并发度与Segment个数相等。
* Java 8为进一步提高并发性，摒弃了分段锁的方案，而是直接使用一个大的数组。
* 同时为了提高哈希碰撞下的寻址性能，Java 8在链表长度超过一定阈值（8）时将链表（寻址时间复杂度为O(N)）转换为 红黑树（寻址时间复杂度为O(log(N))）【源码中标明阈值为8是因为通过泊松分布，在8的时候的碰撞概率是很低的只有0.00000006】

#### 重要参数及初始化
* 桶的树化阈值：即 链表转成红黑树的阈值，在存储数据时，当链表长度 > 该值时，则将链表转换成红黑树
```markdown
static final int TREEIFY_THRESHOLD = 8;
```

* 桶的链表还原阈值：即 红黑树转为链表的阈值，当在扩容（resize（））时（此时 HashMap 的数据存储位置会重新计算），在重新计算存储位置后，当原有的红黑树内数量 < 6 时，则将 红黑树转换成链表
```markdown
static final int UNTREEIFY_THRESHOLD = 6;
```

* 最小树形化容量阈值：即 当哈希表中的容量 > 该值时，才允许树形化链表 （即 将链表 转换成红黑树）否则，若桶内元素太多时，则直接扩容，而不是树形化
    * 为了避免进行扩容、树形化选择的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD
```markdown
static final int MIN_TREEIFY_CAPACITY = 64;
```

* 默认加载因子
```markdown
private static final float LOAD_FACTOR = 0.75f;
```

* `static final int MOVED     = -1;` // hash值是-1，表示这是一个forwardNode节点
* `static final int TREEBIN   = -2;` // hash值是-2  表示这是一个TreeBin节点

* 和 HashMap 中的语义一样，代表整个哈希表。
transient volatile Node<K,V>[] table;
这是一个连接表，用于哈希表扩容，扩容完成后会被重置为 null。
```markdown
/**
* The next table to use; non-null only while resizing.
*/
private transient volatile Node<K,V>[] nextTable;
```

* 该属性保存着整个哈希表中存储的所有的结点的个数总和，有点类似于 HashMap 的 size 属性。
```markdown
private transient volatile long baseCount;
```

* 这是一个重要的属性，无论是初始化哈希表，还是扩容 rehash 的过程，都是需要依赖这个关键属性的。该属性有以下几种取值：
    * 负数代表正在进行初始化或扩容操作
    * -1 代表正在初始化
    * -N 表示有 N-1 个线程正在进行扩容操作
    * 正数或 0 代表 hash 表还没有被初始化，这个数值表示初始化或下一次进行扩容的大小，类似于扩容阈值。它的值始终是当前 ConcurrentHashMap 容量的 0.75 倍，这与 loadfactor 是对应的。实际容量 >=sizeCtl，则扩容
```markdown
private transient volatile int sizeCtl;
```

* 线程迁移 bin 的起始位置，CAS(transferIndex)成功者可迁移 transferIndex 前置 stride 个 bin（见 transfer）
```markdown
private transient volatile int transferIndex;
```
#### put方法源码
```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    //判断concurrentHashMap中的key与value是否为空
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```