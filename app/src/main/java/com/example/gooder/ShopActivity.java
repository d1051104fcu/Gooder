package com.example.gooder;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gooder.adapter.ProductAdapter;
import com.example.gooder.model.Product;
import com.example.gooder.model.User; // 假設您有一個 User 模型
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    public static final String EXTRA_SELLER_ID = "SELLER_ID";
    private static final String TAG = "ShopActivity";

    // UI 元件
    private MaterialToolbar toolbar;
    private ImageView ivSellerLogo;
    private TextView tvSellerName, tvSellerRating, tvAnnouncement;
    private RecyclerView rvProductList;

    // Adapter 和資料
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private String id;

    // Firebase
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        db = FirebaseFirestore.getInstance();

        // 從 Intent 獲取賣家 ID
        String sellerId = getIntent().getStringExtra(EXTRA_SELLER_ID);
        if (sellerId == null || sellerId.isEmpty()) {
            Toast.makeText(this, "無法載入賣場資訊", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindViews();
        setupRecyclerView();

        toolbar.setNavigationOnClickListener(v -> finish());

        loadSellerInfo(sellerId);
        loadSellerProducts(sellerId);
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar_shop);
        ivSellerLogo = findViewById(R.id.iv_seller_logo);
        tvSellerName = findViewById(R.id.tv_seller_name);
        tvSellerRating = findViewById(R.id.tv_seller_rating);
        tvAnnouncement = findViewById(R.id.tv_announcement);
        rvProductList = findViewById(R.id.rv_product_list);
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(productAdapter);
    }

    private void loadSellerInfo(String sellerId) {
        // 假設您的使用者資料儲存在 "users" 集合中
        db.collection("users").document(sellerId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // 假設您有一個 User 模型
                        User seller = documentSnapshot.toObject(User.class);
                        if (seller != null) {
                            tvSellerName.setText(seller.getName()); // 假設 User 有 getName 方法
                            // TODO: 根據您的 User 模型設定 Logo、評分和公告
                            // tvSellerRating.setText("...");
                            // tvAnnouncement.setText("...");
                            // Glide.with(this).load(seller.getLogoUrl()).into(ivSellerLogo);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading seller info", e));
    }

    private void loadSellerProducts(String sellerId) {
        // 假設您的商品資料儲存在 "products" 集合中，且有名為 "seller_id" 的欄位
        db.collection("Products").whereEqualTo("seller_id", sellerId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
//                        product.setId(document.getId()); // 將文件 ID 存入模型
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading products", e));
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

}