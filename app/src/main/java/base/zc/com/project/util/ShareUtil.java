package base.zc.com.project.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareUtil {


    public static void bindUserId(Context context, String userId){
    }

    public static void saveListJsonObject(Context context, String key, List<JSONObject> data){
        save(context, key, data);
    }

    public static void save(Context context, String key, Object data){

        if(context == null){
            return;
        }

        synchronized (ShareUtil.class){
            String type = data.getClass().getSimpleName();

//        Log.e("ShareUtil", "ShareUtil.save***" + type);

            SharedPreferences.Editor editor = context.getSharedPreferences(key, Context.MODE_PRIVATE ).edit();

            if ("Integer".equals(type)) {
                editor.putString(key, Integer.toString((Integer) data));
            } else if ("Boolean".equals(type)) {
                editor.putString(key, Boolean.toString((Boolean) data));
            } else if ("String".equals(type) || "JSONObject".equals(type) || "JSONArray".equals(type)) {
                editor.putString(key, data.toString());
            } else if ("Float".equals(type)) {
                editor.putString(key, Float.toString((Float) data));
            } else if ("Long".equals(type)) {
                editor.putString(key, Long.toString((Long) data));
            } else if("HashMap".equals(type)){
                HashMap<String, String> map = (HashMap<String, String>) data;
                JSONObject json = new JSONObject();
                for(Map.Entry<String, String> entry : map.entrySet()){
                    String key_ = entry.getKey();
                    String value = entry.getValue();
                    try {
                        json.put(key_, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                editor.putString(key, json.toString());
            } else if("ArrayList".equals(type)){
                ArrayList<JSONObject> jsonList = (ArrayList<JSONObject>) data;
                String result = "";
                for (int i = 0; i < jsonList.size(); i++) {
                    String symbol = "";
                    if(i < jsonList.size() - 1){
                        symbol = ",";
                    }
                    result += jsonList.get(i).toString() + symbol;
                }
                editor.putString(key, "[" + result + "]");
            }

            editor.commit();
        }

    }

    public static Object get(Context context, String key){

        if(context == null){
            return "";
        }

        synchronized (ShareUtil.class){
            SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
            String data = sp.getString(key, "");

            if(TextUtils.isEmpty(data)){
                return "";
            }

            try {
                JSONObject json = new JSONObject(data);
                return json;
            } catch (Exception e) {
            }
            try {
                JSONArray array = new JSONArray(data);
                return array;
            } catch (Exception e) {
            }

            return data;
        }

    }

    public static ArrayList<JSONObject> getListJsonObject(Context context, String key){
        synchronized (ShareUtil.class) {
            ArrayList<JSONObject> jsonList = new ArrayList<>();
            Object data = get(context, key);
            if(data != null){
                try {
                    JSONArray array = new JSONArray(data.toString());
                    for (int i = 0; i < array.length(); i++) {
                        jsonList.add(array.optJSONObject(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return jsonList;
        }
    }

    public static void clearData(Context context, String key){
        synchronized (ShareUtil.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

}
