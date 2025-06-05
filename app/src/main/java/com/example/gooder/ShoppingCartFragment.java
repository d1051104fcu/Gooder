package com.example.gooder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gooder.adapter.ShoppingCartShopAdapter;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.ShoppingCartItem;
import com.example.gooder.model.ShoppingCartShop;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private TextView totalPrice;
    ShoppingCartShopAdapter shoppingCartShopAdapter;
    List<ShoppingCartShop> shoppingCartShopList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingCartFragment newInstance(String param1, String param2) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
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
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        recyclerView = view.findViewById(R.id.shoppingCart_recycler_shops);
        totalPrice = view.findViewById(R.id.shoppingCart_totalPrice);
        Button toCheckout = view.findViewById(R.id.shoppingCart_toCheckout);

        setupRecycleView();

        toCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CheckoutFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void setupRecycleView(){
        List<ShoppingCartItem> originalItemList = new ArrayList<>();
        originalItemList.add(new ShoppingCartItem("name1", 360, 0, 1));
        originalItemList.add(new ShoppingCartItem("name2", 200, 0, 1));
        originalItemList.add(new ShoppingCartItem("name3", 300, 0, 2));
        originalItemList.add(new ShoppingCartItem("name4", 500, 0, 1));
        originalItemList.add(new ShoppingCartItem("name5", 100, 0, 1));

        List<ShoppingCartItem> shoppingCartItemList1 = copyShoppingCartItemList(originalItemList);
        List<ShoppingCartItem> shoppingCartItemList2 = copyShoppingCartItemList(originalItemList);
        List<ShoppingCartItem> shoppingCartItemList3 = copyShoppingCartItemList(originalItemList);

        shoppingCartShopList.add(new ShoppingCartShop("shopName1", shoppingCartItemList1));
        shoppingCartShopList.add(new ShoppingCartShop("shopName2", shoppingCartItemList2));
        shoppingCartShopList.add(new ShoppingCartShop("shopName3", shoppingCartItemList3));

        updateTotalPrice();

        shoppingCartShopAdapter = new ShoppingCartShopAdapter(shoppingCartShopList, new OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChangedListener() {
                updateTotalPrice();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        totalPrice.setText("$ " + total);
    }

    private List<ShoppingCartItem> copyShoppingCartItemList(List<ShoppingCartItem> shoppingCartItemList){
        List<ShoppingCartItem> newList = new ArrayList<>();
        for (ShoppingCartItem item: shoppingCartItemList){
            newList.add(new ShoppingCartItem(
                    item.getName(),
                    item.getPrice(),
                    item.getImgId(),
                    item.getCount()
            ));
        }
        return newList;
    }
}