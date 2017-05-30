package net.hrsoft.vote.home.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hrsoft.vote.R;
import net.hrsoft.vote.constant.VoteType;
import net.hrsoft.vote.home.adapter.item.MultyVoteHolder;
import net.hrsoft.vote.home.adapter.item.RankHolder;
import net.hrsoft.vote.home.adapter.item.SingleVoteHolder;
import net.hrsoft.vote.home.model.Records;
import net.hrsoft.vote.home.model.VoteOptionInfo;
import net.hrsoft.vote.util.ToastUtil;
import net.hrsoft.vote.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YangCihang.
 * @since 2017/5/16 0016.
 * Email yangcihang@hrsoft.net
 */

public class VoteVoteRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<VoteOptionInfo> mlist;
    private int type;
    private int maxSelected = 0; //多选的最多选择数
    private List<Boolean> flagList; //标记是否被选中
    private List<Records> recordsList;//投票的记录
    private List<String> scoreList; //记录分数
    private static final int RANK_TEN = 10; //十分制的分数
    private static final int RANK_HUNDRED = 100; //百分之的分数
    private Context context;
    private OnSelectionChangedListener onSelectionChangedListener; //监听选项改变

    /**
     * 当选择选项时回调
     *
     * @param onSelectionChangedListener onSelectionChangedListener
     */
    public void setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener;
    }

    /**
     * 获取投票的记录
     *
     * @return recordsList
     */
    public List<Records> getRecordsList() {
        return recordsList;
    }

    /**
     * 当为打分时获取是否都打了分
     *
     * @return flagList
     */
    public List<Boolean> getFlagList() {
        return flagList;
    }

    public VoteVoteRecAdapter(List<VoteOptionInfo> list, int type, int maxSelected, Context context) {
        mlist = list;
        this.type = type;
        this.maxSelected = maxSelected;
        this.context = context;
        flagList = new ArrayList<>();
        recordsList = new ArrayList<>();
        if (type == VoteType.RANK_HUNDRED || type == VoteType.RANK_TEN) {
            scoreList = new ArrayList<>();
            for (int i = 0; i < maxSelected; i++) {
                scoreList.add("");
                recordsList.add(null);
            }//当为十分和百分制时，初始化分数的list
        }
        for (int i = 0; i < list.size(); i++) {
            flagList.add(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (type) {
            case VoteType.SINGEL_VOTE:
                return VoteType.SINGEL_VOTE;
            case VoteType.MULTY_VOTE:
                return VoteType.MULTY_VOTE;
            case VoteType.RANK_HUNDRED:
                return VoteType.RANK_HUNDRED;
            case VoteType.RANK_TEN:
                return VoteType.RANK_TEN;
            default:
                return VoteType.SINGEL_VOTE;  //默认返回单选（要改）
        }
    }

    /**
     * 根据不同的类型创建不同的Item
     *
     * @param parent   viewGroup
     * @param viewType viewType
     * @return itemView
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (type) {
            case VoteType.SINGEL_VOTE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_vote_single_vote, parent, false);
                return new SingleVoteHolder(view);
            case VoteType.MULTY_VOTE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_vote_multy_vote, parent, false);
                return new MultyVoteHolder(view);
            case VoteType.RANK_TEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_vote_rank, parent, false);
                return new RankHolder(view);
            case VoteType.RANK_HUNDRED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_vote_rank, parent, false);
                return new RankHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_vote_single_vote, parent, false);
                return new SingleVoteHolder(view); //默认返回单选 // TODO: 2017/5/18 0018
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VoteOptionInfo voteOptionInfo = mlist.get(position);
        switch (holder.getItemViewType()) {
            case VoteType.SINGEL_VOTE:
                SingleVoteHolder singleVoteHolder = (SingleVoteHolder) holder;
                singleVoteHolder.optionRbtn.setText(voteOptionInfo.getTitle());
                if (!flagList.get(position)) {
                    singleVoteHolder.optionRbtn.setChecked(false);
                } else {
                    singleVoteHolder.optionRbtn.setChecked(true);
                }
                viewSetTag(singleVoteHolder.optionRbtn, position, voteOptionInfo);
                singleVoteHolder.optionRbtn.setOnClickListener(this);
                break;
            case VoteType.MULTY_VOTE:
                MultyVoteHolder multyVoteHolder = (MultyVoteHolder) holder;
                if (!flagList.get(position)) {
                    multyVoteHolder.optionChk.setChecked(false);
                } else {
                    multyVoteHolder.optionChk.setChecked(true);
                }
                multyVoteHolder.optionChk.setText(voteOptionInfo.getTitle());
                viewSetTag(multyVoteHolder.optionChk, position, voteOptionInfo);
                multyVoteHolder.optionChk.setOnClickListener(this);
                break;
            case VoteType.RANK_HUNDRED:
                RankHolder hundredRankHolder = (RankHolder) holder;
                hundredRankHolder.titleTxt.setText(voteOptionInfo.getTitle());
                viewSetTag(hundredRankHolder.scoreTxt, position, voteOptionInfo);
                hundredRankHolder.scoreTxt.setText(scoreList.get(position));
                hundredRankHolder.scoreTxt.setOnClickListener(this);
                break;
            case VoteType.RANK_TEN:
                RankHolder tenRankHolder = (RankHolder) holder;
                tenRankHolder.titleTxt.setText(voteOptionInfo.getTitle());
                viewSetTag(tenRankHolder.scoreTxt, position, voteOptionInfo);
                tenRankHolder.scoreTxt.setText(scoreList.get(position));
                tenRankHolder.scoreTxt.setOnClickListener(this);
                break;
            default:
                break;
        }
    }

    /**
     * 设置位置和当前选项的tag
     *
     * @param view           view
     * @param position       位置
     * @param voteOptionInfo 选项信息
     */
    private void viewSetTag(View view, int position, VoteOptionInfo voteOptionInfo) {
        view.setTag(R.id.option_position, position);
        view.setTag(R.id.option_value, voteOptionInfo);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    // TODO: 2017/5/19 0019 将来再会封装一次 

    /**
     * 选项的点击事件
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        VoteOptionInfo info = (VoteOptionInfo) v.getTag(R.id.option_value); //选项信息
        Records record = new Records(info.getId(), info.getVoteId(), type, 0);//加载record，默认值为0//记录信息
        int position = (int) v.getTag(R.id.option_position);
        switch (v.getId()) {
            case R.id.rbtn_vote_single_option: //当单选时
                for (int i = 0; i < flagList.size(); i++) {
                    flagList.set(i, false);
                } //将其他的都设为false，当前位置设置为true
                flagList.set(position, true);
                recordsList.clear();
                recordsList.add(record);
                if (onSelectionChangedListener != null) {
                    onSelectionChangedListener.onSelectionChanged(flagList, maxSelected);
                }
                notifyDataSetChanged();
                break;
            case R.id.chk_mul_option: //当多选时
                if (!flagList.get(position)) {
                    flagList.set(position, !flagList.get(position));
                    recordsList.add(record);
                } else {
                    flagList.set(position, !flagList.get(position));
                    for (int i = 0; i < recordsList.size(); i++) {
                        if (recordsList.get(i).getOptionId() == info.getId()) {
                            recordsList.remove(i);
                        }
                    }
                }
                if (recordsList.size() > maxSelected) {
                    flagList.set(position, !flagList.get(position));
                    for (int i = 0; i < recordsList.size(); i++) {
                        if (recordsList.get(i).getOptionId() == info.getId()) {
                            recordsList.remove(i);
                        }
                    }
                    ToastUtil.showToast(context, "最多选" + maxSelected + "项");
                }
                if (onSelectionChangedListener != null) {
                    onSelectionChangedListener.onSelectionChanged(flagList, maxSelected);
                }
                notifyDataSetChanged();
                break;
            case R.id.txt_vote_show_score: //当为打分时
                getScoreDialog(position, record);
                break;
            default:
                break;
        }

    }


    private void getScoreDialog(final int position, final Records record) {
        int rank;
        if (type == VoteType.RANK_HUNDRED) {
            rank = RANK_HUNDRED;
        } else {
            rank = RANK_TEN;
        }
        final String[] ans = new String[1];
        final List<String> wheelViewItems = new ArrayList<>();
        for (int i = 0; i <= rank; i++) {
            wheelViewItems.add(String.valueOf(i));
        }
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.layout_wheel_view, null,false);
        final WheelView wheelView = (WheelView) alertLayout.findViewById(R.id.wheel_vote_rank);
        wheelView.setItems(wheelViewItems);
        wheelView.setSeletion(0);
        wheelView.setOffset(1);
        new AlertDialog.Builder(context)
                .setView(alertLayout)
                .setPositiveButton(R.string.dialog_btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ans[0] = wheelView.getSeletedItem();
                        flagList.set(position, true); //记录是否打分
                        record.setValue(Long.parseLong(ans[0]));
                        recordsList.set(position, record);
                        scoreList.set(position, ans[0]);
                        if (onSelectionChangedListener != null) {
                            onSelectionChangedListener.onSelectionChanged(flagList, maxSelected);
                        }
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .show();
    }
}
