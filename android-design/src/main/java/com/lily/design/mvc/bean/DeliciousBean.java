package com.lily.design.mvc.bean;

/***********
 * @Author rape flower
 * @Date 2017-04-05 13:20
 * @Describe 美食的实体类
 */
public class DeliciousBean {

    public String name;

    public DeliciousBean() {
    }

    public DeliciousBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeliciousBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
