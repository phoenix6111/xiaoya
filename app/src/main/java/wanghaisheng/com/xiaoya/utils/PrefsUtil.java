package wanghaisheng.com.xiaoya.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by sheng on 2016/4/14.
 */
public class PrefsUtil {
    private Context mContext;
    
    public PrefsUtil(Context context) {
        this.mContext = context;
    }
    
    public  static final String PREF_NAME = "creativelocker.pref";
    private  static boolean sIsAtLeastGB;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public  void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public  void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public  void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public  void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public  boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public  String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public  int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public  long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public  float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  SharedPreferences getPreferences() {
        SharedPreferences pre = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  SharedPreferences getPreferences(String prefName) {
        return mContext.getSharedPreferences(prefName,
                Context.MODE_MULTI_PROCESS);
    }
}
