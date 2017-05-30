/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.common;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import butterknife.ButterKnife;

/**
 * 所有Activity的基类
 *
 * @author YuanZeng
 * @since 17/3/28 上午10:27
 * email yuanzeng@hrsoft.net
 */

public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止APP横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 页面初始化操作.
     */
    protected void init() {
        ButterKnife.bind(this);
        initVariable();
        initView();
        loadData();
    }

    /**
     * 初始化相关变量，如 getIntent() 等.
     */
    protected void initVariable() {
        fragmentManager = getSupportFragmentManager();
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

    /**
     * 添加Fragment
     *
     * @param fragment fragment
     * @param bundle   传递的数据
     */
    protected void addFragment(@IdRes int containerViewId, BaseFragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.add(containerViewId, fragment).commit();
    }

    /**
     * 替换Fragment
     *
     * @param fragment fragment
     * @param bundle   传递的数据
     */
    protected void replaceFragment(@IdRes int containerViewId, BaseFragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(containerViewId, fragment).addToBackStack(null).commit();
    }

    /**
     * 获取LayoutId
     *
     * @return LayoutID
     */
    @LayoutRes
    protected abstract int getLayoutId();


}
