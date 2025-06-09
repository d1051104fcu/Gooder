package com.example.gooder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.AdActivity;
import com.example.gooder.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private List<Integer> imageList;

    public ImageSliderAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product1); // object in slider_image.xml
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image, parent, false); // slider_image.xml
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));

        holder.imageView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, AdActivity.class); // 광고 액티비티

            // 이미지에 따라 다른 정보 전달 (예: 광고 ID)
            intent.putExtra("image_index", position); // 또는 imageList.get(position)
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}

