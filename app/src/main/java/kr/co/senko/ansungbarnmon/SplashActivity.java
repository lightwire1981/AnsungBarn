package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.senko.ansungbarnmon.db.DBRequest;
import kr.co.senko.ansungbarnmon.util.PreferenceSetting;
import kr.co.senko.ansungbarnmon.util.Util;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private String currentData;
    private String weekData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (checkNetwork()) {
            checkGroupID();
            Util.setFullScreen(this);
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

    private void getWeekData(String group_id) {
        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject rowData = new JSONObject(result);
                weekData = rowData.getString("list");
                Util.getPhonePermission(this, this, currentData, weekData);
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
        switch (requestCode) {
            case 0:
                break;
            case 100:
                for (int res : grantResults) {
                    allowed = res == PackageManager.PERMISSION_GRANTED;
                    break;
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (allowed) {
            Util.getPhoneNumber(this, this, currentData, weekData);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.phone_number_denied), Toast.LENGTH_SHORT).show();
            Util.toMainActivity(this, this, currentData, weekData);
        }
    }
}