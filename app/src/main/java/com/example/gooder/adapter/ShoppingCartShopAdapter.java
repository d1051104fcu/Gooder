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
import com.example.gooder.model.ShoppingCartShop;
import com.example.gooder.model.ShoppingCartItem;

import java.util.List;

public class ShoppingCartShopAdapter extends RecyclerView.Adapter<ShoppingCartShopAdapter.ViewHolder> {
    List<ShoppingCartShop> shoppingCartShopList;
    private OnItemCheckChangedListener listener;

    public ShoppingCartShopAdapter(List<ShoppingCartShop> list, OnItemCheckChangedListener listener){
        this.shoppingCartShopList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shopping_cart_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartShop shoppingCartShop = shoppingCartShopList.get(position);
        holder.isChoose.setChecked(shoppingCartShop.isChoose());
        holder.shopName.setText(shoppingCartShop.getShopName());

        // isExpand
        holder.isExpand.setImageResource(
                shoppingCartShop.isExpand() ? R.drawable.arrow_drop_up_24px : R.drawable.arrow_drop_down_24px
        );
        holder.isExpand.setOnClickListener(v -> {
            shoppingCartShop.setIsExpand(!shoppingCartShop.isExpand());
            notifyItemChanged(position);
        });

        List<ShoppingCartItem> itemsToShow = shoppingCartShop.isExpand()
                ? shoppingCartShop.getShoppingCartItemList()
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
            shoppingCartShop.setIsChoose(isChecked);
            for (ShoppingCartItem item : shoppingCartShop.getShoppingCartItemList()) {
                item.setIsChoose(isChecked);
            }

            for (int i = 0; i < shoppingCartShop.getShoppingCartItemList().size(); i++) {
                shoppingCartItemAdapter.notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartShopList.size();
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
