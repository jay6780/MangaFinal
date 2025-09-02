package com.m.manga.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m.manga.Activity.ViewChapterActivity;
import com.m.manga.R;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.AppConstant;
import com.m.manga.classes.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailAdapter extends BaseAdapter {
    private List<ApiBean.Chapters> chaptersList;
    private Context context;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";

    public void ascData(List<ApiBean.Chapters> datalist) {
        try {
            chaptersList.clear();
            chaptersList.addAll(datalist);
            Collections.sort(chaptersList, (c1, c2) -> {
                try {
                    int id1 = Integer.parseInt(c1.getChapterId());
                    int id2 = Integer.parseInt(c2.getChapterId());
                    return Integer.compare(id1, id2);
                } catch (NumberFormatException e) {
                    return c1.getChapterId().compareTo(c2.getChapterId());
                }
            });
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            chaptersList.addAll(datalist);
            notifyDataSetChanged();
        }
    }

    public void descData(List<ApiBean.Chapters> datalist) {
        chaptersList.clear();
        chaptersList.addAll(datalist);
        Collections.sort(chaptersList, (c1, c2) -> {
            try {
                int id1 = Integer.parseInt(c1.getChapterId());
                int id2 = Integer.parseInt(c2.getChapterId());
                return Integer.compare(id2, id1);
            } catch (NumberFormatException e) {
                return c2.getChapterId().compareTo(c1.getChapterId());
            }
        });

        notifyDataSetChanged();
    }

    public DetailAdapter(Context context, List<ApiBean.Chapters> chaptersList) {
        this.context = context;
        this.chaptersList = chaptersList;
        appSettingsPrefs = context.getSharedPreferences(PREF, MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        Log.d("DetailAdapter", "Item count: " + chaptersList.size());
        return chaptersList.size();
    }

    @Override
    public Object getItem(int position) {
        return chaptersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
            holder = new ViewHolder();
            holder.chapterId = convertView.findViewById(R.id.chapterId);
            holder.views = convertView.findViewById(R.id.views);
            holder.uploaded = convertView.findViewById(R.id.uploaded);
            holder.timestamp = convertView.findViewById(R.id.timestamp);
            holder.viewSeperator = convertView.findViewById(R.id.viewSeperator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ApiBean.Chapters data = chaptersList.get(position);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);

        if(isNightModeOn) {
            holder.chapterId.setTextColor(Color.parseColor("#ffffff"));
            holder.views.setTextColor(Color.parseColor("#ffffff"));
            holder.uploaded.setTextColor(Color.parseColor("#ffffff"));
            holder.timestamp.setTextColor(Color.parseColor("#ffffff"));
            holder.viewSeperator.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.chapterId.setTextColor(Color.parseColor("#000000"));
            holder.views.setTextColor(Color.parseColor("#000000"));
            holder.uploaded.setTextColor(Color.parseColor("#000000"));
            holder.timestamp.setTextColor(Color.parseColor("#000000"));
            holder.viewSeperator.setBackgroundColor(Color.parseColor("#000000"));
        }
//        Log.d("Chapter","value: "+data.getChapterId());
        holder.chapterId.setText("Chapter: "+data.getChapterId());
        holder.views.setText("Views: "+data.getViews());
        holder.uploaded.setText(data.getUploaded());
        holder.timestamp.setText(data.getTimestamp());

        convertView.setOnClickListener(v -> {
            Intent openanime = new Intent(context, ViewChapterActivity.class);
            openanime.putExtra("chapterId", data.getChapterId());
            openanime.putExtra("Id", SPUtils.getInstance().getString(AppConstant.mangaId));
            context.startActivity(openanime);
        });

        return convertView;
    }

    public void clearList() {
        chaptersList.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView chapterId, views, uploaded, timestamp;
        View viewSeperator;
    }
}