package net.hrsoft.vote.home.fragment;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.common.BaseFragment;
import net.hrsoft.vote.constant.VoteFlag;
import net.hrsoft.vote.home.adapter.HomeVoteRecAdapter;
import net.hrsoft.vote.home.model.VoteInfo;
import net.hrsoft.vote.home.model.AllVotesResponse;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.ToastUtil;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author YangCihang.
 * @since 2017/5/14 0014.
 * Email yangcihang@hrsoft.net
 */

public class HomeAllVotesFragment extends BaseFragment {
    @BindView(R.id.rec_home_starting_vote)
    RecyclerView homeStartingVoteRec;

    private int spanCount = 2;//瀑布流的流数,默认是2
    private boolean isFirstGetInfo = true;//标记是否第一次得到用户信息
    private List<VoteInfo> voteList; //未开始和开始的投票信息列表
    private List<VoteInfo> endVoteList; //已经结束的投票信息
    private HomeVoteRecAdapter homeStartingVoteRecAdapter;
    private int currentPage = 1;//前一页的页数,默认是1
    private final static int ROWS = 8; //一页8条
    private boolean isLastPage = false; //标记是否是最后一页,默认为false

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_all_votes;
    }

    @Override
    protected void initVariable() {
        super.initVariable();
        voteList = new ArrayList<>();
        endVoteList = new ArrayList<>();
        getAllVotesRequest();
    }

    private void getAllVotesRequest() {
        RestClient.getVoterService().allVotesPost(currentPage, ROWS).enqueue(new Callback<APIResponse<AllVotesResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<AllVotesResponse>> call, Response<APIResponse<AllVotesResponse>> response) {
                if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                    if (response.body().getCode() == APICode.SUCCESS) {
                        if (isFirstGetInfo) {
                            voteList.addAll(response.body().getData().getList());
                            separateVotes();
                            isFirstGetInfo = false;
                            initList();
                            if(response.body().getData().isLastPage()) {
                                isLastPage = true;
                            }
                        } else if (!isLastPage) {
                            voteList.addAll(response.body().getData().getList());
                            separateVotes();
                            homeStartingVoteRecAdapter.notifyDataSetChanged();
                            if (currentPage == response.body().getData().getLastPage()) {
                                if (!endVoteList.isEmpty()) {  //如果有已经结束的投票
                                    addLabelLayout();//将结束的投票信息加入
                                    voteList.addAll(endVoteList);
                                    homeStartingVoteRecAdapter.notifyDataSetChanged();
                                }
                                isLastPage = true;
                            }
                        }
                        currentPage += 1;
                    } else if (response.body().getCode() == APICode.TokenExpire) {
                        ToastUtil.showToast(getContext(), R.string.dialog_user_expire);
                    } else if (response.body().getCode() == APICode.NeedLogin) {
                        ToastUtil.showToast(getContext(), R.string.dialog_user_need_to_login);
                    }
                }else {
                    ToastUtil.showToast(getContext(), R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<AllVotesResponse>> call, Throwable t) {
                ToastUtil.showToast(getContext(), R.string.toast_net_has_question);
            }
        });
    }

    /**
     * 添加结束投票的label
     */
    private void addLabelLayout() {
        voteList.add(null);
        homeStartingVoteRecAdapter.notifyDataSetChanged();
    }

    /**
     * 将投票分离成已结束的和未结束的
     */
    private void separateVotes() {
        for (int i = 0; i < voteList.size(); i++) {
            if (voteList.get(i).getFlag() == VoteFlag.FLAG_END) {
                endVoteList.add(voteList.get(i));
                voteList.remove(i);
                i--;
            }
        }
    }

    /**
     * 初始化 List,设置监听事件和设置瀑布流
     */
    private void initList() {
        homeStartingVoteRecAdapter = new HomeVoteRecAdapter(voteList, getContext());
        final GridLayoutManager voteGridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        homeStartingVoteRec.setLayoutManager(voteGridLayoutManager);
        homeStartingVoteRec.setAdapter(homeStartingVoteRecAdapter);
        final int[] lastVisibleItem = new int[1];//记载最后一个位置
        homeStartingVoteRec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem[0] = voteGridLayoutManager.findLastVisibleItemPosition();// TODO: 2017/5/18 0018 这儿改了
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem[0] + 1 == homeStartingVoteRecAdapter.getItemCount()) {
                    if (!isLastPage)
                        getAllVotesRequest();
                }
            }
        }); //设置滚动加载
        voteGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (homeStartingVoteRecAdapter.getItemViewType(position) == HomeVoteRecAdapter.LABEL_VIEW_TYPE) {
                    return spanCount;
                } else {
                    return spanCount - 1;
                }
            }
        });
    }
}
