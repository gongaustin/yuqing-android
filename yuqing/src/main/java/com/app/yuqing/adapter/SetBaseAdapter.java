package com.app.yuqing.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public abstract class SetBaseAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected Context mContext;
    public SetBaseAdapter(Context context,List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void updateData(List<T> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        this.mList.add(t);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}


