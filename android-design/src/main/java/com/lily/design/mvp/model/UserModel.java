package com.lily.design.mvp.model;

import com.lily.design.mvp.bean.UserBean;
import com.lily.design.mvp.proxy.IUserModel;
import com.lily.design.mvp.proxy.OnLoginListener;

/***********
 * @Author rape flower
 * @Date 2017-04-17 11:47
 * @Describe
 */
public class UserModel implements IUserModel{


    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener) {
        //模拟子线程耗时操作
        new Thread() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //模拟登录成功
                if ("test".equals(username) && "123".equals(password)) {
                    UserBean user = new UserBean();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                } else {
                    loginListener.loginFailed();
                }
            }
        }.start();
    }

    @Override
    public void regig() {

    }
}
