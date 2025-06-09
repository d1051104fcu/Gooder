package com.example.gooder.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gooder.R;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ShoppingCartItemAdapter extends RecyclerView.Adapter<ShoppingCartItemAdapter.ViewHolder> {
    List<ShoppingCartItem> shoppingCartItemList;
    private OnItemCheckChangedListener listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    // Map<itemId, maxAmount>
    Map<String, Integer> amountMap = new HashMap<>();


    public ShoppingCartItemAdapter(List<ShoppingCartItem> list, OnItemCheckChangedListener listener){
        this.shoppingCartItemList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_cart_item, parent, false);
        getMaxAmount();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemList.get(position);

        holder.productName.setText(shoppingCartItem.getName());
        holder.productPrice.setText(String.format("$%d", shoppingCartItem.getPrice()));
        holder.productCount.setText(String.valueOf(shoppingCartItem.getCount()));
        holder.divider.setVisibility(position == shoppingCartItemList.size() - 1 ? View.GONE : View.VISIBLE);
        Glide.with(holder.itemView.getContext())
                .load(shoppingCartItem.getImgId())
                        .error(R.drawable.not_found)
                                .into(holder.imgProduct);

        // 先移除 listener，避免觸發循環更新
        holder.isChoose.setOnCheckedChangeListener(null);
        holder.isChoose.setChecked(shoppingCartItem.isChoose());

        // 重新設置 listener
        holder.isChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingCartItem.setIsChoose(isChecked);
            if (listener != null) {
                listener.onItemCheckChangedListener();
            }

            // 延遲執行，避免 crash
            //holder.itemView.post(() -> notifyItemChanged(position));
        });

        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        holder.plus.setOnClickListener(v -> {
            String productId = shoppingCartItem.getProductId();
            String shoppingCartIdId = shoppingCartItem.getShoppingCartId();

            if (shoppingCartItem.getCount() >= amountMap.getOrDefault(productId, 1)){
                Toast.makeText(holder.itemView.getContext(), "已達商品剩餘上限", Toast.LENGTH_SHORT).show();
            }else {
                db.collection("Users").document(uid).collection("ShoppingCart").document(shoppingCartIdId)
                        .update("amount", FieldValue.increment(1))
                        .addOnSuccessListener(task -> {
                            shoppingCartItem.setCount(shoppingCartItem.getCount() + 1);
                            holder.productCount.setText(String.valueOf(shoppingCartItem.getCount()));

                            if (listener != null) {
                                listener.onItemCheckChangedListener();
                            }

                            Log.i("ShoppingCart", "Item plus");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ShoppingCart", "Item plus failed" + e.getMessage());
                        });
            }
        });

        holder.minus.setOnClickListener(v -> {
            String shoppingCartIdId = shoppingCartItem.getShoppingCartId();

            if (shoppingCartItem.getCount() > 1) {
                db.collection("Users").document(uid).collection("ShoppingCart").document(shoppingCartIdId)
                        .update("amount", FieldValue.increment(-1))
                        .addOnSuccessListener(task -> {
                            shoppingCartItem.setCount(shoppingCartItem.getCount() - 1);
                            holder.productCount.setText(String.valueOf(shoppingCartItem.getCount()));

                            if (listener != null) {
                                listener.onItemCheckChangedListener();
                            }

                            Log.i("ShoppingCart", "Item minus");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ShoppingCart", "Item minus failed" + e.getMessage());
                        });
            } else {
                db.collection("Users").document(uid).collection("ShoppingCart").document(shoppingCartIdId)
                        .delete()
                        .addOnSuccessListener(task -> {
                            holder.itemView.post(() -> notifyItemChanged(position));
                            shoppingCartItemList.remove(position);
                            notifyItemRemoved(position);

                            if (listener != null) {
                                listener.onItemCheckChangedListener();
                            }

                            Log.i("ShoppingCart", "Item removed");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ShoppingCart", "Item remove failed" + e.getMessage());
                        });
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

    public void updateData(List<ShoppingCartItem> newList) {
        this.shoppingCartItemList = newList;
        notifyDataSetChanged();
    }

    public void getMaxAmount(){
        for (ShoppingCartItem item: shoppingCartItemList){
            String productId = item.getProductId();
            if (amountMap.containsKey(productId)) continue;
            db.collection("Products").document(productId)
                    .get().addOnSuccessListener(doc -> {
                        int maxAmount = Objects.requireNonNull(doc.getLong("amount")).intValue();
                        amountMap.put(productId, maxAmount);

                        Log.i("ShoppingCart", "productId: " + productId + " maxAmount: " + maxAmount);
                    });
        }
    }

    /*
    private void tempAddData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference productsCollection = db.collection("Products");
        Map<String, Object> product = new HashMap<>();
        product.put("amount", 1);
        product.put("category", "服飾");
        product.put("city", "台中");
        product.put("description", "test product 的 description");
        product.put("imageURL", "0");
        product.put("name", "testProduct");
        product.put("price", 200);
        product.put("seller_id", "4ofaHQnw54NPnZjOfl4LpxpJSAD2");
        product.put("transactionMethod", "面交");

        productsCollection.add(product);
    }
    */
}
