package com.baobomb.watch.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baobomb on 2015/10/8.
 */
public class TrackAdapter extends BaseAdapter {

    List<String> items = new ArrayList<>();

    Context context;

    public TrackAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public String getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TrackItemView trackItemView;
        if (view == null) {
            trackItemView = new TrackItemView(context);
        } else {
            trackItemView = (TrackItemView) view;
        }
        trackItemView.bind(items.get(i));
        return trackItemView;
    }
}