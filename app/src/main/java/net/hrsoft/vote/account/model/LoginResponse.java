package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/12 0012.
 * Email yangcihang@hrsoft.net
 */

public class LoginResponse extends BaseModel {
    private User user;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
