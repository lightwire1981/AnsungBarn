package kr.co.senko.ansungbarnmon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.senko.ansungbarnmon.R;
import kr.co.senko.ansungbarnmon.db.RegionInfo;
import kr.co.senko.ansungbarnmon.util.Util;

public class RegionInfoAdapter extends RecyclerView.Adapter<RegionInfoAdapter.ViewHolder>{

    private final ArrayList<RegionInfo> regionDataSet;

    private Context context;


    public interface RegionSelectListener {
        void result(String regionName);
    }

    private final RegionSelectListener regionSelectListener;

    public RegionInfoAdapter(ArrayList<RegionInfo> resourceDataSet, RegionSelectListener listener) {
        regionDataSet = resourceDataSet;
        this.regionSelectListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_region, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.regionGroup = regionDataSet.get(position).getGroupID();
        holder.regionName.setText(regionDataSet.get(position).getGroupName());
        holder.regionStatus.setImageResource(Util.convertToImage(regionDataSet.get(position).getCurrentValue()));
        holder.regionValue.setText(Util.convertToStatus(holder.itemView.getContext(), regionDataSet.get(position).getCurrentValue()));
        holder.setListener(regionSelectListener);
    }

    @Override
    public int getItemCount() {
        return regionDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String regionGroup;
        TextView regionName, regionValue;
        ImageView regionStatus;
        RegionInfoAdapter.RegionSelectListener regionSelectListener;

        public void setListener(RegionInfoAdapter.RegionSelectListener listener) {
            regionSelectListener = listener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            regionName = itemView.findViewById(R.id.tVwRegionName);
            regionValue = itemView.findViewById(R.id.tVwRegionValue);
            regionStatus = itemView.findViewById(R.id.iVwRegionStatus);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
//                    Toast.makeText(itemView.getContext(), pos+"번 선택", Toast.LENGTH_SHORT).show();
                    regionSelectListener.result(regionGroup);
                }

            });
        }
    }
}
