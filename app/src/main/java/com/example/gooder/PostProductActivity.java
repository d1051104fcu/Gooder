/*
package com.example.gooder;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
*/

package com.example.gooder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gooder.model.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostProductActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextInputEditText etProductName, etImageUrl, etDescription, etAmount, etPrice;
    private ImageView ivImagePreview;
    private Button btnPostProduct;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product); // <-- 修改為新的 layout 檔名

        // 初始化 Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 綁定 UI
        bindViews();

        // 設定 Toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // 監聽圖片 URL 輸入，並使用 Glide 載入預覽
        etImageUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Glide.with(PostProductActivity.this)
                        .load(s.toString())
                        .placeholder(R.drawable.ic_product) // <-- 修改為預設圖片:ic_add_photo
                        .error(R.drawable.not_found) // <-- 修改為載入失敗時顯示的圖片:ic_load_error
                        .into(ivImagePreview);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 設定提交按鈕的點擊事件
        btnPostProduct.setOnClickListener(v -> attemptToPostProduct()); // <-- 修改方法名稱
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar_post_product); // <-- 修改 ID
        etProductName = findViewById(R.id.et_product_name);
        etImageUrl = findViewById(R.id.et_image_url);
        etDescription = findViewById(R.id.et_description);
        etAmount = findViewById(R.id.et_amount);
        etPrice = findViewById(R.id.et_price);
        ivImagePreview = findViewById(R.id.iv_image_preview);
        btnPostProduct = findViewById(R.id.btn_post_product); // <-- 修改 ID
        progressBar = findViewById(R.id.progress_bar);
    }

    private void attemptToPostProduct() { // <-- 修改方法名稱
        // 檢查使用者是否登入
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "請先登入", Toast.LENGTH_SHORT).show();
            // 可以導向到 LoginActivity
            // startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        // 獲取輸入內容
        String name = etProductName.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        // 驗證輸入是否為空
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "所有欄位皆為必填", Toast.LENGTH_SHORT).show();
            return;
        }

        // 顯示進度條，隱藏按鈕
        setLoading(true);

        try {
            int amount = Integer.parseInt(amountStr);
            Long price = Long.parseLong(priceStr);
            String sellerId = currentUser.getUid();

//            Product product = new Product(name, price, description, amount, sellerId, imageUrl);
//            postToFirestore(product); // <-- 修改方法名稱
        } catch (NumberFormatException e) {
            Toast.makeText(this, "庫存和價格必須是有效的數字", Toast.LENGTH_SHORT).show();
            setLoading(false);
        }
    }

    private void postToFirestore(Product product) { // <-- 修改方法名稱
        db.collection("products").add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PostProductActivity.this, "商品已成功發布！", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PostProductActivity.this, "發布失敗: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    setLoading(false);
                });
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnPostProduct.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnPostProduct.setVisibility(View.VISIBLE);
        }
    }
}