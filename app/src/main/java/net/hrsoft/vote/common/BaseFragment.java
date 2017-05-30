/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.common;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Fragment 基类
 *
 * @author YuanZeng
 * @since 17/3/28 上午10:37
 * email yuanzeng@hrsoft.net
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initVariable();
        initView();
        loadData();
        return view;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化相关变量，如 getIntent() 等.
     */
    protected void initVariable() {

    }

    /**
     * 初始化 View 的状态，挂载各种监听事件.
     */
    protected void initView() {

    }

    /**
     * 初始加载数据.
     */
    protected void loadData() {

    }

}
