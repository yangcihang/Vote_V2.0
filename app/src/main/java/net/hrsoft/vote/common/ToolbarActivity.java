/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hrsoft.vote.R;

/**
 * 带有Toolbar的activity基类
 *
 * @author YuanZeng
 * @since 17/3/28 上午10:41
 * email yuanzeng@hrsoft.net
 */

public abstract class ToolbarActivity extends BaseActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        init();
    }

    /**
     * 获取页面View
     *
     * @return View
     */
    private View getContentView() {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout viewRoot = (RelativeLayout) inflater.inflate(R.layout.activity_toolbar_base, null);
        FrameLayout viewContainer = (FrameLayout) viewRoot.findViewById(R.id.view_container);
        viewContainer.addView(inflater.inflate(getLayoutId(), null));
        initToolbar(viewRoot);
        return viewRoot;
    }

    /**
     * 初始化Toolbar.
     */
    private void initToolbar(View root) {
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();//得到目前的ActionBar
//        if (actionBar != null) {
//            actionBar.hide();
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        mToolbar.setNavigationIcon(R.drawable.ic_tool_arrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackBtnOnclick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 获取当前页面的Toolbar.
     *
     * @return Toolbar
     */
    @Nullable
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 设置页面标题文字.
     *
     * @param charSequence 标题
     */
    protected void setActivityTitle(CharSequence charSequence) {
        if (getToolbar() != null) {
            TextView title = (TextView) getToolbar().findViewById(R.id.txt_activity_title);
            title.setText(charSequence);
        }

    }

    /**
     * Toolbar返回按钮的监听事件
     */
    protected void onBackBtnOnclick() {
        this.finish();
    }
}
