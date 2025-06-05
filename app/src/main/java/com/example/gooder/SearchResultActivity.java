package com.example.gooder;

import android.content.ClipData;
import android.os.Bundle;

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

        String query = getIntent().getStringExtra("query");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("test_gigang2")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String title = doc.getString("Title");
                        String category = doc.getString("Category");
                        String body = doc.getString("Body");

                        // 하나라도 포함되면
                        if ((title != null && title.contains(query)) ||
                                (category != null && category.contains(query)) ||
                                (body != null && body.contains(query))) {

                            String imageUrl = doc.getString("ImageUrl");
                            String method = doc.getString("TransactionMethod");
                            Long price = doc.getLong("Price");

                            productList.add(new Product(title, imageUrl, method, price));
                        }
                    }
                    adapter.notifyDataSetChanged();
                });

    }
}