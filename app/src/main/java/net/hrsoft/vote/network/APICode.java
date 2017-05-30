/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.network;

/**
 * API 错误码
 *
 * @author yuanzeng
 * @since 17/1/22 下午6:40
 */
public class APICode {
    public static final int BadRequest = 10000;
    public static final int SERVER_RESPONSE_CODE = 400;
    public static final int SUCCESS = 0;
    public static final int PasswordError = 10001;
    public static final int UserNotExit = 10002;
    public static final int MobileHasBeenRegistered = 10003;
    public static final int NeedLogin = 20001;
    public static final int TokenExpire = 20002;
    public static final int TheVoteHasAlreadyExisted = 30001;
    public static final int TheVoteIsNotExist = 30002;
    public static final int VotePasswordError = 30003;
    public static final int BeyondLimit = 30004;
    public static final int TimeError = 30005;
    public static final int YourVoteIsNotVisibilityLimit = 30006;
    public static final int YouHaveBeenVoted = 30007;
    public static final int VerifyIsWrong = 90001;

}
