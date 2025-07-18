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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gooder.adapter.ShoppingCartShopAdapter;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.listener.ShopNameCallback;
import com.example.gooder.model.CheckoutItem;
import com.example.gooder.model.CheckoutShop;
import com.example.gooder.model.ShoppingCartItem;
import com.example.gooder.model.ShoppingCartShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
    private RelativeLayout loading, cartEmpty;
    ShoppingCartShopAdapter shoppingCartShopAdapter;
    List<ShoppingCartShop> shoppingCartShopList = new ArrayList<>();


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        cartEmpty = view.findViewById(R.id.shoppingCart_cartEmpty);
        loading = view.findViewById(R.id.shoppingCart_loading);

        cartEmpty.setVisibility(View.GONE);
        setupRecycleView();

        toCheckout.setOnClickListener(view1 -> {
            ArrayList<CheckoutShop> checkoutShopList = new ArrayList<>();
            for (ShoppingCartShop shop: shoppingCartShopList){
                List<CheckoutItem> checkoutItemList = new ArrayList<>();
                for (ShoppingCartItem item: shop.getShoppingCartItemList()){
                    if (item.isChoose()){
                        checkoutItemList.add(new CheckoutItem(
                                item.getShoppingCartId(),
                                item.getProductId(),
                                item.getName(),
                                item.getPrice(),
                                item.getImgId(),
                                item.getCount()
                        ));
                    }
                }

                if (!checkoutItemList.isEmpty()){
                    checkoutShopList.add(new CheckoutShop(shop.getSellerId(), shop.getShopName(), checkoutItemList));
                }
            }

            if (checkoutShopList.isEmpty()){
                Toast.makeText(getContext(), "請勾選欲結帳商品", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("checkoutShopList", checkoutShopList);

            CheckoutFragment checkoutFragment = new CheckoutFragment();
            checkoutFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main, checkoutFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void setupRecycleView(){
        /*
        List<ShoppingCartItem> originalItemList = new ArrayList<>();
        originalItemList.add(new ShoppingCartItem("0", "name1", 360, "", 1));
        originalItemList.add(new ShoppingCartItem("0", "name2", 200, "", 1));
        originalItemList.add(new ShoppingCartItem("0", "name3", 300, "", 2));
        originalItemList.add(new ShoppingCartItem("0", "name4", 500, "", 1));
        originalItemList.add(new ShoppingCartItem("0", "name5", 100, "", 1));

        List<ShoppingCartItem> shoppingCartItemList1 = copyShoppingCartItemList(originalItemList);
        List<ShoppingCartItem> shoppingCartItemList2 = copyShoppingCartItemList(originalItemList);
        List<ShoppingCartItem> shoppingCartItemList3 = copyShoppingCartItemList(originalItemList);

        shoppingCartShopList.add(new ShoppingCartShop("shopName1", "", shoppingCartItemList1));
        shoppingCartShopList.add(new ShoppingCartShop("shopName2", "", shoppingCartItemList2));
        shoppingCartShopList.add(new ShoppingCartShop("shopName3", "", shoppingCartItemList3));
         */

        updateShoppingCartListAndTotalPrice();

        shoppingCartShopAdapter = new ShoppingCartShopAdapter(shoppingCartShopList, new OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChangedListener() {
                updateTotalPrice();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shoppingCartShopAdapter);
    }

    private void updateShoppingCartListAndTotalPrice(){
        if (auth.getCurrentUser() != null){
            loading.setVisibility(View.VISIBLE);

            String userId = auth.getCurrentUser().getUid();
            db.collection("Users").document(userId).collection("ShoppingCart")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.i("ShoppingCart", "Document size: " + queryDocumentSnapshots.size());

                        if (queryDocumentSnapshots.isEmpty()) {
                            setCartEmpty(true);
                            return;
                        } else {
                            setCartEmpty(false);
                        }

                        Map<String, ShoppingCartShop> shopMap = new HashMap<>();
                        Map<String, List<ShoppingCartItem>> tempItemMap = new HashMap<>();
                        Set<String> sellerIds = new HashSet<>();
                        List<DocumentSnapshot> cartDocs = queryDocumentSnapshots.getDocuments();

                        AtomicInteger completedCount = new AtomicInteger(0);
                        int totalCount = cartDocs.size();

                        for (DocumentSnapshot doc : cartDocs) {
                            String shoppingCartId = doc.getId();
                            String productId = doc.getString("product_id");
                            int amount = doc.getLong("amount").intValue();
                            Log.i("ShoppingCart", "productId: " + productId + " amount: " + amount);

                            db.collection("Products").document(productId)
                                    .get().addOnSuccessListener(productDoc -> {
                                        if (productDoc.exists()) {
                                            String name = productDoc.getString("name");
                                            int price = Objects.requireNonNull(productDoc.getLong("price")).intValue();
                                            String imgId = productDoc.getString("imageURL");
                                            String sellerId = productDoc.getString("seller_id");
                                            Log.i("ShoppingCart", "sellerId: " + sellerId + " name: " + name + " price: " + price);

                                            ShoppingCartItem item = new ShoppingCartItem(shoppingCartId, productId, name, price, imgId, amount);
                                            tempItemMap.computeIfAbsent(sellerId, k -> new ArrayList<>()).add(item);

                                            // 若尚未查過這個 sellerId 的名稱
                                            if (!shopMap.containsKey(sellerId) && !sellerIds.contains(sellerId)) {
                                                sellerIds.add(sellerId);
                                                getShopName(sellerId, shopName -> {
                                                    ShoppingCartShop shop = new ShoppingCartShop(shopName, sellerId, new ArrayList<>());
                                                    shopMap.put(sellerId, shop);

                                                    // 把之前存的商品加入
                                                    if (tempItemMap.containsKey(sellerId)) {
                                                        shop.getShoppingCartItemList().addAll(tempItemMap.get(sellerId));
                                                    }

                                                    // 當所有商品都完成後才更新 UI
                                                    if (completedCount.incrementAndGet() == totalCount) {
                                                        shoppingCartShopList.clear();
                                                        shoppingCartShopList.addAll(shopMap.values());
                                                        shoppingCartShopAdapter.notifyDataSetChanged();
                                                        updateTotalPrice();
                                                        loading.setVisibility(View.GONE);
                                                    }
                                                });
                                            } else {
                                                // 已經有 shop 了，直接加 item
                                                if (shopMap.containsKey(sellerId)) {
                                                    shopMap.get(sellerId).getShoppingCartItemList().add(item);
                                                }

                                                if (completedCount.incrementAndGet() == totalCount) {
                                                    shoppingCartShopList.clear();
                                                    shoppingCartShopList.addAll(shopMap.values());
                                                    shoppingCartShopAdapter.notifyDataSetChanged();
                                                    updateTotalPrice();
                                                    loading.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                        }
            });

        }else {
            Toast.makeText(getContext(), "請先登入", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        }
    }

    // Map<uid, shopName>
    Map<String, String> shopNameCache = new HashMap<>();
    private void getShopName(String uid, ShopNameCallback callback) {
        if (shopNameCache.containsKey(uid)){
            callback.onShopNameLoaded(shopNameCache.get(uid));
            return;
        }

        db.collection("Users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        callback.onShopNameLoaded(name);
                    } else {
                        callback.onShopNameLoaded("我不告訴你 略略");
                    }
                })
                .addOnFailureListener(e -> callback.onShopNameLoaded("窩不知搗"));
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

    /*
    private List<ShoppingCartItem> copyShoppingCartItemList(List<ShoppingCartItem> shoppingCartItemList){
        List<ShoppingCartItem> newList = new ArrayList<>();
        for (ShoppingCartItem item: shoppingCartItemList){
            newList.add(new ShoppingCartItem(
                    item.getShoppingCartId(),
                    item.getProductId(),
                    item.getName(),
                    item.getPrice(),
                    item.getImgId(),
                    item.getCount()
            ));
        }
        return newList;
    }
    */

    private void setCartEmpty(boolean visibility){
        if (visibility) {
            cartEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            cartEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}