## Java8 基于CAS的ConcurrentHashMap
* Java 7为实现并行访问，引入了Segment这一结构，实现了分段锁，理论上最大并发度与Segment个数相等。
* Java 8使用cas+synchronized组合的方式来实现并发下的线程安全的，这种实现方式比1.5的效率又有了比较大的提升
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
    * sizeCtr=0：默认值；
    * sizeCtr=-1：表示Map正在初始化中；
    * sizeCtr=-N：表示正在有N-1个线程进行扩容操作；
    * sizeCtr>0: 未初始化则表示初始化Map的大小，已初始化则表示下次进行扩容操作的阈值；[表还没有被初始化，这个数值表示初始化或下一次进行扩容的大小，类似于扩容阈值。它的值始终是当前 ConcurrentHashMap 容量的 0.75 倍，这与 loadfactor 是对应的。实际容量 >=sizeCtl，则扩容]
```markdown
private transient volatile int sizeCtl;
```

* 线程迁移 bin 的起始位置，CAS(transferIndex)成功者可迁移 transferIndex 前置 stride 个 bin（见 transfer）
```markdown
private transient volatile int transferIndex;
```
#### 重要的内部类
##### ForwardingNode
> 这是一个特殊Node节点，仅在进行扩容时用作占位符，表示当前位置已被移动或者为null，该node节点的hash值为-1；
```java
static final class ForwardingNode<K,V> extends Node<K,V> {
final Node<K,V>[] nextTable;
ForwardingNode(Node<K,V>[] tab) {
    super(MOVED, null, null, null);
    this.nextTable = tab;
}
```
##### Node
```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val; // Java8增加volatile，保证可见性
    volatile Node<K,V> next;

    Node(inthash, K key, V val, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.val = val;
        this.next = next;
    }

    public final K getKey()       { return key; }
    public final V getValue()     { return val; }
    // HashMap调用Objects.hashCode()，最终也是调用Object.hashCode()；效果一样
    public final int hashCode()   { returnkey.hashCode() ^ val.hashCode(); }
    public final String toString(){ returnkey + "=" + val; }
    public final V setValue(V value) { // 不允许修改value值，HashMap允许
        throw new UnsupportedOperationException();
    }
    // HashMap使用if (o == this)，且嵌套if；concurrent使用&&
    public final boolean equals(Object o) {
        Object k, v, u; Map.Entry<?,?> e;
        return ((o instanceof Map.Entry) &&
                (k = (e = (Map.Entry<?,?>)o).getKey()) != null &&
                (v = e.getValue()) != null &&
                (k == key || k.equals(key)) &&
                (v == (u = val) || v.equals(u)));
    }

    /**
     * Virtualized support for map.get(); overridden in subclasses.
     */
    Node<K,V> find(inth, Object k) { // 增加find方法辅助get方法
        Node<K,V> e = this;
        if (k != null) {
            do {
                K ek;
                if (e.hash == h &&
                    ((ek = e.key) == k || (ek != null && k.equals(ek))))
                    returne;
            } while ((e = e.next) != null);
        }
        return null;
    }
}
```
##### 说明
* 这个 Node 内部类与 HashMap 中定义的 Node 类很相似，但是有一些差别
* 它对 value 和 next 属性设置了 volatile 同步锁
* 它不允许调用 setValue 方法直接改变 Node 的 value 域
* 它增加了 find 方法辅助 map.get()方法

#### TreeNode
```java
// Nodes for use in TreeBins，链表>8，才可能转为TreeNode.
// HashMap的TreeNode继承至LinkedHashMap.Entry；而这里继承至自己实现的Node，将带有next指针，便于treebin访问。
static final class TreeNode<K,V> extends Node<K,V> { 
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;

    TreeNode(int hash, K key, V val, Node<K,V> next,
             TreeNode<K,V> parent) {
        super(hash, key, val, next);
        this.parent = parent;
    }

    Node<K,V> find(int h, Object k) {
        return findTreeNode(h, k, null);
    }

    /**
     * Returns the TreeNode (or null if not found) for the given key
     * starting at given root.
     */ // 查找hash为h，key为k的节点
    final TreeNode<K,V> findTreeNode(int h, Object k, Class<?> kc) {
        if (k != null) { // 比HMap增加判空
            TreeNode<K,V> p = this;
            do  {
                intph, dir; K pk; TreeNode<K,V> q;
                TreeNode<K,V> pl = p.left, pr = p.right;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (pk != null && k.equals(pk)))
                    returnp;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                else if ((kc != null ||
                          (kc = comparableClassFor(k)) != null) &&
                         (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
                else if ((q = pr.findTreeNode(h, k, kc)) != null)
                    returnq;
                else
                    p = pl;
            } while (p != null);
        }
        return null;
    }
}
```
##### 说明
* 树节点类，另外一个核心的数据结构。当链表长度过长的时候，会转换为 TreeNode。但是与 HashMap 不相同的是，它并不是直接转换为红黑树，而是把这些结点包装成 TreeNode 放在 TreeBin 对象中，由 TreeBin 完成对红黑树的包装。而且 TreeNode 在 ConcurrentHashMap 集成自 Node 类，而并非 HashMap 中的集成自 LinkedHashMap.Entry<K,V> 类，也就是说 TreeNode 带有 next 指针，这样做的目的是方便基于 TreeBin 的访问

#### TreeBin
```java
TreeBin(TreeNode<K,V> b) {
    super(TREEBIN, null, null, null);//hash值为常量TREEBIN=-2,表示roots of trees【树的节点】
    this.first = b;
    TreeNode<K,V> r = null;
    for (TreeNode<K,V> x = b, next; x != null; x = next) {
        next = (TreeNode<K,V>)x.next;
        x.left = x.right = null;
        if (r == null) {
            x.parent = null;
            x.red = false;
            r = x;
        }
        else {
            K k = x.key;
            inth = x.hash;
            Class<?> kc = null;
            for (TreeNode<K,V> p = r;;) {
                intdir, ph;
                K pk = p.key;
                if ((ph = p.hash) > h)
                    dir = -1;
                elseif (ph < h)
                    dir = 1;
                elseif ((kc == null &&
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0)
                    dir = tieBreakOrder(k, pk);
                    TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    x.parent = xp;
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    r = balanceInsertion(r, x);
                    break;
                }
            }
        }
    }
    this.root = r;
    assert checkInvariants(root);
}
```
##### 说明
* TreeBin 用于封装维护 TreeNode，包含 putTreeVal、lookRoot、UNlookRoot、remove、balanceInsetion、balanceDeletion 等方法。
* 这里只分析其构造函数，可以看到在构造 TreeBin 节点时，仅仅指定了它的 hash 值为 TREEBIN 常量，这也就是个标识为。同时也看到我们熟悉的红黑树构造方法
* 当链表转树时，用于封装 TreeNode，也就是说，ConcurrentHashMap 的红黑树存放的是 TreeBin，而不是 treeNode。

#### ForwardingNode
```java
// A node inserted at head of bins during transfer operations.连接两个table
// 并不是我们传统的包含key-value的节点，只是一个标志节点，并且指向nextTable，提供find方法而已。生命周期：仅存活于扩容操作且bin不为null时，一定会出现在每个bin的首位。
static final class ForwardingNode<K,V> extends Node<K,V> {
    //新表的引用
    final Node<K,V>[] nextTable;
    ForwardingNode(Node<K,V>[] tab) {
        super(MOVED, null, null, null); // 此节点hash=-1，key、value、next均为null
        this.nextTable = tab;
    }
 
    //进行get操作的线程若发现槽中的节点为ForwordingNode类型
    //说明该桶中所有结点已迁移完成，会调用ForwordingNode的find方法在新表中进行查找
    Node<K,V> find(int h, Object k) {
        // 查nextTable节点，outer避免深度递归
        outer: for (Node<K,V>[] tab = nextTable;;) {
         // n表示新表的长度
            Node<K,V> e; int n;
            if (k == null || tab == null || (n = tab.length) == 0 ||
                (e = tabAt(tab, (n - 1) & h)) == null)
                return null;
            for (;;) { // CAS算法多和死循环搭配！直到查到或null
                int eh; K ek;
                if ((eh = e.hash) == h &&
                    ((ek = e.key) == k || (ek != null && k.equals(ek))))
                    return e;
                if (eh < 0) {
                    if (e instanceof ForwardingNode) {
                        tab = ((ForwardingNode<K,V>)e).nextTable;
                        continue outer;
                    }
                    else
                        return e.find(h, k);
                }
                if ((e = e.next) == null)
                    return null;
            }
        }
    }
}
```
##### 说明
* 一个用于连接两个 table 的节点类。它包含一个 nextTable 指针，用于指向下一张表。而且这个节点的 key value next 指针全部为 null，它的 hash 值为-1. 这里面定义的 find 的方法是从 nextTable 里进行查询节点，而不是以自身为头节点进行查找

#### Unsafe 里的 CAS 操作相关
> ConcurrentHashMap中用到的
```java
// 第一个参数o为给定对象，offset为对象内存的偏移量，通过这个偏移量迅速定位字段并设置或获取该字段的值，
// expected表示期望值，x表示要设置的值
public final native boolean compareAndSwapObject(Object o, long offset,Object expected, Object x);
```

#### put方法源码
> 在多线程中可能有以下两个情况
* 如果一个或多个线程正在对 ConcurrentHashMap 进行扩容操作，当前线程也要进入扩容的操作中。这个扩容的操作之所以能被检测到，是因为 transfer 方法中在空结点上插入 forward 节点，如果检测到需要插入的位置被 forward 节点占有，就帮助进行扩容；
* 如果检测到要插入的节点是非空且不是 forward 节点，就对这个节点加锁，这样就保证了线程安全。尽管这个有一些影响效率，但是还是会比 hashTable 的 synchronized 要好得多。
> 整体流程
1. 校验 key value 值，都不能是 null。这点和 HashMap 不同。
2. 得到 key 的 hash 值。
3. 死循环并更新 tab 变量的值。
4. 如果容器没有初始化，则初始化。调用 initTable 方法。该方法通过一个变量 + CAS 来控制并发。稍后我们分析源码。
5. 根据 hash 值找到数组下标，如果对应的位置为空，就创建一个 Node 对象用 CAS 方式添加到容器。并跳出循环。
6. 如果 hash 冲突，也就是对应的位置不为 null，则判断该槽是否被扩容了（-1 表示被扩容了），如果被扩容了，返回新的数组。
7. 如果 hash 冲突 且 hash 值不是 -1，表示没有被扩容。则进行链表操作或者红黑树操作，注意，这里的 f 头节点被锁住了，保证了同时只有一个线程修改链表。防止出现链表成环。
8. 和 HashMap 一样，如果链表树超过 8，则修改链表为红黑树。
9. 将数组加 1（CAS 方式），如果需要扩容，则调用 transfer 方法进行移动和重新散列，该方法中，如果是槽中只有单个节点，则使用 CAS 直接插入，如果不是，则使用 synchronized 进行同步，防止并发成环。
```java
// 对外暴露的接口
public V put(K key, V value) {
    return putVal(key, value, false);
}

final V putVal(K key, V value, boolean onlyIfAbsent) {
    // 判断key，value的值是否为空，为空的话直接抛出空指针异常
    if (key == null || value == null) throw new NullPointerException();
    // 计算key的32位hash值
    int hash = spread(key.hashCode());
    // 用于计算链表中的节点数【链表长度】；最后数据put完成后，通过该值判断是不是要进行结构转化
    int binCount = 0;
    // 死循环遍历table中的node数据节点【这个过程是非阻塞的，放入失败会一直循环尝试，直至成功】
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;

        // 如果当前tab是空的，就执行`initTable`来初始化数组
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();

        // tabAt方法：从对象的指定偏移量处获取变量的引用，使用volatile的加载语义【获取tab下执行索引的元素】
        // 如果为空，直接通过cas操作进行设值
        // 用一次 CAS 操作将这个新值放入其中即可，这个 put 操作差不多就结束了，可以拉到最后面了
        // 如果 CAS 失败，那就是有并发操作，直接跳出当前循环
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }

        // 当hash为-1的时候，表示当前数组正在进行扩容操作【hash 居然可以等于 MOVED(MOVED = -1;  hash for forwarding nodes)，这个需要到后面才能看明白，不过从名字上也能猜到，肯定是因为在扩容】
        else if ((fh = f.hash) == MOVED)
            // 【表明该位置正在进行扩容操作，让当前线程也帮助该位置上的扩容，并发扩容提高扩容的速度】帮助数据迁移，这个等到看完数据迁移部分的介绍后，再理解这个就很简单了
            tab = helpTransfer(tab, f);

        // 进入此判断，表示通过计算后的索引下已经有元素了
        else {
            V oldVal = null;
            // 给链表头node进行加锁
            // 注意：此时的f，fh，i已经通过上面的if条件判断()中的赋值操作赋值结束了
            synchronized (f) {
                // 通过unsafe下的方法获取指定索引下的元素，再次判断当前元素是不是索引下的表头
                if (tabAt(tab, i) == f) {
                    // 头结点的 hash 值大于 0，说明是链表
                    if (fh >= 0) {
                        // 用于累加，记录链表的长度
                        binCount = 1;
                        // 遍历链表中元素
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            // 如果需要添加的内容的key的hash，key都与当前遍历的节点信息相同，然后判断值是否要进行覆盖
                            if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                                //获取节点中的旧值，最后进行返回
                                oldVal = e.val;
                                // 判断值是否要进行覆盖，onlyIfAbsent为put接口总传参false，所以此处直接覆盖
                                // 仅putIfAbsent()方法中onlyIfAbsent为true；putIfAbsent()包含key则返回get，否则put并返回  
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;

                            // 到了链表的最末端，将这个新值放到链表的最后面
                            if ((e = e.next) == null) {// 注意：此处又最e进行了重新赋值
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    
                    // 红黑树
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        // 调用红黑树的插值方法插入新节点
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key, value)) != null) {
                            // 当索引下节点的key与要添加的key值和hash都相同是，会判断是否覆盖，并将原先节点value值赋值给oldVal，此时进行返回
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            
            // binCount != 0 说明向链表或者红黑树中添加或修改一个节点成功
            // binCount == 0 说明 put 操作将一个新节点添加成为某个桶的首节点
            if (binCount != 0) {
                // 判断该值得长度是否达到了转换树的阈值8
                if (binCount >= TREEIFY_THRESHOLD)
                    // 执行树化操作
                    // 树化操作中会通过setTabAt(tab, index, new TreeBin<K,V>(hd))将Node变换为TreeBin，TreeBin中会将Node中的hash变为-2【super(TREEBIN, null, null, null)】
                    treeifyBin(tab, i);
                if (oldVal != null)
                    // 如果是put方法调用的本方法，就是将被覆盖的值返回
                    // 如果是putIfAbsent调用的本方法，就是将元素中的老值返回
                    return oldVal;
                break;
            }
        }
    }
    // 内部通过cas判断是否要对当前数组进行扩容操作【数组长度超过0.75f，进行扩容】
    // 扩容操作中会通过transfer(tab, nt)将Node变为ForwardingNode，ForwardingNode中会将Node中的hash变为-1【super(MOVED, null, null, null)】
    addCount(1L, binCount);
    return null;
}
```

#### 初始化方法initTable源码
* 主要就是初始化一个合适大小的数组，然后会设置 sizeCtl。
* 初始化方法中的并发问题是通过对 sizeCtl 进行一个 CAS 操作来控制的。
* 该方法的核心思想就是，只允许一个线程对表进行初始化，如果不巧有其他线程进来了，那么会让其他线程交出 CPU 等待下次系统调度。这样，保证了表同时只会被一个线程初始化。
```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        // 若某线程发现sizeCtl<0，意味着其他线程正在初始化，当前线程让出CPU时间片
        if ((sc = sizeCtl) < 0)
            // 失去初始化的竞争机会; 直接自旋
            Thread.yield(); // lost initialization race; just spin

        // CAS 一下，将 sizeCtl 设置为 -1，代表抢到了锁
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            try {
                // 有可能执行至此时，table 已经非空，所以做双重检验
                if ((tab = table) == null || tab.length == 0) {
                    // sc 大于零说明容量已经初始化了，否则使用默认 DEFAULT_CAPACITY 默认初始容量是 16
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    // 初始化数组，长度为 16 或初始化时提供的长度
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    // 将这个数组赋值给 table，table 是 volatile 的
                    table = tab = nt;
                    // 如果 n 为 16 的话，那么这里 sc = 12
                    // 其实就是 0.75 * n
                    sc = n - (n >>> 2);
                }
            } finally {
                // 设置 sizeCtl 为 sc，我们就当是 12 吧
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```
#### 帮助数据迁移helpTransfer源码
```java
final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
    Node<K,V>[] nextTab; int sc;
    if (tab != null && (f instanceof ForwardingNode) &&
        (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {
        //返回一个 16 位长度的扩容校验标识
        int rs = resizeStamp(tab.length);
        while (nextTab == nextTable && table == tab &&
               (sc = sizeCtl) < 0) {
            //sizeCtl 如果处于扩容状态的话
            //前 16 位是数据校验标识，后 16 位是当前正在扩容的线程总数
            //这里判断校验标识是否相等，如果校验符不等或者扩容操作已经完成了，直接退出循环，不用协助它们扩容了
            // 如果 sizeCtl 无符号右移  16 不等于 rs （ sc前 16 位如果不等于标识符，则标识符变化了）
            // 或者 sizeCtl == rs + 1  （扩容结束了，不再有线程进行扩容）（默认第一个线程设置 sc ==rs 左移 16 位 + 2，当第一个线程结束扩容了，就会将 sc 减一。这个时候，sc 就等于 rs + 1）
            // 或者 sizeCtl == rs + 65535  （如果达到最大帮助线程的数量，即 65535）
            // 或者转移下标正在调整 （扩容结束）
            // 结束循环，返回 table
            if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                sc == rs + MAX_RESIZERS || transferIndex <= 0)
                break;
            //否则调用 transfer 帮助它们进行扩容
            //sc + 1 标识增加了一个线程进行扩容
            if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                transfer(tab, nextTab);
                break;
            }
        }
        return nextTab;
    }
    return table;
}
```
#### 数据迁移transfer源码
> 我们在 putVal 方法中遍历整个 hash 表的桶结点，如果遇到 hash 值等于 MOVED，说明已经有线程正在扩容 rehash 操作，整体上还未完成，不过我们要插入的桶的位置已经完成了所有节点的迁移。
```java
private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
    // 旧数组的长度
    int n = tab.length, stride;
    if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)
        stride = MIN_TRANSFER_STRIDE; // subdivide range
    // 如果新数组为空，初始化，大小为原数组的两倍，n << 1
    if (nextTab == null) {            // initiating
        try {
            @SuppressWarnings("unchecked")
            Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];
            nextTab = nt;
        } catch (Throwable ex) {      // try to cope with OOME
            sizeCtl = Integer.MAX_VALUE;
            return;
        }
        nextTable = nextTab;
        transferIndex = n;
    }
    // 新数组长度
    int nextn = nextTab.length;
    // 若原数组上是转移节点，说明该节点正在被扩容
    ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
    boolean advance = true;
    boolean finishing = false; // to ensure sweep before committing nextTab
    // 自旋，i 值会从原数组的最大值递减到 0
    for (int i = 0, bound = 0;;) {
        Node<K,V> f; int fh;
        while (advance) {
            int nextIndex, nextBound;
            // 结束循环的标志
            if (--i >= bound || finishing)
                advance = false;
            // 已经拷贝完成
            else if ((nextIndex = transferIndex) <= 0) {
                i = -1;
                advance = false;
            }
            // 每次减少 i 的值
            else if (U.compareAndSwapInt
                     (this, TRANSFERINDEX, nextIndex,
                      nextBound = (nextIndex > stride ?
                                   nextIndex - stride : 0))) {
                bound = nextBound;
                i = nextIndex - 1;
                advance = false;
            }
        }
        // if 任意条件满足说明拷贝结束了
        if (i < 0 || i >= n || i + n >= nextn) {
            int sc;
            // 拷贝结束，直接赋值，因为每次拷贝完一个节点，都在原数组上放转移节点，所以拷贝完成的节点的数据一定不会再发生变化
            // 原数组发现是转移节点，是不会操作的，会一直等待转移节点消失之后在进行操作
            // 也就是说数组节点一旦被标记为转移节点，是不会再发生任何变动的，所以不会有任何线程安全的问题
            // 所以此处直接赋值，没有任何问题。
            if (finishing) {
                nextTable = null;
                table = nextTab;
                sizeCtl = (n << 1) - (n >>> 1);
                return;
            }
            if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                    return;
                finishing = advance = true;
                i = n; // recheck before commit
            }
        }
        else if ((f = tabAt(tab, i)) == null)
            advance = casTabAt(tab, i, null, fwd);
        else if ((fh = f.hash) == MOVED)
            advance = true; // already processed
        else {
            synchronized (f) {
                // 节点的拷贝
                if (tabAt(tab, i) == f) {
                    Node<K,V> ln, hn;
                    if (fh >= 0) {
                        int runBit = fh & n;
                        Node<K,V> lastRun = f;
                        for (Node<K,V> p = f.next; p != null; p = p.next) {
                            int b = p.hash & n;
                            if (b != runBit) {
                                runBit = b;
                                lastRun = p;
                            }
                        }
                        if (runBit == 0) {
                            ln = lastRun;
                            hn = null;
                        }
                        else {
                            hn = lastRun;
                            ln = null;
                        }
                        // 如果节点只有单个数据，直接拷贝，如果是链表，循环多次组成链表拷贝
                        for (Node<K,V> p = f; p != lastRun; p = p.next) {
                            int ph = p.hash; K pk = p.key; V pv = p.val;
                            if ((ph & n) == 0)
                                ln = new Node<K,V>(ph, pk, pv, ln);
                            else
                                hn = new Node<K,V>(ph, pk, pv, hn);
                        }
                        // 在新数组位置上放置拷贝的值
                        setTabAt(nextTab, i, ln);
                        setTabAt(nextTab, i + n, hn);
                        // 在老数组位置上放上 ForwardingNode 节点
                        // put 时，发现是 ForwardingNode 节点，就不会再动这个节点的数据了
                        setTabAt(tab, i, fwd);
                        advance = true;
                    }
                    // 红黑树的拷贝
                    else if (f instanceof TreeBin) {
                        // 红黑树的拷贝工作，同 HashMap 的内容，代码忽略
                        ...
                        // 在老数组位置上放上 ForwardingNode 节点
                        setTabAt(tab, i, fwd);
                        advance = true;
                    }
                }
            }
        }
    }
}
```
#### 链表转红黑树treeifyBin源码 
> treeifyBin 不一定就会进行红黑树转换，也可能是仅仅做数组扩容[当数组的长度小于64，即是链表长度超过了8，也不会进行树化，只会进行数组扩容]
```java
private final void treeifyBin(Node<K,V>[] tab, int index) {
    Node<K,V> b; int n, sc;
    if (tab != null) {
        // MIN_TREEIFY_CAPACITY 为 64
        // 所以，如果数组长度小于 64 的时候，其实也就是 32 或者 16 或者更小的时候，会进行数组扩容
        if ((n = tab.length) < MIN_TREEIFY_CAPACITY)
            // 后面我们再详细分析这个方法
            tryPresize(n << 1);
        // b 是头结点
        else if ((b = tabAt(tab, index)) != null && b.hash >= 0) {
            // 加锁
            synchronized (b) {
                if (tabAt(tab, index) == b) {
                    // 下面就是遍历链表，建立一颗红黑树
                    TreeNode<K,V> hd = null, tl = null;
                    for (Node<K,V> e = b; e != null; e = e.next) {
                        TreeNode<K,V> p =
                            new TreeNode<K,V>(e.hash, e.key, e.val,
                                              null, null);
                        if ((p.prev = tl) == null)
                            hd = p;
                        else
                            tl.next = p;
                        tl = p;
                    }
                    // 将红黑树设置到数组相应位置中
                    setTabAt(tab, index, new TreeBin<K,V>(hd));
                }
            }
        }
    }
}
```
#### 扩容tryPresize源码
> 扩容也是做翻倍扩容的，扩容后数组容量为原来的 2 倍
```java
// 首先要说明的是，方法参数 size 传进来的时候就已经翻了倍了
private final void tryPresize(int size) {
    // c：size 的 1.5 倍，再加 1，再往上取最近的 2 的 n 次方。
    int c = (size >= (MAXIMUM_CAPACITY >>> 1)) ? MAXIMUM_CAPACITY :
        tableSizeFor(size + (size >>> 1) + 1);
    int sc;
    while ((sc = sizeCtl) >= 0) {
        Node<K,V>[] tab = table; int n;

        // 这个 if 分支和之前说的初始化数组的代码基本上是一样的，在这里，我们可以不用管这块代码
        if (tab == null || (n = tab.length) == 0) {
            n = (sc > c) ? sc : c;
            if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                try {
                    if (table == tab) {
                        @SuppressWarnings("unchecked")
                        Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                        table = nt;
                        sc = n - (n >>> 2); // 0.75 * n
                    }
                } finally {
                    sizeCtl = sc;
                }
            }
        }
        else if (c <= sc || n >= MAXIMUM_CAPACITY)
            break;
        else if (tab == table) {
            int rs = resizeStamp(n);
            if (sc < 0) {
                Node<K,V>[] nt;
                //RESIZE_STAMP_SHIFT=16,MAX_RESIZERS=2^15-1
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                    sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                    transferIndex <= 0)
                    break;
                // 2. 用 CAS 将 sizeCtl 加 1，然后执行 transfer 方法
                //    此时 nextTab 不为 null
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                    transfer(tab, nt);
            }
            // 1. 将 sizeCtl 设置为 (rs << RESIZE_STAMP_SHIFT) + 2)
            //   计算出来结果是一个比较大的负数
            //  调用 transfer 方法，此时 nextTab 参数为 null
            else if (U.compareAndSwapInt(this, SIZECTL, sc,
                                         (rs << RESIZE_STAMP_SHIFT) + 2))
                transfer(tab, null);
        }
    }
}
```

#### get源码
```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        // 判断头结点是否就是我们需要的节点
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        // 如果头结点的 hash 小于 0，说明 正在扩容，或者该位置是红黑树
        else if (eh < 0)
            // 参考 ForwardingNode.find(int h, Object k) 和 TreeBin.find(int h, Object k)
            return (p = e.find(h, key)) != null ? p.val : null;

        // 遍历链表
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```
