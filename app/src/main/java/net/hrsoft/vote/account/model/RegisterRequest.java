package net.hrsoft.vote.account.model;

/**
 * @author YangCihang.
 * @since 2017/5/13 0013.
 * Email yangcihang@hrsoft.net
 */

public class RegisterRequest {
    private String mobile;
    private String code;
    private String password;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }
}
