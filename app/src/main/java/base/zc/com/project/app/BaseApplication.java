package base.zc.com.project.app;

import android.app.Application;
import android.util.Log;

import base.zc.com.project.R;
import base.zc.com.project.util.LogUtil;
import base.zc.com.project.util.NetUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initNet();
        initPush();

    }

    //初始化okgo
    private void initNet() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
//        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkGo.init(this);
        //以下都不是必须的，根据需要自行选择
        OkGo.getInstance()//
//                .debug("OkGo")                                              //是否打开调试
//                .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)               //全局的连接超时时间
//                .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
//                .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
//                //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
//                //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
//                .addCommonHeaders(headers)                                         //设置全局公共头
//                .addCommonParams(params);
                .setCertificates()
                .addCommonHeaders(new HttpHeaders(NetUtil.HEADER_CXY_KEY, NetUtil.HEADER_CXY_PARAM));
        ;

        if(LogUtil.isDebug){
            OkGo.getInstance().debug("OkGo");
        }

        NetUtil.setIsNeedEncrypt(true);

    }

    /**
     * 初始化极光推送
     */
    private void initPush() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
//        Set<String> set = new HashSet<>();
//        set.add("信息1");
//        set.add("魔兽2");
//        JPushInterface.setTags(this, set, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                LogUtil.e("返回", "gotResult: " + i);
//                if (i == 0) {
//                    for (String value : set) {
//                        LogUtil.e("tag值", "值: " + value);
//                    }
//                }
//            }
//        });
        String id = JPushInterface.getRegistrationID(this);
        Log.e("极光id", id + "***");
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.mipmap.app_icon;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        JPushInterface.setPushNotificationBuilder(1, builder);
//
        CustomPushNotificationBuilder builderCustom = new
                CustomPushNotificationBuilder(this,
                R.layout.custom_push,
                R.id.iv_icon,
                R.id.tv_title,
                R.id.tv_message);
        // 指定定制的 Notification Layout
        builderCustom.statusBarDrawable = R.mipmap.app_icon;
        // 指定最顶层状态栏小图标
        builderCustom.layoutIconDrawable = R.mipmap.app_icon ;
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(1, builderCustom);

    }

}
