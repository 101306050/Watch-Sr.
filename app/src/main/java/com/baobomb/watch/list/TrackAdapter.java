package com.baobomb.watch.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baobomb.watch.R;

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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.card_track, null);
            holder.id = (TextView) view.findViewById(R.id.id);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.id.setText("裝置ID : "+items.get(i));
        return view;
    }

    class ViewHolder {
        TextView id;
    }
}