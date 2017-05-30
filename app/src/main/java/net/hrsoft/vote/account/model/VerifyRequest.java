package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/13 0013.
 * Email yangcihang@hrsoft.net
 */

public class VerifyRequest extends BaseModel {
    private String mobile;
    private int type;

    public String getMobile() {
        return mobile;
    }

    public int getType() {
        return type;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setType(int type) {
        this.type = type;
    }
}
