package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/12 0012.
 * Email yangcihang@hrsoft.net
 */

public class LoginRequest extends BaseModel {
     private String mobile;
     private String password;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }
}
