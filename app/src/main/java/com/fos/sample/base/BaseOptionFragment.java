package com.fos.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fos.fosmvp.common.base.BaseFragment;
import com.fos.fosmvp.common.base.BaseModel;
import com.fos.fosmvp.common.base.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xxjsb on 2019/3/5.
 */

public abstract class BaseOptionFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        initView(savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mUnbinder = ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mUnbinder.unbind();//释放所有绑定的view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化view
    protected abstract void initView(Bundle savedInstanceState);
}
