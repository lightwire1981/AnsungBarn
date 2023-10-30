package kr.co.senko.ansungbarnmon;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

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

        TextView tvwLogin = findViewById(R.id.tVwFooterLogin);
        tvwLogin.setTag("login");
        tvwLogin.setOnClickListener(onClickListener);
        TextView tvwPrivacy = findViewById(R.id.tVwFooterPrivacy);
        tvwPrivacy.setTag("privacy");
        tvwPrivacy.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = view -> {
        Intent i = new Intent(MainActivity.this, WebActivity.class);
        switch (view.getTag().toString()) {
            case "login":
                i.putExtra("url", "login");
                break;
            case "privacy":
                i.putExtra("url", "privacy");
                break;
            default:
                break;
        }
        startActivity(i);
    };

    /**
     * 현재 까지 오늘자 정보 표시
     */
    private void getCurrentData() {
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
        Toast.makeText(getBaseContext(), "폰 번호 : "+Util.PHONE_NUMBER, Toast.LENGTH_SHORT).show();
        if (Util.PHONE_NUMBER.isEmpty()) {
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
            return;
        }

        DBRequest.OnCompleteListener onCompleteListener = result -> {
            try {
                JSONObject response = new JSONObject(result);
                if (response.getString("msg").equals("Y")) {
                    String userID = new JSONObject(response.getString("result")).getString("dt_op_user_id");
                    Log.i("############# User ID : ", userID);
                    PreferenceSetting.SavePreference(getBaseContext(), PreferenceSetting.PREFERENCE_KEY.USER_INFO, userID);
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

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        };
//        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.LOGIN_AUTH, "01053793629", onCompleteListener);
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.LOGIN_AUTH, Util.PHONE_NUMBER, onCompleteListener);
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
                    new Handler().postDelayed(() -> ((DrawerLayout)findViewById(R.id.drawerMain)).close(), 500);
                };

                RegionInfoAdapter regionInfoAdapter = new RegionInfoAdapter(regionInfoList, regionSelectListener);
                rcyVwRegion.setAdapter(regionInfoAdapter);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        new DBRequest(getBaseContext(), new Handler(Looper.getMainLooper())).executeAsync(DBRequest.REQUEST_TYPE.CURRENT, "", onCompleteListener);



    }

    private Timer upDateTimer = new Timer();

    @Override
    protected void onResume() {
        super.onResume();
        Util.setFullScreen(this);
        upDateTimer = new Timer();
        TimerTask upDateTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("<<<<<<<<< Data Updated", "");
                getCurrentData();
                getWeeklyData();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), R.string.msg_data_update, Toast.LENGTH_SHORT).show());
            }
        };
        upDateTimer.schedule(upDateTask, 0, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        upDateTimer.cancel();
    }
}