package com.example.gooder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.adapter.ShoppingCartShopAdapter;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCartItem;
import com.example.gooder.model.ShoppingCartShop;

import java.util.ArrayList;
import java.util.List;
/*
public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView totalPrice;
    ShoppingCartShopAdapter shoppingCartShopAdapter;
    List<ShoppingCartShop> shoppingCartShopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.shoppingCart_recycler_shops);
        totalPrice = findViewById(R.id.shoppingCart_totalPrice);
        Button toCheckout = findViewById(R.id.shoppingCart_toCheckout);

        toCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Need to fix: Nav
                /*
                startActivity(new Intent(ShoppingCartActivity.this, ));
                finish();
            }
        });
    }

    private void setupRecycleView(){
        List<ShoppingCartItem> shoppingCartItemList = new ArrayList<>();
        shoppingCartItemList.add(new ShoppingCartItem("name1", 360, 0, 1));
        shoppingCartItemList.add(new ShoppingCartItem("name2", 200, 0, 1));
        shoppingCartItemList.add(new ShoppingCartItem("name3", 300, 0, 2));
        shoppingCartItemList.add(new ShoppingCartItem("name4", 500, 0, 1));
        shoppingCartItemList.add(new ShoppingCartItem("name5", 100, 0, 1));


        shoppingCartShopList.add(new ShoppingCartShop("shopName1", shoppingCartItemList));
        shoppingCartShopList.add(new ShoppingCartShop("shopName2", shoppingCartItemList));
        shoppingCartShopList.add(new ShoppingCartShop("shopName3", shoppingCartItemList));

        shoppingCartShopAdapter = new ShoppingCartShopAdapter(shoppingCartShopList, new OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChangedListener() {
                updateTotalPrice();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shoppingCartShopAdapter);
    }

    private void updateTotalPrice(){
        int total = 0;
        for (ShoppingCartShop shop: shoppingCartShopList){
            for (ShoppingCartItem item: shop.getShoppingCartItemList()){
                if (item.isChoose()){
                    total += item.getPrice() * item.getCount();
                }
            }
        }
        totalPrice.setText("$" + total);
    }
}
*/