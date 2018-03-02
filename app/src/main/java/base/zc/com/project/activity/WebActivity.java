package base.zc.com.project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.bean.LoginEvent;
import base.zc.com.project.util.CartUtil;
import base.zc.com.project.util.MyWVJBWebView;
import base.zc.com.project.util.NetStringCallback;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.UiUtil;
import base.zc.com.project.view.WebViewProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

//与Wap端交互
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    private static final String TAG = "WebViewJavascriptBridge";
    private MyWVJBWebView webView;
    private ImageView iv_back;
    private TextView tv_title;
    private String url = "";
    private TextView tv_menu;
    private List<JSONObject> addresses;
    private LinearLayout ll_right;
    private WebViewProgressBar webViewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        EventBus.getDefault().register(this);
        webView = (MyWVJBWebView) findViewById(R.id.webView);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_menu = ((TextView) findViewById(R.id.tv_menu));
        ll_right = ((LinearLayout) findViewById(R.id.ll_right));
        webViewProgressBar = ((WebViewProgressBar) findViewById(R.id.webViewProgressBar));
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSavePassword(false);
        settings.setUserAgentString(settings.getUserAgentString() + "/-app_floruit_android");
        //webView.setWebViewClient(new CustomWebViewClient(webView));
        webView.registerHandler("WebParameters", new MyWVJBWebView.WVJBHandler() {

            @Override
            public void request(Object data, MyWVJBWebView.WVJBResponseCallback callback) {
                getData(data.toString());

            }
        });
        webView.registerHandler("Call", new MyWVJBWebView.WVJBHandler() {
            @Override
            public void request(Object data, MyWVJBWebView.WVJBResponseCallback callback) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    final String phone = jsonObject.optString("phone");
                    String[] buttonNames = {"取消", "确认"};
                    View.OnClickListener exitListenter = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    };
                    View.OnClickListener confirmlListenter = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + phone);
                            intent.setData(data);
                            startActivity(intent);
                        }
                    };
                    View.OnClickListener[] onClickListeners = {exitListenter, confirmlListenter};
                    UiUtil.showButtonMessage(WebActivity.this, "提示", "是否拨打" + phone+"?", null, new int[]{1, 1}, buttonNames, onClickListeners);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        webView.registerHandler("YSFChat", new MyWVJBWebView.WVJBHandler() {

            @Override
            public void request(Object data, MyWVJBWebView.WVJBResponseCallback callback) {

//                chat(data.toString());
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webViewProgressBar.setProgress(newProgress/100f);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
            }

        });
        webView.registerHandler("StartActivity", new MyWVJBWebView.WVJBHandler() {

            @Override
            public void request(Object data, MyWVJBWebView.WVJBResponseCallback callback) {
                openNewPage(data.toString());
            }
        });

        if(!TextUtils.isEmpty(url)){
            if(url.startsWith("http")){
                webView.loadUrl(url);
            }else{
                webView.loadUrl(NetUtil.WEB_HOST+url);
            }

        }
        tv_menu.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void openNewPage(String text) {
        Log.e("openNewPage", "" + text);
        try {

            JSONObject jsonObject = new JSONObject(text);
            if (jsonObject.has("page")) {
                String page = jsonObject.optString("page");
                if ("goBack".equals(page)) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }

                } else if ("1".equals(page) || "0".equals(page)) {
                    WebActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                        }
                    });
                }
            } else {
                String url = jsonObject.optString("url");
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                Intent intentWithUrl = NetUtil.getIntentWithUrl(mContext, url);
                startActivity(intentWithUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String text) {
        try {
            UiUtil.showLoading(mContext);
            JSONObject jsonObject = new JSONObject(text);
            final String api = jsonObject.optString("api");
            String method = jsonObject.optString("method");
            final JSONObject json = jsonObject.optJSONObject("data");
            Map<String, String> paramz = new HashMap<>();
            if (json != null) {
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    paramz.put(key, json.optString(key));
                }
            }
            final String callBackName = jsonObject.optString("callBackName");
            if ("get".equals(method)) {
                NetUtil.get(mContext, api.substring(1), paramz)
                        .execute(new NetStringCallback(mContext) {
                            @Override
                            public void onSidInvalid() {
                                super.onSidInvalid();
                                hideLoading();
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int code = jsonObject.optInt("code");
                                    String message = jsonObject.optString("message");
                                    String data = jsonObject.optString("data");
                                    String text = null;
                                    if (code == 200) {
                                        text = "{\"err\":false,\"data\":" + data + "}";
                                        webView.callHandler(callBackName, text, new MyWVJBWebView.WVJBResponseCallback() {
                                            @Override
                                            public void callback(Object data) {

                                            }
                                        });
                                        if ("/app/address/address_list".equals(api)) {
                                            addresses = NetUtil.getJsonObjectList(jsonObject.optJSONArray("data"));

                                        }

                                    } else {
                                        text = "{\"err\":true,\"data\":" + data + "}";
                                        webView.callHandler(callBackName, text, new MyWVJBWebView.WVJBResponseCallback() {

                                            @Override
                                            public void callback(Object data) {
                                            }
                                        });
                                        hideLoading();
                                        UiUtil.showToast(mContext, jsonObject.getString("message"));
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onErrorWithSafe(Call call, Response response, Exception e) {
                                hideLoading();
                                UiUtil.showToast(mContext, getString(R.string.net_error));
                            }
                        });
            } else {
                NetUtil.post(mContext, api.substring(1), paramz)
                        .execute(new NetStringCallback(mContext) {
                            @Override
                            public void onSidInvalid() {
                                super.onSidInvalid();
                                hideLoading();
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {

                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int code = jsonObject.optInt("code");
                                    String message = jsonObject.optString("message");
                                    String data = jsonObject.optString("data");
                                    String text = null;
                                    if (code == 200) {
                                        text = "{\"err\":false,\"data\":" + data + "}";
                                        webView.callHandler(callBackName, text, new MyWVJBWebView.WVJBResponseCallback() {

                                            @Override
                                            public void callback(Object data) {
                                            }
                                        });
                                    }else {
                                        text = "{\"err\":true,\"data\":" + data + "}";
                                        webView.callHandler(callBackName, text, new MyWVJBWebView.WVJBResponseCallback() {

                                            @Override
                                            public void callback(Object data) {
                                            }
                                        });
                                        hideLoading();
                                        UiUtil.showToast(mContext, jsonObject.getString("message"));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorWithSafe(Call call, Response response, Exception e) {
                                hideLoading();
                                UiUtil.showToast(mContext, getString(R.string.net_error));
                            }
                        });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void hideLoading() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UiUtil.hideLoading(mContext);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        if (!TextUtils.isEmpty(CartUtil.getSessionId(mContext))) {
            if(!TextUtils.isEmpty(url)){
                if(url.startsWith("http")){
                    webView.loadUrl(url);
                }else{
                    webView.loadUrl(NetUtil.WEB_HOST+url);
                }

            }
        }
    }
}
