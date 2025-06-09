package com.example.gooder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gooder.R;
import com.example.gooder.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvTitle.setText(product.getName());
        holder.tvMethod.setText("交易方式: " + product.getMethod());
        holder.tvPrice.setText("$ " + product.getPrice());

        Glide.with(context)
                .load(product.getImageURL())
                .placeholder(R.drawable.ic_product) // 預設圖片
                .error(R.drawable.not_found) // 錯誤圖片
                .into(holder.ivImage);

        // TODO: 在這裡加入點擊事件，以進入商品詳情頁
        // holder.itemView.setOnClickListener(v -> { ... });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvMethod, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // 根據您的 item_product.xml 綁定 ID
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvTitle = itemView.findViewById(R.id.tv_product_title);
            tvMethod = itemView.findViewById(R.id.tv_product_method);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}