package com.abhi.logsbrologs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by men_in_black007 on 3/4/17.
 */

public class LogsRecyclerAdapter extends RecyclerView.Adapter<LogsRecyclerAdapter.MyViewHolder> {

    private List<LogsModel> logsModelList;
    private Context mContext;

    public LogsRecyclerAdapter(Context context, List<LogsModel> logs) {
        logsModelList = logs;
        mContext = context;
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
