package base.zc.com.project.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

public class PermissionCheckerUtil {

    private final Context mContext;
    private static Handler handler = new Handler();

    public final static String[] PERMISSIONS = new String[]{

            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };

    public PermissionCheckerUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 打开权限选择页面
     * @param context
     */
    public static void openSetPermission(Context context){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    public interface onPermissionCheckListener{

        /**
         * 所有权限全部通过,6.0一下直接返回此方法
         */
        public void onPermissionAllPassed();

        /**
         * 所有权限已经全部拒绝
         */
        public void onPermissionAllRefused(String[] refusePermissions);

        /**
         * 被拒绝的权限
         * @param refusePermissions 被拒绝的权限
         * @param refusedPermisionNames 权限自定义名称
         */
        public void onPermissionSomeRefused(String[] refusePermissions, String[] refusedPermisionNames);

//        /**
//         * 所有权限的通过拒绝状态
//         * @param permissions 所有权限
//         * @param isPasses 是否通过
//         */
//        public void onPermissionCheckedStatus(String[] permissions, boolean[] isPasses);

    }

}
