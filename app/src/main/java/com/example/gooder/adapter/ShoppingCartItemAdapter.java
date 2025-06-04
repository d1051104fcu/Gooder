package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCartItem;

import java.util.List;


public class ShoppingCartItemAdapter extends RecyclerView.Adapter<ShoppingCartItemAdapter.ViewHolder> {
    List<ShoppingCartItem> shoppingCartItemList;
    private OnItemCheckChangedListener listener;

    public ShoppingCartItemAdapter(List<ShoppingCartItem> list, OnItemCheckChangedListener listener){
        this.shoppingCartItemList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemList.get(position);
        holder.imgProduct.setImageResource(shoppingCartItem.getImgId());
        holder.productName.setText(shoppingCartItem.getName());
        holder.productPrice.setText("$" + shoppingCartItem.getPrice());
        holder.productCount.setText(shoppingCartItem.getCount());
        holder.isChoose.setChecked(shoppingCartItem.isChoose());
        holder.divider.setVisibility(position == shoppingCartItemList.size() - 1 ? View.GONE : View.VISIBLE);

        holder.isChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingCartItem.setIsChoose(isChecked);
            if (listener != null){
                listener.onItemCheckChangedListener();
            }

            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.plus.setOnClickListener(v -> {
            shoppingCartItem.setCount(shoppingCartItem.getCount() + 1);
            holder.productCount.setText(shoppingCartItem.getCount());
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.minus.setOnClickListener(v -> {
            if (shoppingCartItem.getCount() > 1){
                shoppingCartItem.setCount(shoppingCartItem.getCount() - 1);
                holder.productCount.setText(shoppingCartItem.getCount());
                notifyItemChanged(holder.getAdapterPosition());
            }else {
                // Need to fix: Remove from firebase
                shoppingCartItemList.remove(shoppingCartItem);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount(){
        return shoppingCartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox isChoose;
        ImageView imgProduct;
        TextView productName, productPrice, productCount;
        Button minus, plus;
        View divider;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            isChoose = itemView.findViewById(R.id.shoppingCart_item_isChoose);
            imgProduct = itemView.findViewById(R.id.shoppingCart_img);
            productName = itemView.findViewById(R.id.shoppingCart_ProductName);
            productPrice = itemView.findViewById(R.id.shoppingCart_ProductPrice);
            productCount = itemView.findViewById(R.id.shoppingCart_ProductCount_number);
            minus = itemView.findViewById(R.id.shoppingCart_ProductCount_minus);
            plus = itemView.findViewById(R.id.shoppingCart_ProductCount_plus);
            divider = itemView.findViewById(R.id.shoppingCart_divider);
        }
    }

    public boolean isAllChecked(){
        for (ShoppingCartItem item : shoppingCartItemList){
            if (!item.isChoose()){
                return false;
            }
        }
        return true;
    }
}
