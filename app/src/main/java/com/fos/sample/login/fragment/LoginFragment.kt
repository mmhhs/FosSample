package com.fos.sample.ui.login.fragment


import android.os.Bundle
import android.view.View
import com.fos.fosmvp.common.base.BaseFragment
import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.utils.LogUtils
import com.fos.fosmvp.common.utils.ToastUtils
import com.fos.sample.R
import com.fos.sample.entity.login.UserEntity
import com.fos.sample.ui.login.contract.LoginContract
import com.fos.sample.ui.login.model.LoginModel
import com.fos.sample.ui.login.presenter.LoginPresenter

/**
 * Fragment使用示例
 *
 */
class LoginFragment : BaseFragment<LoginPresenter, LoginModel>(), LoginContract.View,View.OnClickListener {
    var tel = ""
    var password = ""

    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun initPresenter() {
        mPresenter!!.setViewModel(this, mModel!!)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onClick(view: View) {

    }

    fun login() {

        LogUtils.e("tel= "+tel)
        ToastUtils.showShort("登录")
        mPresenter!!.getLoginRequest(tel, password)
    }

    override fun returnLoginSucceed(userEntity: UserEntity) {
        ToastUtils.showShort("登录成功")
    }


    override fun returnLoginFail(baseResponse: BaseResponse<*>?, isVisitError: Boolean) {
        try {
            if (!isVisitError&&baseResponse!=null) {
                ToastUtils.showShort(baseResponse.msg!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
