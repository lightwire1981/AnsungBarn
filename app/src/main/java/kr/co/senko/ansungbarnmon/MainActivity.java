package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import kr.co.senko.ansungbarnmon.adapter.RegionInfoAdapter;
import kr.co.senko.ansungbarnmon.adapter.TodayInfoAdapter;
import kr.co.senko.ansungbarnmon.adapter.WeekInfoAdapter;
import kr.co.senko.ansungbarnmon.db.CurrentInfo;
import kr.co.senko.ansungbarnmon.db.DBRequest;
import kr.co.senko.ansungbarnmon.db.RegionInfo;
import kr.co.senko.ansungbarnmon.db.WeekInfo;
import kr.co.senko.ansungbarnmon.util.PreferenceSetting;
import kr.co.senko.ansungbarnmon.util.Util;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private static String GROUP_ID;
    private RecyclerView rcyVwCurrent;
    private ViewPager2 vpWeeklyInfo;
    private RecyclerView rcyVwRegion;

    private CurrentInfo SelectedInfo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GROUP_ID = PreferenceSetting.LoadPreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO);

        ImageButton drawerMenuBtn = findViewById(R.id.ibtnDrawerMenu);
        drawerMenuBtn.setOnClickListener(view -> {
            Log.d("<<<<<< button Clicked", "button clicked");
            DrawerLayout drawerLayout = findViewById(R.id.drawerMain);
            View drawer = findViewById(R.id.drawer);
            drawerLayout.openDrawer(drawer);
        });

        SetWidget();
        getCurrentData();
        getWeeklyData();
        checkLoginActivate();
        getRegionData();
    }

    private void SetWidget() {
        rcyVwCurrent = findViewById(R.id.rcVwCurrentData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcyVwCurrent.setLayoutManager(linearLayoutManager);

        vpWeeklyInfo = findViewById(R.id.vpWeeklyInfo);

        rcyVwRegion = findViewById(R.id.rcVwRegionList);
        LinearLayoutManager lmgr = new LinearLayoutManager(this);
        lmgr.setOrientation(RecyclerView.VERTICAL);
        rcyVwRegion.setLayoutManager(lmgr);
    }

    /**
     * 현재 까지 오늘자 정보 표시
     */
    private void getCurrentData() {
        Log.d("???????????? Group ID : ", GROUP_ID);
        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject rowData = new JSONObject(result);
                JSONArray dataArray = new JSONArray(rowData.getString("list"));
                JSONObject currentData = dataArray.getJSONObject(0);
                CurrentInfo currentInfo = new CurrentInfo(currentData);
                SelectedInfo = currentInfo;
//                for(String value : currentInfo.getCurrentLog()) {
//                    Log.i("Log :", value);
//                }

                ((TextView)findViewById(R.id.tVwCurrentRegionName)).setText(currentInfo.getGroupName());
                ((TextView)findViewById(R.id.tVwCurrentRegionTime)).setText(Util.convertToDate(currentInfo.getCurrentTime()));
                ((TextView)findViewById(R.id.tVwCurrentRegionValue)).setText(Util.convertToStatus(getBaseContext(), currentInfo.getCurrentValue()));
                ((ImageView)findViewById(R.id.iVwCurrentRegionStatus)).setImageResource(Util.convertToImage(currentInfo.getCurrentValue()));

                ((TextView)findViewById(R.id.tVwDrawerRegionName)).setText(currentInfo.getGroupName());
                ((ImageView)findViewById(R.id.IVwDrawerRegionStatus)).setImageResource(Util.convertToImage(currentInfo.getCurrentValue()));
                ((TextView)findViewById(R.id.tVwDrawerRegionValue)).setText(Util.convertToStatus(getBaseContext(), currentInfo.getCurrentValue()));

                TodayInfoAdapter todayInfoAdapter = new TodayInfoAdapter(currentInfo);
                rcyVwCurrent.setAdapter(todayInfoAdapter);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, GROUP_ID, onCompleteListener);
    }

    /**
     * 오늘 까지 주중 정보 표시
     */
    private void getWeeklyData() {
        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject rowData = new JSONObject(result);
                JSONArray dataArray = new JSONArray(rowData.getString("list"));
                WeekInfo weekInfo = new WeekInfo(dataArray);

                WeekInfoAdapter weekInfoAdapter = new WeekInfoAdapter(weekInfo);
                vpWeeklyInfo.setAdapter(weekInfoAdapter);

                CircleIndicator3 vpWeekInfoIndicator = findViewById(R.id.pagerIndicator);
                vpWeekInfoIndicator.setViewPager(vpWeeklyInfo);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.WEEK, GROUP_ID, onCompleteListener);
    }

    /**
     * 농장주 로그인 활성화
     */
    private void checkLoginActivate() {
        if (Util.PHONE_NUMBER.isEmpty()) return;

        boolean isActivate = false;
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
        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject rowData = new JSONObject(result);
                JSONArray dataArray = new JSONArray(rowData.getString("list"));
                ArrayList<RegionInfo> regionInfoList = new ArrayList<>();
                for (int index=0; index < dataArray.length(); index++) {
                    regionInfoList.add(new RegionInfo(dataArray.getJSONObject(index)));
                }

                @SuppressLint("NotifyDataSetChanged") RegionInfoAdapter.RegionSelectListener regionSelectListener = regionGroup -> {
                    Log.d("<<<<< Selected Region Group", regionGroup);
                    PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.REGION_INFO, regionGroup);
                    GROUP_ID = regionGroup;
                    getCurrentData();
                    Objects.requireNonNull(rcyVwCurrent.getAdapter()).notifyDataSetChanged();
                    getWeeklyData();
                    Objects.requireNonNull(vpWeeklyInfo.getAdapter()).notifyDataSetChanged();
                    ((DrawerLayout)findViewById(R.id.drawerMain)).close();
                };

                RegionInfoAdapter regionInfoAdapter = new RegionInfoAdapter(regionInfoList, regionSelectListener);
                rcyVwRegion.setAdapter(regionInfoAdapter);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, "", onCompleteListener);



    }
    @Override
    protected void onResume() {
        super.onResume();
        Util.setFullScreen(this);
    }
}