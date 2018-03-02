package base.zc.com.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.receiver.NotificationReceiver;
import base.zc.com.project.util.CartUtil;
import base.zc.com.project.util.NetStringCallback;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.ShareUtil;
import base.zc.com.project.util.UiUtil;

import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private ImageView app_icon_iv;
    /**
     * 请填写用户名
     */
    private EditText username_et;
    /**
     * 请输入密码
     */
    private EditText password_et;
    private ImageView password_eye_iv;
    /**
     * 登录
     */
    private TextView login_tv;
    /**
     * 忘记密码?
     */
    private TextView forget_passwrod_tv;
    private ImageView phone_del_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        //退出登录清除消息通知
        for (int i = 0; i < NotificationReceiver.messageIdLst.size(); i++) {
            Integer notifyId = NotificationReceiver.messageIdLst.get(i);
            JPushInterface.clearNotificationById(mContext, notifyId);
        }
        NotificationReceiver.messageIdLst.clear();

    }

    private void initView() {
        app_icon_iv = (ImageView) findViewById(R.id.app_icon_iv);
        username_et = (EditText) findViewById(R.id.username_et);
        password_et = (EditText) findViewById(R.id.password_et);
        password_eye_iv = (ImageView) findViewById(R.id.password_eye_iv);
        login_tv = (TextView) findViewById(R.id.login_tv);
        forget_passwrod_tv = (TextView) findViewById(R.id.forget_passwrod_tv);
        phone_del_iv = (ImageView) findViewById(R.id.phone_del_iv);
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        password_eye_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_et.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password_eye_iv.setImageResource(R.mipmap.paw_see_open);
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    password_eye_iv.setImageResource(R.mipmap.paw_see);
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                password_et.setSelection(password_et.getText().length());

            }
        });
        phone_del_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_et.setText("");
            }
        });
        username_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              if(s.length()>0){
                  phone_del_iv.setVisibility(View.VISIBLE);
              }else{
                  phone_del_iv.setVisibility(View.GONE);
              }
              setLoginBtnStyle();
            }
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setLoginBtnStyle();
            }
        });
        forget_passwrod_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,FindPasswordActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void setLoginBtnStyle() {
        String username = username_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        if(!TextUtils.isEmpty(username) &&!TextUtils.isEmpty(password)){
            login_tv.setBackgroundResource(R.drawable.update_btn);
        }else{
            login_tv.setBackgroundResource(R.drawable.login_btn_disable);
        }
    }

    private void login() {
        String username = username_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            UiUtil.showToast(mContext, "请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            UiUtil.showToast(mContext, "请输入密码");
            return;
        }

        if(!NetUtil.isConnect(mContext)){
            UiUtil.showToast(mContext, getString(R.string.net_disconnect));
            UiUtil.hideLoading(mContext);
            return ;
        }

        UiUtil.showLoading(mContext);
        HashMap<String, String> param = new HashMap<>();
        param.put("password", ""+password);
        param.put("username", ""+username);
        NetUtil.post(mContext, NetUtil.LOGIN, param).execute(new NetStringCallback(mContext) {

            @Override
            public void onSidInvalid() {
            }

            @Override
            public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {
                UiUtil.hideLoading(mContext);
                try {
                    JSONObject json = new JSONObject(s);
                    int code = json.optInt("code");
                    if (code == 200) {
                        JSONObject data = json.optJSONObject("data");
                        JSONObject userinfo = data.optJSONObject("userinfo");
                        String session = userinfo.optString("session_id");
                        CartUtil.saveSessionId(mContext,session);
                        ShareUtil.save(mContext, "loginInfo", data.toString());
                        Intent intent = new Intent(mContext,MainActivity.class);
                        startActivity(intent);
                        finishWithDefault();
                    }else{
                        UiUtil.showToast(mContext, json.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtil.showToast(mContext, getResources().getString(R.string.net_error));
                }
            }

            @Override
            public void onErrorWithSafe(Call call, Response response, Exception e) {
                UiUtil.hideLoading(mContext);
                UiUtil.showToast(mContext, getResources().getString(R.string.net_error));
            }
        });
    }
}
