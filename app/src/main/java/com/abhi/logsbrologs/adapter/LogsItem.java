package com.abhi.logsbrologs.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhi.logsbrologs.Constants;
import com.abhi.logsbrologs.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by Abhishek on 03-05-2017.
 */

public class LogsItem extends AbstractItem<LogsItem, LogsItem.ViewHolder> {

    private String log;
    private String time;
    private Constants.LogLevel logLevel;

    public LogsItem(String log, String time, Constants.LogLevel logLevel) {
        this.log = log;
        this.time = time;
        this.logLevel = logLevel;
    }

    public void setLog(String Log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    @Override
    public int getType() {
        return R.id.log;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fastadapter_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        if (log != null)
            viewHolder.title.setText(log);
        if (time != null)
            viewHolder.time.setText(time);

        switch (logLevel) {
            case LOGLEVEL_I:
                viewHolder.logLevel.setImageResource(R.drawable.i);
                break;
            case LOGLEVEL_V:
                viewHolder.logLevel.setImageResource(R.drawable.v);
                break;
            case LOGLEVEL_W:
                viewHolder.logLevel.setImageResource(R.drawable.w);
                break;
            case LOGLEVEL_D:
                viewHolder.logLevel.setImageResource(R.drawable.d);
                break;
            case LOGLEVEL_E:
                viewHolder.logLevel.setImageResource(R.drawable.e);
                break;
            case LOGLEVEL_F:
                viewHolder.logLevel.setImageResource(R.drawable.f);
                break;
            case LOGLEVEL_DENIALS:
                viewHolder.logLevel.setImageResource(R.drawable.denial);
                break;
            case LOGLEVEL_UNDEFINED:
                break;
        }
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        TextView time;
        ImageView logLevel;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.log);
            time = (TextView) view.findViewById(R.id.time);
            logLevel = (ImageView) view.findViewById(R.id.loglevel);
        }
    }
}
