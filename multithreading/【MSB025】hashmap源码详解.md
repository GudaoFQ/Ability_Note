#### 在jdk源码中Entry的实现类如下
```java
static class Node<K,V> implements Map.Entry<K,V> {
      // key的哈希值
      final int hash;
      final K key;
      V value;
      // 下一个Node，没有则为null
      Node<K,V> next;

//省略下面代码
}
```
#### 在HashMap类中还定义了几个静态常量，这几个常量是一些很重要的属性，源码如下
```java
public class HashMap<K,V> extends AbstractMap<K,V> 
	implements Map<K,V>, Cloneable, Serializable {

	 /**
     * HashMap的默认初始容量大小 16
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * HashMap的最大容量 2的30次方
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 负载因子，代表了table的填充度有多少，默认是0.75。当数组中的数据大于总长度的0.75倍时
     * HashMap会自动扩容，默认扩容到原长度的两倍。为什么是两倍，而不是1.5倍，或是3倍。这个
     * 2倍很睿智，后面会说到
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 默认阈值，当桶(bucket)上的链表长度大于这个值时会转成红黑树，put方法的代码里有用到
     * 在jdk1.7中链表就是普通的单向链表，很多数据出现哈希碰撞导致这些数据集中在某一个哈希桶上，
     * 因而导致链表很长，会出现效率问题，jdk1.8对此做了优化，默认当链表长度大于8时转化为红黑树
     */
    static final int TREEIFY_THRESHOLD = 8;
    
    /**
     * 和上一个的阈值相对的阈值，当桶(bucket)上的链表长度小于这个值时红黑树退化成链表
     */
    static final int UNTREEIFY_THRESHOLD = 6;
    
    /**
     * 用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，如果期间其他线程的参与导致HashMap      * 的结构发生变化了（比如put，remove等操作），需要抛出异常ConcurrentModificationException
     */
    transient int modCount;
}
```
HashMap有两个有参构造器可以用来设置initialCapacity和loadFactor的值，即HashMap的初始容量和负载因子的值，如果不传参则都使用默认值。

### 数据写入操作
> 向HashMap中写入数据的过程，简单总结起来分为这么几步
* 计算要插入数据的Hash值，并根据该值确定元素的插入位置（即在动态数组中的位置）。
* 将元素放入到数组的指定位置
    * 如果该数组位置之前没有元素，则直接放入
        * 放入该位置后，数组元素超过扩容阈值，则对数组进行扩容
        * 放入该位置后，数组元素没超过扩容阈值，写入结束
    * 如果该数组位置之前有元素，则挂载到已有元素的后端
        * 如果之前元素组成了树，则挂入树的指定位置
        * 如果之前元素组成了链表
            * 如果加入该元素链表长度超过8，则将链表转化为红黑树后插入
            * 如果加入该元素链表长度不超过8，则直接插入
            
#### Put源码
```java
//提供给外部调的方法
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
			   boolean evict) {
			   
	//table全局变量,存储链表头节点数组
	Node<K,V>[] tab; Node<K,V> p; int n, i;
	
	//如果table数组是空的，则创建一个头结点数据，默认长度是16
	if ((tab = table) == null || (n = tab.length) == 0)
		n = (tab = resize()).length;
		
	//n是table长度,根据数组长度和key的哈希值，定位当前key在table中的下标位置,如果为空则新建一个node。不为空走else
	if ((p = tab[i = (n - 1) & hash]) == null)
		tab[i] = newNode(hash, key, value, null);
	else {
		Node<K,V> e; K k;
		// 如果新的key与table中索引处取出的头节点的key相等，且hash值一致，则把新的node替换掉旧的node
		if (p.hash == hash &&
			((k = p.key) == key || (key != null && key.equals(k))))
			e = p;
		//如果头节点不是空，且头节点的类型是树节点类型，则把当前节点插入当前头节点所在的树中(红黑树，防止链表过长，1.8的优化)
		else if (p instanceof TreeNode)
			e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
		//map的数据结构处理
		else {
			//遍历链表
			for (int binCount = 0; ; ++binCount) {
				如果当前节点的下一个节点为空，则把新节点插入到下一个节点
				if ((e = p.next) == null) {
					p.next = newNode(hash, key, value, null);
					//如果链表长度大于或等于8，则把链表转化为红黑树，重点转化方法(treeifyBin)
					if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
					//此方法构建红黑树
						treeifyBin(tab, hash);
					break;
				}
				//如果当前节点的key和hash均和待插入的节点相等，则退出循环，(注意此时e的值在前一个if时赋值过，因此当前的e值，就是链表中的当前节点值)
				if (e.hash == hash &&
					((k = e.key) == key || (key != null && key.equals(k))))
					break;
				p = e;
			}
		}
		//如果老节点不是空，则将老节点的值替换为新值，并返回老的值
		if (e != null) { // existing mapping for key
			V oldValue = e.value;
			if (!onlyIfAbsent || oldValue == null)
				e.value = value;
			//此方法实现的逻辑,是把入参的节点放置在链表的尾部，但在HashMap中是空实现，在LinkedHashMap中有具体实现
			afterNodeAccess(e);
			return oldValue;
		}
	}
    //保证并发访问时，若HashMap内部结构发生变化，快速响应失败
	++modCount;
    //当table[]长度大于临界阈值，调用resize方法进行扩容
	if (++size > threshold)
		resize();
	//此方法在HashMap中是空方法，在LinkedHashMap中有实现
	afterNodeInsertion(evict);
	return null;
	}
```

#### treeifyBin方法构建红黑树
> 此方法中比较重要的方法就是treefyBin方法了，此方法以当前节点为头节点，构建一个双向链表(双向链表：链表的一种，它的每个数据结点中都有两个指针，分别指向直接后继和直接前驱。所以，从双向链表中的任意一个结点开始，都可以很方便地访问它的前驱结点和后继结点
```java
/**
 * tab：元素数组，
 * hash：hash值（要增加的键值对的key的hash值）
 */
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
        /*
         * 如果元素数组为空 或者 数组长度小于 树结构化的最小限制
         * MIN_TREEIFY_CAPACITY 默认值64，对于这个值可以理解为：如果元素数组长度小于这个值，没有必要去进行结构转换
         * 当一个数组位置上集中了多个键值对，那是因为这些key的hash值和数组长度取模之后结果相同。（并不是因为这些key的hash值相同）
         * 因为hash值相同的概率不高，所以可以通过扩容的方式，来使得最终这些key的hash值在和新的数组长度取模之后，拆分到多个数组位置上。
         */
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize(); // 扩容，可参见resize方法解析
     
        // 如果元素数组长度已经大于等于了 MIN_TREEIFY_CAPACITY，那么就有必要进行结构转换了
        // 根据hash值和数组长度进行取模运算后，得到链表的首节点
        else if ((e = tab[index = (n - 1) & hash]) != null) { 
            TreeNode<K,V> hd = null, tl = null; // 定义首、尾节点
            do { 
                TreeNode<K,V> p = replacementTreeNode(e, null); // 将该节点转换为 树节点
                if (tl == null) // 如果尾节点为空，说明还没有根节点
                    hd = p; // 首节点（根节点）指向 当前节点
                else { // 尾节点不为空，以下两行是一个双向链表结构
                    p.prev = tl; // 当前树节点的 前一个节点指向 尾节点
                    tl.next = p; // 尾节点的 后一个节点指向 当前节点
                }
                tl = p; // 把当前节点设为尾节点
            } while ((e = e.next) != null); // 继续遍历链表
     
            // 到目前为止 也只是把Node对象转换成了TreeNode对象，把单向链表转换成了双向链表
     
            // 把转换后的双向链表，替换原来位置上的单向链表
            if ((tab[index] = hd) != null)
                hd.treeify(tab);//此处单独解析
        }

}
```
#### treeify方法构建红黑树
然后在这个方法中构建红黑树，jdk1.8对HashMap的优化核心也在于此方法，可以重点看看
```java
final void treeify(Node<K,V>[] tab) {
    TreeNode<K,V> root = null; // 定义树的根节点
	//这里的this是通过hd.treeify(tab);方法调用的，因此this就是hd，而hd是table数组中某个值，也就是链表的头节点
	// 以下的代码就是从头节点开始遍历链表
    for (TreeNode<K,V> x = this, next; x != null; x = next) { // 遍历链表，x指向当前节点、next指向下一个节点
        next = (TreeNode<K,V>)x.next; // 下一个节点
        x.left = x.right = null; // 设置当前节点的左右节点为空
        if (root == null) { // 如果还没有根节点
            x.parent = null; // 当前节点的父节点设为空
            x.red = false; // 当前节点的红色属性设为false（把当前节点设为黑色）
            root = x; // 根节点指向到当前节点
        }
        else { // 如果已经存在根节点了
            K k = x.key; // 取得当前链表节点的key
            int h = x.hash; // 取得当前链表节点的hash值
            Class<?> kc = null; // 定义key所属的Class
			//遍历红黑树
            for (TreeNode<K,V> p = root;;) { // 从根节点开始遍历，此遍历没有设置边界，只能从内部跳出
                // GOTO1
                int dir, ph; // dir 标识方向（左右）、ph标识当前树节点的hash值
                K pk = p.key; // 当前树节点的key
                if ((ph = p.hash) > h) // 如果当前树节点hash值 大于 当前链表节点的hash值
                    dir = -1; // 标识当前链表节点会放到当前树节点的左侧
                else if (ph < h)
                    dir = 1; // 右侧
 
                /*
                 * 如果两个节点的key的hash值相等，那么还要通过其他方式再进行比较
                 * 如果当前链表节点的key实现了comparable接口，并且当前树节点和链表节点是相同Class的实例，那么通过comparable的方式再比较两者。
                 * 如果还是相等，最后再通过 tieBreakOrder 比较一次
                 */
                else if ((kc == null &&
				//comparableClassFor返回键的类对象，该类必须实现Comparable接口，否则返回null
                            (kc = comparableClassFor(k)) == null) ||
                            (dir = compareComparables(kc, k, pk)) == 0)
                    dir = tieBreakOrder(k, pk);
 
                TreeNode<K,V> xp = p; // 保存当前树节点
 
                /*
                 * 如果dir 小于等于0 ： 当前链表节点一定放置在当前树节点的左侧，但不一定是该树节点的左孩子，也可能是左孩子的右孩子 或者 更深层次的节点。
                 * 如果dir 大于0 ： 当前链表节点一定放置在当前树节点的右侧，但不一定是该树节点的右孩子，也可能是右孩子的左孩子 或者 更深层次的节点。
                 * 如果当前树节点不是叶子节点，那么最终会以当前树节点的左孩子或者右孩子 为 起始节点  再从GOTO1 处开始 重新寻找自己（当前链表节点）的位置
                 * 如果当前树节点就是叶子节点，那么根据dir的值，就可以把当前链表节点挂载到当前树节点的左或者右侧了。
                 * 挂载之后，还需要重新把树进行平衡。平衡之后，就可以针对下一个链表节点进行处理了。
                 */
				 //如果dir等于-1则，插入到左树，如果是1则插入到右树
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    x.parent = xp; // 当前链表节点 作为 当前树节点的子节点
                    if (dir <= 0)
                        xp.left = x; // 作为左孩子
                    else
                        xp.right = x; // 作为右孩子
                    root = balanceInsertion(root, x); // 重新平衡
                    break;
                }
            }
        }
    }
 
    // 把所有的链表节点都遍历完之后，最终构造出来的树可能经历多个平衡操作，根节点目前到底是链表的哪一个节点是不确定的
    // 因为我们要基于树来做查找，所以就应该把 tab[N] 得到的对象一定根节点对象，而目前只是链表的第一个节点对象，所以要做相应的处理。
    moveRootToFront(tab, root); 
}
```

### 数据读取操作
> 相比于数据写入，数据读取操作要简单一些。总体过程总结为
* 根据要取得key的值，hash出数组中的指定位置
* 取出指定位置的元素（这时，key的hash值是一样的）
    * 如果key也完全一样，则返回该值，查找结束。
    * 如果key不一样，判断其后面挂载的是树还是列表
        * 如果是树，按照树的方法查找
        * 如果是列表，按照列表的方法查找
```java
// 供外部调用的方法
public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // 判断数组存在且不为空，否则直接返回null
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            // 第一个节点key与要查找的完全一致
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            // 第一个节点key与要查找的不一致
            if (first instanceof TreeNode)
                // 按照树的方法查找
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                // 按照列表方法查找
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

#### 数组扩容resize方法
* 获取原HashMap的容量与负载容量
* 如果原HashMap的容量大于0
    * 如果原HashMap的容量超过了HashMap的最大值，就不再进行扩容
    * 如果原HashMap的容量没有超过最大值，就扩充为原来的2倍
* 如果此时的新的HashMap负载容量还是0，计算新的resize上限
* 将原HashMap中的每个bucket都移动到新的HashMap中的buckets中
```java
final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table; //当前所有元素所在的数组，称为老的元素数组
        int oldCap = (oldTab == null) ? 0 : oldTab.length; //老的元素数组长度
        int oldThr = threshold;	// 老的扩容阀值设置
        int newCap, newThr = 0;	// 新数组的容量，新数组的扩容阀值都初始化为0
        if (oldCap > 0) {	// 如果老数组长度大于0，说明已经存在元素
            // PS1
            if (oldCap >= MAXIMUM_CAPACITY) { // 如果数组元素个数大于等于限定的最大容量（2的30次方）
                // 扩容阀值设置为int最大值（2的31次方 -1 ），因为oldCap再乘2就溢出了。
                threshold = Integer.MAX_VALUE;	
                return oldTab;	// 返回老的元素数组
            }
 
           /*
            * 如果数组元素个数在正常范围内，那么新的数组容量为老的数组容量的2倍（左移1位相当于乘以2）
            * 如果扩容之后的新容量小于最大容量  并且  老的数组容量大于等于默认初始化容量（16），那么新数组的扩容阀值设置为老阀值的2倍。（老的数组容量大于16意味着：要么构造函数指定了一个大于16的初始化容量值，要么已经经历过了至少一次扩容）
            */
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
 
        // PS2
        // 运行到这个else if  说明老数组没有任何元素
        // 如果老数组的扩容阀值大于0，那么设置新数组的容量为该阀值
        // 这一步也就意味着构造该map的时候，指定了初始化容量。
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            // 能运行到这里的话，说明是调用无参构造函数创建的该map，并且第一次添加元素
            newCap = DEFAULT_INITIAL_CAPACITY;	// 设置新数组容量 为 16
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY); // 设置新数组扩容阀值为 16*0.75 = 12。0.75为负载因子（当元素个数达到容量了4分之3，那么扩容）
        }
 
        // 如果扩容阀值为0 （PS2的情况）
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);  // 参见：PS2
        }
        threshold = newThr; // 设置map的扩容阀值为 新的阀值
        @SuppressWarnings({"rawtypes","unchecked"})
            // 创建新的数组（对于第一次添加元素，那么这个数组就是第一个数组；对于存在oldTab的时候，那么这个数组就是要需要扩容到的新数组）
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;	// 将该map的table属性指向到该新数组
        if (oldTab != null) {	// 如果老数组不为空，说明是扩容操作，那么涉及到元素的转移操作
            for (int j = 0; j < oldCap; ++j) { // 遍历老数组
                Node<K,V> e;
                if ((e = oldTab[j]) != null) { // 如果当前位置元素不为空，那么需要转移该元素到新数组
                    oldTab[j] = null; // 释放掉老数组对于要转移走的元素的引用（主要为了使得数组可被回收）
                    if (e.next == null) // 如果元素没有有下一个节点，说明该元素不存在hash冲突
                        // PS3
                        // 把元素存储到新的数组中，存储到数组的哪个位置需要根据hash值和数组长度来进行取模
                        // 【hash值  %   数组长度】   =    【  hash值   & （数组长度-1）】
                        //  这种与运算求模的方式要求  数组长度必须是2的N次方，但是可以通过构造函数随意指定初始化容量呀，如果指定了17,15这种，岂不是出问题了就？没关系，最终会通过tableSizeFor方法将用户指定的转化为大于其并且最相近的2的N次方。 15 -> 16、17-> 32
                        newTab[e.hash & (newCap - 1)] = e;
 
                        // 如果该元素有下一个节点，那么说明该位置上存在一个链表了（hash相同的多个元素以链表的方式存储到了老数组的这个位置上了）
                        // 例如：数组长度为16，那么hash值为1（1%16=1）的和hash值为17（17%16=1）的两个元素都是会存储在数组的第2个位置上（对应数组下标为1），当数组扩容为32（1%32=1）时，hash值为1的还应该存储在新数组的第二个位置上，但是hash值为17（17%32=17）的就应该存储在新数组的第18个位置上了。
                        // 所以，数组扩容后，所有元素都需要重新计算在新数组中的位置。
 
 
                    else if (e instanceof TreeNode)  // 如果该节点为TreeNode类型
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);  // 此处单独展开讨论
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;  // 按命名来翻译的话，应该叫低位首尾节点
                        Node<K,V> hiHead = null, hiTail = null;  // 按命名来翻译的话，应该叫高位首尾节点
                        // 以上的低位指的是新数组的 0  到 oldCap-1 、高位指定的是oldCap 到 newCap - 1
                        Node<K,V> next;
                        // 遍历链表
                        do {  
                            next = e.next;
                            // 这一步判断好狠，拿元素的hash值  和  老数组的长度  做与运算
                            // PS3里曾说到，数组的长度一定是2的N次方（例如16），如果hash值和该长度做与运算，那么该hash值可参与计算的有效二进制位就是和长度二进制对等的后几位，如果结果为0，说明hash值中参与计算的对等的二进制位的最高位一定为0.
                            // 因为数组长度的二进制有效最高位是1（例如16对应的二进制是10000），只有*..0**** 和 10000 进行与运算结果才为00000（*..表示不确定的多个二进制位）。又因为定位下标时的取模运算是以hash值和长度减1进行与运算，所以下标 = (*..0**** & 1111) 也= (*..0**** & 11111) 。1111是15的二进制、11111是16*2-1 也就是31的二级制（2倍扩容）。
                            // 所以该hash值再和新数组的长度取摸的话mod值也不会放生变化，也就是说该元素的在新数组的位置和在老数组的位置是相同的，所以该元素可以放置在低位链表中。
                            if ((e.hash & oldCap) == 0) {  
                                // PS4
                                if (loTail == null) // 如果没有尾，说明链表为空
                                    loHead = e; // 链表为空时，头节点指向该元素
                                else
                                    loTail.next = e; // 如果有尾，那么链表不为空，把该元素挂到链表的最后。
                                loTail = e; // 把尾节点设置为当前元素
                            }
 
                            // 如果与运算结果不为0，说明hash值大于老数组长度（例如hash值为17）
                            // 此时该元素应该放置到新数组的高位位置上
                            // 例：老数组长度16，那么新数组长度为32，hash为17的应该放置在数组的第17个位置上，也就是下标为16，那么下标为16已经属于高位了，低位是[0-15]，高位是[16-31]
                            else {  // 以下逻辑同PS4
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) { // 低位的元素组成的链表还是放置在原来的位置
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {  // 高位的元素组成的链表放置的位置只是在原有位置上偏移了老数组的长度个位置。
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead; // 例：hash为 17 在老数组放置在0下标，在新数组放置在16下标；    hash为 18 在老数组放置在1下标，在新数组放置在17下标；                   
                        }
                    }
                }
            }
        }
        return newTab; // 返回新数组
    }
```