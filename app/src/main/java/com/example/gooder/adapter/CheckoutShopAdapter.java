package com.example.gooder.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.listener.OnItemCheckChangedListener;
import com.example.gooder.model.CheckoutShop;
import com.example.gooder.model.TradeMode;

import java.util.List;

public class CheckoutShopAdapter extends RecyclerView.Adapter<CheckoutShopAdapter.ViewHolder> {

    private final List<CheckoutShop> checkoutShopList;
    private final ArrayAdapter<String> tradeModeAdapter;
    private final OnItemCheckChangedListener tradeModeChangeListener;

    public CheckoutShopAdapter(List<CheckoutShop> list, Context context, OnItemCheckChangedListener listener) {
        this.checkoutShopList = list;
        this.tradeModeChangeListener = listener;

        List<String> tradeModes = TradeMode.getDisplayList();
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
                String selectedDisplayMode = (String) parent.getItemAtPosition(position);
                String mode = TradeMode.extractModeFromDisplay(selectedDisplayMode);
                int freight = TradeMode.getFreight(mode);

                checkoutShop.setTradeMode(mode);
                checkoutShop.setFreight(freight);

                if (mode.equals("面交")) {
                    holder.linearAddress.setVisibility(View.GONE);
                    holder.dividerUpAddress.setVisibility(View.GONE);
                    holder.address.setText("");
                    checkoutShop.setAddress("");
                } else {
                    holder.dividerUpAddress.setVisibility(View.VISIBLE);
                    holder.linearAddress.setVisibility(View.VISIBLE);
                }

                if (tradeModeChangeListener != null){
                    tradeModeChangeListener.onItemCheckChangedListener();
                }

                Log.d("selectedDisplayMode", mode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkoutShop.setAddress(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        LinearLayout linearAddress;
        View dividerUpAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.checkout_shopName);
            recyclerProducts = itemView.findViewById(R.id.checkout_recycler_products);
            tradeMode = itemView.findViewById(R.id.checkout_tradeMode);
            address = itemView.findViewById(R.id.checkout_address);
            linearAddress = itemView.findViewById(R.id.checkout_linear_address);
            dividerUpAddress = itemView.findViewById(R.id.checkout_divider_UpAddress);

            recyclerProducts.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
