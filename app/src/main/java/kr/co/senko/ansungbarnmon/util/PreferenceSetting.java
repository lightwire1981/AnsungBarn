package kr.co.senko.ansungbarnmon.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferenceSetting {
    private static final String TAG = "PreferenceSetting";
    public enum PREFERENCE_KEY {
        REGION_INFO,
        USER_INFO,
        PHONE_NUMBER
    }

    public static String LoadPreference(Context context, PREFERENCE_KEY category) {
        String returnValue;
        SharedPreferences preferences = context.getSharedPreferences("prefInfo", Activity.MODE_PRIVATE);

        switch (category) {
            case REGION_INFO:
                returnValue = preferences.getString(PREFERENCE_KEY.REGION_INFO.name(), "");
                break;
            case USER_INFO:
                returnValue = preferences.getString(PREFERENCE_KEY.USER_INFO.name(), "");
                break;
            case PHONE_NUMBER:
                returnValue = preferences.getString(PREFERENCE_KEY.PHONE_NUMBER.name(), "");
                break;
            default:
                returnValue = "";
                break;
        }
        return returnValue;
    }

    public static void SavePreference(Context context, PREFERENCE_KEY category, String value) {
        SharedPreferences preferences = context.getSharedPreferences("prefInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(category.name(), value);
        editor.apply();
        Log.i(TAG, "정보 반영 됨");
    }
}
