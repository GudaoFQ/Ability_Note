package desgindemo.stretagy.easystrategy;

/**
 * 用户身高比较策略实体
 * Author : GuDao
 * 2020-10-15
 */

public class PeopleHeightComparator implements Comparator<People> {
    @Override
    public int comparable(People o1, People o2) {
        if (o1.getHeight() > o2.getHeight()) return 1;
        else if (o1.getHeight() < o2.getHeight()) return -1;
        return 0;
    }
}
