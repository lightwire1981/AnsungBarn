package kr.co.senko.ansungbarnmon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rd.PageIndicatorView;

import kr.co.senko.ansungbarnmon.util.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PageIndicatorView pageIndicatorView = findViewById(R.id.vpWeeklyInfo);
        pageIndicatorView.setCount(5);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Util.setFullScreen(this);
    }
}