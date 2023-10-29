package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;

import kr.co.senko.ansungbarnmon.R;
import kr.co.senko.ansungbarnmon.db.CurrentInfo;
import kr.co.senko.ansungbarnmon.util.Util;

public class TodayInfoAdapter extends RecyclerView.Adapter<TodayInfoAdapter.ViewHolder>{

    private final CurrentInfo currentDataSet;
    private Context context;

    public TodayInfoAdapter (CurrentInfo resourceDataSet) {
        currentDataSet = resourceDataSet;
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
        String status = currentDataSet.getCurrentLog().get(position);
        try {
            holder.timeLog.setText(Util.convertBeforeHours(currentDataSet.getCurrentTime(), position+1));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.valueLog.setText(Util.convertToStatus(holder.itemView.getContext(), status));
        holder.statusLog.setImageResource(Util.convertToImage(status));
    }

    @Override
    public int getItemCount() {
        return currentDataSet.getCurrentLog().size();
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
