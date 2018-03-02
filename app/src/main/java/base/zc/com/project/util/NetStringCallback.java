package base.zc.com.project.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import base.zc.com.project.activity.BaseActivity;
import base.zc.com.project.activity.LoginActivity;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public abstract class NetStringCallback extends StringCallback {

    private Context context;
    private Fragment fragment;

    public NetStringCallback(Context context){
        this.context = context;
    }

    public NetStringCallback(Fragment fragment){
        this.context = fragment.getActivity();
        this.fragment = fragment;
    }

    @Override
    public void onSuccess(String s, Call call, Response response) {
        if(!needCallBack() || isActivityDestroyed())
            return;
        boolean isSessionOk = true;
        try {
            JSONObject json = new JSONObject(s);
            int code = json.optInt("code");
            String message = json.optString("message");
            //当前用户未登录，或者sessionId失效, 清空sessionId
            if(code == 403){
                CartUtil.clearSessionId(context);
                onSidInvalid();
                UiUtil.showToast(context, "登录异常请重新登录");
                UiUtil.hideLoading(context, true);
                isSessionOk = false;
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }

            if(code == 400 && !TextUtils.isEmpty(message) && message.indexOf("非法访问") != -1){
                json.put("message", "");
                onSuccess(json.toString(), call, response, isSessionOk);
                return;
            }
        } catch (JSONException e) {
//            e.printStackTrace();
        }
//        if(isSessionOk){
//            LogUtil.e("验证sessionId成功", "当前sessionId可使用，或者此次接口无需验证sessionId，当前sessionId为<" + CartUtil.getSessionId(context) + ">");
//        }else{
//            LogUtil.e("验证sessionId失败", "当前未登录，或者sessionId已失效");
//        }
        onSuccess(s, call, response, isSessionOk);

    }

    public abstract void onSuccess(String s, Call call, Response response, boolean isSessionOk);

    public void onSidInvalid(){};

    /**
     * 安全的错误回调，主要用于处理fragment中getActivity为null或者not attach to 的情况
     * @param call
     * @param response
     * @param e
     */
    public abstract void onErrorWithSafe(Call call, Response response, Exception e);

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if(needCallBack() && !isActivityDestroyed()){
            onErrorWithSafe(call, response, e);
        }
    }

    private boolean needCallBack(){
        if(context == null || (fragment != null && !fragment.isAdded())){
            return false;
        }
        return true;
    }

    private boolean isActivityDestroyed(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(context instanceof BaseActivity && ((BaseActivity) context).isDestroyed()){
                return true;
            }
        }
        return false;
    }

}
