package com.wedding.project.model;

/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
public class MainTest {
    public static void main(String[] args) {
        // 两个son对象的父类信息完全不一样；只有子类信息是相同的情况下测试
        Son son1 = new Son("父亲邮箱1","父亲地址：北京",001,"son1");
        Son son2 = new Son("父亲邮箱2","父亲地址：江苏",001,"son1");
        System.out.println("未开启callsuper=true："+(son1.equals(son2)));
    }
}
