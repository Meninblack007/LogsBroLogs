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

    public String log;
    private String time;
    private Constants.Loglevel loglevel;

    public LogsItem(String log, String time, Constants.Loglevel loglevel) {
        this.log = log;
        this.time = time;
        this.loglevel = loglevel;
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
        switch (loglevel) {
            case LOGLEVEL_I:
                viewHolder.loglevel.setImageResource(R.drawable.i);
                break;
            case LOGLEVEL_V:
                viewHolder.loglevel.setImageResource(R.drawable.v);
                break;
            case LOGLEVEL_W:
                viewHolder.loglevel.setImageResource(R.drawable.w);
                break;
            case LOGLEVEL_D:
                viewHolder.loglevel.setImageResource(R.drawable.d);
                break;
            case LOGLEVEL_E:
                viewHolder.loglevel.setImageResource(R.drawable.e);
                break;
            case LOGLEVEL_F:
                viewHolder.loglevel.setImageResource(R.drawable.f);
                break;
            case LOGLEVEL_UNDEFINED:
        }
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        TextView time;
        ImageView loglevel;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.log);
            time = (TextView) view.findViewById(R.id.time);
            loglevel = (ImageView) view.findViewById(R.id.loglevel);

        }
    }

}
