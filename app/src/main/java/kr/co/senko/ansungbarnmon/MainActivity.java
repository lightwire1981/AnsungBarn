package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Random;

import kr.co.senko.ansungbarnmon.adapter.TodayInfoAdapter;
import kr.co.senko.ansungbarnmon.adapter.WeekInfoAdapter;
import kr.co.senko.ansungbarnmon.util.Util;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 vpWeeklyInfo = findViewById(R.id.vpWeeklyInfo);
        WeekInfoAdapter weekInfoAdapter = new WeekInfoAdapter();
        vpWeeklyInfo.setAdapter(weekInfoAdapter);

        CircleIndicator3 vpWeekInfoIndicator = findViewById(R.id.pagerIndicator);
        vpWeekInfoIndicator.setViewPager(vpWeeklyInfo);

        ImageButton drawerMenuBtn = findViewById(R.id.ibtnDrawerMenu);
        drawerMenuBtn.setOnClickListener(view -> {
            Log.d("<<<<<< button Clicked", "button clicked");
            DrawerLayout drawerLayout = findViewById(R.id.drawerMain);
            View drawer = findViewById(R.id.drawer);
            drawerLayout.openDrawer(drawer);
        });

        getTodayData();
    }

    private void getTodayData() {

        ArrayList<String> tempDataSet = new ArrayList<>();
        for (int i=0; i<24; i++) {
            Random random = new Random();
            int num = random.nextInt(4);
            tempDataSet.add(num+"");
        }

        RecyclerView recyclerView = findViewById(R.id.rcVwTodayData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        TodayInfoAdapter todayInfoAdapter = new TodayInfoAdapter(tempDataSet);
        recyclerView.setAdapter(todayInfoAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setFullScreen(this);
    }
}