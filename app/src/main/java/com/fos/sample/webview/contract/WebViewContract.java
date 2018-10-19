package com.fos.sample.webview.contract;

import com.foton.almighty.entity.login.LoginResult;
import com.foton.almighty.entity.repair.IntegralEntity;
import com.foton.almighty.entity.web.WebEntity;
import com.foton.common.base.BaseModel;
import com.foton.common.base.BasePresenter;
import com.foton.common.base.BaseView;
import com.foton.common.basebean.BaseResponse;

import java.util.Map;

import rx.Observable;

public interface WebViewContract {
    interface Model extends BaseModel {
        //完成评价
        Observable<BaseResponse<IntegralEntity>> updateEvaluate(Map map);
        //分享统计
        Observable<BaseResponse<IntegralEntity>> saveShareMsg(Map map);
        //添加积分
        Observable<BaseResponse<IntegralEntity>> updateIntegral(Map map);
        //问卷调查提交
        Observable<BaseResponse> submitQuestionnaire(Map map);
        //校验token
        Observable<BaseResponse> getTokenStatus(Map map);
        //获取新token
        Observable<BaseResponse<LoginResult>> getNewToken(Map map);
    }

    interface View extends BaseView {
        //完成评价提交成功返回
        void returnUpdateEvaluateSucceed(IntegralEntity integralEntity);
        //完成评价提交失败返回
        void returnUpdateEvaluateFail(String msg, boolean isVisitError);
        //分享统计提交成功返回
        void returnSaveShareMsgSucceed(IntegralEntity integralEntity);
        //分享统计提交失败返回
        void returnSaveShareMsgFail(String msg, boolean isVisitError);
        //添加积分提交成功返回
        void returnUpdateIntegralSucceed(IntegralEntity integralEntity);
        //添加积分提交失败返回
        void returnUpdateIntegralFail(String msg, boolean isVisitError);
        //问卷提交成功返回
        void returnSubmitQuestionnaireSucceed(BaseResponse res);
        //问卷提交失败返回
        void returnSubmitQuestionnaireFail(String msg, boolean isVisitError);
        //校验token成功返回
        void returnTokenStatusSucceed();
        //校验token失败返回
        void returnTokenStatusFail(String msg, boolean isVisitError);
        //获取新token成功返回
        void returnNewTokenSucceed(LoginResult entity);
        //获取新token失败返回
        void returnNewTokenFail(String msg, boolean isVisitError);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        //发起完成评价请求
        public abstract void updateEvaluateRequest(boolean showDialog, String memberID, String orderNo, String orderId);
        //发起分享统计请求
        public abstract void saveShareMsgRequest(boolean showDialog, String memberID, WebEntity webEntity);
        //发起添加积分请求
        public abstract void updateIntegralRequest(boolean showDialog, String memberID, String phone, String id);
        //发起问卷提交请求
        public abstract void submitQuestionnaireRequest( String id);
        //发起校验token请求
        public abstract void getTokenStatusRequest(boolean showDialog,String ticketName,String ticketValue,String ip);
        //发起获取新token请求
        public abstract void getNewTokenRequest(boolean showDialog,String userName,String password,String ip);
    }
}
