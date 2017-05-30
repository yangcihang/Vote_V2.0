package net.hrsoft.vote.home.activity;

import android.app.AlertDialog;
import android.app.backup.BackupDataOutput;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.activity.RegisterActivity;
import net.hrsoft.vote.account.model.VerifyRequest;
import net.hrsoft.vote.account.model.VerifyResponse;
import net.hrsoft.vote.account.model.User;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.constant.VerifyType;
import net.hrsoft.vote.home.model.ModifyMobileRequest;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.RegexUtil;
import net.hrsoft.vote.util.ToastUtil;
import net.hrsoft.vote.widget.CleanableEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyMobileActivity extends ToolbarActivity {

    @BindView(R.id.edit_modify_mobile_mobile)
    CleanableEditText mobileEdit;
    @BindView(R.id.btn_modify_mobile_send_verify)
    Button sendVerifyBtn;
    @BindView(R.id.btn_modify_mobile_to_modify)
    Button toModifyBtn;
    @BindView(R.id.edit_modify_mobile_verify)
    CleanableEditText verifyEdit;

    private String oldMobile;
    private String verify;
    private String newMobile;
    private String password;

    @Override
    protected void init() {
        super.init();
        this.setActivityTitle(getString(R.string.title_all_activity));
        User user = (User) VoteApplication.getCache().getSerializableObj(CacheKey.USER);
        if (user != null) {
            oldMobile = user.getMobile();
        }
    }

    /**
     * 发送验证码请求
     */
    @OnClick(R.id.btn_modify_mobile_send_verify)
    void sendVerify() {
        newMobile = mobileEdit.getText().toString().trim();
        if(newMobile.isEmpty()) {
            ToastUtil.showToast(this,R.string.toast_mobile_is_empty);
        } else {
            if (!RegexUtil.isMobileNum(newMobile)) {
                ToastUtil.showToast(ModifyMobileActivity.this, R.string.toast_mobile_is_wrong);
            } else {
                VerifyRequest verifyRequest = new VerifyRequest();
                verifyRequest.setMobile(newMobile);
                verifyRequest.setType(VerifyType.MOBILE_TYPE);
                RestClient.getVoterService().registerVerifyPost(verifyRequest).enqueue(new Callback<APIResponse<VerifyResponse>>() {
                    @Override
                    public void onResponse(Call<APIResponse<VerifyResponse>> call, Response<APIResponse<VerifyResponse>> response) {
                        if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                            ToastUtil.showToast(ModifyMobileActivity.this, "验证码为" + response.body().getData().getCode());
                        } else {
                            ToastUtil.showToast(ModifyMobileActivity.this,R.string.toast_net_has_question);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<VerifyResponse>> call, Throwable t) {
                        ToastUtil.showToast(ModifyMobileActivity.this, R.string.toast_net_has_question);
                    }
                });

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        sendVerifyBtn.setClickable(false);
                        sendVerifyBtn.setText((millisUntilFinished / 1000) + " s");
                    }

                    @Override
                    public void onFinish() {
                        sendVerifyBtn.setText(R.string.btn_get_verify);
                        sendVerifyBtn.setClickable(true);
                    }
                }.start();
            }
        }
    }

    @OnClick(R.id.btn_modify_mobile_to_modify)
    void modifyMobile() {
        newMobile = mobileEdit.getText().toString().trim();
        verify = verifyEdit.getText().toString().trim();
        if(!RegexUtil.isMobileNum(newMobile)) {
            ToastUtil.showToast(this,R.string.toast_mobile_is_wrong);
        } else if(verify==null||verify.isEmpty()){
            ToastUtil.showToast(this,R.string.toast_not_input_verify);
        }else {
            toInputPasswordAndModify();
        }
    }

    private void toInputPasswordAndModify() {
        final EditText passwordEdit = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(passwordEdit)
                .setTitle(R.string.dialog_input_password)
                .setNegativeButton(R.string.dialog_btn_cancel,null)
                .setPositiveButton(R.string.dialog_btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password = passwordEdit.getText().toString().trim();
                        toModifyMobile();
                    }
                })
                .show();
    }

    private void toModifyMobile() {
        ModifyMobileRequest modifyMobileRequest =new ModifyMobileRequest();
        modifyMobileRequest.setNewMobile(newMobile);
        modifyMobileRequest.setOldMobile(oldMobile);
        modifyMobileRequest.setPassword(password);
        modifyMobileRequest.setCode(verify);
        RestClient.getVoterService().modifyMobilePost(modifyMobileRequest).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code()<APICode.SERVER_RESPONSE_CODE) {
                    if(response.body().getCode()== APICode.SUCCESS) {
                        VoteApplication.getInstance().exitApp();
                        VoteApplication.getInstance().goLoginActivity();
                        ToastUtil.showToast(getApplicationContext(),R.string.toast_modify_success);
                    } else if(response.body().getCode() == APICode.VerifyIsWrong) {
                        ToastUtil.showToast(ModifyMobileActivity.this,R.string.toast_verify_is_wrong);
                    } else {
                        ToastUtil.showToast(ModifyMobileActivity.this,R.string.toast_net_has_question);
                    }
                } else {
                    ToastUtil.showToast(ModifyMobileActivity.this,R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                ToastUtil.showToast(ModifyMobileActivity.this,R.string.toast_net_has_question);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_mobile;
    }
}
