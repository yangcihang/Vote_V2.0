package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/8 0008.
 * Email yangcihang@hrsoft.net
 */

public class User extends BaseModel {
    private long userId;
    private String userName;
    private String mobile;
    private String sex;

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
