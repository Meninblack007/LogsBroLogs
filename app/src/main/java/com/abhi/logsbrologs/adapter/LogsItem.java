package com.abhi.logsbrologs.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abhi.logsbrologs.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by Abhishek on 03-05-2017.
 */

public class LogsItem extends AbstractItem<LogsItem, LogsItem.ViewHolder> {

    public String log;

    public LogsItem(String log) {
        this.log = log;
    }

    @Override
    public int getType() {
        return R.id.title;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fastadapter_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.title.setText(log);

    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

        }
    }

}