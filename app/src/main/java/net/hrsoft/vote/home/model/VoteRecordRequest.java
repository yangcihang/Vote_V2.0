package net.hrsoft.vote.home.model;

import net.hrsoft.vote.common.BaseModel;

import java.util.List;

/**
 * @author YangCihang.
 * @since 2017/5/18 0018.
 * Email yangcihang@hrsoft.net
 */

public class VoteRecordRequest extends BaseModel {
    private List<Records> records;

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<Records> getRecords() {
        return records;
    }
}
