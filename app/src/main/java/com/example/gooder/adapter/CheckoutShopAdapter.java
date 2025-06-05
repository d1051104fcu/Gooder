package com.example.gooder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.CheckoutItem;
import com.example.gooder.model.CheckoutShop;

import java.util.ArrayList;
import java.util.List;

public class CheckoutShopAdapter extends RecyclerView.Adapter<CheckoutShopAdapter.ViewHolder> {

    private List<CheckoutShop> checkoutShopList;
    private Context context;
    private ArrayAdapter<String> tradeModeAdapter;

    public CheckoutShopAdapter(List<CheckoutShop> list, Context context) {
        this.checkoutShopList = list;
        this.context = context;

        String[] tradeModes = {"宅配", "超商", "面交"};
        ArrayAdapter<String> tradeModeAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                tradeModes
        );
        this.tradeModeAdapter = tradeModeAdapter;
        tradeModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_shop, parent, false);
        return new CheckoutShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckoutShop checkoutShop = checkoutShopList.get(position);
        holder.shopName.setText(checkoutShop.getShopName());
        holder.recyclerProducts.setAdapter(new CheckoutItemAdapter(checkoutShop.getCheckoutItemList()));

        holder.tradeMode.setAdapter(tradeModeAdapter);
        holder.tradeMode.setSelection(0);
        holder.tradeMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMode = (String) parent.getItemAtPosition(position);
                Log.d("selectedMode", selectedMode);
                /*
                if (selectedMode.equals("面交")) {
                    holder.address.setVisibility(View.GONE);
                } else {
                    holder.address.setVisibility(View.VISIBLE);
                }
                */
                checkoutShop.setTradeMode(selectedMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return checkoutShopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        RecyclerView recyclerProducts;
        Spinner tradeMode;
        EditText address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.checkout_shopName);
            recyclerProducts = itemView.findViewById(R.id.checkout_recycler_products);
            tradeMode = itemView.findViewById(R.id.checkout_tradeMode);
            address = itemView.findViewById(R.id.checkout_address);

            recyclerProducts.setLayoutManager(new LinearLayoutManager(itemView.getContext()));;
        }
    }
}
