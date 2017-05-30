package net.hrsoft.vote.account.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/13 0013.
 * Email yangcihang@hrsoft.net
 */

public class VerifyResponse extends BaseModel {
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
