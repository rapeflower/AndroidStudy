package com.lily.design.mvp.proxy;

/***********
 * @Author rape flower
 * @Date 2017-04-17 11:03
 * @Describe 用户实体Model（此处是接口形式定义）
 */
public interface IUserModel {
    public void login(String username, String password, OnLoginListener loginListener);
    public void register();
}