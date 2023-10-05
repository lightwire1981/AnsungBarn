package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.senko.ansungbarnmon.util.Util;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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