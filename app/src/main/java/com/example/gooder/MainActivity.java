package com.example.gooder;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Fragment chatFragment;
    private Fragment settingFragment;

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

        bottomNav = findViewById(R.id.bottom_nav);
        chatFragment = ChatFragment.newInstance("", "");
        settingFragment = SettingFragment.newInstance("", "");

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.menu_chat){
                    setCurrentFrament(chatFragment);
                }
                else if(itemId == R.id.menu_setting){
                    setCurrentFrament(settingFragment);
                }
                else if(itemId == R.id.menu_post){

                }
                else if(itemId == R.id.menu_category){

                }
                else{

                }
                return true;
            }
        });
    }

    private void setCurrentFrament(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();
    }
}