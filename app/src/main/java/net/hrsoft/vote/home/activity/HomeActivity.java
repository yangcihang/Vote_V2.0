package net.hrsoft.vote.home.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.activity.LoginActivity;
import net.hrsoft.vote.account.model.User;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.home.fragment.HomeAllVotesFragment;
import net.hrsoft.vote.home.fragment.HomeUserVotesFragment;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;


import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class HomeActivity extends ToolbarActivity implements View.OnClickListener {

    @BindView(R.id.layout_home_bottom)
    LinearLayout homeBottomLayout;
    @BindView(R.id.layout_home_header)
    LinearLayout homeHeaderLayout;
    @BindView(R.id.layout_frame_content)
    FrameLayout contentFrameLayout;
    @BindView(R.id.txt_home_header_account)
    TextView headAccountTxt;
    @OnClick(R.id.layout_home_header)
    void toUserCenter() {
        startActivity(new Intent(this,UserCenterActivity.class));
    }

    private LinearLayout bottomLayoutBar;//待加载的底部布局
    Button loginBottomPastBtn; //登陆时底部用户投票的button
    Button loginBottomCurrentBtn;//登陆时底部当前投票的button
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }


    /**
     * 初始化两个RecyclerView的信息
     */
    @Override
    protected void init() {
        super.init();
        this.setActivityTitle(getString(R.string.title_all_activity));
        replaceFragment(R.id.layout_frame_content,new HomeAllVotesFragment(),null);//初始加载第一个界面
        loadHeaderBottomLayout();
    }

    /**
     * 加载首部和底部布局
     */
    private void loadHeaderBottomLayout() {
        if (VoteApplication.getInstance().getUser() == null) {
            homeHeaderLayout.setVisibility(GONE);
            addLayout(R.layout.bottom_home_unlogin_bar);
            ImageView mobileToLoginImg = (ImageView) bottomLayoutBar.findViewById(R.id.img_to_mobile_login);//登陆图片的点击事件
            mobileToLoginImg.setOnClickListener(this);
        } else {
            homeHeaderLayout.setVisibility(View.VISIBLE);
            addLayout(R.layout.bottom_home_login_bar);
            User user = (User) VoteApplication.getCache().getSerializableObj(CacheKey.USER);//获取当前user信息
            try {
                    headAccountTxt.setText(user.getMobile()+" >");
            } catch (Exception e) {
                VoteApplication.getInstance().goLoginActivity();
            }
            loginBottomCurrentBtn = (Button) bottomLayoutBar.findViewById(R.id.btn_home_login_current);
            loginBottomCurrentBtn.setSelected(true);//默认为true
            loginBottomPastBtn = (Button)bottomLayoutBar.findViewById(R.id.btn_home_login_past);
            loginBottomCurrentBtn.setOnClickListener(this);
            loginBottomPastBtn.setOnClickListener(this);
        }
    }
    private void addLayout(int bottomLayoutId) {
        homeBottomLayout.removeAllViews();
        bottomLayoutBar = (LinearLayout)LayoutInflater.from(this).inflate(bottomLayoutId,homeBottomLayout,false);
        homeBottomLayout.addView(bottomLayoutBar);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_to_mobile_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_home_login_current:
                if(!loginBottomCurrentBtn.isSelected()) {
                    loginBottomCurrentBtn.setSelected(true);
                    loginBottomPastBtn.setSelected(false);
                    replaceFragment(R.id.layout_frame_content,new HomeAllVotesFragment(),null);
                }
                break;
            case R.id.btn_home_login_past:
                if(!loginBottomPastBtn.isSelected()) {
                    loginBottomPastBtn.setSelected(true);
                    loginBottomCurrentBtn.setSelected(false);
                    replaceFragment(R.id.layout_frame_content,new HomeUserVotesFragment(),null);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onBackBtnOnclick() {
        exitDialog();
    }

    /**
     * 返回按键弹出Dialog
     *
     * @param keyCode keyCode
     * @param event   event
     * @return false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog();
        }
        return false;
    }

    /**
     * 退出时候提示的对话框
     */
    private void exitDialog() {
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        isExit.setTitle(getString(R.string.dialog_txt_title));
        isExit.setMessage(getString(R.string.dialog_message_content));
        isExit.setButton(getString(R.string.dialog_btn_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (VoteApplication.getInstance().getUser() == null) {
                    VoteApplication.getInstance().exitApp();
                } else {
                    RestClient.getVoterService().logoutPost().enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            if (response.body().getCode() < APICode.SERVER_RESPONSE_CODE||response.body().getCode()==APICode.TokenExpire) {
                                VoteApplication.getCache().clear();
                                VoteApplication.getInstance().exitApp();
                            } else {
                                VoteApplication.getCache().clear();
                                VoteApplication.getInstance().exitApp();
                                // TODO: 2017/5/14 0014 修改退出逻辑,包括onFailure
                            }
                        }
                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            VoteApplication.getCache().clear();
                            VoteApplication.getInstance().exitApp();
                        }
                    });
                }
            }
        });
        isExit.setButton2(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        isExit.show();
    }
}
