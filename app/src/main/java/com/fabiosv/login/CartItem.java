package com.fabiosv.login;

public class CartItem {
    public final int productId;
    public final String title;
    public final double unitPrice;
    public final int imageRes;
    public int quantity;

    public CartItem(int productId, String title, double unitPrice, int imageRes, int quantity) {
        this.productId = productId;
        this.title = title;
        this.unitPrice = unitPrice;
        this.imageRes = imageRes;
        this.quantity = quantity;
    }

    public double subtotal() { return unitPrice * quantity; }
}
