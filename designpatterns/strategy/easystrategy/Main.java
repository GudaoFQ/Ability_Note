package desgindemo.stretagy.easystrategy;

import java.util.ResourceBundle;

/**
 * Author : GuDao
 * 2020-10-14
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        People[] peoples = new People[]{new People("001", 10, 180),new People("002",12,190),new People("003",5,190), new People("004", 70, 165)};
        Sorter<People>sorter = new Sorter<>();
        /**
         * 使用new方法新建对象
         */
        sorter.sort(peoples, new PeopleAgeComparator());
        //sorter.sort(peoples, new PeopleHeightComparator());

        /**
         * 使用配置文件【更容易扩展】
         */
        //ResourceBundle config = ResourceBundle.getBundle("strategyconfig");
        //String beanName = config.getString("peopleHeightComparator");
        //Comparator comparator = (Comparator) Class.forName(beanName).newInstance();
        //sorter.sort(peoples, comparator);
        //for (People p:peoples) {
        //    System.out.println(p);
        //}
    }
}
