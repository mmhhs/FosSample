package com.fos.sample.webview.model;

import com.foton.almighty.api.Api;
import com.foton.almighty.api.ApiService;
import com.foton.almighty.entity.login.LoginResult;
import com.foton.almighty.entity.repair.IntegralEntity;
import com.foton.almighty.ui.webview.contract.WebViewContract;
import com.foton.common.basebean.BaseResponse;

import java.util.Map;

import rx.Observable;


public class WebViewModel implements WebViewContract.Model {
    @Override
    public Observable<BaseResponse<IntegralEntity>> updateEvaluate(Map map) {
        return Api.createApi(ApiService.class).updateEvaluate(map);
    }

    @Override
    public Observable<BaseResponse<IntegralEntity>> saveShareMsg(Map map) {
        return Api.createApi(ApiService.class).saveShareMsg(map);
    }

    @Override
    public Observable<BaseResponse<IntegralEntity>> updateIntegral(Map map) {
        return Api.createApi(ApiService.class).updateIntegral(map);
    }

    @Override
    public Observable<BaseResponse> submitQuestionnaire(Map map) {
        return Api.createApi(ApiService.class).finishQuestionnaire(map);
    }

    @Override
    public Observable<BaseResponse> getTokenStatus(Map map) {
        return Api.createApi(ApiService.class).getTokenStatus(map);
    }

    @Override
    public Observable<BaseResponse<LoginResult>> getNewToken(Map map) {
        return Api.createApi(ApiService.class).getNewToken(map);
    }
}
