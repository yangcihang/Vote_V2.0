package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/24 0024.
 * Email yangcihang@hrsoft.net
 */

public class ChangSexRequest extends BaseModel {
    String newSex;

    public void setNewSex(String newSex) {
        this.newSex = newSex;
    }

    public String getNewSex() {
        return newSex;
    }
}
