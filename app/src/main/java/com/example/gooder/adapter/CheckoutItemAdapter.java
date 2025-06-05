package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.CheckoutItem;

import java.util.List;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder> {

    private List<CheckoutItem> checkoutItemList;

    public CheckoutItemAdapter(List<CheckoutItem> list) {
        this.checkoutItemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckoutItem checkoutItem = checkoutItemList.get(position);
        holder.imgProduct.setImageResource(checkoutItem.getImgId());
        holder.productName.setText(checkoutItem.getName());
        holder.productPrice.setText(String.format("$%d", checkoutItem.getPrice()));
        holder.productCount.setText(String.valueOf(checkoutItem.getCount()));
        holder.divider.setVisibility(position == checkoutItemList.size() - 1 ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return checkoutItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView productName, productPrice, productCount;
        View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.checkout_img);
            productName = itemView.findViewById(R.id.checkout_ProductName);
            productPrice = itemView.findViewById(R.id.checkout_ProductPrice);
            productCount = itemView.findViewById(R.id.checkout_ProductCount);
            divider = itemView.findViewById(R.id.checkout_divider);
        }
    }
}
