package com.fos.sample.ui.login.model;


import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.http.Api;
import com.fos.sample.api.ApiService;
import com.fos.sample.entity.login.UserEntity;
import com.fos.sample.ui.login.contract.LoginContract;


import java.util.Map;

import io.reactivex.Observable;


public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<BaseResponse<UserEntity>> getLoginData(Map map) {
        return Api.createApi(ApiService.class).login(map);
    }



}
