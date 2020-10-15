package desgindemo.stretagy.easystrategy;

/**
 * 比较策略算法
 * Author : GuDao
 * 2020-10-14
 */
public class Sorter<T> {
    //使用选择排序进行比较
    public void sort(T[] objs, Comparator<T> comparator){
        for (int i = 0; i < objs.length-1; i++) {
            int minPos=i;
            for (int j = i+1; j < objs.length; j++) {
                minPos = comparator.comparable(objs[j],objs[minPos]) < 0 ? j : minPos;
            }
            //minPos大的话，此时选用j索引；如果比较结果<0，那么minPos还是i；所以下列方法中传输objs,i,minPos
            swap(objs,i,minPos);
        }
    }

    //位置互换
    private void swap(T[] objs, int i, int minPos) {
        T temp = objs[i];
        objs[i] = objs[minPos];
        objs[minPos] = temp;
    }
}
