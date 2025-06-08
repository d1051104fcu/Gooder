package com.example.gooder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gooder.ProductDetailActivity;
import com.example.gooder.R;
import com.example.gooder.model.Product;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public SearchResultAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textTitle, textMethod, textPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_image);
            textTitle = itemView.findViewById(R.id.tv_product_title);
            textMethod = itemView.findViewById(R.id.tv_product_method);
            textPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textTitle.setText(product.getName());

        holder.textMethod.setText(product.getMethod());
        holder.textPrice.setText(product.getPrice() + " 元");

        Glide.with(context)
                .load(product.getImageURL())
                .into(holder.imageView);

        // 여기서 데이터 바인딩 (예: TextView, ImageView)
//        holder.textTitle.setText(product.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId()); // 🔹 ID 전달
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

