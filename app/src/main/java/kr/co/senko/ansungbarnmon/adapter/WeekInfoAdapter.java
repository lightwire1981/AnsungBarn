package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import kr.co.senko.ansungbarnmon.R;

public class WeekInfoAdapter extends RecyclerView.Adapter<WeekInfoHolder> {

    @NonNull
    @Override
    public WeekInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.page_week, parent, false);
        return new WeekInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekInfoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
