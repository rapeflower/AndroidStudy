package com.lily.design.mvp.proxy;

import com.lily.design.mvp.bean.UserBean;

/***********
 * @Author rape flower
 * @Date 2017-04-17 14:10
 * @Describe 用户登陆监听
 */
public interface OnLoginListener {
    void loginSuccess(UserBean user);

    void loginFailed();
}
