package base.zc.com.project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import base.zc.com.project.R;
import base.zc.com.project.activity.MainActivity;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.UiUtil;
import cn.jpush.android.api.JPushInterface;

public class NotificationReceiver extends BroadcastReceiver {

    private final static String TAG = "极光消息接收";
    public static List<Integer> messageIdLst = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!context.getSharedPreferences("push", Context.MODE_PRIVATE).getBoolean("isPush", true)) {
            return;
        }
        Bundle bundle = intent.getExtras();
        try {
            Log.d("TAG", "onReceive - " + intent.getAction());
            Iterator<String> iterator = bundle.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = bundle.getString(key);
                Log.e("NotificationReceiver", "NotificationReceiver.onReceive123123***" + key + "," + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String id = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            LogUtil.e(TAG,  "JPush用户注册成功" + id);
//            UiUtil.showToast(context, "JPush用户注册成功--" + id);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            showNotification(context, bundle, intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            showNotification(context, bundle, intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            processCustomMessage(context, bundle, intent);
        } else {
//            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void showNotification(Context context, Bundle bundle, Intent intent) {
        intent.setClass(context, NotificationReceiver.class);

        boolean needShowNotification = true;
        //普通通知
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);

        if (!TextUtils.isEmpty(message)) //此时极光一定会弹一个通知，这边就不弹自定义消息了
            return;
        //其他情况下，如果附加字段有title和message，直接赋值显示
        String extraData = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject json = new JSONObject(extraData);
            if (json.keys().hasNext()) { //附加字段
                title = json.optString("title");
                message = json.optString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //自定义通知
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            title = bundle.getString(JPushInterface.EXTRA_TITLE);
            message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        }
        if (!TextUtils.isEmpty(message) && TextUtils.isEmpty(title)) {
            title = context.getResources().getString(R.string.app_name);
        }
        if (TextUtils.isEmpty(message) || TextUtils.isEmpty(title)) {
            needShowNotification = false;
        }

        if (needShowNotification) {
            intent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
            UiUtil.showCustomNotification(context, 2, intent, title, message);
        }


    }

    /**
     * 处理自定义消息
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle, Intent intent) {
        String extraData = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String pName = intent.getComponent().getPackageName();
        intent.setClassName(pName, MainActivity.class.getName());
        intent.putExtra("tab", 0);
        try {
            JSONObject json = new JSONObject(extraData);
            if (json.keys().hasNext()) {
                String title = json.optString("title");
                String message = json.optString("message");
                String type = json.optString("type");
                if("lowStock".equals(type)){
                    intent.putExtra("tab", 0);
                }else if("pickingList".equals(type)){
                    intent.putExtra("tab", 1);
                }else if("settleBill".equals(type)){
                    intent.putExtra("tab", 3);
//                    EventBus.getDefault().post(new StatementsChooseEvent("0"));
                }else if(json.has("url")){
                    String url = json.optString("url");
                    intent = NetUtil.getIntentWithUrl(pName, url);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

//                if (json.has("goods_id")) {
//                    int goods_id = json.optInt("goods_id");
//                    intent.setClassName(pName, GoodsDetailActivity.class.getName());
//                    intent.putExtra("goods_id", goods_id);
//                } else if (json.has("cart_id")) {
//                    String cate_id = json.optString("cart_id");
//                    if (!TextUtils.isEmpty(cate_id)) {
//                        intent.setClassName(pName, MainActivity.class.getName());
//                        intent.putExtra("tab", 1);
//                        intent.putExtra("cate_id", cate_id);
//                    }
//                } else if (json.has("refund_id")) {
//                    String refund_id = json.optString("refund_id");
//                    if (!TextUtils.isEmpty(refund_id)) {
//                        intent.setClassName(pName, RefundActivity.class.getName());
//                    }
//                } else if (json.has("todayorder")) {
//                    String todayorder = json.optString("todayorder");
//                    if (!TextUtils.isEmpty(todayorder)) {
//                        intent.setClassName(pName, TodayOrderActivity.class.getName());
//                    }
//                }

            }else{
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送点击统计
        JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));
//        Intent newIntent = new Intent(pName, MainActivity.class);
//        newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        newIntent.setAction(Intent.ACTION_MAIN);
//        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        pName.startActivity(newIntent);
    }

}
