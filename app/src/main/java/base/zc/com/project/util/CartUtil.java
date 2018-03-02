package base.zc.com.project.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CartUtil {

    /**
     * 更新指定文本购物车数字
     * @param context
     * @param toTextView 要更新的textView
     */
    public static void updateCurrentCartNum(Context context, TextView toTextView){
        if(context ==null){
            return;
        }

        if(toTextView != null){
            int cartNum = getCurrentCartNum(context);
            if(cartNum ==0){
                toTextView.setVisibility(View.GONE);
            }else{
                toTextView.setVisibility(View.VISIBLE);
                if(cartNum > 99){
                    toTextView.setText("...");
                }else{
                    toTextView.setText("" + cartNum);
                }
            }
        }
    }

    /**
     * 获得当前购物车数量
     * @param context
     * @return
     */
    public static int getCurrentCartNum(Context context){
        if(context ==null){
            return 0;
        }

        return context.getSharedPreferences("cart", Context.MODE_PRIVATE).getInt("cart_count", 0);
    }

    /**
     * 获得当前登陆sid
     * @param context
     * @return
     */
    public static String getSessionId(Context context){
        if(context ==null){
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",  Context.MODE_PRIVATE);
        return sharedPreferences.getString("session", "");
    }

    /**
     * 当前是否登陆
     * @param context
     * @return
     */
    public static boolean isLoginIn(Context context){
        return !TextUtils.isEmpty(getSessionId(context));
    }

    /**
     * 保存sessionId
     * @param context
     */
    public static void saveSessionId(Context context, String sessionId){
        if(context ==null){
            return ;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",  Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("session", sessionId).commit();
    }

    /**
     * 清除sessionId (当退出登录或者sessionId失效等情况使用)
     * @param context
     */
    public static void clearSessionId(Context context){
        saveSessionId(context, null);
    }

    public static boolean  hasPermission(Context context,String permission){
        if(context ==null){
            return false;
        }
        JSONObject data = (JSONObject) ShareUtil.get(context, "loginInfo");
        JSONArray permission_actions = data.optJSONArray("permission_actions");
        if(permission_actions != null && permission_actions.length()>0){
            for (int i = 0; i < permission_actions.length(); i++) {
                String s = permission_actions.optString(i);
                if(TextUtils.equals(s,permission)){
                    return true;
                }
            }
        }
       return false;

    }


}
