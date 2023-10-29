package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import kr.co.senko.ansungbarnmon.R;
import kr.co.senko.ansungbarnmon.db.WeekInfo;
import kr.co.senko.ansungbarnmon.util.Util;

public class DailyInfoAdapter extends RecyclerView.Adapter<DailyInfoAdapter.ViewHolder>{

    private final ArrayList<String> dailyDataSet;
    private int dayCount = 0;
    private Context context;

    public DailyInfoAdapter(ArrayList<String> dailyDataSet, int position) {
        this.dailyDataSet = dailyDataSet;
        this.dayCount = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] beforeDate = Util.convertBeforeDays(position+1);
        holder.weekDay.setText(beforeDate[0]);
        holder.weekName.setText(beforeDate[1]);
        holder.weekStatus.setImageResource(Util.convertToImage(dailyDataSet.get(position)));
        holder.weekValue.setText(Util.convertToStatus(holder.itemView.getContext(), dailyDataSet.get(position)));
    }

    @Override
    public int getItemCount() {
        return dailyDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView weekDay, weekName, weekValue;
        ImageView weekStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weekDay = itemView.findViewById(R.id.tVwWeekDay);
            weekName = itemView.findViewById(R.id.tVwWeekName);
            weekValue = itemView.findViewById(R.id.tVwWeekValue);
            weekStatus = itemView.findViewById(R.id.iVwWeekStatus);
        }
    }
}
