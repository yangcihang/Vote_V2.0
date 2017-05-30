package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/23 0023.
 * Email yangcihang@hrsoft.net
 */

public class ModifyMobileRequest extends BaseModel {
    private String oldMobile;
    private String newMobile;
    private String password;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
