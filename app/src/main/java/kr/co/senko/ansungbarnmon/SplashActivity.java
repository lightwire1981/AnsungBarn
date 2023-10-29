package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.senko.ansungbarnmon.db.DBRequest;
import kr.co.senko.ansungbarnmon.util.PreferenceSetting;
import kr.co.senko.ansungbarnmon.util.Util;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO).isEmpty()) {
            DBRequest.OnCompleteListener onCompleteListener = result -> {
                String groupID = "";
                try {
                    JSONObject rowData = new JSONObject(result);
                    JSONArray dataArray = new JSONArray(rowData.getString("list"));
                    groupID = dataArray.getJSONObject(0).getString("sys_op_group_id");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO, groupID);
            };
            new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, "", onCompleteListener);
        }
        Util.setFullScreen(this);
        Util.getPhonePermission(this, this);
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
            Util.getPhoneNumber(this, this);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.phone_number_denied), Toast.LENGTH_SHORT).show();
            Util.toMainActivity(this, this);
        }
    }
}