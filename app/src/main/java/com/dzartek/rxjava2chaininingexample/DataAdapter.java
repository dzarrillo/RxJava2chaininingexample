package com.dzartek.rxjava2chaininingexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dzarrillo on 2/25/2018.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{
    private final String TAG = DataAdapter.class.getName();
    private List<String> mDataList = new ArrayList<>();

    public DataAdapter(List<String> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
            v.setFocusable(true);
            return new DataViewHolder(v);
        } else {
            throw new RuntimeException(TAG + " Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.textViewData.setText(mDataList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }


    public class DataViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textViewData)
        TextView textViewData;

        public DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
