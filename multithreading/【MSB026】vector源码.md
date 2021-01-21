#### 源码解析
```java
public class Vector<E>
        extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    //保存Vector中数据的数组
    protected Object[] elementData;

    //实际数据的数量
    protected int elementCount;

    //容量增长系数
    protected int capacityIncrement;

    //Vector的序列版本号
    private static final long serialVersionUID = -2767605614048989439L;

    //指定Vector容量大小和增长系数的构造方法
    public Vector(int initialCapacity, int capacityIncrement) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        //新建一个数组，数组容量是 initialCapacity
        this.elementData = new Object[initialCapacity];
        //设置容量的增长系数
        this.capacityIncrement = capacityIncrement;
    }

    //指定Vector容量大小的构造方法
    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    //构造方法，默认容量是10
    public Vector() {
        this(10);
    }

    //指定集合的Vector构造方法
    public Vector(Collection<? extends E> c) {
        //获取集合c的数组，并将其赋值给elementData
        elementData = c.toArray();
        //设置数组的长度
        elementCount = elementData.length;
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
    }

    //将数组Vector的全部元素都拷贝到数组anArray中
    public synchronized void copyInto(Object[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    //将当前容量值设为实际元素个数
    public synchronized void trimToSize() {
        modCount++;
        int oldCapacity = elementData.length;
        if (elementCount < oldCapacity) {
            elementData = Arrays.copyOf(elementData, elementCount);
        }
    }

    //确认Vector的容量
    public synchronized void ensureCapacity(int minCapacity) {
        if (minCapacity > 0) {
            modCount++;
            ensureCapacityHelper(minCapacity);
        }
    }

    //确认Vector容量的帮助方法
    private void ensureCapacityHelper(int minCapacity) {
        // 当Vector的容量不足以容纳当前的全部元素，就进行扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    //要分配的数组的最大大小
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //扩容。
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        //如果容量增量系数（capacityIncrement） > 0，就把容量增大到capacityIncrement
        //否则，将容量增大一倍
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                capacityIncrement : oldCapacity);
        //下面的处理方法与ArrayList一致
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
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

    //设置容量值为 newSize
    public synchronized void setSize(int newSize) {
        modCount++;
        if (newSize > elementCount) {
            //如果newSize大于Vector的容量，就调整Vector的大小
            ensureCapacityHelper(newSize);
        } else {
            //如果newSize小于/等于Vector容量，就将newSize位置开始的元素都设为null
            for (int i = newSize; i < elementCount; i++) {
                elementData[i] = null;
            }
        }
        elementCount = newSize;
    }

    //返回Vector的总容量
    public synchronized int capacity() {
        return elementData.length;
    }

    //返回Vector的实际大小，即Vector中元素个数
    public synchronized int size() {
        return elementCount;
    }

    //判断vector是否为空
    public synchronized boolean isEmpty() {
        return elementCount == 0;
    }

    //返回Vector中全部元素对应的Enumeration
    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;

            //是否存在下一个元素
            public boolean hasMoreElements() {
                return count < elementCount;
            }

            //获取下一个元素
            public E nextElement() {
                synchronized (Vector.this) {
                    if (count < elementCount) {
                        return elementData(count++);
                    }
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    //返回vector中是否包含对象o
    public boolean contains(Object o) {
        return indexOf(o, 0) >= 0;
    }

    //查找并返回元素o在Vector中的索引值
    public int indexOf(Object o) {
        return indexOf(o, 0);
    }

    //从index位置开始向后查找元素o
    //如果找到，就返回元素的索引值；否则，返回-1
    public synchronized int indexOf(Object o, int index) {
        if (o == null) {
            //如果查找元素为null，则正向找出null，并返回它对应的序号
            for (int i = index; i < elementCount; i++)
                if (elementData[i] == null)
                    return i;
        } else {
            //如果查找元素不为null，则正向找出该元素，并返回它对应的序号
            for (int i = index; i < elementCount; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //从后向前查找元素o，并返回元素的索引值
    public synchronized int lastIndexOf(Object o) {
        return lastIndexOf(o, elementCount - 1);
    }

    //从后向前查找元素o，开始位置是从前向后的第index个数
    //如果找到，就返回元素的索引值，否则，返回-1
    public synchronized int lastIndexOf(Object o, int index) {
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= " + elementCount);

        if (o == null) {
            //如果查找元素为null，就反向找出null元素，并返回它对应的序号
            for (int i = index; i >= 0; i--)
                if (elementData[i] == null)
                    return i;
        } else {
            //如果查找元素不为null，就反向找出该元素，并返回它对应的序号
            for (int i = index; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //返回Vector中index位置的元素
    //如果index越界，就抛出异常
    public synchronized E elementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }

        return elementData(index);
    }

    //获取Vector中的第一个元素
    //如果失败，就抛出异常
    public synchronized E firstElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(0);
    }

    //获取Vector中的最后一个元素
    //如果失败就抛出异常
    public synchronized E lastElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(elementCount - 1);
    }

    //设置index位置的元素值为obj
    public synchronized void setElementAt(E obj, int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                    elementCount);
        }
        elementData[index] = obj;
    }

    //删除index位置的元素
    public synchronized void removeElementAt(int index) {
        modCount++;
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                    elementCount);
        } else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        int j = elementCount - index - 1;
        if (j > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }
        elementCount--;
        elementData[elementCount] = null; /* to let gc do its work */
    }

    //在index位置处插入元素obj
    public synchronized void insertElementAt(E obj, int index) {
        modCount++;
        if (index > elementCount) {
            throw new ArrayIndexOutOfBoundsException(index
                    + " > " + elementCount);
        }
        ensureCapacityHelper(elementCount + 1);
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        elementData[index] = obj;
        elementCount++;
    }

    //将元素obj添加到Vector末尾
    public synchronized void addElement(E obj) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = obj;
    }

    //在Vector中查找并删除元素obj
    //成功的话，返回true;否则，返回false
    public synchronized boolean removeElement(Object obj) {
        modCount++;
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    //删除Vector中的全部元素
    public synchronized void removeAllElements() {
        modCount++;
        //将Vector中的全部元素设为null
        for (int i = 0; i < elementCount; i++)
            elementData[i] = null;

        elementCount = 0;
    }

    //克隆方法
    public synchronized Object clone() {
        try {
            @SuppressWarnings("unchecked")
            Vector<E> v = (Vector<E>) super.clone();
            v.elementData = Arrays.copyOf(elementData, elementCount);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    //返回Object数组
    public synchronized Object[] toArray() {
        return Arrays.copyOf(elementData, elementCount);
    }

    //返回Vector的T数组。
    @SuppressWarnings("unchecked")
    public synchronized <T> T[] toArray(T[] a) {
        //如果数组a的大小 < Vector的元素个数
        //就新建一个T[]数组，数组大小是Vector的元素个数，并将Vector全部拷贝到新数组中
        if (a.length < elementCount)
            return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());

        //如果数组a大小 >= Vector的元素个数
        //就将Vector的全部元素都拷贝到数组a中
        System.arraycopy(elementData, 0, a, 0, elementCount);

        if (a.length > elementCount)
            a[elementCount] = null;

        return a;
    }


    //返回index位置元素
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    //获取index位置的元素
    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    //设置index位置的值为element。并返回index位置的原始值
    public synchronized E set(int index, E element) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    //将元素e添加到Vector最后
    public synchronized boolean add(E e) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    //删除Vector中的元素o
    public boolean remove(Object o) {
        return removeElement(o);
    }

    //在index位置添加元素element
    public void add(int index, E element) {
        insertElementAt(element, index);
    }

    //删除index位置的元素，并返回index位置的原始值
    public synchronized E remove(int index) {
        modCount++;
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);
        E oldValue = elementData(index);

        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        elementData[--elementCount] = null; // Let gc do its work

        return oldValue;
    }

    //清空Vector
    public void clear() {
        removeAllElements();
    }

    // Bulk Operations

    //返回Vector是否包含c
    public synchronized boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    //将集合c添加到Vector中
    public synchronized boolean addAll(Collection<? extends E> c) {
        modCount++;
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);
        //将集合c的全部元素拷贝到数组elementData中
        System.arraycopy(a, 0, elementData, elementCount, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    //删除集合c的全部元素
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    //删除不是集合c的元素
    public synchronized boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    //从index位置开始，将集合c添加到Vector中
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        modCount++;
        if (index < 0 || index > elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);

        int numMoved = elementCount - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    //返回两个对象是否相等
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

    //计算哈希值
    public synchronized int hashCode() {
        return super.hashCode();
    }

    //调用父类的toString()
    public synchronized String toString() {
        return super.toString();
    }

    //获取Vector中fromIndex到toIndex的子集(左闭右开)
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return Collections.synchronizedList(super.subList(fromIndex, toIndex),
                this);
    }

    //删除Vector中fromIndex到toIndex的元素
    protected synchronized void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = elementCount - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                numMoved);

        // Let gc do its work
        int newElementCount = elementCount - (toIndex - fromIndex);
        while (elementCount != newElementCount)
            elementData[--elementCount] = null;
    }

    //java.io.Serializable 的写入方法
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        final java.io.ObjectOutputStream.PutField fields = s.putFields();
        final Object[] data;
        synchronized (this) {
            fields.put("capacityIncrement", capacityIncrement);
            fields.put("elementCount", elementCount);
            data = elementData.clone();
        }
        fields.put("elementData", data);
        s.writeFields();
    }

    //返回一个ListIterator
    public synchronized ListIterator<E> listIterator(int index) {
        if (index < 0 || index > elementCount)
            throw new IndexOutOfBoundsException("Index: " + index);
        return new ListItr(index);
    }

    //返回一个ListIterator迭代器，该迭代器是fail-fast机制的
    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    //返回一个Iterator迭代器，该迭代器是fail-fast机制的
    public synchronized Iterator<E> iterator() {
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
    final class ListItr extends Itr implements ListIterator<E> {
       ···
    }

    //1.8方法，用于函数式编程
    @Override
    public synchronized void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int elementCount = this.elementCount;
        for (int i = 0; modCount == expectedModCount && i < elementCount; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    //移除集合中满足给定条件的所有元素 1.8新增
    @Override
    @SuppressWarnings("unchecked")
    public synchronized boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // figure out which elements are to be removed
        // any exception thrown from the filter predicate at this stage
        // will leave the collection unmodified
        int removeCount = 0;
        final int size = elementCount;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        for (int i = 0; modCount == expectedModCount && i < size; i++) {
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

        // shift surviving elements left over the spaces left by removed elements
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i = 0, j = 0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k = newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            elementCount = newSize;
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
    public synchronized void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = elementCount;
        for (int i = 0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    //排序
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, elementCount, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    //获取一个分割器，java8开始提供
    @Override
    public Spliterator<E> spliterator() {
        return new VectorSpliterator<>(this, null, 0, -1, 0);
    }

    //基于索引的、二分的、懒加载的分割器
    static final class VectorSpliterator<E> implements Spliterator<E> {
        ···
    }
}
```
#### Enumeration
> Vector中有一个elements()方法用来返回一个Enumeration,以匿名内部类的方式实现：
```java
 //返回Vector中全部元素对应的Enumeration
public Enumeration<E> elements() {
    return new Enumeration<E>() {
        int count = 0;

        //是否存在下一个元素
        public boolean hasMoreElements() {
            return count < elementCount;
        }

        //获取下一个元素
        public E nextElement() {
            synchronized (Vector.this) {
                if (count < elementCount) {
                    return elementData(count++);
                }
            }
            throw new NoSuchElementException("Vector Enumeration");
        }
    };
}
```
* numeration接口和Iterator类似，产生先于Iterator，都用作于对集合进行迭代，不过**没有删除功能**，已经被Iterator取代。
* 还有Iterator是Fail-Fast（快速失败）的，但Enumeration不是，这一点很重要。
    * Fail-Fast快速失效机制尽早抛出错误对程序来说提高了程序的反应速率。
