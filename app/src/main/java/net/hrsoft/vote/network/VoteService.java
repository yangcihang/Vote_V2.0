/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.network;

import net.hrsoft.vote.account.model.ForgetPasswordRequest;
import net.hrsoft.vote.account.model.LoginRequest;
import net.hrsoft.vote.account.model.LoginResponse;
import net.hrsoft.vote.account.model.RegisterRequest;
import net.hrsoft.vote.account.model.VerifyRequest;
import net.hrsoft.vote.account.model.VerifyResponse;
import net.hrsoft.vote.home.model.AllVotesResponse;
import net.hrsoft.vote.home.model.ChangSexRequest;
import net.hrsoft.vote.home.model.ModifyMobileRequest;
import net.hrsoft.vote.home.model.UserHasVotedResponse;
import net.hrsoft.vote.home.model.VoteInfoResponse;
import net.hrsoft.vote.home.model.VoteRecordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API接口
 *
 * @author yuanzeng
 * @since 17/1/22 下午6:43
 */

public interface VoteService {
    /**
     * 登录
     * @param loginRequest 登录请求体
     * @return response
     */
    @POST("user/login")
    Call<APIResponse<LoginResponse>>loginPost(@Body LoginRequest loginRequest);

    /**
     * 注册时候发送验证码的请求
     * @param verifyRequest verifyRequest
     * @return response
     */
    @POST("user/verify")
    Call<APIResponse<VerifyResponse>>registerVerifyPost(@Body VerifyRequest verifyRequest);

    /**
     * 发送注册的请求
     * @param registerRequest registerRequest
     * @return response
     */
    @POST("user/register")
    Call<APIResponse>registerPost(@Body RegisterRequest registerRequest);

    /**
     * 退出登录的请求
     * @return response
     */
    @POST("user/logout")
    Call<APIResponse>logoutPost();

    /**
     * 首页获取所有投票信息的请求
     * @return response
     */
    @GET("vote/all")
    Call<APIResponse<AllVotesResponse>>allVotesPost(@Query("page") int page, @Query("rows") int rows);

    /**
     * 获取单个投票信息的请求
     * @param voteId voteId
     * @return response
     */
    @GET("vote/info")
    Call<APIResponse<VoteInfoResponse>>voteInfoPost(@Query("voteId") long voteId);

    /**
     * 发送提交请求
     * @param voteId voteId
     * @return response
     */
    @POST("vote/{voteId}/submit")
    Call<APIResponse>voteRecordsPost(@Path("voteId") long voteId,@Body VoteRecordRequest voteRecordRequest);

    /**
     * 用户投票信息的请求
     * @param page page
     * @param rows rows
     * @return response
     */
    @GET("vote/haveVoted")
    Call<APIResponse<UserHasVotedResponse>>userVotesPost(@Query("page")int page,@Query("rows")int rows);

    /**
     * 修改绑定手机的请求
     * @param modifyMobileRequest modifyMobileRequest
     * @return response
     */
    @PUT("user/changeMobile")
    Call<APIResponse>modifyMobilePost(@Body ModifyMobileRequest modifyMobileRequest);

    /**
     * 修改密码的请求
     * @param forgetPasswordRequest  forgetPasswordRequest
     * @return response
     */
    @PUT("user/forget")
    Call<APIResponse>modifyPasswordPost(@Body ForgetPasswordRequest forgetPasswordRequest);

    /**
     * 改变性别的请求
     * @return response
     */
    @PUT("user/changeSex")
    Call<APIResponse>changeSexPost(@Body ChangSexRequest changSexRequest);
}
