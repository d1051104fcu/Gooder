package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCart;
import com.example.gooder.model.ShoppingCartItem;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    List<ShoppingCart> shoppingCartList;

    public ShoppingCartAdapter(List<ShoppingCart> list){
        this.shoppingCartList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shopping_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCart shoppingCart = shoppingCartList.get(position);
        holder.isChoose.setChecked(shoppingCart.isChoose());
        holder.shopName.setText(shoppingCart.getShopName());

        // isExpand
        holder.isExpand.setImageResource(
                shoppingCart.isExpand() ? R.drawable.arrow_drop_up_24px : R.drawable.arrow_drop_down_24px
        );
        holder.isExpand.setOnClickListener(v -> {
            shoppingCart.setIsExpand(!shoppingCart.isExpand());
            notifyItemChanged(position);
        });

        List<ShoppingCartItem> itemsToShow = shoppingCart.isExpand()
                ? shoppingCart.getShoppingCartItems()
                : null;

        // isChoose
        final ShoppingCartItemAdapter[] adapterHolder = new ShoppingCartItemAdapter[1];
        ShoppingCartItemAdapter shoppingCartItemAdapter = new ShoppingCartItemAdapter(
                itemsToShow,
                new OnItemCheckChangedListener(){
                    @Override
                    public void onItemCheckChangedListener() {
                        holder.isChoose.setChecked(adapterHolder[0].isAllChecked());
                    }
        });
        adapterHolder[0] = shoppingCartItemAdapter;

        holder.recyclerProducts.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerProducts.setAdapter(shoppingCartItemAdapter);

        holder.isChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingCart.setIsChoose(isChecked);
            for (ShoppingCartItem item : shoppingCart.getShoppingCartItems()) {
                item.setIsChoose(isChecked);
            }

            for (int i = 0; i < shoppingCart.getShoppingCartItems().size(); i++) {
                shoppingCartItemAdapter.notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox isChoose;
        TextView shopName;
        ImageButton isExpand;
        RecyclerView recyclerProducts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isChoose = itemView.findViewById(R.id.shoppingCart_isChoose);
            shopName = itemView.findViewById(R.id.shoppingCart_shopName);
            isExpand = itemView.findViewById(R.id.shoppingCart_isExpand);
            recyclerProducts = itemView.findViewById(R.id.shoppingCart_recycler_products);
        }
    }

}
