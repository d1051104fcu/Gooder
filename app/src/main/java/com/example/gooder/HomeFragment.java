package com.example.gooder;

// 수동 추가 ViewPager2, Handler 不知道爲啥不能自動import
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gooder.adapter.ImageSliderAdapter;
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

    // FireBase => for four frames in GridLayout
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FrameLayout[] frames = new FrameLayout[4];
    //

    // advertisement => ViewPager2
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
// test ---
        SearchView searchView = view.findViewById(R.id.search);
        // 처음부터 검색창 펼치기 (아이콘화 해제)
        searchView.setIconified(false);

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

        // 검색어 제출 시 저장하기
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                saveSearchQuery(requireContext(), query);

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
// test ---

////        AutoCompleteTextView searchInput = view.findViewById(R.id.search_input);
//        EditText searchInput = view.findViewById(R.id.search_input);
//        Button searchButton = view.findViewById(R.id.search_button);

//// 저장된 검색 기록 불러오기
//        Set<String> historySet = getSearchHistory(requireContext());  // Set<String>
//        List<String> historyList = new ArrayList<>(historySet);
//
//// 자동완성 어댑터 연결
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_dropdown_item_1line,
//                historyList
//        );
//        searchInput.setAdapter(adapter);

//// 검색 버튼 클릭 시 처리
//        searchButton.setOnClickListener(v -> {
//            String query = searchInput.getText().toString().trim();
//
//            if (!query.isEmpty()) {
//                saveSearchQuery(requireContext(), query);  // 검색어 저장
//
//                // 다음 화면으로 이동
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
//                intent.putExtra("query", query);
//                startActivity(intent);
//            }
//        });


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

        // 예: TextView[]과 ImageView[] 배열로 4개의 frame을 관리한다고 가정

        TextView tv_title1 = view.findViewById(R.id.tv_title1);
        TextView tv_title2 = view.findViewById(R.id.tv_title2);
        TextView tv_title3 = view.findViewById(R.id.tv_title3);
        TextView tv_title4 = view.findViewById(R.id.tv_title4);
        ImageView iv_product1 = view.findViewById(R.id.iv_product1);
        ImageView iv_product2 = view.findViewById(R.id.iv_product2);
        ImageView iv_product3 = view.findViewById(R.id.iv_product3);
        ImageView iv_product4 = view.findViewById(R.id.iv_product4);

        TextView tv_method1 = view.findViewById(R.id.tv_method1);
        TextView tv_method2 = view.findViewById(R.id.tv_method2);
        TextView tv_method3 = view.findViewById(R.id.tv_method3);
        TextView tv_method4 = view.findViewById(R.id.tv_method4);

        TextView tv_price1 = view.findViewById(R.id.tv_price1);
        TextView tv_price2 = view.findViewById(R.id.tv_price2);
        TextView tv_price3 = view.findViewById(R.id.tv_price3);
        TextView tv_price4 = view.findViewById(R.id.tv_price4);

        TextView[] titles = { tv_title1, tv_title2, tv_title3, tv_title4 };
        ImageView[] images = { iv_product1, iv_product2, iv_product3, iv_product4 };

        TextView[] methods = { tv_method1, tv_method2, tv_method3, tv_method4 };
        TextView[] prices =  { tv_price1, tv_price2, tv_price3, tv_price4 };

        LinearLayout ll1 = view.findViewById(R.id.frame1);
        LinearLayout ll2 = view.findViewById(R.id.frame2);
        LinearLayout ll3 = view.findViewById(R.id.frame3);
        LinearLayout ll4 = view.findViewById(R.id.frame4);
        LinearLayout[] frames = { ll1, ll2, ll3, ll4 };

        List<String> titlesList = new ArrayList<>();
        List<String> imageUrls = new ArrayList<>();
//        List<Long> priceList = new ArrayList<>();
//        List<String> transactionMethodList = new ArrayList<>();

//        db.collection("test_gigang2")
//                .whereEqualTo("City", "台北")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
//
//                    for (int i = 0; i < docs.size() && i < 4; i++) {
//                        DocumentSnapshot doc = docs.get(i);
//                        String title = doc.getString("Title");
//                        String imageUrl = doc.getString("ImageUrl");
//                        String method = doc.getString("TransactionMethod");
//                        Long price = doc.getLong("Price");
//                        // Price
//                        // Body
//                        // TransactionMethod
//                        // Category
//
//                        titles[i].setText(title);
////                        titlesList.add(title);
//                        imageUrls.add(imageUrl);
//
//                        methods[i].setText(method);
//                        prices[i].setText(String.valueOf(price) + '元');
//
//
//                        Glide.with(view.getContext())  // fragment 안이니까 view.getContext()
//                                .load(imageUrl)
//                                .into(images[i]);
//
//                        // 프레임 클릭 시 상세 화면 이동
//                        int index = i; // 내부 클래스에서 사용하려면 final 또는 effectively final
//                        frames[i].setOnClickListener(v -> {
//                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//
////                            intent.putExtra("title", titlesList.get(index));
////                            intent.putExtra("imageUrl", imageUrls.get(index));
//                            intent.putExtra("productId", doc.getId());
//                            startActivity(intent);
//                        });
//                    }
//                });

//        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                recyclerView.setVisibility(View.VISIBLE);
//                loadSearchHistoryAndShow();
//            } else {
//                recyclerView.setVisibility(View.GONE);
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                saveSearchQuery(query);
//                recyclerView.setVisibility(View.GONE);
//                // 실제 검색 동작 수행
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 텍스트 변화 시에도 검색 기록 필터링 가능
//                filterSearchHistory(newText);
//                return false;
//            }
//        });

        Spinner citySpinner = view.findViewById(R.id.spinner_city);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.city_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // 선택 리스너 설정
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Products")
                        .whereEqualTo("city", selectedCity) // <- 도시 필터링
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                            // UI에 최대 4개까지만 출력
                            for (int i = 0; i < docs.size() && i < 4; i++) {
                                DocumentSnapshot doc = docs.get(i);
                                String title = doc.getString("name");
                                String imageURL = doc.getString("imageURL");
                                String method = doc.getString("transactionMethod");
                                Long price = doc.getLong("price");

                                titles[i].setText(title);
                                methods[i].setText(method);
                                prices[i].setText(String.valueOf(price) + "元");

                                Glide.with(getContext()).load(imageURL).into(images[i]);

//                                Glide.with(getContext()) // 또는 getActivity()
//                                        .load(imageUrl)
//                                        .error(R.drawable.not_found) // 실패 시 이미지
//                                        .into(images[i]);


                                int index = i;
                                frames[i].setOnClickListener(v -> {
                                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                    intent.putExtra("productId", doc.getId());
                                    startActivity(intent);
                                });
                            }
                        });

//                loadProductsByCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

//        private void loadProductsByCity(String city) {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("test_gigang2")
//                    .whereEqualTo("City", city) // <- 도시 필터링
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
//
//
//                        // UI에 최대 4개까지만 출력
//                        for (int i = 0; i < docs.size() && i < 4; i++) {
//                            DocumentSnapshot doc = docs.get(i);
//                            String title = doc.getString("Title");
//                            String imageUrl = doc.getString("ImageUrl");
//                            String method = doc.getString("TransactionMethod");
//                            Long price = doc.getLong("Price");
//
//                            titles[i].setText(title);
//                            methods[i].setText(method);
//                            prices[i].setText(String.valueOf(price) + "元");
//
//                            Glide.with(getContext()).load(imageUrl).into(images[i]);
//
//                            int index = i;
//                            frames[i].setOnClickListener(v -> {
//                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                                intent.putExtra("productId", doc.getId());
//                                startActivity(intent);
//                            });
//                        }
//                    });
//        }

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