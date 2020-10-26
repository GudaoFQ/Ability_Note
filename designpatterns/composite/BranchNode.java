package designdemo.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意：这里的name只是个人测试使用的，方便查看节点是哪一个
 * 树干节点
 * Author : GuDao
 * 2020-10-21
 */

public class BranchNode extends Node {
    List<Node> nodes = new ArrayList<>();

    String name;

    //业务处理
    @Override
    void process() {
        System.out.println(name);
    }

    /**
     * 调用有参构造为节点赋值
     *
     * @param name 的名字
     */
    public BranchNode(String name) {
        this.name = name;
    }

    /**
     * 在枝干上添加叶子
     *
     * @param node 节点
     * @return {@link Node}
     */
    public Node add(Node node){
        nodes.add(node);
        return this;
    }
}
