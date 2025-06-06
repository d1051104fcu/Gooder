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
        TextView tvTitle = findViewById(R.id.tv_product_title);
        TextView tvCity = findViewById(R.id.tv_city);
        TextView tvMethod = findViewById(R.id.tv_method);
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvBody = findViewById(R.id.tv_body);
        TextView tvPrice = findViewById(R.id.price_tv);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("test_gigang2").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("Title");
                        String imageUrl = documentSnapshot.getString("ImageUrl");
                        String method = documentSnapshot.getString("TransactionMethod");
                        String category = documentSnapshot.getString("Category");
                        String body = documentSnapshot.getString("Body");
                        Long price = documentSnapshot.getLong("Price");
                        // Price
                        // Body
                        // TransactionMethod
                        // Category

                        tvTitle.setText(title);
//                        tvCity.setText();
                        tvCategory.setText(category);
                        tvBody.setText(body);
                        tvMethod.setText(method);
                        tvPrice.setText(String.valueOf(price) + '元');

                        Glide.with(this)
                                .load(imageUrl)
                                .into(ivImage);
                    }
                });
    }
}