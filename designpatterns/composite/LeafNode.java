package designdemo.composite;

/**
 * 叶子节点
 * Author : GuDao
 * 2020-10-21
 */

public class LeafNode extends Node {
    String name;

    @Override
    void process() {
        System.out.println(name);
    }

    public LeafNode(String name) {
        this.name = name;
    }
}