package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import kr.co.senko.ansungbarnmon.R;

public class WeekInfoAdapter extends RecyclerView.Adapter<WeekInfoAdapter.WeekInfoHolder> {

    private ArrayList<JSONArray> weeklyDataSet = new ArrayList<>();

    @NonNull
    @Override
    public WeekInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.page_week, parent, false);
        return new WeekInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekInfoHolder holder, int position) {
//        position
        ArrayList<String> tempData = new ArrayList<>();
        for(int i=0; i<7; i++) {
            tempData.add(i+"");
        }
        DailyInfoAdapter dailyInfoAdapter = new DailyInfoAdapter(tempData);
        holder.recyclerView.setAdapter(dailyInfoAdapter);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class WeekInfoHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public WeekInfoHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rcyVwWeekly);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private void getData() {
        weeklyDataSet.add(null);
    }
}
