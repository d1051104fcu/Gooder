package com.example.gooder.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TradeMode {
    private static final Map<String, Integer> SHIPPING_MAP = new LinkedHashMap<>();
    static {
        SHIPPING_MAP.put("宅配", 100);
        SHIPPING_MAP.put("超商", 30);
        SHIPPING_MAP.put("面交", 0);
    }

    public static List<String> getDisplayList() {
        List<String> displayList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : SHIPPING_MAP.entrySet()) {
            String display = entry.getKey() + (entry.getValue() > 0 ? " +$" + entry.getValue() : "");
            displayList.add(display);
        }

        return displayList;
    }

    public static int getFreight(String tradeMode) {
        return SHIPPING_MAP.getOrDefault(tradeMode, 0) ;
    }

    public static String extractModeFromDisplay(String display) {
        return display.split(" ")[0];
    }

    public static String getDefaultTradeMode() {
        for (Map.Entry<String, Integer> entry : SHIPPING_MAP.entrySet()) {
            return entry.getKey();
        }
        return "";
    }

    public static int getDefaultFright() {
        for (Map.Entry<String, Integer> entry : SHIPPING_MAP.entrySet()) {
            return entry.getValue();
        }
        return 0;
    }

}
