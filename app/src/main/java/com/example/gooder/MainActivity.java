package com.example.gooder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Fragment chatFragment;
    private Fragment settingFragment;

    // 基江
    private Fragment homeFragment;
    //
//    private Fragment shopFragment;
//    private Fragment postFragment;
    private Fragment shoppingCartFragment;
    private Fragment checkoutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );


        FirebaseApp.initializeApp(this);

        bottomNav = findViewById(R.id.bottom_nav);
        chatFragment = ChatListFragment.newInstance("", "");
        settingFragment = SettingFragment.newInstance("", "");

        // 基江
        homeFragment = HomeFragment.newInstance("", "");

//        shopFragment = ShopFragment.newInstance("", "");
//        postFragment = PostFragment.newInstance("", "");

        shoppingCartFragment = ShoppingCartFragment.newInstance("", "");

        SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLogin = myPrefs.getBoolean("isLogin", false);
        String targetFragment = getIntent().getStringExtra("targetFragment");
        if (targetFragment != null) {
            if (targetFragment.equals("checkout")) {
                bottomNav.setSelectedItemId(R.id.menu_shoppingCart);

                Bundle bundle = getIntent().getBundleExtra("checkoutBundle");
                CheckoutFragment checkoutFragment = new CheckoutFragment();
                checkoutFragment.setArguments(bundle);
                setCurrentFragment(checkoutFragment);
            }
        }else if (isLogin) {
            setCurrentFragment(homeFragment);
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.menu_chat){
                    setCurrentFragment(chatFragment);
                }
                else if(itemId == R.id.menu_setting){
                    setCurrentFragment(settingFragment);
                }
                else if(itemId == R.id.menu_post){
//                    setCurrentFragment(postFragment);
                    Intent intent = new Intent(MainActivity.this, PostProductActivity.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.menu_shoppingCart){
                    setCurrentFragment(shoppingCartFragment);
                }
                else{ // itemId == R.id.menu_home / 基江 : an object in menu > bottom_nav_menu
                    setCurrentFragment(homeFragment);

                }
                return true;
            }
        });
    }

    private void setCurrentFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();

    }
}