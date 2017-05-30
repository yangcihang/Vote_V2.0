package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/24 0024.
 * Email yangcihang@hrsoft.net
 */

public class ForgetPasswordRequest extends BaseModel {
    private String code;
    private String mobile;
    private String newPassword;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
