package base.zc.com.project.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.zc.com.project.R;
import base.zc.com.project.app.BaseApplication;
import base.zc.com.project.bean.DeviceInfo;
import base.zc.com.project.util.LogUtil;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.PermissionCheckerUtil;
import base.zc.com.project.util.UiUtil;
import okhttp3.Call;
import okhttp3.Response;

public class BaseActivity extends AppCompatActivity {

    private int exitCount = 0;
    private Handler exitHandler = new Handler();
    protected Handler handler = new Handler();
    protected Context mContext;
    protected boolean isMainPage = false; //是否是主页
    protected boolean canBackToFinish = true; //是否能返回退出
    public BaseApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        application = (BaseApplication) getApplication();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(!canBackToFinish){
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK &&
                (UiUtil.hideLoading(this)
                || UiUtil.isNoMissLoadingShow(this)
                || UiUtil.closeBottomMessage(this)
                || UiUtil.closeCustomMessage(this))){
            return true;
        }

        //主页
        if(isMainPage){
            if(keyCode == KeyEvent.KEYCODE_BACK){

                exitCount++;
                if(exitCount == 1){
                    UiUtil.showToast(this, getResources().getString(R.string.exit_message));
                    exitHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exitCount = 0;
                        }
                    }, 3000);
                    return true;
                }else{
                    setResult(2000);
                    finishWithDefault();

                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //每次恢复页面都请求更新方法，获取时间戳
        final int versionCode = NetUtil.getAppVersionCode(this);
        final String versionName = NetUtil.getAppVersionName(this);

        NetUtil.get(this, NetUtil.VERSION_UPDATE).tag(this).execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s);
                            int code = json.optInt("code");
                            if(code == 200){
                                json = json.optJSONObject("data");
                                //赋值时间戳, 后面所有请求都需要此值
                                long server_timestamp = json.optLong("server_timestamp");
                                long currentTime = System.currentTimeMillis() / 1000;
                                NetUtil.TIME_STAMP = server_timestamp - currentTime;
                                LogUtil.e("时间戳base***" + NetUtil.TIME_STAMP );

                            }else{
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

            @Override
            public void onError(Call call, Response response, Exception e) {

            }
        });

        requestAppPermission(
                new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new String[]{"读取手机状态", "存储卡操作"},
                new PermissionCheckerUtil.onPermissionCheckListener() {
                    @Override
                    public void onPermissionAllPassed() {
                        DeviceInfo deviceInfo = new DeviceInfo(mContext);

                        LogUtil.e("当前设备信息1", "<" + deviceInfo.deveiceId + ">");
                        LogUtil.e("当前设备信息2", "<" + deviceInfo.deveiceIp + ">");
                        LogUtil.e("当前设备信息3", "<" + deviceInfo.netType + ">");
                        
                    }

                    @Override
                    public void onPermissionAllRefused(String[] refusePermissions) {
                    }

                    @Override
                    public void onPermissionSomeRefused(String[] refusePermissions, String[] refusedPermisionNames) {
                    }

                });
        
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_start_in, R.anim.activity_start_out);
    }

    public void startActivityWithDefault(Intent intent){
        super.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_finish_in, R.anim.activity_finish_out);
    }

    public void finishWithDefault(){
        super.finish();
//        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_start_in, R.anim.activity_start_out);
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓6.0申请权限相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    protected PermissionCheckerUtil.onPermissionCheckListener permissionCheckListener;
    protected boolean isShowDelfaultPopTip = true;
    protected boolean isMustOpenPermision = false;
    protected String[] permissionNames;
    private boolean isJustChoosePermission = false;

    public boolean isJustChoosePermission() {
        if (isJustChoosePermission) {
            isJustChoosePermission = false;
            return true;
        }
        return false;
    }

    protected void requestAppPermissionCancelable(String[] permissions, String[] permissionNames, PermissionCheckerUtil.onPermissionCheckListener permissionCheckListener) {
        requestAppPermission(permissions, permissionNames, permissionCheckListener, true, false);
    }

    public void requestAppPermission(String[] permissions, String[] permissionNames, PermissionCheckerUtil.onPermissionCheckListener permissionCheckListener) {
        requestAppPermission(permissions, permissionNames, permissionCheckListener, true, true);
    }

    protected void requestAppPermission(String[] permissions, String[] permissionNames, PermissionCheckerUtil.onPermissionCheckListener permissionCheckListener, boolean isShowDelfaultPopTip, boolean isMustOpenPermision) {
        this.permissionNames = permissionNames;
        this.permissionCheckListener = permissionCheckListener;
        this.isShowDelfaultPopTip = isShowDelfaultPopTip;
        this.isMustOpenPermision = isMustOpenPermision;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> lackPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (PermissionCheckerUtil.lacksPermissions(this, permission)) {
                    lackPermissionList.add(permission);
                }
            }
            if (lackPermissionList.size() > 0) {
                String[] newPermissions = new String[lackPermissionList.size()];
                for (int i = 0; i < lackPermissionList.size(); i++) {
                    newPermissions[i] = lackPermissionList.get(i);
                }

                if (!isPermissionDialogShow) {
                    requestPermissions(newPermissions, 0);
                }

            } else {
                if (permissionCheckListener != null) {
                    permissionCheckListener.onPermissionAllPassed();
                }
            }
        } else {
            if (permissionCheckListener != null) {
                permissionCheckListener.onPermissionAllPassed();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        isJustChoosePermission = true;

        if (permissionCheckListener != null) {
            boolean isAllPass = true;
            boolean isCompletelyRefused = true;
            boolean[] isPasses = new boolean[permissions.length];
            String message = "您已拒绝了相关权限，不能进行后续操作，请手动打开相关权限";
            String refuseName = "";
            List<String> refusedList = new ArrayList<>();
            List<String> refusedNameList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isAllPass = false;
                    isPasses[i] = false;
                    refusedList.add(permissions[i]);
                    refusedNameList.add(permissionNames[i]);
                    Log.e("BaseScanActivity", "BaseScanActivity.onRequestPermissionsResult***拒绝的权限:" + permissionNames[i] + ", 是否勾选了不再询问:" + !shouldShowRequestPermissionRationale(permissions[i]));
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        isCompletelyRefused = false;
                    }
                    refuseName += permissionNames[i] + ",";
                } else {
                    isCompletelyRefused = false;
                    isPasses[i] = true;
                }
            }

            if (!TextUtils.isEmpty(refuseName)) {
                refuseName = refuseName.substring(0, refuseName.length() - 1);
                message = "您已拒绝了<" + refuseName + ">的权限，不能进行后续操作，请手动打开相关权限";
            }

            if (isCompletelyRefused) {
                showPermissonDialog(message);
                permissionCheckListener.onPermissionAllRefused(permissions);
            } else {
                if (isAllPass) {
                    permissionCheckListener.onPermissionAllPassed();
                } else {
                    String[] refusedPermissions = new String[refusedList.size()];
                    String[] refusedPermissionNames = new String[refusedNameList.size()];
                    for (int i = 0; i < refusedList.size(); i++) {
                        refusedPermissions[i] = refusedList.get(i);
                        refusedPermissionNames[i] = refusedNameList.get(i);
                    }
//                    showPermissonDialog(message);
                    permissionCheckListener.onPermissionSomeRefused(refusedPermissions, refusedPermissionNames);
                }
            }

//            permissionCheckListener.onPermissionCheckedStatus(permissions, isPasses);

        }

    }

    private boolean isPermissionDialogShow = false;

    protected void showPermissonDialog(String message) {
        if (isShowDelfaultPopTip) {
            if (isMustOpenPermision) {
                UiUtil.showButtonMessage(mContext, "提示", message, null, new int[]{0}, new String[]{"确定"}, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiUtil.closeBottomMessage(mContext);
                        PermissionCheckerUtil.openSetPermission(mContext);
                        isPermissionDialogShow = false;
                    }
                });
                isPermissionDialogShow = true;
            } else {
                UiUtil.showButtonMessage(mContext, "提示", message, null, new int[]{1}, new String[]{"取消", "确定"}, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiUtil.closeBottomMessage(mContext);
                        isPermissionDialogShow = false;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiUtil.closeBottomMessage(mContext);
                        PermissionCheckerUtil.openSetPermission(mContext);
                        isPermissionDialogShow = false;
                    }
                });
                isPermissionDialogShow = true;
            }
        }
    }

    /**
     * 设置沉浸状态栏
     */
    protected void setStatusBar(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
