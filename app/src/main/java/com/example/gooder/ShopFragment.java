package com.example.gooder;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gooder.adapter.ProductItemAdapter;
import com.example.gooder.model.ProductItem;
import com.example.gooder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ProductItemAdapter adapter;
    private List<ProductItem> productList;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_shop, container, false);

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        TextView sellerName = view.findViewById(R.id.sellerName);
        TextView sellerRating = view.findViewById(R.id.sellerRating);
        recyclerView = view.findViewById(R.id.productListRecyclerView);

        sellerName.setText("mtos 美迪奧斯");
        sellerRating.setText("出貨率 99%");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = getMockProducts();
        adapter = new ProductItemAdapter(productList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<ProductItem> getMockProducts() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("商品1 A01", "$3990", R.drawable.ic_product));
        list.add(new ProductItem("商品2 A02", "$2990", R.drawable.ic_product));
        return list;
    }

}
