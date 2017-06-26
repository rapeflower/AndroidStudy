package com.lily.design.mvp.proxy;

import com.lily.design.mvp.bean.UserBean;

/***********
 * @Author rape flower
 * @Date 2017-04-17 11:03
 * @Describe 用户试图类（此处是接口形式定义）
 */
public interface IUserLoginView {

    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(UserBean user);

    void showFailedError();
}
