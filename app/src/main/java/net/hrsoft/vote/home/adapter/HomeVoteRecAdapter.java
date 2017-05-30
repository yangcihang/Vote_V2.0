package net.hrsoft.vote.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.constant.VoteFlag;
import net.hrsoft.vote.home.activity.VoteActivity;
import net.hrsoft.vote.home.model.VoteInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.START;
import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

/**
 * @author YangCihang.
 * @since 2017/5/9 0009.
 * Email yangcihang@hrsoft.net
 */

public class HomeVoteRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VoteInfo> mlist;
    private Context mcontext;
    private long oneDayTime = 86400000; //一天的时间
    private long oneHourTime = 3600000; //一小时的时间
    private long oneMinuteTime = 60000; //一分钟的时间
    public static final int ITEM_VIEW_TYPE = 0;
    public static final int LABEL_VIEW_TYPE = 1;


    public HomeVoteRecAdapter(List<VoteInfo> list, Context context) {
        mlist = list;
        mcontext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist.get(position) == null) {
            return LABEL_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_VIEW_TYPE) {
            view = LayoutInflater.from(mcontext).inflate(R.layout.item_rec_home_vote, parent, false);
            return new StartingVoteHolder(view);
        } else {
            view = LayoutInflater.from(mcontext).inflate(R.layout.label_rec_hint_end_txt, parent, false);
            return new LabelHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof LabelHolder)) {
            final VoteInfo vote = mlist.get(position);
            ((StartingVoteHolder) holder).titleTxt.setText(vote.getTitle());
            ((StartingVoteHolder) holder).numTxt.setText(String.valueOf(vote.getParticipatorNum()));
            if (vote.getFlag() == VoteFlag.FLAG_START || vote.getFlag() == VoteFlag.FLAG_NOT_START) {
                ((StartingVoteHolder) holder).restTimeTxt.setVisibility(View.VISIBLE);
                ((StartingVoteHolder) holder).statueTxt.setGravity(START|CENTER); //这儿必须加强限定，和后面对应，否则list滑动会出现bug
                if (vote.getFlag() == VoteFlag.FLAG_NOT_START) {
                    ((StartingVoteHolder) holder).statueTxt.setText(R.string.txt_vote_not_start);
                    ((StartingVoteHolder) holder).restTimeTxt.setText("剩余"+getRestTime(vote.getEndTime())+"开始");// TODO: 2017/5/20 0020

                } else {
                    ((StartingVoteHolder) holder).statueTxt.setText(R.string.txt_vote_start);
                    ((StartingVoteHolder) holder).restTimeTxt.setText("剩余"+getRestTime(vote.getEndTime())+"结束");// TODO: 2017/5/20 0020
                }
            } else {
                ((StartingVoteHolder) holder).restTimeTxt.setVisibility(GONE);
                ((StartingVoteHolder) holder).statueTxt.setText(R.string.txt_vote_finish);
                ((StartingVoteHolder) holder).statueTxt.setGravity(CENTER);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/5/12 0012 按照一定的类型进行启动
                    Intent intent = new Intent(mcontext, VoteActivity.class);
                    intent.putExtra(CacheKey.VOTE_TYPE, vote.getType());
                    intent.putExtra(CacheKey.VOTE_FLAG, vote.getFlag());
                    intent.putExtra(CacheKey.VOTE_ID, vote.getId());
                    intent.putExtra(CacheKey.VOTE_TITLE, vote.getTitle());
                    mcontext.startActivity(intent);
                }
            });
        }
    }

    private String  getRestTime(long endTime) {
        long currentTime = System.currentTimeMillis();
        long restTime = endTime - currentTime;
        Date date = new Date(restTime);
        long formatTime ;
        if(restTime>oneDayTime) {
            formatTime = restTime/oneDayTime;
           return String.valueOf(formatTime)+"日";
        } else if(restTime>oneHourTime){
            formatTime = restTime/oneHourTime;
            return String.valueOf(formatTime)+"小时";
        } else {
            formatTime = restTime/oneMinuteTime;
            return String .valueOf(formatTime)+"分钟";
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    private class StartingVoteHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        TextView statueTxt;
        TextView numTxt;
        TextView restTimeTxt;

        StartingVoteHolder(View itemView) {
            super(itemView);
            titleTxt = (TextView) itemView.findViewById(R.id.txt_home_vote_title);
            statueTxt = (TextView) itemView.findViewById(R.id.txt_home_vote_statue);
            numTxt = (TextView) itemView.findViewById(R.id.txt_home_vote_num);
            restTimeTxt = (TextView) itemView.findViewById(R.id.txt_home_vote_rest_time);
            Drawable[] drawables = numTxt.getCompoundDrawables();
            drawables[0].setBounds(0, 0, 50, 50);
            numTxt.setCompoundDrawables(drawables[0], null, null, null);
        }
    }

    private class LabelHolder extends RecyclerView.ViewHolder {
        LabelHolder(View itemView) {
            super(itemView);
        }
    }
}
