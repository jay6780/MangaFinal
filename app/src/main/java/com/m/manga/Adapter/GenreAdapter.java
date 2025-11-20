package com.m.manga.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.m.manga.classes.Listerner.GenreNameListerner;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    List<String> genreNameList;
    Context context;
    int selectPosition = -1;
    private GenreNameListerner genreNameListerner;
    private boolean isLess = false;

    public void firstHighlight(int firstPosition) {
        selectPosition = firstPosition;
        genreNameListerner.getName(genreNameList.get(firstPosition));
        notifyDataSetChanged();
    }

    public void expand(boolean isExpand) {
        isLess = isExpand;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView genre_name;
        public ViewHolder(View view) {
            super(view);
            genre_name = view.findViewById(R.id.genre_name);
        }
    }

    public GenreAdapter(Context context,List<String> genreNameList,GenreNameListerner genreNameListerner) {
        this.context = context;
        this.genreNameList = genreNameList;
        this.genreNameListerner = genreNameListerner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.genre_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String genreName = genreNameList.get(position);

        if (selectPosition == position) {
            holder.genre_name.setBackgroundResource(R.drawable.bg_selected);
            holder.genre_name.setTextColor(Color.WHITE);
        } else {
            holder.genre_name.setBackgroundResource(R.drawable.bg_unselected);
            holder.genre_name.setTextColor(Color.BLACK);
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)  holder.genre_name.getLayoutParams();
        if(isLess){
            params.setMargins(20,15,20,10);
        }else{
            params.setMargins(5,0,5,0);
        }
        holder.genre_name.setLayoutParams(params);

        holder.genre_name.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        holder.genre_name.setText(genreName);

        holder.itemView.setOnClickListener(v -> {
            if (selectPosition == position) {
                selectPosition = -1;
                genreNameListerner.getName("");
            } else {
                selectPosition = position;
                genreNameListerner.getName(genreName);
                isLess = false;
                genreNameListerner.isExpand(true,selectPosition);
            }
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return genreNameList.size();
    }
}