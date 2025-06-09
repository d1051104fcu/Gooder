package com.example.gooder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gooder.listener.ShopNameCallback;
import com.example.gooder.model.CheckoutItem;
import com.example.gooder.model.CheckoutShop;
import com.example.gooder.model.ShoppingCartItem;
import com.example.gooder.model.ShoppingCartShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String seller_id = "", name = "", imageURL = "", shopName = "我不告訴你 略略";
    Long price = 1L, amount = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String productId = getIntent().getStringExtra("productId");

        ImageView ivImage = findViewById(R.id.iv_product_image);
        TextView tvName = findViewById(R.id.tv_product_title);
        TextView tvCity = findViewById(R.id.tv_city);
        TextView tvMethod = findViewById(R.id.tv_method);
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvPrice = findViewById(R.id.price_tv);
        TextView tvAmount = findViewById(R.id.tv_amount);
        TextView tvSellerId = findViewById(R.id.tv_seller_id);
        Button addToShoppingCart = findViewById(R.id.product_addToShoppingCart);
        Button buyNow = findViewById(R.id.product_buyNow);
        String userId = auth.getCurrentUser().getUid();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        amount = documentSnapshot.getLong("amount");
                        String category = documentSnapshot.getString("category");
                        String city = documentSnapshot.getString("city");
                        String description = documentSnapshot.getString("description");
                        imageURL = documentSnapshot.getString("imageURL");
                        name = documentSnapshot.getString("name");
                        price = documentSnapshot.getLong("price");
                        seller_id = documentSnapshot.getString("seller_id");
                        String method = documentSnapshot.getString("transactionMethod");
                        getShopName(seller_id);

                        tvAmount.setText("數量： " + String.valueOf(amount));
                        tvCategory.setText("分類： " + category);
                        tvCity.setText(city);
                        tvDescription.setText("説明： " + description);
                        tvName.setText(name);
                        tvPrice.setText(String.valueOf(price) + '元');
                        tvSellerId.setText("賣家： " + seller_id);
                        tvMethod.setText(method);


                        Glide.with(this)
                                .load(imageURL)
                                .into(ivImage);
                    }
                });

        addToShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(userId).collection("ShoppingCart")
                        .whereEqualTo("product_id", productId).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Map<String, Object> shoppingCart = new HashMap<>();
                                shoppingCart.put("product_id", productId);
                                shoppingCart.put("amount", 1);
                                shoppingCart.put("seller_id", seller_id);

                                db.collection("Users").document(userId).collection("ShoppingCart")
                                        .add(shoppingCart)
                                        .addOnSuccessListener(task -> {
                                            Toast.makeText(ProductDetailActivity.this, "加入購物車成功！！", Toast.LENGTH_SHORT).show();
                                            Log.d("Product", "加入購物車成功" + task.getId());
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Product", "加入購物車失敗" + e.getMessage());
                                        });
                            } else {
                                int shoppingCartAmount = queryDocumentSnapshots.getDocuments().get(0).getLong("amount").intValue();
                                if (shoppingCartAmount >= amount){
                                    Toast.makeText(ProductDetailActivity.this, "商品數量不足", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                db.collection("Users").document(userId).collection("ShoppingCart").document(documentId)
                                        .update("amount", FieldValue.increment(1))
                                        .addOnSuccessListener(task -> {
                                            Toast.makeText(ProductDetailActivity.this, "加入購物車成功！！", Toast.LENGTH_SHORT).show();
                                            Log.d("Product", "更新購物車數量成功");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Product", "加入購物車失敗" + e.getMessage());
                                        });
                            }
                        });
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CheckoutShop> checkoutShopList = new ArrayList<>();
                List<CheckoutItem> checkoutItemList = new ArrayList<>();

                CheckoutItem item = new CheckoutItem("", productId, name, Math.toIntExact(price), imageURL, 1);
                checkoutItemList.add(item);

                CheckoutShop shop = new CheckoutShop(seller_id, shopName, checkoutItemList);
                checkoutShopList.add(shop);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("checkoutShopList", checkoutShopList);

                CheckoutFragment checkoutFragment = new CheckoutFragment();
                checkoutFragment.setArguments(bundle);

                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                intent.putExtra("targetFragment", "checkout");
                intent.putExtra("checkoutBundle", bundle);
                startActivity(intent);
            }
        });
    }

    private void getShopName(String uid) {
        db.collection("Users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        shopName = doc.getString("name");
                        Log.d("Product", "getShopName Success: " + shopName);
                    }
                })
                .addOnFailureListener(e -> Log.e("Product", "getShopName Failed: " + e.getMessage()));
    }
}