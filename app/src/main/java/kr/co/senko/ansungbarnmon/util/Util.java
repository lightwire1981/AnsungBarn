package kr.co.senko.ansungbarnmon.util;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.co.senko.ansungbarnmon.MainActivity;
import kr.co.senko.ansungbarnmon.R;
import pub.devrel.easypermissions.EasyPermissions;

public class Util {

    public static String PHONE_NUMBER;
    public final static String MAIN_DATA = "mainData";
    public static void setFullScreen(Activity base) {

        View decorView = base.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void getPhonePermission(Context context, Activity activity, String... mainData) {

        String[] perms = {Manifest.permission.READ_PHONE_NUMBERS};

        if (EasyPermissions.hasPermissions(context, perms)) {
            getPhoneNumber(context, activity, mainData);
        } else {
            Log.i("<<<<Check Permission>>", "퍼미션 없음 권한 요청");
            EasyPermissions.requestPermissions(activity,
                    "앱 동작을 위해 전화 번호 접근 권한이 필요 합니다. 권한을 승인 하시겠습니까?",
                    100,
                    Manifest.permission.READ_PHONE_NUMBERS);
        }
    }
    @SuppressLint("HardwareIds")
    public static void getPhoneNumber(Context context, Activity activity, String... mainData) {
        Log.i("<<<<Check Permission>>", "퍼미션 존재함");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String fullNum = telephonyManager.getLine1Number() == null ? "":telephonyManager.getLine1Number();
        PHONE_NUMBER = "010"+fullNum.substring((fullNum.length()-8));

        Log.i("<<<<< Phone Number >>>>>", PHONE_NUMBER);
        toMainActivity(context, activity, mainData);
    }

    public static void toMainActivity(Context context, Activity activity, String... mainData) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(Util.MAIN_DATA, mainData);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            context.startActivity(i);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            activity.finish();
        }, 2000);
    }

    public static int convertToImage(String value) {
        int resourceID;
        switch (value) {
            case "0":
                resourceID = R.drawable.emo_good;
                break;
            case "1":
                resourceID = R.drawable.emo_normal;
                break;
            case "2":
                resourceID = R.drawable.emo_bad;
                break;
            case "3":
                resourceID = R.drawable.emo_worst;
                break;
            default:
                resourceID = 0;
                break;
        }
        return resourceID;
    }

    public static String convertToStatus(Context context, String value) {
        return context.getResources().getStringArray(R.array.status_value)[Integer.parseInt(value)];
    }

    public static String convertToDate(String timestamp) {
        try {
            @SuppressLint("SimpleDateFormat") Date origin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            SimpleDateFormat myFormat = new SimpleDateFormat("MM. dd(E) a h:mm", Locale.KOREAN);
            assert origin != null;
            return myFormat.format(origin);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertBeforeHours(String timestamp, int interval) throws ParseException {

        @SuppressLint("SimpleDateFormat") Date origin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
        Calendar calendar = Calendar.getInstance();

        assert origin != null;
        calendar.setTime(origin);
        calendar.add(Calendar.HOUR, -interval);

        SimpleDateFormat myFormat = new SimpleDateFormat("a h시", Locale.KOREAN);
        return myFormat.format(calendar.getTime());
    }

    public static String[] convertBeforeDays(int interval) {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        calendar.add(Calendar.DATE, -interval);

        SimpleDateFormat myFormat = new SimpleDateFormat("MM월 dd일", Locale.KOREAN);
        String day = myFormat.format(calendar.getTime());
        myFormat = new SimpleDateFormat("EEEE", Locale.KOREAN);
        String week = myFormat.format(calendar.getTime());
        return new String[]{day, week};
    }
}
