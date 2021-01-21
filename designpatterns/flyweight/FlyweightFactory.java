package designdemo.flyweight;

import java.util.HashMap;

/**
 * 共享工厂
 * Author : GuDao
 * 2020-10-26
 */

public class FlyweightFactory {
    private HashMap<String, Flyweight> flyweihts = new HashMap<>();
    private Flyweight flyweight;

    public Flyweight getFlyweight(String key){
        if(flyweihts.containsKey(key)){
            flyweight = flyweihts.get(key);
            System.out.println("factory调用了对象："+key);
        }else {
            flyweight = new SpecificFlyweight(key);
            flyweihts.put(key, flyweight);
        }
        return flyweight;
    }
}
