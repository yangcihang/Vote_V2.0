/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.network;


import net.hrsoft.vote.common.BaseModel;

/**
 * API 网络请求返回数据格式
 *
 * @author yuanzeng
 * @since 17/1/22 下午6:40
 */
public class APIResponse<T> extends BaseModel {
    private int code = -2;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
