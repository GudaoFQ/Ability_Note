package designdemo.composite;

/**
 * Author : GuDao
 * 2020-10-21
 */

public class Main {
    public static void main(String[] args) {
        Node leaf001 = new LeafNode("叶子001");
        Node leaf002 = new LeafNode("叶子002");
        Node leaf003 = new LeafNode("叶子003");
        Node leaf004 = new LeafNode("叶子004");

        Node branch001 = new BranchNode("root");
        Node branch002 = new BranchNode("枝干001");
        Node branch003 = new BranchNode("枝干002");

        ((BranchNode) branch001).add(branch002);
        ((BranchNode) branch001).add(branch003);

        ((BranchNode) branch002).add(leaf001);
        ((BranchNode) branch002).add(leaf002);
        ((BranchNode) branch002).add(leaf003);
        ((BranchNode) branch003).add(leaf004);

        tree(branch001,0);
    }

    public static void tree(Node node,int num){
        for(int i=0;i<num;i++) System.out.print("-");
        node.process();
        if(node instanceof BranchNode){
            for(Node n : ((BranchNode)node).nodes){
                tree(n,num+1);
            }
        }
    }
}
