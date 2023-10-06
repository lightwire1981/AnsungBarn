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

public class DailyInfoAdapter extends RecyclerView.Adapter<DailyInfoAdapter.ViewHolder>{

    private final ArrayList<String> dailyDataSet;
    private Context context;

    public DailyInfoAdapter(ArrayList<String> resourceDataSet) {
        dailyDataSet = resourceDataSet;
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
        String[] weekName = context.getResources().getStringArray(R.array.week_name);
        String[] value = context.getResources().getStringArray(R.array.status_value);
        Random random = new Random();
        int num = random.nextInt(4);
        holder.weekName.setText(weekName[position]);
        switch (num) {
            case 0:
                holder.weekStatus.setImageResource(R.drawable.emo_good);
                break;
            case 1:
                holder.weekStatus.setImageResource(R.drawable.emo_normal);
                break;
            case 2:
                holder.weekStatus.setImageResource(R.drawable.emo_bad);
                break;
            case 3:
                holder.weekStatus.setImageResource(R.drawable.emo_worst);
                break;
            default:
                break;
        }
        holder.weekValue.setText(value[num]);
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
