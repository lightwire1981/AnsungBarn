package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import kr.co.senko.ansungbarnmon.adapter.RegionInfoAdapter;
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

        ImageButton drawerMenuBtn = findViewById(R.id.ibtnDrawerMenu);
        drawerMenuBtn.setOnClickListener(view -> {
            Log.d("<<<<<< button Clicked", "button clicked");
            DrawerLayout drawerLayout = findViewById(R.id.drawerMain);
            View drawer = findViewById(R.id.drawer);
            drawerLayout.openDrawer(drawer);
        });

        getTodayData();
        getWeeklyData();
        checkLoginActivate();
        getRegionData();
    }

    /**
     * 현재 까지 오늘자 정보 표시
     */
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

    /**
     * 오늘 까지 주중 정보 표시
     */
    private void getWeeklyData() {
        ViewPager2 vpWeeklyInfo = findViewById(R.id.vpWeeklyInfo);
        WeekInfoAdapter weekInfoAdapter = new WeekInfoAdapter();
        vpWeeklyInfo.setAdapter(weekInfoAdapter);

        CircleIndicator3 vpWeekInfoIndicator = findViewById(R.id.pagerIndicator);
        vpWeekInfoIndicator.setViewPager(vpWeeklyInfo);
    }

    /**
     * 농장주 로그인 활성화
     */
    private void checkLoginActivate() {
        if (Util.PHONE_NUMBER.isEmpty()) return;

        boolean isActivate = true;
        // 농장주 번호 확인 구문

        if (isActivate) {
            TextView tvwLogin = findViewById(R.id.tVwFooterLogin);
            tvwLogin.setVisibility(View.VISIBLE);
        } else {
            TextView tvwCity = findViewById(R.id.tVwFooterCity);
            Space spBlank = findViewById(R.id.spFooterBlank);
            TextView tvwTerms = findViewById(R.id.tVwFooterTerms);
            TextView tvwPrivacy = findViewById(R.id.tVwFooterPrivacy);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tvwCity.getLayoutParams();
            params.weight = 2;
            tvwCity.setLayoutParams(params);
            params = (LinearLayout.LayoutParams)tvwTerms.getLayoutParams();
            params.weight = 2;
            tvwTerms.setLayoutParams(params);
            params = (LinearLayout.LayoutParams)tvwPrivacy.getLayoutParams();
            params.weight = 2;
            tvwPrivacy.setLayoutParams(params);
            params = (LinearLayout.LayoutParams)spBlank.getLayoutParams();
            params.weight = 4;
            spBlank.setLayoutParams(params);
        }
    }

    /**
     * 지역 정보 표시(슬라이드 메뉴)
     */
    private void getRegionData() {
        ArrayList<String[]> tempDataSet = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Random random = new Random();
            int num = random.nextInt(4);
            String[] value = {"죽림리", num+""};
            tempDataSet.add(value);
        }
        RecyclerView recyclerView = findViewById(R.id.rcVwRegionList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        RegionInfoAdapter.RegionSelectListener regionSelectListener = regionName -> {
            Log.d("<<<<< Selected Region Name", regionName);
            ((DrawerLayout)findViewById(R.id.drawerMain)).close();
        };

        RegionInfoAdapter regionInfoAdapter = new RegionInfoAdapter(tempDataSet, regionSelectListener);
        recyclerView.setAdapter(regionInfoAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setFullScreen(this);
    }
}