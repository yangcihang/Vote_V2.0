package net.hrsoft.vote.home.adapter.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import net.hrsoft.vote.R;

/**
 * @author YangCihang.
 * @since 2017/5/18 0018.
 * Email yangcihang@hrsoft.net
 */

public class SingleVoteHolder extends RecyclerView.ViewHolder {
    public RadioButton optionRbtn;
    public SingleVoteHolder(View itemView) {
        super(itemView);
        optionRbtn = (RadioButton) itemView.findViewById(R.id.rbtn_vote_single_option);
    }
}