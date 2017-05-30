package net.hrsoft.vote.home.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.model.User;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.home.model.ChangSexRequest;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCenterActivity extends ToolbarActivity {

    @BindView(R.id.txt_user_center_mobile)
    TextView mobileTxt;
    @BindView(R.id.txt_user_center_change_mobile)
    TextView changeMobileTxt;
    @BindView(R.id.txt_user_center_sex)
    TextView sexTxt;

    private RadioGroup radioGroup;
    @OnClick(R.id.txt_user_center_change_mobile)
    void onChangeMobile() {
        startActivity(new Intent(this,ModifyMobileActivity.class));
    }
    @OnClick(R.id.txt_user_center_sex)
    void onChangeSex() {
        View sexView = LayoutInflater.from(this).inflate(R.layout.layout_select_sex, null, false);
        //每次点击都加载一次，要是放在init里面，被dialog用一次后，就不能再用了
        radioGroup = (RadioGroup) sexView.findViewById(R.id.group_sex);
        new AlertDialog.Builder(this).
                setView(sexView).
                setNegativeButton(R.string.dialog_btn_cancel,null)
                .setPositiveButton(R.string.dialog_btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      sendChangeSexRequest();
                    }
                })
                .show();
    }

    private void sendChangeSexRequest() {
        final String newSex;
        switch (radioGroup.getId()) {
            case R.id.rbtn_male:
                newSex = "male";
                break;
            case R.id.rbtn_female:
                newSex = "female";
                break;
            default:
                newSex = "male"; //默认为男
                break;
        }
        ChangSexRequest changSexRequest = new ChangSexRequest();
        changSexRequest.setNewSex(newSex);
        RestClient.getVoterService().changeSexPost(changSexRequest).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code()< APICode.SERVER_RESPONSE_CODE) {
                    if(response.body().getCode() == APICode.SUCCESS) {
                        ToastUtil.showToast(UserCenterActivity.this,R.string.toast_change_sex_success);
                        if(newSex.equals("female")){
                            sexTxt.setText("女");
                        }else {
                            sexTxt.setText("男");
                        }
                    } else if(response.body().getCode() == APICode.TokenExpire) {
                        ToastUtil.showToast(UserCenterActivity.this,R.string.dialog_user_expire);
                    }
                } else {
                    ToastUtil.showToast(UserCenterActivity.this,R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                ToastUtil.showToast(UserCenterActivity.this,R.string.toast_net_has_question);

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }

    @Override
    protected void init() {
        super.init();
        this.setActivityTitle(getString(R.string.title_all_activity));
        User user = (User) VoteApplication.getCache().getSerializableObj(CacheKey.USER);//获取当前user信息
        mobileTxt.setText(user != null ? user.getMobile() : null);
        if(user != null && user.getSex().equals("male")) {
            sexTxt.setText("男");
        }else {
            sexTxt.setText("女");
        }

    }
}
