package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.co.senko.ansungbarnmon.db.DBRequest;
import kr.co.senko.ansungbarnmon.util.PreferenceSetting;
import kr.co.senko.ansungbarnmon.util.Util;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String currentData;
    private String weekData;

    private ProgressBar progressBar;
    private TextView tvwProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.pgbrCircle);
        tvwProgress = findViewById(R.id.tvwProgress);

        if (checkNetwork()) {
            checkGroupID();
            Util.setFullScreen(this, Util.ScreenType.FULL);
        }
    }

    private Boolean checkNetwork() {
        boolean enable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

        if(networkCapabilities == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("인터넷 접속 확인").setMessage("서비스 사용을 위해 모바일 통신 또는 WiFi 연결이 필요합니다");
            builder.setNeutralButton("확인", (dialogInterface, i) -> finish());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return enable;
        }

        if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            enable = true;
        }

        return enable;
    }

    private void checkGroupID() {
        progressBar.setVisibility(View.VISIBLE);
        tvwProgress.setVisibility(View.VISIBLE);
        String GROUP_ID = PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO);
        if (GROUP_ID.isEmpty()) {
            DBRequest.OnCompleteListener onCompleteListener = result -> {
                String groupID = "";
                try {
                    JSONObject rowData = new JSONObject(result);
                    JSONArray dataArray = new JSONArray(rowData.getString("list"));
//                    Log.d("%%%%%%%%%%%%%%%%%%% ", dataArray.getString(0));
                    currentData = dataArray.getString(0);
                    groupID = dataArray.getJSONObject(0).getString("sys_op_group_id");
                    getWeekData(groupID);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO, groupID);

            };
            new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, "", onCompleteListener);
        } else {
            getMainData(GROUP_ID);
        }
    }

    private void getMainData(String group_id) {
        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject rowData = new JSONObject(result);
                JSONArray dataArray = new JSONArray(rowData.getString("list"));
                currentData = dataArray.getString(0);
                getWeekData(group_id);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, group_id, onCompleteListener);
    }

    private void checkUserAgree() {
        if (PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER).equals("NONE")||
        PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER).equals("YES")) {
            Util.toMainActivity(this, currentData, weekData);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.user_agree_title);
        builder.setMessage(R.string.user_agree_detail);
        builder.setPositiveButton(R.string.user_agree, (dialogInterface, i) -> {
            Util.getPhonePermission(getBaseContext(), this, currentData, weekData);
        });
        builder.setNegativeButton(R.string.user_disagree, (dialogInterface, i) -> {
            Toast.makeText(getBaseContext(), R.string.user_disagree_result, Toast.LENGTH_SHORT).show();
            PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER, "NONE");
            Util.toMainActivity(this, currentData, weekData);
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void getWeekData(String group_id) {
        DBRequest.OnCompleteListener onCompleteListener = result -> {

            try {
                JSONObject rowData = new JSONObject(result);
                weekData = rowData.getString("list");
//                String value = PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER);
//                if (PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER).equals("NONE")) {
//                    Util.toMainActivity(this, this, currentData, weekData);
//                    return;
//                }
//                Util.getPhonePermission(this, this, currentData, weekData);
//                checkUserAgree();
//                Util.toMainActivity(this, currentData, weekData);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra(Util.MAIN_DATA, new String[]{currentData, weekData});

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                    tvwProgress.setVisibility(View.INVISIBLE);
                }, 2000);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.WEEK, group_id, onCompleteListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allowed = true;

        if (requestCode == 100) {
            for (int res : grantResults) {
                allowed = res == PackageManager.PERMISSION_GRANTED;
                break;
            }
        } else {
            allowed = false;
        }
        if (allowed) {
            PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER, "YES");
            Toast.makeText(getBaseContext(), R.string.user_agree_result, Toast.LENGTH_SHORT).show();
            Util.getPhoneNumber(this, this, currentData, weekData);
        } else {
            Toast.makeText(getApplicationContext(), R.string.user_disagree_result, Toast.LENGTH_SHORT).show();
            PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER, "NONE");
            Util.toMainActivity(this, currentData, weekData);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USE_NUMBER, "NONE");
        Toast.makeText(getApplicationContext(), getString(R.string.phone_number_denied), Toast.LENGTH_SHORT).show();
        Util.toMainActivity(this, currentData, weekData);
    }
}