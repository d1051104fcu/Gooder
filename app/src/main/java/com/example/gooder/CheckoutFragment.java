package com.example.gooder;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gooder.adapter.CheckoutShopAdapter;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.CheckoutItem;
import com.example.gooder.model.CheckoutShop;
import com.example.gooder.model.Order;
import com.example.gooder.model.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    private RecyclerView recyclerView;
    private TextView freight, totalPrice;
    private RelativeLayout loading;
    CheckoutShopAdapter checkoutShopAdapter;
    List<CheckoutShop> checkoutShopList = new ArrayList<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            checkoutShopList = getArguments().getParcelableArrayList("checkoutShopList");
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
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
        Button checkout = view.findViewById(R.id.checkout_checkout);
        loading = view.findViewById(R.id.checkout_loading);

        setupRecyclerView();

        setupUIToHideKeyboard(view);

        String uid = auth.getCurrentUser().getUid();
        checkout.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            int expectedTaskCount = 0;
            for (CheckoutShop shop: checkoutShopList){
                expectedTaskCount += 1;     // Add Order
                expectedTaskCount += shop.getCheckoutItemList().size() * 3;     // Add Order_Products, Delete ShoppingCart, Update Products amount
                expectedTaskCount += 1;     // Add Trading_History
            }
            int finalExpectedTaskCount = expectedTaskCount;
            AtomicInteger completedTaskCount = new AtomicInteger();
            Log.d("Checkout", "checkoutShopList size: " + checkoutShopList.size());

            for (CheckoutShop shop: checkoutShopList){
                Order order = new Order(uid, shop.getSellerId(), shop.getTradeMode(), shop.getAddress(), shop.getFreight(), shop.getTotalPrice(), now);

                // Add Order
                db.collection("Orders")
                        .add(order)
                        .addOnSuccessListener(task -> {
                            completedTaskCount.getAndIncrement();
                            checkIfCheckComplete(finalExpectedTaskCount, completedTaskCount);

                            String docId = task.getId();
                            for (CheckoutItem item: shop.getCheckoutItemList()){
                                OrderItem orderItem = new OrderItem(item.getProductId(), item.getName(), item.getPrice(), item.getCount());

                                // Add Order_Products
                                db.collection("Orders").document(docId).collection("Order_Products")
                                        .add(orderItem)
                                        .addOnSuccessListener(task1 -> {
                                            completedTaskCount.getAndIncrement();
                                            checkIfCheckComplete(finalExpectedTaskCount, completedTaskCount);
                                            Log.d("Checkout", "Add Order_Products Successfully!: " + task1.getId());

                                            // Delete ShoppingCart
                                            db.collection("Users").document(uid).collection("ShoppingCart").document(item.getShoppingCartId())
                                                    .delete()
                                                    .addOnSuccessListener(task2 -> {
                                                        completedTaskCount.getAndIncrement();
                                                        checkIfCheckComplete(finalExpectedTaskCount, completedTaskCount);
                                                        Log.d("Checkout", "Delete ShoppingCart Successfully! " + item.getShoppingCartId());
                                                    })
                                                    .addOnFailureListener(e -> Log.w("Checkout", "Error deleting document", e));

                                            // Update Products amount
                                            db.collection("Products").document(item.getProductId())
                                                    .update("amount", FieldValue.increment(-item.getCount()))
                                                    .addOnSuccessListener(task2 -> {
                                                        completedTaskCount.getAndIncrement();
                                                        checkIfCheckComplete(finalExpectedTaskCount, completedTaskCount);
                                                        Log.d("Checkout", "Update Products amount Successfully! " + item.getProductId());
                                                    })
                                                    .addOnFailureListener(e -> Log.w("Checkout", "Error updating Products amount", e));
                                        })
                                        .addOnFailureListener(e -> Log.e("Checkout", "Error adding document" + e));
                            }

                            Map<String, Object> trading_history = new HashMap<>();
                            trading_history.put("order_id", docId);
                            trading_history.put("time", now);

                            // Add Trading_History
                            db.collection("Users").document(uid).collection("Trading_History")
                                    .add(trading_history)
                                    .addOnSuccessListener(task1 -> {
                                        completedTaskCount.getAndIncrement();
                                        checkIfCheckComplete(finalExpectedTaskCount, completedTaskCount);
                                        Log.d("Checkout", "Add Trading_History Successfully! : " + task1.getId());
                                    })
                                    .addOnFailureListener(e -> Log.e("Checkout", "Error adding Trading_History" + e));
                        })
                        .addOnFailureListener(e -> Log.e("Checkout", "Error adding document" + e));
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        setTotalPrice();
        updateFreight();

        checkoutShopAdapter = new CheckoutShopAdapter(checkoutShopList, requireContext(), this::updateFreight);
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

    private void updateFreight(){
        int total = 0;
        for (CheckoutShop shop: checkoutShopList){
            total += shop.getFreight();
        }

        freight.setText("$ " + total);
    }

    private void setupUIToHideKeyboard(View view) {
        // 若不是 EditText，設 onTouchListener
        if (!(view instanceof EditText) && !view.isClickable()) {
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
                return false; // 不攔截事件
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUIToHideKeyboard(innerView);
            }
        }
    }

    private void checkIfCheckComplete(int expectedCount, AtomicInteger completedCount){
        if (expectedCount == completedCount.get()){
            loading.setVisibility(View.GONE);
            navToHomeFragment();
        }
    }

    private void navToHomeFragment(){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, new ShoppingCartFragment())
                .commit();
    }
}