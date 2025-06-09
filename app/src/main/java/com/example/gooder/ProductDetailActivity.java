package com.example.gooder;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class ProductDetailActivity extends AppCompatActivity {

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long amount = documentSnapshot.getLong("amount");
                        String category = documentSnapshot.getString("category");
                        String city = documentSnapshot.getString("city");
                        String description = documentSnapshot.getString("description");
                        String imageURL = documentSnapshot.getString("imageURL");
                        String name = documentSnapshot.getString("name");
                        Long price = documentSnapshot.getLong("price");
                        String seller_id = documentSnapshot.getString("seller_id");
                        String method = documentSnapshot.getString("transactionMethod");

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
    }
}