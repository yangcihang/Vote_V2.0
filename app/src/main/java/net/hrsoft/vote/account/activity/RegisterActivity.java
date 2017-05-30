package net.hrsoft.vote.account.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Button;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.model.RegisterRequest;
import net.hrsoft.vote.account.model.VerifyRequest;
import net.hrsoft.vote.account.model.VerifyResponse;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.VerifyType;
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

public class RegisterActivity extends ToolbarActivity {

    @BindView(R.id.btn_register_send_verify)
    Button sendVerifyBtn;
    @BindView(R.id.edit_register_mobile)
    CleanableEditText mobileEdit;
    @BindView(R.id.edit_register_password)
    CleanableEditText passwordEdit;
    @BindView(R.id.edit_register_verify)
    CleanableEditText verifyEdit;
    @BindView(R.id.btn_register)
    Button registerBtn;

    private String mobile;
    private String password;
    private String verify;

    @OnClick(R.id.btn_register_send_verify)
    void onSendVerify() {
        mobile = mobileEdit.getText().toString().trim();
        if(mobile.isEmpty()) {
           ToastUtil.showToast(this,R.string.toast_mobile_is_empty);
        } else {
            if (!RegexUtil.isMobileNum(mobile)) {
                ToastUtil.showToast(RegisterActivity.this, R.string.toast_mobile_is_wrong);
            } else {
                VerifyRequest verifyRequest = new VerifyRequest();
                verifyRequest.setMobile(mobile);
                verifyRequest.setType(VerifyType.REGISTER_TYPE);
                RestClient.getVoterService().registerVerifyPost(verifyRequest).enqueue(new Callback<APIResponse<VerifyResponse>>() {
                    @Override
                    public void onResponse(Call<APIResponse<VerifyResponse>> call, Response<APIResponse<VerifyResponse>> response) {
                        if (response.code() < APICode.SERVER_RESPONSE_CODE) {
                            ToastUtil.showToast(RegisterActivity.this, "验证码为" + response.body().getData().getCode());
                        } else {
                            ToastUtil.showToast(RegisterActivity.this,R.string.toast_net_has_question);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<VerifyResponse>> call, Throwable t) {
                        ToastUtil.showToast(RegisterActivity.this, R.string.toast_net_has_question);
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
    @OnClick(R.id.btn_register)
    void onRegister() {
        mobile = mobileEdit.getText().toString().trim();
        password = passwordEdit.getText().toString().trim();
        verify = verifyEdit.getText().toString().trim();
        if(!RegexUtil.isMobileNum(mobile)) {
            ToastUtil.showToast(RegisterActivity.this,R.string.toast_mobile_is_wrong);
        } else if(password.length()>20||password.length()<6) {
            ToastUtil.showToast(RegisterActivity.this,R.string.toast_password_is_wrong);
        } else if(verify==null||verify.isEmpty()){
            ToastUtil.showToast(this,R.string.toast_not_input_verify);
        }else {
            sendRegisterRequest();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }
    @Override
    protected void init() {
        super.init();
        this.setActivityTitle(getString(R.string.title_all_activity));
    }
    private void sendRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setCode(verify);
        registerRequest.setMobile(mobile);
        registerRequest.setPassword(password);
        RestClient.getVoterService().registerPost(registerRequest).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code()<APICode.SERVER_RESPONSE_CODE) {
                    if(response.body().getCode()==APICode.SUCCESS) {
                        ToastUtil.showToast(RegisterActivity.this,R.string.toast_register_is_success);
                        goLogin();
                    } else {
                        ToastUtil.showToast(RegisterActivity.this,R.string.toast_verify_is_wrong);
                    }
                } else {
                    ToastUtil.showToast(RegisterActivity.this,R.string.toast_net_has_question);
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                ToastUtil.showToast(RegisterActivity.this,R.string.toast_net_has_question);
            }
        });
    }

    private void goLogin() {
        VoteApplication.getInstance().clearAllActivity();
        startActivity(new Intent(this,LoginActivity.class));
    }
}
