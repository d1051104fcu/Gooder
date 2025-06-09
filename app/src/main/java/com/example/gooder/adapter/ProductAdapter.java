package com.example.gooder.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gooder.ProductDetailActivity;
import com.example.gooder.R;
import com.example.gooder.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, method, price;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_home);
            title = itemView.findViewById(R.id.tv_title_home);
            method = itemView.findViewById(R.id.tv_method_home);
            price = itemView.findViewById(R.id.tv_price_home);

        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_2, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
//        holder.imageView.setImageResource(product.getImageURL());
        holder.title.setText(product.getName());
        holder.method.setText(product.getMethod());
        holder.price.setText(String.valueOf(product.getPrice() + " 元"));

        Glide.with(context)
                .load(product.getImageURL())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onItemClick(product); // 클릭 이벤트 전달
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}
