package kr.co.senko.ansungbarnmon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.rd.PageIndicatorView;

import kr.co.senko.ansungbarnmon.adapter.WeekInfoAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PageIndicatorView pageIndicatorView = findViewById(R.id.pagerIndicator);

        ViewPager2 viewPager2 = findViewById(R.id.vpWeeklyInfo);
        WeekInfoAdapter weekInfoAdapter = new WeekInfoAdapter();
        viewPager2.setAdapter(weekInfoAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Util.setFullScreen(this);
    }
}