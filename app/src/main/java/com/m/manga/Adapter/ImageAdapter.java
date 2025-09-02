package com.m.manga.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.m.manga.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imageUrls;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_anime;
        TextView page_number;

        public ViewHolder(View view) {
            super(view);
            image_anime = view.findViewById(R.id.image_anime);
            page_number = view.findViewById(R.id.page_number);
            image_anime.setPadding(0, 0, 0, 0);
        }
    }

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_item, viewGroup, false);
        view.setPadding(0, 0, 0, 0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String image = imageUrls.get(position);
        holder.page_number.setText(String.valueOf(position + 1));

//        Log.d("Image_url","value: "+image);


        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.mipmap.app_icon)
                .error(R.mipmap.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .asBitmap()
                .load(image)
                .apply(requestOptions)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerInside()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        // Handle failure if needed
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model,
                                                   Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.image_anime.setImageBitmap(resource);
                        return true;
                    }
                })
                .into(holder.image_anime);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}