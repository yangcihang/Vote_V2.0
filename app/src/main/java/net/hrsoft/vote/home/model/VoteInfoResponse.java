package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

import java.util.ArrayList;

/**
 * @author YangCihang.
 * @since 2017/5/17 0017.
 * Email yangcihang@hrsoft.net
 */

public class VoteInfoResponse extends BaseModel{
    private VoteInfo voteShow;
    private ArrayList<VoteOptionInfo> options;

    public void setOptions(ArrayList<VoteOptionInfo> options) {
        this.options = options;
    }

    public ArrayList<VoteOptionInfo> getOptions() {
        return options;
    }

    public void setVoteShow(VoteInfo voteShow) {
        this.voteShow = voteShow;
    }

    public VoteInfo getVoteShow() {
        return voteShow;
    }

}
