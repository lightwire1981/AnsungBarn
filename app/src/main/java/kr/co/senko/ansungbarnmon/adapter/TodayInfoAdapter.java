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

import kr.co.senko.ansungbarnmon.R;

public class TodayInfoAdapter extends RecyclerView.Adapter<TodayInfoAdapter.ViewHolder>{

    private final ArrayList<String> todayDataSet;
    private Context context;

    public TodayInfoAdapter (ArrayList<String> resourceDataSet) {
        todayDataSet = resourceDataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] value = context.getResources().getStringArray(R.array.status_value);
        int statusValue = Integer.parseInt(todayDataSet.get(position));
        switch (statusValue) {
            case 0:
                holder.statusLog.setImageResource(R.drawable.emo_good);
                break;
            case 1:
                holder.statusLog.setImageResource(R.drawable.emo_normal);
                break;
            case 2:
                holder.statusLog.setImageResource(R.drawable.emo_bad);
                break;
            case 3:
                holder.statusLog.setImageResource(R.drawable.emo_worst);
                break;
            default:
                break;
        }
        holder.valueLog.setText(value[statusValue]);
    }

    @Override
    public int getItemCount() {
        return todayDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView timeLog, valueLog;
        private final ImageView statusLog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeLog = itemView.findViewById(R.id.tVwDailyTimeLog);
            valueLog = itemView.findViewById(R.id.tVwDailyValueLog);
            statusLog = itemView.findViewById(R.id.iVwDailyStatusLog);
        }
    }
}
