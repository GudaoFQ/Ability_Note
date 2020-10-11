package desgindemo.factory.easyfactory;

/**
 * 通过反射创建出实体类
 * Author : GuDao
 * 2020-10-09
 */

public class EasyFactory {
    public static Object getBean(Class<? extends MoveAble> clazz){
        Object obj = null;
        try {
            obj = Class.forName(clazz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
