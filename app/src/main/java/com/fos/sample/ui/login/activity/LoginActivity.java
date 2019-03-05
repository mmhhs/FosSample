package com.fos.sample.ui.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.utils.ToastUtils;
import com.fos.sample.R;
import com.fos.sample.base.BaseOptionActivity;
import com.fos.sample.entity.login.UserEntity;
import com.fos.sample.ui.login.contract.LoginContract;
import com.fos.sample.ui.login.model.LoginModel;
import com.fos.sample.ui.login.presenter.LoginPresenter;

/**
 * Activity使用示例
 */
public class LoginActivity extends BaseOptionActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    public String tel = "";
    public String password = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        setPresenter();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }




    @Override
    public void returnLoginSucceed(UserEntity userEntity) {
        ToastUtils.showShort("登录成功");
        finish();
    }


    @Override
    public void returnLoginFail(BaseResponse baseResponse, boolean isVisitError) {
        try {
            if (!isVisitError){
                ToastUtils.showShort(baseResponse.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
