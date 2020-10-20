package designdemo.observer.simpleobserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : GuDao
 * 2020-10-19
 */

public abstract class Subject {
    //观察者的存储器
    List<Company> observers = new ArrayList<>();

    /**
     * 观察者添加
     *
     * @param observer 观察者
     * @return {@link Subject}
     */
    public Subject add(Company observer){
        observers.add(observer);
        return this;
    }

    /**
     * 观察者移除
     *
     * @param observer 观察者
     * @return {@link Subject}
     */
    public Subject remove(Company observer){
        observers.remove(observer);
        return this;
    }

    /**
     * 业务实现
     */
    public abstract void implementation(float parities);
}
