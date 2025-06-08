package com.example.gooder;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.adapter.SearchResultAdapter;
import com.example.gooder.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Product> productList = new ArrayList<>();

        SearchResultAdapter adapter = new SearchResultAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false); // 처음부터 검색창 펼치기 (아이콘화 해제)

        String query = getIntent().getStringExtra("query"); // 초기 검색어 가져오기

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Products")
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
//                        String name = doc.getString("name");
//                        String category = doc.getString("category");
//                        String description = doc.getString("description");
//
//                        // 하나라도 포함되면
//                        if ((name != null && name.toLowerCase().contains(query.toLowerCase())) ||
//                                (category != null && category.toLowerCase().contains(query.toLowerCase())) ||
//                                (description != null && description.toLowerCase().contains(query.toLowerCase()))) {
//
//
//                            String imageURL = doc.getString("imageURL");
//                            String method = doc.getString("transactionMethod");
//                            Long price = doc.getLong("price");
//                            String city = doc.getString("city");
//                            Long amount = doc.getLong("amount");
//
//                            productList.add(new Product(doc.getId(), name, imageURL, method, price, city, amount, category, description));
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                });

        if (query != null) {
            searchView.setQuery(query, false);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        productList.clear();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            String name = doc.getString("name");
                            String category = doc.getString("category");
                            String description = doc.getString("description");

                            if ((name != null && name.toLowerCase().contains(query.toLowerCase())) ||
                                    (category != null && category.toLowerCase().contains(query.toLowerCase())) ||
                                    (description != null && description.toLowerCase().contains(query.toLowerCase()))) {

                                String imageURL = doc.getString("imageURL");
                                String method = doc.getString("transactionMethod");
                                Long price = doc.getLong("price");
                                String city = doc.getString("city");
                                Long amount = doc.getLong("amount");

                                productList.add(new Product(doc.getId(), name, imageURL, method, price, city, amount, category, description));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    });
        }

// 🔹 SearchView 검색 리스너 등록
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newQuery) {
                // 🔁 위와 똑같은 검색 로직 붙이기
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Products")
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            productList.clear();
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                String name = doc.getString("name");
                                String category = doc.getString("category");
                                String description = doc.getString("description");

                                if ((name != null && name.toLowerCase().contains(newQuery.toLowerCase())) ||
                                        (category != null && category.toLowerCase().contains(newQuery.toLowerCase())) ||
                                        (description != null && description.toLowerCase().contains(newQuery.toLowerCase()))) {

                                    String imageURL = doc.getString("imageURL");
                                    String method = doc.getString("transactionMethod");
                                    Long price = doc.getLong("price");
                                    String city = doc.getString("city");
                                    Long amount = doc.getLong("amount");

                                    productList.add(new Product(doc.getId(), name, imageURL, method, price, city, amount, category, description));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

}