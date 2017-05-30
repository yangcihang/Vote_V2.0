package net.hrsoft.vote.home.adapter.item;

/**
 * @author YangCihang.
 * @since 2017/5/18 0018.
 * Email yangcihang@hrsoft.net
 */

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.hrsoft.vote.R;

public class RankHolder extends RecyclerView.ViewHolder {
    public TextView titleTxt;
    public TextView scoreTxt;
    public RankHolder(View itemView) {
        super(itemView);
        titleTxt = (TextView) itemView.findViewById(R.id.txt_vote_rank_title);
        scoreTxt = (TextView)itemView.findViewById(R.id.txt_vote_show_score);
    }
}