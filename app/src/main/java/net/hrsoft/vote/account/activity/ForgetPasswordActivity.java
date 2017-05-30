package net.hrsoft.vote.account.activity;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.model.ForgetPasswordRequest;
import net.hrsoft.vote.account.model.VerifyRequest;
import net.hrsoft.vote.account.model.VerifyResponse;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.VerifyType;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.RegexUtil;
import net.hrsoft.vote.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends ToolbarActivity {

    @BindView(R.id.edit_mobile)
    EditText mobileEdit;
    @BindView(R.id.edit_new_password)
    EditText passwordEdit;
    @BindView(R.id.edit_verify)
    EditText verifyEdit;
    @BindView(R.id.btn_modify_password)
    Button modifyPasswordBtn;
    @BindView(R.id.btn_send_verify)
    Button sendVerifyBtn;

    private String mobile;
    private String newPassword;
    private String verify;

    @Override
    protected void init() {
        super.init();
        setActivityTitle(getString(R.string.title_all_activity));
    }

    @Override
    public void finish() {
        super.finish();

    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.btn_send_verify)
    void onSendVerify() {
        mobile = mobileEdit.getText().toString().trim();
        if(mobile.isEmpty()) {
            ToastUtil.showToast(this,R.string.toast_mobile_is_empty);
        } else {
            if (!RegexUtil.isMobileNum(mobile)) {
                ToastUtil.showToast(this, R.string.toast_mobile_is_wrong);
            } else {
                VerifyRequest verifyRequest = new VerifyRequest();
                verifyRequest.setMobile(mobile);
                verifyRequest.setType(VerifyType.FORGET_PASSWORD_TYPE);
                RestClient.getVoterService().registerVerifyPost(verifyRequest).enqueue(new Callback<APIResponse<VerifyResponse>>() {
                    @Override
                    public void onResponse(Call<APIResponse<VerifyResponse>> call, Response<APIResponse<VerifyResponse>> response) {
                        if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                            ToastUtil.showToast(ForgetPasswordActivity.this, "验证码为" + response.body().getData().getCode());
                        } else {
                            ToastUtil.showToast(ForgetPasswordActivity.this,R.string.toast_net_has_question);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<VerifyResponse>> call, Throwable t) {
                        ToastUtil.showToast(ForgetPasswordActivity.this, R.string.toast_net_has_question);
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


    @OnClick(R.id.btn_modify_password)
    void onModifyPassword() {
        mobile = mobileEdit.getText().toString().trim();
        newPassword = passwordEdit.getText().toString().trim();
        verify = verifyEdit.getText().toString().trim();
        if(!RegexUtil.isMobileNum(mobile)) {
            ToastUtil.showToast(this,R.string.toast_mobile_is_wrong);
        } else if(newPassword.length()>20|| newPassword.length()<6) {
            ToastUtil.showToast(this,R.string.toast_password_is_wrong);
        } else if(verify==null||verify.isEmpty()){
            ToastUtil.showToast(this,R.string.toast_not_input_verify);
        }else {
            sendModifyPasswordRequest();
        }
    }

    private void sendModifyPasswordRequest() {
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
        forgetPasswordRequest.setCode(verify);
        forgetPasswordRequest.setMobile(mobile);
        forgetPasswordRequest.setNewPassword(newPassword);
        RestClient.getVoterService().modifyPasswordPost(forgetPasswordRequest).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code()<APICode.SERVER_RESPONSE_CODE) {
                    if(response.body().getCode() == APICode.SUCCESS) {
                        VoteApplication.getInstance().exitApp();
                        VoteApplication.getInstance().goLoginActivity();
                        ToastUtil.showToast(getApplicationContext(),R.string.toast_modify_success);
                    } else if(response.body().getCode() == APICode.VerifyIsWrong) {
                        ToastUtil.showToast(ForgetPasswordActivity.this,R.string.toast_verify_is_wrong);
                    } else {
                        ToastUtil.showToast(ForgetPasswordActivity.this,R.string.toast_net_has_question);
                    }
                } else {
                    ToastUtil.showToast(ForgetPasswordActivity.this,R.string.toast_net_has_question);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                ToastUtil.showToast(ForgetPasswordActivity.this,R.string.toast_net_has_question);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }
}
