/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 没有Toolbar的Activity基类
 *
 * @author YuanZeng
 * @since 17/3/28 上午10:46
 * email yuanzeng@hrsoft.net
 */

public abstract class NoToolbarActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        init();
    }

    private View getContentView() {
        return getLayoutInflater().inflate(getLayoutId(), null);
    }

}
