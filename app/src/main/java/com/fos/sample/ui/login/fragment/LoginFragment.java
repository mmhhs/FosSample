package com.fos.sample.ui.login.fragment;

import android.os.Bundle;

import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.utils.ToastUtils;
import com.fos.sample.R;
import com.fos.sample.base.BaseOptionFragment;
import com.fos.sample.entity.login.UserEntity;
import com.fos.sample.ui.login.contract.LoginContract;
import com.fos.sample.ui.login.model.LoginModel;
import com.fos.sample.ui.login.presenter.LoginPresenter;

/**
 * Fragment使用示例
 *
 */
public class LoginFragment extends BaseOptionFragment<LoginPresenter, LoginModel> implements LoginContract.View{

    public String tel = "";
    public String password = "";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        setPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }



    @Override
    public void returnLoginSucceed(UserEntity userEntity) {
        ToastUtils.showShort("");
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
