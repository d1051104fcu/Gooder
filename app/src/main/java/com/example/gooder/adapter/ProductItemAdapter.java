package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.ProductItem;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder> {

    private List<ProductItem> productList;

    public ProductItemAdapter(List<ProductItem> productList) {
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, priceView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            nameView = itemView.findViewById(R.id.productName);
            priceView = itemView.findViewById(R.id.productPrice);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductItem item = productList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.nameView.setText(item.getName());
        holder.priceView.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
