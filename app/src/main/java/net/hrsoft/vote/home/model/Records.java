package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/18 0018.
 * Email yangcihang@hrsoft.net
 */

public class Records extends BaseModel {
    private long optionId;
    private long voteId;
    private long type;
    private long value;

    public Records(long optionId,long voteId,long type,long value) {
        this.optionId = optionId;
        this.voteId = voteId;
        this.type = type;
        this.value = value;
    }
    public void setType(long type) {
        this.type = type;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getOptionId() {
        return optionId;
    }

    public long getType() {
        return type;
    }

    public long getValue() {
        return value;
    }

    public long getVoteId() {
        return voteId;
    }
}
