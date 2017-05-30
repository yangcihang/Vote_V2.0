package net.hrsoft.vote.home.adapter;

import java.util.List;

/**
 * @author YangCihang.
 * @since 2017/5/24 0024.
 * Email yangcihang@hrsoft.net
 */

public interface OnSelectionChangedListener {
    void onSelectionChanged(List<Boolean> flagList,int maxSelected);
}
