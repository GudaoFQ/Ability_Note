package designdemo.observer.enentobserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : GuDao
 * 2020-10-20
 */

public class Child {
    //监听者集合
    List<Listerner> listers = new ArrayList<>();

    public Child add(Listerner lister){
        listers.add(lister);
        return this;
    }

    public void childProcess(ActionEvent actionEvent){
        for(Listerner lister : listers){
            lister.process(actionEvent);
        }
    }
}
