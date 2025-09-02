package com.m.manga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m.manga.Activity.InformationActivity;
import com.m.manga.R;
import com.m.manga.classes.ApiBean;

import java.util.List;

public class PageAdapter2 extends RecyclerView.Adapter<PageAdapter2.ViewHolder> {
    private List<ApiBean.Manga> dataList;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView animeImg;
        TextView title;
        public ViewHolder(View view) {
            super(view);
            animeImg = view.findViewById(R.id.animeImg);
            title = view.findViewById(R.id.title);

        }

    }

    public PageAdapter2(Context context, List<ApiBean.Manga> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.anime_item, viewGroup, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ApiBean.Manga data = dataList.get(position);
        holder.title.setTextColor(Color.parseColor("#262626"));

        Glide.with(context)
                .asBitmap().
                load(data.getImgUrl())
                .centerCrop()
                .into(holder.animeImg);
        holder.title.setText(data.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openanime = new Intent(context, InformationActivity.class);
                openanime.putExtra("animeTitle",data.getTitle());
                openanime.putExtra("desc",data.getDescription());
                openanime.putExtra("thumbUrl",data.getImgUrl());
                openanime.putExtra("Id",data.getId());
                context.startActivity(openanime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}