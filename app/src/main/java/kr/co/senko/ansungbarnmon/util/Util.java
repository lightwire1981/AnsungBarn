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

import kr.co.senko.ansungbarnmon.MainActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class Util {
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

    public static void getPhonePermission(Context context, Activity activity) {

        String[] perms = {Manifest.permission.READ_PHONE_NUMBERS};

        if (EasyPermissions.hasPermissions(context, perms)) {
            getPhoneNumber(context, activity);
        } else {
            Log.i("<<<<Check Permission>>", "퍼미션 없음 권한 요청");
            EasyPermissions.requestPermissions(activity,
                    "앱 동작을 위해 전화 번호 접근 권한이 필요 합니다. 권한을 승인 하시겠습니까?",
                    100,
                    Manifest.permission.READ_PHONE_NUMBERS);
        }
    }
    @SuppressLint("HardwareIds")
    public static void getPhoneNumber(Context context, Activity activity) {
        String phoneNumber;
        Log.i("<<<<Check Permission>>", "퍼미션 존재함");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        phoneNumber = telephonyManager.getLine1Number() == null ? "":telephonyManager.getLine1Number();
        Log.i("<<<<< Phone Number >>>>>", phoneNumber);
        toMainActivity(context, activity);
    }

    public static void toMainActivity(Context context, Activity activity) {
        Intent i = new Intent(context, MainActivity.class);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            context.startActivity(i);
            activity.finish();
        }, 2000);
    }
}
