package com.fabiosv.login;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static final int MAX_PER_PRODUCT = 5;
    private static CartManager instance;
    private final Map<Integer, CartItem> items = new LinkedHashMap<>();

    private CartManager(){}

    public static synchronized CartManager get() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public synchronized boolean addOrIncrement(int productId, String title, double price, int imageRes) {
        CartItem ci = items.get(productId);
        if (ci == null) {
            items.put(productId, new CartItem(productId, title, price, imageRes, 1));
            return true;
        } else if (ci.quantity < MAX_PER_PRODUCT) {
            ci.quantity++;
            return true;
        }
        return false; // tope 5
    }

    public synchronized void setQuantity(int productId, int qty) {
        CartItem ci = items.get(productId);
        if (ci == null) return;
        if (qty <= 0) items.remove(productId);
        else ci.quantity = Math.min(qty, MAX_PER_PRODUCT);
    }

    public synchronized void remove(int productId) { items.remove(productId); }

    public synchronized List<CartItem> all() { return new ArrayList<>(items.values()); }

    public synchronized void clear() { items.clear(); }

    public synchronized double subtotal() {
        double s = 0;
        for (CartItem ci : items.values()) s += ci.subtotal();
        return s;
    }

    public synchronized int totalUnits() {
        int u = 0;
        for (CartItem ci : items.values()) u += ci.quantity;
        return u;
    }

    public int maxPerProduct() { return MAX_PER_PRODUCT; }
}
