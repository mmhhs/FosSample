package com.fos.sample.ui.login.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fos.fosmvp.common.base.BaseActivity
import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.utils.ToastUtils
import com.fos.sample.R
import com.fos.sample.ali.ALiMainActivity
import com.fos.sample.baidu.ActivityMain
import com.fos.sample.entity.login.UserEntity
import com.fos.sample.kdxf.voicedemo.MainActivity
import com.fos.sample.ui.login.contract.LoginContract
import com.fos.sample.ui.login.model.LoginModel
import com.fos.sample.ui.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Activity使用示例
 */
class LoginActivity : BaseActivity<LoginPresenter, LoginModel>(), LoginContract.View ,View.OnClickListener{

    var tel = ""
    var password = ""

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initPresenter() {
        mPresenter!!.setViewModel(this, mModel!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.button1 -> startActivity(Intent(LoginActivity@this, MainActivity::class.java))
            R.id.button2 -> startActivity(Intent(LoginActivity@this, ActivityMain::class.java))
            R.id.button3 -> startActivity(Intent(LoginActivity@this, ALiMainActivity::class.java))
//            R.id.button4 -> ToastUtils.showShort("不提供Android SDK")
        }
    }


    fun login() {

    }

    override fun returnLoginSucceed(userEntity: UserEntity) {
        ToastUtils.showShort("登录成功")
        finishActivity()
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

    companion object {

        /**
         * 入口
         *
         * @param activity
         */
        fun startAction(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }


}
