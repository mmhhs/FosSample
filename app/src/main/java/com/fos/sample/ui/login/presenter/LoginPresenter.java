package com.fos.sample.ui.login.presenter;


import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.baserx.RxObserver;
import com.fos.fosmvp.common.http.Api;
import com.fos.sample.entity.login.UserEntity;
import com.fos.sample.ui.login.contract.LoginContract;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getLoginRequest(String tel, String password) {
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("name", tel);
        argMap.put("password", password);
        Observable<BaseResponse<UserEntity>> observable = Api.initObservable(mModel.getLoginData(argMap));
        RxObserver rxObserver = new RxObserver<BaseResponse<UserEntity>>(BaseApplication.getAppContext()) {
            @Override
            protected void _onNext(BaseResponse<UserEntity> res) {
                if (res.isSucceed()) {
                    mView.returnLoginSucceed(res.getData());
                } else {
                    mView.returnLoginFail(res, false);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnLoginFail(null, true);
            }
        };
        observable.subscribe(rxObserver);
        mRxManage.add(rxObserver);
    }


}