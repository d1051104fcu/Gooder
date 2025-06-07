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
//        //--
//        SearchView searchView;
//        //--
        SearchResultAdapter adapter = new SearchResultAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        //--
        // SearchView 초기화
//        searchView = findViewById(R.id.searchView);
//        // 텍스트가 변경될 때마다 필터 메소드 호출
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // 키보드에 “검색 버튼” 누를 때
//                adapter.filter(query);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 타이핑 한 글자마다 실시간 필터
//                adapter.filter(newText);
//                return true;
//            }
//        });
//
//        // 인텐트에서 전달된 초기 검색어가 있을 때 (옵션)
//        String initialQuery = getIntent().getStringExtra("query");
//        if (initialQuery != null && !initialQuery.isEmpty()) {
//            // Firestore 데이터 로딩 후 필터를 바로 적용하기 위해 약간의 지연을 둘 수도 있고,
//            // 아니면 onSuccessListener 안에서 adapter.filter(initialQuery) 호출.
//            searchView.setQuery(initialQuery, true);
//        }
//        //--

        String query = getIntent().getStringExtra("query");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String name = doc.getString("name");
                        String category = doc.getString("category");
                        String description = doc.getString("description");

                        // 하나라도 포함되면
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
}