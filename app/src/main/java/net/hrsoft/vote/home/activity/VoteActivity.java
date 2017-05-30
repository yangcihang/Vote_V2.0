package net.hrsoft.vote.home.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.activity.LoginActivity;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.constant.VoteFlag;
import net.hrsoft.vote.constant.VoteType;
import net.hrsoft.vote.home.adapter.OnSelectionChangedListener;
import net.hrsoft.vote.home.adapter.VoteVoteRecAdapter;
import net.hrsoft.vote.home.model.VoteInfoResponse;
import net.hrsoft.vote.home.model.VoteOptionInfo;
import net.hrsoft.vote.home.model.VoteRecordRequest;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class VoteActivity extends ToolbarActivity implements View.OnClickListener {
    @BindView(R.id.layout_vote_bottom)
    LinearLayout voteBottomLayout; //底部的Layout
    @BindView(R.id.txt_vote_has_selected)
    TextView hasSelectedHintTxt;
    @BindView(R.id.rec_vote_vote)
    RecyclerView voteOptionsRec;
    @BindView(R.id.txt_vote_vote_time)
    TextView voteTimeTxt;
    @BindView(R.id.txt_vote_vote_title)
    TextView voteTitleTxt;
    @BindView(R.id.txt_vote_vote_statue)
    TextView voteStatueTxt;

    private VoteVoteRecAdapter voteRecAdapter; //投票的适配
    private int voteType; //投票的类型
    private int statue = VoteFlag.FLAG_USER_UNLOGIN; //当前用户和投票的状态,默认为未登录
    private LinearLayout bottomLayoutBar; //需要加载的底部布局
    private int voteId; //传入的voteId
    private List<VoteOptionInfo> voteOptionInfoList; //投票选项信息的list

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote;
    }

    @Override
    protected void initVariable() {
        super.initVariable();
        voteOptionInfoList = new ArrayList<>();
        int flag = getIntent().getIntExtra(CacheKey.VOTE_FLAG, VoteFlag.FLAG_NOT_START);//默认为未开始
        int type = getIntent().getIntExtra(CacheKey.VOTE_TYPE, VoteType.SINGEL_VOTE);//默认为单选
        String title = getIntent().getStringExtra(CacheKey.VOTE_TITLE);
        voteTitleTxt.setText(title); //设置Title
        voteId = getIntent().getIntExtra(CacheKey.VOTE_ID, 0); //获取投票Id，默认为0
        sendVoteInfoRequest();
        if (VoteApplication.getInstance().getUser() == null) {
            statue = VoteFlag.FLAG_USER_UNLOGIN;
        } else {
            statue = flag;
        }
        voteType = type;
    }

    /**
     * 加载view
     */
    @Override
    protected void initView() {
        super.initView();
        loadVoteStatue();
        loadBottomLayout();
    }

    /**
     * 加载状态
     */
    private void loadVoteStatue() {
        switch (statue) {
            case VoteFlag.FLAG_START:
                voteStatueTxt.setText(R.string.txt_vote_start);
                break;
            case VoteFlag.FLAG_END:
                voteStatueTxt.setText(R.string.txt_home_vote_item_statue_end);
                break;
            case VoteFlag.FLAG_NOT_START:
                voteStatueTxt.setText(R.string.txt_vote_not_start);
                break;
            default:
                break;
        }

    }

    /**
     * 获取单个投票信息的请求
     */
    private void sendVoteInfoRequest() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_network_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RestClient.getVoterService().voteInfoPost(voteId).enqueue(new Callback<APIResponse<VoteInfoResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<VoteInfoResponse>> call, Response<APIResponse<VoteInfoResponse>> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                    if (response.body().getCode() == APICode.SUCCESS) {
                        voteOptionInfoList = response.body().getData().getOptions();
                        loadTimeView(response.body().getData().getVoteShow().getStartTime(),
                                response.body().getData().getVoteShow().getEndTime());
                        loadAdapter(response.body().getData().getVoteShow().getMax()); //加载adapter
                    } else {
                        ToastUtil.showToast(VoteActivity.this, R.string.toast_net_has_question);
                    }
                } else {
                    ToastUtil.showToast(VoteActivity.this, R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<VoteInfoResponse>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.showToast(VoteActivity.this, R.string.toast_net_has_question);
            }
        });
    }

    /**
     * 加载剩余时间
     */
    private void loadTimeView(long startTime, long endTime) {
        String start = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd").format(endTime);
        voteTimeTxt.setText(start + "  -  " + end);
    }

    /**
     * 加载Adapter
     */
    private void loadAdapter(int maxSelected) {
        voteRecAdapter = new VoteVoteRecAdapter(voteOptionInfoList, voteType, maxSelected, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        voteOptionsRec.setAdapter(voteRecAdapter);
        voteOptionsRec.setLayoutManager(linearLayoutManager);
        /**
         * 选择选项时，改变底部hintTxt
         */
        voteRecAdapter.setOnSelectionChangedListener(new OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(List<Boolean> flagList, int maxSelected) {
                int num = 0;
                StringBuilder selection = new StringBuilder();
                for (int i = 0; i < flagList.size(); i++) {
                    if (flagList.get(i)) {
                        int t = i+1; //从1开始计数
                        selection.append("选项" +t+ " ");
                        num++;
                    }
                }
                int restNum = maxSelected - num;
                if (hasSelectedHintTxt.getVisibility() == View.VISIBLE) {
                    switch (voteType) {
                        case VoteType.RANK_HUNDRED:
                            if(restNum == 0){
                                hasSelectedHintTxt.setText("您已经完成了全部的打分");
                            }else {
                                hasSelectedHintTxt.setText("还有" + restNum + "项没打分");
                            }
                            break;
                        case VoteType.RANK_TEN:
                            if(restNum == 0){
                                hasSelectedHintTxt.setText("您已经完成了全部的打分");
                            }else {
                                hasSelectedHintTxt.setText("还有" + restNum + "项没打分");
                            }
                            break;
                        case VoteType.MULTY_VOTE:
                            hasSelectedHintTxt.setText("你已经选择了：" + selection);
                            break;
                        case VoteType.SINGEL_VOTE:
                            hasSelectedHintTxt.setText("你已经选择了：" + selection);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /**
     * 加载底部布局
     */
    private void loadBottomLayout() {
        if (statue == VoteFlag.FLAG_USER_UNLOGIN) {
            hasSelectedHintTxt.setVisibility(GONE);
            addLayout(R.layout.bottom_home_unlogin_bar);
            ImageView toLoginImg = (ImageView) bottomLayoutBar.findViewById(R.id.img_to_mobile_login);
            toLoginImg.setOnClickListener(this);
        } else if (statue == VoteFlag.FLAG_START) {
            addLayout(R.layout.bottom_vote_vote_start_bar);
            Button confirmVoteBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_vote_bottom_confirm_vote);
            Button shareBtn = (Button)bottomLayoutBar.findViewById(R.id.btn_vote_bottom_share);
            confirmVoteBtn.setOnClickListener(this);
            shareBtn.setOnClickListener(this);
        } else if (statue == VoteFlag.FLAG_NOT_START) {
            hasSelectedHintTxt.setVisibility(GONE);
            addLayout(R.layout.bottom_vote_not_start);
            Button shareBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_vote_bottom_share);
            shareBtn.setOnClickListener(this);
        } else if (statue == VoteFlag.FLAG_END) {
            hasSelectedHintTxt.setVisibility(GONE);
            addLayout(R.layout.bottom_vote_vote_finish_bar);
            Button backToHomeBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_vote_bottom_back);
            backToHomeBtn.setOnClickListener(this);
        } else if (voteType == VoteType.RANK_TEN || voteType == VoteType.RANK_HUNDRED) {
            addLayout(R.layout.bottom_vote_rank_start_bar);
            Button confirmRankBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_vote_bottom_confirm_rank);
            Button shareBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_vote_bottom_share);
            shareBtn.setOnClickListener(this);
            confirmRankBtn.setOnClickListener(this);
        }
    }

    /**
     * 添加布局
     *
     * @param bottomLayoutId 布局的id
     */
    private void addLayout(int bottomLayoutId) {
        voteBottomLayout.removeAllViews();
        bottomLayoutBar = (LinearLayout) LayoutInflater.from(this).inflate(bottomLayoutId, voteBottomLayout, false);
        voteBottomLayout.addView(bottomLayoutBar);
    }


    @Override
    protected void init() {
        this.setActivityTitle(getString(R.string.title_all_activity));
        super.init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_to_mobile_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_vote_bottom_back:
                this.finish();
                break;
            case R.id.btn_vote_bottom_confirm_vote:
                sendVoteSubmitRecordRequest();
                break;
            case R.id.btn_vote_bottom_share:
                showShareDialog();
                break;
            case R.id.txt_encode_share:
                createEncodePicture();
                break;
            default:
                break;
        }
    }

    /**
     * 加载二维码图片
     */
    private void createEncodePicture() {

    }

    /**
     * 展示分享的对话窗
     */
    private void showShareDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Dialog_share);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share_vote,null);
        TextView encodeTxt = (TextView) dialogView.findViewById(R.id.txt_encode_share);
        encodeTxt.setOnClickListener(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getWindow().getDecorView().getWidth();
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    /**
     * 发送提交的请求
     */
    private void sendVoteSubmitRecordRequest() {
        VoteRecordRequest voteRecordRequest = new VoteRecordRequest();
        voteRecordRequest.setRecords(voteRecAdapter.getRecordsList());
        if (voteType == VoteType.RANK_HUNDRED || voteType == VoteType.RANK_TEN) {
            List<Boolean> flagList = voteRecAdapter.getFlagList();
            for (Boolean flag : flagList) {
                if (!flag) {
                    ToastUtil.showToast(VoteActivity.this, R.string.toast_all_votes_must_to_score);
                    return;
                }
            }
        }
        RestClient.getVoterService().voteRecordsPost(voteId, voteRecordRequest).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                    if (response.body().getCode() == APICode.SUCCESS) {
                        ToastUtil.showToast(VoteActivity.this, R.string.toast_vote_records_submit_succsee);
                    } else if (response.body().getCode() == APICode.YouHaveBeenVoted) {
                        ToastUtil.showToast(VoteActivity.this, R.string.toast_vote_records_has_voted);
                    } else if (response.body().getCode() == APICode.TokenExpire) {
                        ToastUtil.showToast(VoteActivity.this, R.string.dialog_user_expire);
                    }
                } else {
                    ToastUtil.showToast(VoteActivity.this, R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                ToastUtil.showToast(VoteActivity.this, R.string.toast_net_has_question);
            }
        });
    }
}
