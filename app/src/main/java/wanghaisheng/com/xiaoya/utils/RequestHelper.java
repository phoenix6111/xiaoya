package wanghaisheng.com.xiaoya.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestHelper {
    Logger logger = Logger.getLogger("RequestUtil");

    private SecurityHelper mSecurityHelper;
    private Context mContext;
    private SettingPrefHelper mSettingPrefHelper;

    public RequestHelper(SecurityHelper mSecurityHelper, Context mContext, SettingPrefHelper mSettingPrefHelper) {
        this.mSecurityHelper = mSecurityHelper;
        this.mContext = mContext;
        this.mSettingPrefHelper = mSettingPrefHelper;
    }


    public Map<String, String> getHttpRequestMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("client", getDeviceId());
        //map.put("night", mSettingPrefHelper.getNightModel() ? "1" : "0");

        return map;
    }

    public String getAndroidId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getDeviceId() {
        String deviceId;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() == null) {
            deviceId = getAndroidId();
        } else {
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }

    public String getRequestSign(Map<String, String> map) {
        ArrayList<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i = i + 1) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            Map.Entry<String, String> map1 = list.get(i);
            builder.append(map1.getKey()).append("=").append(map1.getValue());
        }
        builder.append("HUPU_SALT_AKJfoiwer394Jeiow4u309");
        return mSecurityHelper.getMD5(builder.toString());
    }


}