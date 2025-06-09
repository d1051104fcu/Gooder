package com.example.gooder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SearchResultActivity extends AppCompatActivity {
    List<Product> productList = new ArrayList<>();
    SearchResultAdapter adapter = new SearchResultAdapter(this, productList);

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
//        List<Product> productList = new ArrayList<>();
//
//        SearchResultAdapter adapter = new SearchResultAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false); // 처음부터 검색창 펼치기 (아이콘화 해제)

        String query = getIntent().getStringExtra("query"); // 초기 검색어 가져오기

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

        // 🔹 1. AutoCompleteTextView 연결
        int autoCompleteId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        AutoCompleteTextView searchAutoComplete = searchView.findViewById(autoCompleteId);

// 🔹 2. SharedPreferences로부터 검색 기록 불러오기
        Set<String> historySet = getSearchHistory(this);
        List<String> historyList = new ArrayList<>(historySet);

// 🔹 3. 어댑터 설정
        ArrayAdapter<String> adapterAutoComplete = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, historyList);
        searchAutoComplete.setAdapter(adapterAutoComplete);
        searchAutoComplete.setThreshold(1);

// 🔹 4. 포커스 시 드롭다운 자동 노출
        searchAutoComplete.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchAutoComplete.post(searchAutoComplete::showDropDown);
            }
        });

// 🔹 5. 자동완성 항목 클릭 시 검색 수행
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedQuery = (String) parent.getItemAtPosition(position);
            searchView.setQuery(selectedQuery, false); // 텍스트 설정

            saveSearchQuery(this, selectedQuery); // 저장
            performSearch(selectedQuery); // 검색 실행
        });

// 🔹 6. 검색어 제출 시 (직접 입력)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newQuery) {
                saveSearchQuery(getApplicationContext(), newQuery);

                if (!historyList.contains(newQuery)) {
                    historyList.add(newQuery);
                    adapterAutoComplete.notifyDataSetChanged();
                }

                performSearch(newQuery); // 검색 수행
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

//// 🔹 SearchView 검색 리스너 등록
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String newQuery) {
//                // 🔁 위와 똑같은 검색 로직 붙이기
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("Products")
//                        .get()
//                        .addOnSuccessListener(querySnapshot -> {
//                            productList.clear();
//                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
//                                String name = doc.getString("name");
//                                String category = doc.getString("category");
//                                String description = doc.getString("description");
//
//                                if ((name != null && name.toLowerCase().contains(newQuery.toLowerCase())) ||
//                                        (category != null && category.toLowerCase().contains(newQuery.toLowerCase())) ||
//                                        (description != null && description.toLowerCase().contains(newQuery.toLowerCase()))) {
//
//                                    String imageURL = doc.getString("imageURL");
//                                    String method = doc.getString("transactionMethod");
//                                    Long price = doc.getLong("price");
//                                    String city = doc.getString("city");
//                                    Long amount = doc.getLong("amount");
//
//                                    productList.add(new Product(doc.getId(), name, imageURL, method, price, city, amount, category, description));
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                        });
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//    }

    }

    private void performSearch (String query){
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
                    adapter.notifyDataSetChanged(); // 결과 반영
                });
    }

    private void saveSearchQuery (Context context, String query){
        SharedPreferences prefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE);
        Set<String> oldHistory = prefs.getStringSet("search_history", new LinkedHashSet<>());
        Set<String> newHistory = new LinkedHashSet<>(oldHistory);
        newHistory.add(query);
        prefs.edit().putStringSet("search_history", newHistory).apply();
    }

    private Set<String> getSearchHistory (Context context){
        SharedPreferences prefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE);
        return prefs.getStringSet("search_history", new LinkedHashSet<>());
    }

}