package com.example.gooder;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gooder.adapter.CheckoutShopAdapter;
import com.example.gooder.model.CheckoutItem;
import com.example.gooder.model.CheckoutShop;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private TextView freight, totalPrice;
    private Button checkout;
    CheckoutShopAdapter checkoutShopAdapter;
    List<CheckoutShop> checkoutShopList = new ArrayList<>();

    public CheckoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutFragment newInstance(String param1, String param2) {
        CheckoutFragment fragment = new CheckoutFragment();
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
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        recyclerView = view.findViewById(R.id.checkout_recycler_shops);
        freight = view.findViewById(R.id.checkout_freight);
        totalPrice = view.findViewById(R.id.checkout_totalPrice);
        checkout = view.findViewById(R.id.checkout_checkout);

        setupRecyclerView();

        setupUIToHideKeyboard(view);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Need to fix this
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        List<CheckoutItem> originalList = new ArrayList<>();
        originalList.add(new CheckoutItem("name1", 360, 0, 1));
        /*
        originalList.add(new CheckoutItem("name2", 200, 0, 1));
        originalList.add(new CheckoutItem("name3", 300, 0, 2));
        originalList.add(new CheckoutItem("name4", 500, 0, 1));
        originalList.add(new CheckoutItem("name5", 100, 0, 1));

         */

        List<CheckoutItem> checkoutItemList1 = copyCheckoutItemList(originalList);
        List<CheckoutItem> checkoutItemList2 = copyCheckoutItemList(originalList);
        List<CheckoutItem> checkoutItemList3 = copyCheckoutItemList(originalList);

        checkoutShopList.add(new CheckoutShop("shop1", checkoutItemList1));
        checkoutShopList.add(new CheckoutShop("shop2", checkoutItemList2));
        //checkoutShopList.add(new CheckoutShop("shop3", checkoutItemList3));

        setTotalPrice();
        freight.setText("$ 0");

        checkoutShopAdapter = new CheckoutShopAdapter(checkoutShopList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(checkoutShopAdapter);
    }

    private void setTotalPrice(){
        int total = 0;
        for (CheckoutShop shop: checkoutShopList){
            for (CheckoutItem item: shop.getCheckoutItemList()){
                total += item.getPrice() * item.getCount();
            }
        }
        totalPrice.setText("$ " + total);
    }

    private List<CheckoutItem> copyCheckoutItemList(List<CheckoutItem> checkoutItemList){
        List<CheckoutItem> newList = new ArrayList<>();
        for (CheckoutItem item: checkoutItemList){
            newList.add(new CheckoutItem(
                    item.getName(),
                    item.getPrice(),
                    item.getImgId(),
                    item.getCount()
            ));
        }
        return newList;
    }

    private void setupUIToHideKeyboard(View view) {
        // 若不是 EditText，設 onTouchListener
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View currentFocus = requireActivity().getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        currentFocus.clearFocus();
                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        }
                    }
                    v.performClick();
                }
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUIToHideKeyboard(innerView);
            }
        }
    }
}