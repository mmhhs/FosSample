package com.fos.sample.ui.login.contract;


import com.fos.fosmvp.common.base.BaseModel;
import com.fos.fosmvp.common.base.BasePresenter;
import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.base.BaseView;
import com.fos.sample.entity.login.UserEntity;


import java.util.Map;

import io.reactivex.Observable;


public interface LoginContract {
    interface Model extends BaseModel {
        //请求用户登录信息
        Observable<BaseResponse<UserEntity>> getLoginData(Map map);
    }

    interface View extends BaseView {
        //登录成功返回
        void returnLoginSucceed(UserEntity userEntity);
        //登录失败返回
        void returnLoginFail(BaseResponse baseResponse, boolean isVisitError);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        //发起用户登录请求
        public abstract void getLoginRequest(String tel,String password);
    }
}