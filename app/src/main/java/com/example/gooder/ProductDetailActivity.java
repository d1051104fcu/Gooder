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

        TextView tvTitle = findViewById(R.id.tv_product_title);
        ImageView ivImage = findViewById(R.id.iv_product_image);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("test_gigang").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("Title");
                        String imageUrl = documentSnapshot.getString("ImageUrl");

                        tvTitle.setText(title);

                        Glide.with(this)
                                .load(imageUrl)
                                .into(ivImage);
                    }
                });
    }
}