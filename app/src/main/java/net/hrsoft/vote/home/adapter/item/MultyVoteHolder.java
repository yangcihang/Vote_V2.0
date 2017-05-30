package net.hrsoft.vote.home.adapter.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import net.hrsoft.vote.R;

/**
 * @author YangCihang.
 * @since 2017/5/18 0018.
 * Email yangcihang@hrsoft.net
 */

public class MultyVoteHolder extends RecyclerView.ViewHolder {
    public CheckBox optionChk;
    public MultyVoteHolder(View itemView) {
        super(itemView);
        optionChk = (CheckBox) itemView.findViewById(R.id.chk_mul_option);
    }
}