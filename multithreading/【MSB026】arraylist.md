## ArrayList
> 以数组实现。节约空间，但数组有容量限制。超出限制时会增加50%容量，用System.arraycopy()复制到新的数组，因此最好能给出数组大小的预估值。默认第一次插入元素时创建大小为10的数组
* 按照数组索引访问元素：get(int index)/set(int index)的性能很高，这是数组的优势。直接在数组末尾加入元素：add(e)的性能也高，但如果按索引插入、删除元素：add(i,e)、remove(i)、remove(e)，则要用System.arraycopy()来移动部分受影响的元素，性能就变差了，这是数组的劣势。
* ArrayList不是线程安全的，只能在单线程环境下，多线程环境下可以考虑用Collections.synchronizedList(List list)方法返回一个线程安全的ArrayList对象，也可以使用concurrent并发包下的CopyOnWriteArrayList类。
* ArrayList实现了Serializable接口，因此它支持序列化，能够通过序列化传输，实现了RandomAccess接口，支持快速随机访问，实际上根据源码我们知道就是通过索引序号进行快速访问，实现了Cloneable接口，能被克隆。

#### List继承自Collection，和Collection比较，List 还添加了以下操作方法:
* 增删改查：add(index,object)、remove(index)、set(index,object)、get(index);
* 搜索：indexOf(),lastIndexOf();
* 迭代器：listIterator();
* 获取列表中的小列表：subList(int fromIndex, int toIndex);

#### 总结
1. ArrayList是基于数组实现的，它的内存储元素的数组为 elementData;elementData的声明为：transient Object[] elementData;
2. ArrayList中EMPTY_ELEMENTDATA和DEFAULTCAPACITY_EMPTY_ELEMENTDATA的使用；这两个常量，使用场景不同。前者是用在用户通过ArrayList(int initialCapacity)该构造方法直接指定初始容量为0时，后者是用户直接使用无参构造创建ArrayList时。
3. ArrayList默认容量为10。调用无参构造新建一个ArrayList时，它的elementData = DEFAULTop CAPACITY_EMPTY_ELEMENTDATA, 当第一次使用 add() 添加元素时，ArrayList的容量会为 10。
4. ArrayList的扩容计算为 newCapacity = oldCapacity + (oldCapacity >> 1);且扩容并非是无限制的，有内存限制，虚拟机限制。
5. ArrayList的toArray()方法和subList()方法，在源数据和子数据之间的区别；
    1. toArray():对该方法返回的数组，进行操作（增删改查）都不会影响源数据（ArrayList中elementData）。二者之间是不会相互影响的。
    2. subList():对返回的子集合，进行操作（增删改查）都会影响父集合。而且若是对父集合中进行删除操作（仅仅在删除子集合的首个元素）时，会抛出异常java.util.ConcurrentModificationException。
    
#### ArrayList和Vector区别以及其扩容机制
> 相同点
1. ArrayList和Vector都是继承了相同的父类和实现了相同的接口
    （extends AbstractList implements List, Cloneable, Serializable, RandomAccess）
2. 底层都是数组（Object[]）实现的
3. 初始默认长度都为10。

> 不同点
1. 同步性（Synchronization）：
    Vector中的public方法多数添加了synchronized关键字、以确保方法同步、也即是Vector线程安全、ArrayList线程不安全。
2. 扩容（Resize）：
    ArrayList以1.5倍的方式在扩容、Vector 当扩容容量增量大于0时、新数组长度为原数组长度+扩容容量增量、否则新数组长度为原数组长度的2倍。
3. 性能（Performance）：
    由于第一点的原因、在性能方便通常情况下ArrayList的性能更好、而Vector存在synchronized 的锁等待情况、需要等待释放锁这个过程、所以性能相对较差。
4. 快速失败（fail-fast）：
    Vector 的 elements 方法返回的 Enumeration 不是 快速失败（fail-fast）的。而ArrayList是快速失败（fail-fast）

#### 源码解析
```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    //序列号版本
    private static final long serialVersionUID = 8683452581122892189L;
    
    //默认初始容量为10
    private static final int DEFAULT_CAPACITY = 10;

    //共享空数组实例用于空实例
    private static final Object[] EMPTY_ELEMENTDATA = {};

    //共享空数组实例用于默认大小的空实例
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    //ArrayList基于该数组实现，用该数组保存数据
    transient Object[] elementData; 

    //ArrayList中实际数据的数量
    private int size;

    //ArrayList带容量大小的构造方法
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            //新建一个数组
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            //创建空数组实例
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    //ArrayList无参构造方法。当元素第一次被加入时，扩容为默认大小10
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    //创建一个包含Collection的ArrayList
    public ArrayList(Collection<? extends E> c) {
        //调用toArray()方法把collection转换为数组
        elementData = c.toArray();
        //将转换后的 Object[] 长度赋值给当前 ArrayList 的 size，并判断是否为 0 
        if ((size = elementData.length) != 0) {
            if (elementData.getClass() != Object[].class)
                 // 若 c.toArray() 返回的数组类型不是 Object[]，则利用 Arrays.copyOf(); 来构造一个大小为 size 的 Object[] 数组 
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            //换成空数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    //将当前容量值设为实际元素个数
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            //调整数组缓冲区elementData，变为实际存储大小Arrays.copyOf(elementData,size)
            elementData = (size == 0)? EMPTY_ELEMENTDATA : Arrays.copyOf(elementData, size);
        }
    }

    //确定ArrayList的容量。(指定最小容量)
    public void ensureCapacity(int minCapacity) {
        //最小扩容，默认是10
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) ? 0: DEFAULT_CAPACITY;
        //如果用户指定的最小容量>最小扩容10，就以用户指定为准
        if (minCapacity > minExpand) {
            //确定明确的容量
            ensureExplicitCapacity(minCapacity);
        }
    }

    //私有方法：确定ArrayList的容量大小
    private void ensureCapacityInternal(int minCapacity) {
        //确保容量大小>=默认大小
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        //确定明确容量
        ensureExplicitCapacity(minCapacity);
    }

    //确定ArrayList的具体大小
    private void ensureExplicitCapacity(int minCapacity) {
        //将“修改统计数”+1，该变量主要是用来实现fail-fast机制
        modCount++;
        // 超出了数组可容纳的长度，需要进行动态扩展
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    //要分配的数组的最大大小
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //扩容。确保至少能容纳最小容量参数指定的元素个数。
    //这是动态扩容的精髓，ArrayList的奥秘一览无余
    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        //得到数组的旧容量，进行oldCapacity + (oldCapacity >> 1)，将oldCapacity右移一位，相当于oldCapacity/2
        //这样的结果便是将新数组的容量扩展到原来数组的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //判断新数组的容量够不够，够了就直接使用这个大小创建新的数组
        //不够就将数组大小设置为需要的大小
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        //在判断有没有超过上面的最大容量限制，超出限制就调hugeCapacity（）方法进行处理
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        //将原来数组的值copy新数组中去，ArrayList的引用指向新数组
        //这儿会新创建数组，如果数据量很大，重复的创建数组，会影响效率
        //因此最好在合适的时候通过构造方法指定默认的capaticy大小
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    //超出容量限制的处理方法
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    //返回ArrayList的实际大小
    public int size() {
        return size;
    }
    
    //返回ArrayList是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    //ArrayList是否包含Object(o)
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    //正向查找，返回元素的索引值
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //反向查找，返回元素的索引值
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //克隆函数
    //对拷贝出来的ArrayList对象的操作，不会影响原来的ArrayList
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            //将当前ArrayList的全部元素拷贝到v中
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    //返回ArrayList的Object数组
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    //返回ArrayList元素组成的数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        //如果数组a的大小 < ArrayList的元素个数
        if (a.length < size)
            //就新建一个T[]数组，数组大小为ArrayList的元素个数，然后将ArrayList全部拷贝到新数组中
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
            
       //如果数组a的大小 >= ArrayList的元素个数
       //就将ArrayList的全部元素都拷贝到数组中
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    //获取index位置的元素值
    public E get(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

        return (E) elementData[index];
    }

    //设置index位置的值为element
    public E set(int index, E element) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

     //添加元素
     //根据传入的最小需要容量minCapacity来和数组的容量长度对比，若minCapactity大于或等于数组容量，则需要进行扩容。
    public boolean add(E e) {
        //确定ArrayList的容量大小
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //添加e到ArrayList中
        elementData[size++] = e;
        return true;
    }

    //将e添加到ArrayList的指定位置
    public void add(int index, E element) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }

    //删除ArrayList指定位置的元素
    public E remove(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        modCount++;
        E oldValue = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
        elementData[--size] = null; // clear to let GC do its work
        return oldValue;
    }

    //删除ArrayList的指定元素
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            //遍历ArrayList，找到元素o,则删除，并返回true.
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    //快速删除第index个元素
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        //从 index+1 开始，用后面的元素替换前面的元素。
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,numMoved);
        //将最后一个元素设为null
        elementData[--size] = null; // clear to let GC do its work
    }

    //清空ArrayList，将全部的元素设为null
    public void clear() {
        modCount++;

        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    //将集合c追加到ArrayList中
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    //从index位置开始，将集合c添加到ArrayList
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    //删除fromIndex到toIndex之间的全部元素
    protected void removeRange(int fromIndex, int toIndex) {
        if (toIndex < fromIndex) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }

        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // clear to let GC do its work
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

 
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    //删除ArrayList中包含Collection c中的的元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    //保留ArrayList中包含Collection c中的的元素
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    //批量删除
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            if (r != size) {
                System.arraycopy(elementData, r,elementData,w,size - r);
                w += size - r;
            }
            if (w != size) {
                // clear to let GC do its work
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    //java.io.Serializable的写入方法
    //将ArrayList的容量，所有的元素值都写入到输出流中
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // Write out element count, and any hidden stuff
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // 写入数组的容量
        s.writeInt(size);

        // 写入数组的每一个元素
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    //java.io.Serializable的读取方法：根据写入方式读出
    //先将ArrayList的容量读出，再将所有的元素值读出
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        elementData = EMPTY_ELEMENTDATA;

        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // 从输入流中读取ArrayList的容量
        s.readInt(); // ignored

        if (size > 0) {
            // be like clone(), allocate array based upon size not capacity
            ensureCapacityInternal(size);

            Object[] a = elementData;
            //从输入流中将所有的元素值读出
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    //返回一个ListIterator
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    //返回一个ListIterator迭代器，该迭代器是fail-fast机制的
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    //返回一个Iterator迭代器，该迭代器是fail-fast机制的
    public Iterator<E> iterator() {
        return new Itr();
    }
    
    //AbstractList.Itr的优化版本，不做深究
    private class Itr implements Iterator<E> {
    ···
    }

    /**
     * AbAbstractList.ListItr 的优化版本
     * ListIterator与普通的Iterator的区别：
     * 它可以进行双向移动，而普通的迭代器只能单向移动
     * 它可以添加元素（有add（）方法），而后者不行
     */
    private class ListItr extends Itr implements ListIterator<E> {
    ...  
    }

   
   
    //获取从fromIndex到toIndex之间的子集合（左闭右开区间）
    /**
     * 如果fromIndex == toIndex，则返回空集合
     * 对该子集合的操作，会影响原有集合
     * 当调用了subList()后，若对原集合进行删除操作(删除subList中的首个元素)时，会抛出异常
     * 该自己支持所有的集合操作
     */
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);    //合法性检查
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        //越界检查
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        //非法参数检查
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +  ") > toIndex(" + toIndex + ")");
    }
    
    //嵌套内部类，实现了RandomAccess，提供快速随机访问特性
    private class SubList extends AbstractList<E> implements RandomAccess {
    ...
    }

    //1.8方法，用于函数式编程
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    //获取一个分割器，java8开始提供
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    //基于索引的、二分的、懒加载的分割器
    static final class ArrayListSpliterator<E> implements Spliterator<E> {
    ...
    }

    //移除集合中满足给定条件的所有元素 1.8新增
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        int removeCount = 0;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            this.size = newSize;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    //1.8新增 替换方法
    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    //排序
    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}
```