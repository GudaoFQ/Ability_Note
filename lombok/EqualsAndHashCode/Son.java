package com.wedding.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: Gudao
 * @Date: 2021/4/10
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Son extends Father{
    private Integer id;
    private String name;

    // 可以默认不写，通过set方法设置father中的属性
    public Son(String email, String adress, Integer id, String name) {
        super(email, adress);
        this.id = id;
        this.name = name;
    }
}
