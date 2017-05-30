package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

/**
 * @author YangCihang.
 * @since 2017/5/15 0015.
 * Email yangcihang@hrsoft.net
 */

public class VoteInfo extends BaseModel{
    private int id;
    private String title;
    private long startTime;
    private long endTime;
    private int type;
    private int max;
    private String  mobile;
    private long participatorNum;
    private int flag;
    private String qaPath;
    private long creatorId;

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getQaPath() {
        return qaPath;
    }

    public void setQaPath(String qaPath) {
        this.qaPath = qaPath;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String  getMobile() {
        return mobile;
    }

    public void setMobile(String  mobile) {
        this.mobile = mobile;
    }

    public long getParticipatorNum() {
        return participatorNum;
    }

    public void setParticipatorNum(long participatorNum) {
        this.participatorNum = participatorNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
