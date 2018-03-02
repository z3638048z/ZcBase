package base.zc.com.project.util;

import android.util.Log;

public class LogUtil {

    public static final boolean isDebug = true; //测试时用true, 上线前改成false
    private static final String TAG = "LogUtil";

    public static void e(String message){
        e(TAG, message);
    }

    public static void e(String tag, String message){
        if(isDebug)
            Log.e(tag, message);
    }

    public static void d(String message){
        d(TAG, message);
    }

    public static void d(String tag, String message){
        if(isDebug)
            Log.d(tag, message);
    }

    public static void i(String message){
        i(TAG, message);
    }

    public static void i(String tag, String message){
        if(isDebug)
            Log.i(tag, message);
    }

    public static void v(String message){
        v(TAG, message);
    }

    public static void v(String tag, String message){
        if(isDebug)
            Log.v(tag, message);
    }

    public static void w(String message){
        w(TAG, message);
    }

    public static void w(String tag, String message){
        if(isDebug)
            Log.w(tag, message);
    }


}
