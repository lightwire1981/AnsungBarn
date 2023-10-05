package kr.co.senko.ansungbarnmon;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import kr.co.senko.ansungbarnmon.adapter.WeekInfoAdapter;
import kr.co.senko.ansungbarnmon.util.Util;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2 = findViewById(R.id.vpWeeklyInfo);
        WeekInfoAdapter weekInfoAdapter = new WeekInfoAdapter();
        viewPager2.setAdapter(weekInfoAdapter);

        CircleIndicator3 circleIndicator = findViewById(R.id.pagerIndicator);
        circleIndicator.setViewPager(viewPager2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setFullScreen(this);
    }
}