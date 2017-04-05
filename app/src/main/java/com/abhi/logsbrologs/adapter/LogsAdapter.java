package com.abhi.logsbrologs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhi.logsbrologs.MainActivity;
import com.abhi.logsbrologs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by men_in_black007 on 3/4/17.
 */

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {

    private List<LogsModel> logsModelList,itemsCopy;
    private Context mContext;

    public LogsAdapter(Context context, List<LogsModel> logs) {
        logsModelList = logs;
        mContext = context;
        this.itemsCopy=new ArrayList<>();
        this.itemsCopy.addAll(this.logsModelList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.recyclerview_item, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(logsModelList.get(position).getLog());
    }



    public void filter(String text) {
        logsModelList.clear();
        if(text.isEmpty()){
            MainActivity.isSearching = false;
            logsModelList.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(LogsModel item: itemsCopy){
                if(item.getLog().toLowerCase().contains(text)){
                    logsModelList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return logsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}