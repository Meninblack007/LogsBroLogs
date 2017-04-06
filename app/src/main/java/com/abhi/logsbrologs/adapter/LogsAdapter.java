package com.abhi.logsbrologs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhi.logsbrologs.R;

import java.util.List;

/**
 * Created by men_in_black007 on 3/4/17.
 */

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {

    private List<LogsModel> logsModelList;
    private Context mContext;

    public LogsAdapter(Context context, List<LogsModel> logs) {
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
        String log = logsModelList.get(position).getLog();
        String [] splitWord = log.split("\\s+");
        if (splitWord.length > 4 ) {
            char logType = splitWord[4].charAt(0);
            switch (logType) {
                case 'E':
                case 'A':
                    holder.title.setTextColor(mContext.getColor(R.color.orange));
                    break;
                case 'W':
                    holder.title.setTextColor(mContext.getColor(R.color.yellow));
                    break;
                case 'D':
                    holder.title.setTextColor(mContext.getColor(R.color.blue));
                    break;
                case 'I':
                    holder.title.setTextColor(mContext.getColor(R.color.yellow));
                    break;
                case 'V':
                    holder.title.setTextColor(mContext.getColor(R.color.blue));
                    break;
                case 'F':
                    holder.title.setTextColor(mContext.getColor(R.color.red));
                    break;
                default:
                    holder.title.setText(logsModelList.get(position).getLog());
                    break;
            }
        }
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
