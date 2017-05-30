package net.hrsoft.vote.account.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import net.hrsoft.vote.R;
import net.hrsoft.vote.VoteApplication;
import net.hrsoft.vote.account.model.LoginRequest;
import net.hrsoft.vote.account.model.LoginResponse;
import net.hrsoft.vote.account.model.User;
import net.hrsoft.vote.common.ToolbarActivity;
import net.hrsoft.vote.constant.CacheKey;
import net.hrsoft.vote.home.activity.HomeActivity;
import net.hrsoft.vote.network.APICode;
import net.hrsoft.vote.network.APIResponse;
import net.hrsoft.vote.network.RestClient;
import net.hrsoft.vote.util.ToastUtil;
import net.hrsoft.vote.widget.CleanableEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ToolbarActivity {

    @BindView(R.id.txt_forget_password)
    TextView forgetPasswordTxt;
    @BindView(R.id.edit_mobile)
    CleanableEditText mobileEdit;
    @BindView(R.id.edit_password)
    CleanableEditText passwordEdit;
    @BindView(R.id.btn_login)
    Button loginBtn;
    @BindView(R.id.btn_register)
    Button registerBtn;

    private LoginRequest loginRequest ;

    @OnClick(R.id.txt_forget_password)
    void onForgetPassword() {
        startActivity(new Intent(this,ForgetPasswordActivity.class));
    }

    /**
     * 到注册页面
     */
    @OnClick(R.id.btn_register)
    void onRegister() {
        startActivity(new Intent(this,RegisterActivity.class));
    }


    @OnClick(R.id.btn_login)
    void onLogin() {
        String mobile = mobileEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        loginRequest.setPassword(password);
        loginRequest.setMobile(mobile);
        if(mobile.isEmpty()||password.isEmpty()) {
            ToastUtil.showToast(this,R.string.toast_mobile_password_is_empty);
         } else {
            sendLoginRequest();
        }
       
    }

    /**
     * 登录请求
     */
    private void sendLoginRequest() {
        RestClient.getVoterService().loginPost(loginRequest).enqueue(new Callback<APIResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<LoginResponse>> call, Response<APIResponse<LoginResponse>> response) {
                if(response.code()< APICode.SERVER_RESPONSE_CODE) {
                    if(response.body().getCode()==APICode.UserNotExit) {
                        ToastUtil.showToast(LoginActivity.this,R.string.toast_user_not_exit);
                    } else if(response.body().getCode() == APICode.SUCCESS){
                        loginSuccess(response.body().getData().getUser(),response.body().getData().getToken());
                    } else if(response.body().getCode() == APICode.PasswordError) {
                        ToastUtil.showToast(LoginActivity.this,R.string.toast_password_error);
                    }
                } else {
                    ToastUtil.showToast(LoginActivity.this,R.string.toast_net_has_question);
                }
            }
            @Override
            public void onFailure(Call<APIResponse<LoginResponse>> call, Throwable t) {
                ToastUtil.showToast(LoginActivity.this,R.string.toast_net_has_question);
            }
        });
    }

    private void loginSuccess(User user,String token) {
        VoteApplication.getCache().putString(CacheKey.TOKEN,token);
        VoteApplication.getCache().putSerializableObj(CacheKey.USER,user);
        VoteApplication.getInstance().clearAllActivity();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    protected void init() {
        super.init();
        loginRequest = new LoginRequest();
        this.setActivityTitle(getString(R.string.title_all_activity));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
