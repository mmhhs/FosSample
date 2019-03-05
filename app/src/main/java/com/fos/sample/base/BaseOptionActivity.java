package com.fos.sample.base;

import android.os.Bundle;

import com.fos.fosmvp.common.base.BaseActivity;
import com.fos.fosmvp.common.base.BaseModel;
import com.fos.fosmvp.common.base.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xxjsb on 2019/3/5.
 */

public abstract class BaseOptionActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity {
    public Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        this.initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mUnbinder.unbind();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //初始化view
    public abstract void initView(Bundle savedInstanceState);
}
