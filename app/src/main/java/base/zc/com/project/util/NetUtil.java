package base.zc.com.project.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.activity.BaseActivity;
import base.zc.com.project.activity.MainActivity;
import base.zc.com.project.activity.WebActivity;
import base.zc.com.project.bean.DeviceInfo;
import base.zc.com.project.view.ProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

public class NetUtil {



    public static boolean isNeedEncrypt = true; // 是否是加密请求地址 false：请求地址不加密参数请求；true：请求地址加密参数请求 注意对应host地址

    /**
     * 设置请求是否需要加密
     * @param isNeedEncrypt_
     */
    public static void setIsNeedEncrypt(boolean isNeedEncrypt_){
        isNeedEncrypt = isNeedEncrypt_;
        if(isNeedEncrypt){
             HOST = "https://api.cuixianyuan.com/";
             HOST = "https://biz.cuixianyuan.com/";
//             HOST = "http://172.28.22.99:8019/";
//             HOST = "http://172.28.22.103:10010/";
//            HOST="http://172.28.22.102:8019/";
//            HOST="https://test.cuixianyuan.com/";
//            HOST = "http://218.241.151.237:8084/";
//               WEB_HOST="http://cxy.tunnel.2bdata.com/#/";
//            WEB_HOST="http://218.241.151.238:18082/#/";
            WEB_HOST="https://m.cuixianyuan.com/#/";
//            WEB_HOST="http://218.241.151.238:8082/#/";
//            WEB_HOST="http://172.28.22.99:9999/#/";
        }else{
//           HOST = "http://192.168.31.152:8019/";
            HOST = "https://test.cuixianyuan.com/";
            HOST = "http://172.28.22.103:10010/";
        }
    }

    /**
     * 接口host地址
     */
    public static String HOST = "https://api.cuixianyuan.com/";
//    private static String HOST = "https://devapi.cuixianyuan.com/";base
//    private static String HOST = "http://test.cuixianyuan.com/";
//    private static final String HOST = "http://192.168.31.146:8019/";
//    public static final String WEB_HOST="http://cxy.tunnel.2bdata.com/#/";
    public static String WEB_HOST="http://218.241.151.238:18082/#/";

    public static final String HEADER_CXY_KEY = "floruit";

    public static final String HEADER_CXY_PARAM = "df043ade68528c069c92ff182f9c9e51";

    /**
     * 登录
     */
    public static final String LOGIN = "app/login/login";

    /**
     * 商品列表
     */
    public static final String GOODS_LIST = "app/goods/goods_list";

    /**
     * 关键字搜索商品
     */
    public static final String GOODS_SEARCH = "app/goods/goods_list";

    /**
     * 发送短信验证码，用于找回密码
     */
     public static final String SEND_CODE="app/user/send_code";
    /**
     * 找回密码
     */
    public static final String MODIFY_PASSWORD="app/user/modify_password";

    /**
     * 个人中心首页
     */
      public static final String MEMBER_CENTER_INFO="app/ucenter/index";

    /**
     * 退出登录
     */
    public static final String LOGINOUT="app/login/logout";

    /**
     * 获得版本升级信息
     */
//    public static final String VERSION_UPDATE = "version/";
    public static final String VERSION_UPDATE = "https://api.cuixianyuan.com/version/"; //http://218.241.151.237:8080

    /************************************请求所需值************************************/
    /**
     * 请求接口的固定appid
     */
    public static final String CXY_APPID = "20003";

    /**
     * 请求接口固定秘钥
     */
    public static final String CXY_APP_KEY = "8a5cbffe32b7d34d7a88b80c2a09e586";

    /**
     * 时间戳
     */
    public static long TIME_STAMP = 0;

    /**
     * 文件下载路径
     */
    public static final String FILE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/floruit/cxy";

    /**
     * 上传图片地址
     */
    public static final String UPLOAD_FILE = "app/goods/upload_image";

    /**
     * 修改密码
     */
    public static final String LOGIN_PASSWORD_CHANGE="app/ucenter/edit_password";

    /**
     * 获取添加商品时信息
     */
    public static final String GOODS_ADD="app/goods/goods_add";

    /**
     * 获取编辑商品时信息
     */
    public static final String GOODS_EDIT="app/goods/goods_edit";

    /**
     * 提交商品信息
     */
    public static final String ADD_SAVE_GOODS="app/goods/add_save_goods";

    /**
     * 编辑商品信息
     */
    public static final String EDIT_SAVE_GOODS="app/goods/edit_save_goods";

    /**
     * 商品分类级别信息
     */
    public static final String GOODS_CLASS="app/goods/goods_class";

    /**
     * 商品上架
     */
    public static final String GOODS_SHOW="app/goods/goods_show";

    /**
     * 商品下架
     */
    public static final String GOODS_UNSHOW="app/goods/goods_unshow";

    /**
     * 商品列表
     */
    public static final String STORE_GOODS="app/goods/store_goods";


    public final static String TAG = "NetUtil";
    /**
     * 结算单列表
     */
    public static final String BILL_LIST ="app/settle/bill_list" ;
    /**
     * 结算单详情
     */
    public static final String BILL_DETAIL ="app/settle/bill_detail" ;
    /**
     * 结算单确认
     */
    public static final String BILL_CONFIRM ="app/settle/bill_confirm" ;
    /**
     * 备货单列表
     */
    public static final String PICKING_LIST = "app/pickup/picking_list";
    /**
     * 退货信息
     */
    public static final String REFUND_INFO = "app/refund/refund_stat";
    /**
     * 钱包列表
     */
    public static final String WALLET_LIST = "app/wallet/list";
    /**
     * 提现详情
     */
    public static final String WITHDRAW_DETAIL = "app/wallet/detail";
    /**
     * 提现
     */
    public static final String WITHDRAW = "app/wallet/withdraw";
    /**
     * 商品列表修改价格
     */
    public static final String PRICE_EDIT = "app/goods/price_edit";
    /**
     * 商品列表修改库存
     */
    public static final String STORAGE_EDIT = "app/goods/storage_edit";
    /**
     * 商品删除
     */
    public static final String GOODS_DELETE = "app/goods/goods_delete";
    /**
     * 封装成加密请求
     * @param url 请求地址
     * @param getParams  url中所带请求参数
     */
    public static GetRequest get(Context context, String url, Map<String, String> getParams){

        if(url != null && !url.startsWith("http")){
            url = HOST + url;
        }

        if(getParams == null)
            getParams = new HashMap<>();

        if(!isNeedEncrypt && !url.contains("api.cuixianyuan.com")){
            getParams.put("sid", CartUtil.getSessionId(context));
            return OkGo.get(url).params(getParams).tag(context);
        }

        LogUtil.e(TAG, "请求方式GET");
        LogUtil.e(TAG, "请求地址" + url);
        LogUtil.e(TAG, "请求实际get参数" + getParams.toString());

        long currentTime = System.currentTimeMillis() / 1000;
        long timestamp = currentTime + TIME_STAMP;

        String atom = getAtom(context, getParams, timestamp);
        String sign = getSign(context, getParams, timestamp);

        GetRequest request = OkGo.get(url + "?app_id=" + CXY_APPID + "&atom=" + atom + "&sign=" + sign)
                .tag(context);
        return request;

    }

    public static GetRequest get(Context context, String url){
        return get(context, url, null);
    }

    /**
     * 封装成加密请求
     * @param url 请求地址
     * @param getParams url中所带请求参数
     * @param bodyParams body请求参数
     */
    public static PostRequest post(Context context, String url, Map<String, String> getParams, Map<String, String> bodyParams){

        if(url != null && !url.startsWith("http")){
            url = HOST + url;
        }

        if(getParams == null)
            getParams = new HashMap<>();
        if(bodyParams == null)
            bodyParams = new HashMap<>();

        if(!isNeedEncrypt){
            return OkGo.post(url + "?sid=" + CartUtil.getSessionId(context)).params(bodyParams).tag(context);
        }

        LogUtil.e(TAG, "请求方式post");
        LogUtil.e(TAG, "请求地址" + url);
        LogUtil.e(TAG, "请求实际get参数" + getParams.toString());
        LogUtil.e(TAG, "请求实际post参数" + bodyParams.toString());

        long currentTime = System.currentTimeMillis() / 1000;
        long timestamp = currentTime + TIME_STAMP;

        String atom = getAtom(context, getParams, timestamp);

        Map<String, String> realBodyMap = new HashMap<>();
        if(bodyParams.size() > 0){
            String postData = getAtomWithNoCommonParam(context, bodyParams);
            realBodyMap.put("postdata", postData);
        }

        String sign = getSign(context, bodyParams, timestamp);

        PostRequest request = OkGo.post(url +"?app_id=" + CXY_APPID + "&atom=" + atom + "&sign=" + sign)
                .tag(context);
        return request.params(realBodyMap);
    }

    public static PostRequest post(Context context, String url, Map<String, String> bodyParams){
        return post(context, url, null, bodyParams);
    }

    public static PostRequest post(Context context, String url){
        return post(context, url, null, null);
    }

    public static void cancelTag(Object tag){
        OkGo.getInstance().cancelTag(tag);
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if(!isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId.toString();
    }

    /**
     * 检查网络是否可用
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("error", e.toString());
        }
        return false;
    }

    /**
     * 获取渠道信息
     * @param context
     * @return
     */
    public static String getMetaData(Context context) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get("CXY_CHANNEL");
            if (value != null) {
                return value.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回当前程序versionName
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        int versioncode;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtil.e("VersionInfo", "Exception");
        }
        return versionName;
    }
    /**
     * 返回当前程序versionCode
     */
    public static int getAppVersionCode(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            LogUtil.e("VersionInfo", "Exception");
        }
        return versioncode;
    }

    /**
     * 不用加常规参数，此方法为postdata加密时需要参数
     * @param context
     * @param param
     * @return
     */
    public static String getAtomWithNoCommonParam(Context context, Map<String, String> param){
        String atom = "";
        if(param == null){
            param = new HashMap<>();
        }

        String symbol = "";
        for(Map.Entry<String, String>  entry : param.entrySet()){
            if(TextUtils.isEmpty(atom)){
                symbol = "";
            }else{
                symbol = "&";
            }
            atom += symbol + entry.getKey() + "=" + entry.getValue();
        }

        try {
            atom = Base64.encodeToString(atom.getBytes("UTF-8"), Base64.NO_WRAP);
//            atom = org.apache.commons.codec.binary.Base64.encodeBase64String(atom.getBytes("UTF-8"));
            //post体的数据在传入okhttpUtil后会自行urlEncode一次，这里先去掉
//            atom = URLEncoder.encode(atom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            atom = "";
        }

        return atom;
    }

    /**
     * 获取请求时的atom
     * @param param
     * @return
     */
    public static String getAtom(Context context, Map<String, String> param, long timestamp){

        String atom = "";
        if(param == null){
            param = new HashMap<>();
        }
        param.put("app_id", CXY_APPID);
        param.put("cv", "CXYSJ_" + getAppVersionName(context) + "_A_CN");
        param.put("sid", context.getSharedPreferences("login", Context.MODE_PRIVATE).getString("session", ""));
        param.put("timestamp", timestamp + "");
        param.put("channel_id", getMetaData(context));
        DeviceInfo deviceInfo = new DeviceInfo(context);
        param.put("device_id", deviceInfo.deveiceId);
        param.put("registration_id", JPushInterface.getRegistrationID(context));

        String symbol = "";
        for(Map.Entry<String, String>  entry : param.entrySet()){
            if(TextUtils.isEmpty(atom)){
                symbol = "";
            }else{
                symbol = "&";
            }
            atom += symbol + entry.getKey() + "=" + entry.getValue();
        }

        LogUtil.e("atom", atom);

        try {
            atom = Base64.encodeToString(atom.getBytes("UTF-8"), Base64.NO_WRAP);
//            atom = org.apache.commons.codec.binary.Base64.encodeBase64String(atom.getBytes("UTF-8"));
            atom = URLEncoder.encode(atom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            atom = "";
        }

        return atom;
    }

    /**
     * 获取请求时的签名
     * @param param
     * @return
     */
    public static String getSign(Context context, Map<String, String> param, long timestamp){

        Map<String, String> newMap = new HashMap<>();

        if(param != null){
            for (Map.Entry<String, String> entry : param.entrySet()) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }

        newMap.put("app_id", CXY_APPID);
        newMap.put("cv", "CXYSJ_" + getAppVersionName(context) + "_A_CN");
        newMap.put("sid", context.getSharedPreferences("login", Context.MODE_PRIVATE ).getString("session", ""));
        newMap.put("timestamp", timestamp + "");
        newMap.put("channel_id", getMetaData(context));
        DeviceInfo deviceInfo = new DeviceInfo(context);
        newMap.put("device_id", deviceInfo.deveiceId);
        newMap.put("registration_id", JPushInterface.getRegistrationID(context));


        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            sortList.add(entry.getKey());
        }
        Collections.sort(sortList);

        String signStr = "";

        for (int i = 0; i < sortList.size(); i++) {
            String paramKey = sortList.get(i);
            String paramValue = newMap.get(paramKey);
            signStr += paramKey  + paramValue;
        }

        signStr += CXY_APP_KEY;
        LogUtil.e("signStr2", signStr);
        signStr = Md5Util.getInstance().md5Digest(signStr);
        return signStr;
    }

    public interface DownLoadCallBack{
        /**
         * 下载回调
         * @param current  已下载字节数
         * @param total  文件总共字节数
         * @param percent 下载百分比
         * @param downLoadFile 下载的本地文件对象，在percent=1时为下载完成
         */
        public void callBack(int current, int total, float percent, File downLoadFile);

        /**
         * 下载失败
         */
        public void error(String message);
    }

    private static Handler downLoadHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Map<String, Object> downloadMap = (Map<String, Object>) msg.obj;
            DownLoadCallBack callback = (DownLoadCallBack) downloadMap.get("callback");

            switch (msg.what){
                case 0:
                    int current = (int) downloadMap.get("current");
                    int total = (int) downloadMap.get("total");
                    File file = (File) downloadMap.get("file");
                    float percent = (float) downloadMap.get("percent");
                    callback.callBack(current, total, percent, file);
                    break;
                case 1:
                    String message = (String) downloadMap.get("message");
                    callback.error(message);
                    break;
            }

        }
    };


    /**
     * 下载文件
     * @param _urlStr  文件地址
     * @param toFilePath  下载到本地的文件夹路径(如：/sdcard/cxy_apk)
     * @param toFileName  下载到本地的文件名(如：floruit_cxy_1.0.0.apk)
     * @param callback  下载回调
     */
    public static void downLoadFile(final String _urlStr, final String toFilePath, final String toFileName, @NonNull final DownLoadCallBack callback){

        new Thread(){
            @Override
            public void run() {
                super.run();

                File f = new File(toFilePath);
                if(!f.exists()){
                    f.mkdirs();
                }
                File file = new File(f, toFileName);
                //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
                if(file.exists()){
                    file.delete();
                }
                try {
                    // 构造URL
                    URL url = new URL(_urlStr);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    //获得文件的长度
                    int contentLength = con.getContentLength();
                    System.out.println("长度 :"+contentLength);
                    if(contentLength == -1){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("message", "升级失败，升级文件无法获取");
                        map.put("callback", callback);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = map;
                        downLoadHandler.sendMessage(message);
                        return;
                    }
                    // 输入流
                    InputStream is = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流
                    OutputStream os = new FileOutputStream(file);
                    int currentLength = 0;
                    Map<String, Object> map = new HashMap<String, Object>();
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                        currentLength += len;
                        map.put("current", currentLength);
                        map.put("total", contentLength);
                        float percent = (currentLength + 0f) / contentLength;
                        map.put("percent", percent);
                        map.put("file", file);
                        map.put("callback", callback);
                        Message message = new Message();
                        message.what = 0;
                        message.obj = map;
                        downLoadHandler.sendMessage(message);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("message", "升级失败，升级文件读写异常");
                    map.put("callback", callback);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = map;
                    downLoadHandler.sendMessage(message);
                }
            }
        }.start();

    }

    /**
     * 上传图片
     * @param toUploadBm 要上传的图片对象
     * @param filePath 图片要保存的位置
     * @param fileName 图片名称
     * @return
     */
    public static void uploadBitmap(Context context, Bitmap toUploadBm, String filePath, String fileName, StringCallback calback){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            toUploadBm.compress(Bitmap.CompressFormat.PNG, 100, baos);

            File f = new File(filePath);
            if(!f.exists()){
                f.mkdirs();
            }
            File file = new File(f, fileName);
            if(file.exists()){
                file.delete();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());

            if(calback != null){
                NetUtil.post(context, UPLOAD_FILE )
                        .params("upload_image", file)
                        .execute(calback);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void uploadFile(Context context, File file, StringCallback callback){
        NetUtil.post(context, UPLOAD_FILE )
                .params("upload_image", file).execute(callback);
    }
    public static void uploadBitmap(Context context, Bitmap toUploadBm, String fileName, StringCallback calback){
        uploadBitmap(context, toUploadBm, FILE_DOWNLOAD_PATH, fileName, calback);
    }

    public static void uploadBitmap(Context context, Bitmap toUploadBm, StringCallback calback){
        uploadBitmap(context, toUploadBm, FILE_DOWNLOAD_PATH, new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".png", calback);
    }

    /**
     * 打开浏览器访问网页
     * @param context
     * @param url 访问地址
     */
    public static void openBrowser(Context context, String url){
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    public interface DownLoadApkListener{
        public void onDownLoadSuccess(boolean isLastedVersion);
        public void onDownLoadError();
    }

    public static void updateCheckAndDownloadNewVersionApk(final Context context, int delayShowTime){
        updateCheckAndDownloadNewVersionApk(context, delayShowTime, null);
    }

    /**
     * 检查更新并弹框提示下载安装
     * @param context
     */
    public static void updateCheckAndDownloadNewVersionApk(final Context context, final int delayShowTime, final DownLoadApkListener listener){

        if (!NetUtil.isConnect(context)) {
            return;
        }

        final int versionCode = NetUtil.getAppVersionCode(context);
        final String versionName = NetUtil.getAppVersionName(context);
        NetUtil.get(context, NetUtil.VERSION_UPDATE).tag(context).execute(new NetStringCallback(context) {

            @Override
            public void onErrorWithSafe(Call call, Response response, Exception e) {
//                UiUtil.hideLoading(context);
            }

            @Override
            public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {
//                UiUtil.hideLoading(context);
                try {
                    JSONObject json = new JSONObject(s);
                    int code = json.optInt("code");
                    if(code == 200){
                        json = json.optJSONObject("data");
                        //赋值时间戳, 后面所有请求都需要此值
                        long server_timestamp = json.optLong("server_timestamp");
                        long currentTime = System.currentTimeMillis() / 1000;
                        NetUtil.TIME_STAMP = server_timestamp - currentTime;
                        LogUtil.e("时间戳home", "" + NetUtil.TIME_STAMP );
                        JSONObject update = json.optJSONObject("update");
                        int serverVersionCode = update.optInt("version");
                        final String serverVersionName = update.optString("version_code");
                        int forceUpdate = update.optInt("force_update", 0);
                        final String downLoadUrl = update.optString("download_url");
                        if(versionCode < serverVersionCode && !TextUtils.isEmpty(downLoadUrl)){

                            final View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
                            final LinearLayout ll_update_info = (LinearLayout) view.findViewById(R.id.ll_update_info);
                            String updateInfo = update.optString("change_log");
                            ll_update_info.removeAllViews();
                            JSONArray tips = new JSONArray(updateInfo);
                            for (int i = 0; i < tips.length(); i++) {
                                TextView tv_tip = new TextView(context);
//                                tv_tip.setText((i + 1) + "、" + tips.optString(i));
                                tv_tip.setText(tips.optString(i));
                                tv_tip.setTextSize(13);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                if(i > 0){
                                    lp.setMargins(0, DisplayUtil.dip2px(context, 7), 0, 0);
                                }
                                ll_update_info.addView(tv_tip, lp);
                            }
                            FrameLayout fl_close = (FrameLayout) view.findViewById(R.id.fl_close);
                            if(forceUpdate == 1){
                                fl_close.setVisibility(View.INVISIBLE);
                            }else{
                                fl_close.setVisibility(View.VISIBLE);
                                fl_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UiUtil.closeCustomMessage(context);
                                        OkGo.getInstance().cancelTag(context);
                                    }
                                });
                            }
                            final TextView tv_download_progress = (TextView) view.findViewById(R.id.tv_download_progress);
                            final ProgressBar pb_download_progress = (ProgressBar) view.findViewById(R.id.pb_download_progress);
                            ProgressBar  pb_download_back = (ProgressBar) view.findViewById(R.id.pb_download_back);
                            pb_download_back.setColor(context.getResources().getColor(R.color.colorLightGray));
                            pb_download_back.setProgress(1);
                            Button btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
                            TextView tv_update_title = (TextView) view.findViewById(R.id.tv_update_title);
                            final FrameLayout rl_progress = (FrameLayout) view.findViewById(R.id.fl_progress);
                            tv_update_title.setText(context.getString(R.string.app_name)+"V" + serverVersionName + "版本上线");
                            final RelativeLayout rl_update = (RelativeLayout) view.findViewById(R.id.rl_update);
                            btn_update_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        v.setVisibility(View.GONE);
                                        rl_progress.setVisibility(View.VISIBLE);
                                        OkGo.get(downLoadUrl).tag(context).execute(new FileCallback(NetUtil.FILE_DOWNLOAD_PATH, "floruit_seller_" + serverVersionName + ".apk") {
                                            @Override
                                            public void onSuccess(File file, Call call, Response response) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        UiUtil.closeCustomMessage(context);
                                                    }
                                                }, 1000);
                                                Intent intent = new Intent();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.setAction(Intent.ACTION_VIEW);
                                                Uri uri = null;
                                                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
                                                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    uri = FileProvider.getUriForFile(context, "base.zc.com.project.fileprovider", file);
                                                }else{
                                                    uri = Uri.fromFile(file);
                                                }
                                                intent.setDataAndType(uri,
                                                        "application/vnd.android.package-archive");
                                                if(context instanceof BaseActivity)
                                                    ((BaseActivity)context).startActivityWithDefault(intent);
                                            }

                                            @Override
                                            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                                super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                                                tv_download_progress.setText("正在下载... " + (int) (progress * 100) + "%");
                                                pb_download_progress.setProgress(progress);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        UiUtil.showToast(context, "下载失败，请稍后重试");
                                    }
                                }
                            });

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    UiUtil.showCustomMessage(context, view, rl_update);
                                }
                            }, delayShowTime);

                            if(listener != null){
                                listener.onDownLoadSuccess(false);
                            }

                        }else{
                            if(listener != null){
                                listener.onDownLoadSuccess(true);
                            }
                        }

                    }else{
                        UiUtil.showToast(context, json.optString("message"));
                        if(listener != null){
                            listener.onDownLoadError();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(listener != null){
                        listener.onDownLoadError();
                    }
                }
            }
        });
    }

    //中文转Unicode
    public static String gbEncoding(final String gbString) {   //gbString = "测试"
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }

    //Unicode转中文
    public static String decodeUnicode(final String dataStr) {

        try{
            int start = 0;
            int end = 0;
            final StringBuffer buffer = new StringBuffer();
            while (start > -1) {
                end = dataStr.indexOf("u", start + 1);
                String charStr = "";
                if (end == -1) {
                    charStr = dataStr.substring(start + 1, dataStr.length());
                } else {
                    charStr = dataStr.substring(start + 1, end);
                }
                char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
                buffer.append(new Character(letter).toString());
                start = end;
            }
            return buffer.toString();
        }catch(Exception e){
//          e.printStackTrace();
        }
      return dataStr;
    }


    /**
     * 转换JsonArray获得list对象
     * @param data
     * @return
     */
    public static ArrayList<JSONObject> getJsonObjectList(JSONArray data){
        ArrayList<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            list.add(data.optJSONObject(i));
        }
        return list;
    }

    public static Intent getIntentWithUrl(Context context, String url){
        return getIntentWithUrl(null, context, null, url);
    }

    public static Intent getIntentWithUrl(String pName, String url){
        return getIntentWithUrl(null, null, pName, url);
    }

    public static Intent getIntentWithUrl(Intent defaultIntent, Context context, String url){
        return getIntentWithUrl(defaultIntent, context, null, url);
    }

    public static Intent getIntentWithUrl(Intent defaultIntent, String pName, String url){
        return getIntentWithUrl(defaultIntent, null, pName, url);
    }

    /**
     * 通过url跳转至不同页面(首页, 推送, app拉起)
     * @param url
     */
    public static Intent getIntentWithUrl(Intent defaultIntent, Context context, String pName, String url){
        Intent intent;
        if(defaultIntent == null){
            if(context == null){
                intent = new Intent();
                intent.setClassName(pName, MainActivity.class.getName());
            }else{
                intent = new Intent(context, MainActivity.class);
            }
        }else{
            intent = defaultIntent;
        }
//        if(url.contains("/#/cart")){ //购物车
//            if(MainActivity.class.getSimpleName().equals(UiUtil.getTopActivitySimpleName(context))){
//                intent.putExtra("tab", 2);
//            }else{
//                if(TextUtils.isEmpty(pName)){
//                    intent.setClassName(context, CartAndClassActivity.class.getName());
//                }else{
//                    intent.setClassName(pName, CartAndClassActivity.class.getName());
//                }
//                intent.putExtra("type", "cart");
//            }
//        }else if(url.contains("/#/details")){ //商品详情
//            String str = "/#/details/";
//            int startIndex = url.indexOf(str) + str.length();
//            int endIndex ;
//            if(url.contains("?")){
//                endIndex = url.lastIndexOf("?");
//            }else{
//                endIndex = url.length();
//            }
//            String id_str = url.substring(startIndex, endIndex);
//            int goods_id = Integer.parseInt(id_str);
//            if(TextUtils.isEmpty(pName)){
//                intent.setClassName(context, GoodsDetailActivity.class.getName());
//            }else{
//                intent.setClassName(pName, GoodsDetailActivity.class.getName());
//            }
//            intent.putExtra("goods_id", goods_id);
//        }
//        else if(url.lastIndexOf("#") == url.length() - 1
//                || url.lastIndexOf("#/") == url.length() - 2
//                || url.indexOf("#/?_k") > -1){ //如果地址最后只有#那么跳首页
//            intent.putExtra("tab", 0);
//        }
//        else{ //其他Web页面
//            String newUrl = url.substring(url.indexOf("/#/") + 3);
//            intent.putExtra("url", newUrl);
//            if(TextUtils.isEmpty(pName)){
//                intent.setClassName(context, WebActivity.class.getName());
//            }else{
//                intent.setClassName(pName, WebActivity.class.getName());
//            }
//        }


        String newUrl = url.substring(url.indexOf("/#/") + 3);
        intent.putExtra("url", newUrl);
        if(TextUtils.isEmpty(pName)){
            intent.setClassName(context, WebActivity.class.getName());
        }else{
            intent.setClassName(pName, WebActivity.class.getName());
        }

//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

}
