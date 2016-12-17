package com.example.povilas.gameslibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by povilas on 16.12.15.
 */

public class NewsAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<News> mNews;

    public NewsAdapter(List<News> mNews, Context mContext) {
        this.mNews = mNews;
        this.mContext = mContext;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public Object getItem(int i) {
        return mNews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.news_layout_item, null);
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.ivNews);
        Picasso.with(mContext).load(mNews.get(position).header_img).into(viewHolder.thumbnail);

        viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
        viewHolder.title.setText(mNews.get(position).title);

        viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
        viewHolder.date.setText(mNews.get(position).date);

        viewHolder.content = (TextView) convertView.findViewById(R.id.tvContent);
        viewHolder.content.setText(mNews.get(position).content);

        return convertView;
    }

    private class ViewHolder {
        ImageView thumbnail;
        TextView title, date, content;

    }
}