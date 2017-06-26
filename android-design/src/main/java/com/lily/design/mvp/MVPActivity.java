package com.lily.design.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lily.design.R;
import com.lily.design.mvp.bean.UserBean;
import com.lily.design.mvp.presenter.UserPresenter;
import com.lily.design.mvp.proxy.IUserLoginView;

/***********
 * @Author rape flower
 * @Date 2017-04-17 13:11
 * @Describe (MVP)
 */
public class MVPActivity extends Activity implements View.OnClickListener, IUserLoginView {

    EditText etUserName, etPwd;
    ProgressBar pbLoading;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        initView();

        userPresenter = new UserPresenter(this);
    }

    /**
     * 初始化
     */
    private void initView() {
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etPwd = (EditText) findViewById(R.id.et_user_pwd);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                userPresenter.login();
                break;
            case R.id.btn_clear:
                userPresenter.clear();
                break;
            default:
                break;
        }
    }

    @Override
    public String getUserName() {
        return etUserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPwd.getText().toString();
    }

    @Override
    public void clearUserName() {
        etUserName.setText("");
    }

    @Override
    public void clearPassword() {
        etPwd.setText("");
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void toMainActivity(UserBean user) {
        Toast.makeText(this, user.getUsername() +
                " login success ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(this,
                " login failed ", Toast.LENGTH_SHORT).show();
    }
}
