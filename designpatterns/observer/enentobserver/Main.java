package designdemo.observer.enentobserver;

/**
 * child为监听事件源，fatherlisterner与matherListerner为监听者；通过event来执行监听者早中晚需要做的事情
 * Author : GuDao
 * 2020-10-20
 */

public class Main {
    public static void main(String[] args) {
        Child child = new Child();
        ActionEvent event = new ActionEvent("morning", child);

        Listerner fatherLister = new FatherListener();
        Listerner matherLister = new MatherListener();
        child.add(fatherLister).add(matherLister);

        child.childProcess(event);
    }
}
