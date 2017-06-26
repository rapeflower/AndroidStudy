package com.lily.design.mvp.presenter;

import android.os.Handler;

import com.lily.design.mvp.bean.UserBean;
import com.lily.design.mvp.model.UserModel;
import com.lily.design.mvp.proxy.IUserModel;
import com.lily.design.mvp.proxy.IUserLoginView;
import com.lily.design.mvp.proxy.OnLoginListener;

/***********
 * @Author rape flower
 * @Date 2017-04-17 11:03
 * @Describe 用户主导器
 */
public class UserPresenter {

    private IUserLoginView userLoginView;
    private IUserModel userModel;
    private Handler mHandler = new Handler();

    public UserPresenter(IUserLoginView view) {
        this.userLoginView = view;
        this.userModel = new UserModel();
    }

    public void login() {
        userLoginView.showLoading();
        userModel.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(final UserBean user) {
                //需要在UI线程中执行
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userLoginView.toMainActivity(user);
                        userLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed() {
                //需要在UI线程执行
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userLoginView.showFailedError();
                        userLoginView.hideLoading();
                    }
                });
            }
        });
    }

    public void clear()
    {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }
}
