package com.example.gooder;

// 수동 추가 ViewPager2, Handler 不知道爲啥不能自動import
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.gooder.adapter.ImageSliderAdapter;
import com.example.gooder.adapter.ProductAdapter;
import com.example.gooder.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;
    private int currentPage = 0;
    private List<Integer> imageList;
    //

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SearchView searchView = view.findViewById(R.id.search);

        searchView.setIconified(false); // 처음부터 검색창 펼치기 (아이콘화 해제)

        // SearchView 내부의 AutoCompleteTextView 찾기
        int autoCompleteId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        AutoCompleteTextView searchAutoComplete = searchView.findViewById(autoCompleteId);

        // 저장된 검색 기록 불러오기
        Set<String> historySet = getSearchHistory(requireContext());
        List<String> historyList = new ArrayList<>(historySet);

        // 어댑터 세팅
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, historyList);
        searchAutoComplete.setAdapter(adapter);

        searchAutoComplete.setThreshold(1); // 최소 1글자 입력 시 자동완성
        searchAutoComplete.setAdapter(adapter);

        // 포커스 시 드롭다운
        searchAutoComplete.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchAutoComplete.post(() -> searchAutoComplete.showDropDown());
            }
        });

        // 자동완성 항목 클릭 시
        searchAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedQuery = (String) parent.getItemAtPosition(position);

            // 검색어 저장
            saveSearchQuery(requireContext(), selectedQuery);

            // 검색 결과 액티비티로 이동
            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
            intent.putExtra("query", selectedQuery);
            startActivity(intent);
        });

        // 검색어 제출 시 저장하기
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                saveSearchQuery(requireContext(), query);

                if (!historyList.contains(query)) {
                    historyList.add(query); // 리스트에 추가
                    adapter.notifyDataSetChanged(); // 어댑터 갱신
                }

                // 검색 결과 액티비티로 이동
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // ViewPager2와 어댑터 설정
        viewPager = view.findViewById(R.id.viewPager);
        imageList = Arrays.asList(
                R.drawable.img1_test,
                R.drawable.img2_test,
                R.drawable.img3_test
        );
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(imageList);
        viewPager.setAdapter(imageSliderAdapter);

        // Handler로 자동 슬라이드 설정
        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                currentPage = (currentPage + 1) % imageList.size();
                viewPager.setCurrentItem(currentPage, true);
                sliderHandler.postDelayed(this, 3000); // 3초마다 슬라이드
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        Spinner citySpinner = view.findViewById(R.id.spinner_city);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.city_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);


        RecyclerView homeRecyclerView = view.findViewById(R.id.home_recyclerView);
        List<Product> productList = new ArrayList<>();
        int spanCount = 2; // 한 줄에 2개씩 표시
// test
//        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
//        homeRecyclerView.addItemDecoration(divider);
// test

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), spanCount);
        homeRecyclerView.setLayoutManager(gridLayoutManager);

        ProductAdapter productAdapter = new ProductAdapter(requireContext(), productList);
        homeRecyclerView.setAdapter(productAdapter);

        // 선택 리스너 설정
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();

                // 기존 목록 초기화
                productList.clear();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Products")
                        .whereEqualTo("city", selectedCity) // <- 도시 필터링
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot doc : docs) {
                                String name = doc.getString("name");
                                String imageURL = doc.getString("imageURL");
                                String method = doc.getString("method");
                                Long price = doc.getLong("price");
                                String city = doc.getString("city");
                                Long amount = doc.getLong("amount");
                                String category = doc.getString("category");
                                String description = doc.getString("description");

                                productList.add(new Product(doc.getId(), name, imageURL, method, price, city, amount, category, description));

                            }
                            productAdapter.notifyDataSetChanged(); // ✅ 어댑터 갱신
                        });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }

    // 검색 기록 저장 함수
    private void saveSearchQuery(Context context, String query) {
        SharedPreferences prefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE);
        Set<String> oldHistory = prefs.getStringSet("search_history", new LinkedHashSet<>());
        // 복사본 생성 (원본을 직접 수정하지 않음)
        Set<String> newHistory = new LinkedHashSet<>(oldHistory);
        newHistory.add(query);
        prefs.edit().putStringSet("search_history", newHistory).apply();
    }

    // 검색 기록 불러오기 함수
    private Set<String> getSearchHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE);
        return prefs.getStringSet("search_history", new LinkedHashSet<>());
    }


}