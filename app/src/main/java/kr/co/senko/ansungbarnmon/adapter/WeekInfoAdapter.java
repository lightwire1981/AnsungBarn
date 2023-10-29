package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.senko.ansungbarnmon.R;
import kr.co.senko.ansungbarnmon.db.WeekInfo;

public class WeekInfoAdapter extends RecyclerView.Adapter<WeekInfoAdapter.WeekInfoHolder> {

    private final WeekInfo weekDataSet;


    public WeekInfoAdapter(WeekInfo weekDataSet) {
        this.weekDataSet = weekDataSet;
    }

    @NonNull
    @Override
    public WeekInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.page_week, parent, false);
        return new WeekInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekInfoHolder holder, int position) {

        DailyInfoAdapter dailyInfoAdapter = new DailyInfoAdapter(weekDataSet.getWeekLog().get(position), position);
        holder.recyclerView.setAdapter(dailyInfoAdapter);
    }

    @Override
    public int getItemCount() {
        return weekDataSet.getWeekLog().size();
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
}
