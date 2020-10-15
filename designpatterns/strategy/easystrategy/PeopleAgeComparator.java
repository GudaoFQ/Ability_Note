package desgindemo.stretagy.easystrategy;

/**
 * 用户年龄比较策略实体
 * Author : GuDao
 * 2020-10-14
 */
public class PeopleAgeComparator implements Comparator<People> {
    @Override
    public int comparable(People o1, People o2) {
        if(o1.getAge()>o2.getAge())return 1;
        else if(o1.getAge()<o2.getAge())return -1;
        return 0;
    }
}
