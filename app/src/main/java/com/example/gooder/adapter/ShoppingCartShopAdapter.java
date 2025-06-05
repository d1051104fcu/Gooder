package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCartShop;
import com.example.gooder.model.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartShopAdapter extends RecyclerView.Adapter<ShoppingCartShopAdapter.ViewHolder> {
    List<ShoppingCartShop> shoppingCartShopList;
    private OnItemCheckChangedListener listener;
    private CompoundButton.OnCheckedChangeListener shopCheckListener;

    public ShoppingCartShopAdapter(List<ShoppingCartShop> list, OnItemCheckChangedListener listener){
        this.shoppingCartShopList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_cart_shop, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(shoppingCartShopList.get(position));
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

        private ShoppingCartItemAdapter itemAdapter;
        private final ShoppingCartShopAdapter adapterRef;

        public ViewHolder(@NonNull View itemView, ShoppingCartShopAdapter adapter) {
            super(itemView);
            this.adapterRef = adapter;

            isChoose = itemView.findViewById(R.id.shoppingCart_isChoose);
            shopName = itemView.findViewById(R.id.checkout_shopName);
            isExpand = itemView.findViewById(R.id.shoppingCart_isExpand);
            recyclerProducts = itemView.findViewById(R.id.checkout_recycler_products);

            recyclerProducts.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            itemAdapter = new ShoppingCartItemAdapter(new ArrayList<>(), () -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    adapterRef.notifyItemChanged(getAdapterPosition());
                    if (adapterRef.listener != null) adapterRef.listener.onItemCheckChangedListener();
                }
            });
            recyclerProducts.setAdapter(itemAdapter);
        }

        public void bind(ShoppingCartShop shop) {
            shopName.setText(shop.getShopName());

            // 取消 listener，避免迴圈觸發
            isChoose.setOnCheckedChangeListener(null);
            isChoose.setChecked(shop.isChoose());

            // 全選邏輯
            isChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                shop.setIsChoose(isChecked);
                for (ShoppingCartItem item : shop.getShoppingCartItemList()) {
                    item.setIsChoose(isChecked);
                }
                itemAdapter.updateData(shop.getShoppingCartItemList()); // 只會更新 CheckBox 狀態，不會重建 Adapter
                if (adapterRef.listener != null) adapterRef.listener.onItemCheckChangedListener();
            });

            // 展開邏輯
            isExpand.setImageResource(shop.isExpand() ? R.drawable.arrow_drop_up_24px : R.drawable.arrow_drop_down_24px);
            isExpand.setOnClickListener(v -> {
                shop.setIsExpand(!shop.isExpand());
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    adapterRef.notifyItemChanged(pos);
                }
            });

            // 更新資料
            List<ShoppingCartItem> items = shop.isExpand() ? shop.getShoppingCartItemList() : new ArrayList<>();
            itemAdapter.updateData(items);

            // 觸發一次子項檢查（確保 UI 正確）
            updateShopCheckBox(shop);
        }

        private void updateShopCheckBox(ShoppingCartShop shop) {
            isChoose.setOnCheckedChangeListener(null);
            isChoose.setChecked(shop.getShoppingCartItemList().stream().allMatch(ShoppingCartItem::isChoose));
            isChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                shop.setIsChoose(isChecked);
                for (ShoppingCartItem item : shop.getShoppingCartItemList()) {
                    item.setIsChoose(isChecked);
                }
                itemAdapter.updateData(shop.getShoppingCartItemList());
                if (adapterRef.listener != null) adapterRef.listener.onItemCheckChangedListener();
            });
        }
    }

}
