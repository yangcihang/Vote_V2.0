package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/17 0017.
 * Email yangcihang@hrsoft.net
 */

public class VoteOptionInfo extends BaseModel {
    private long id;
    private long voteId;
    private String title;
    private long num;
    private long value;

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }
}
