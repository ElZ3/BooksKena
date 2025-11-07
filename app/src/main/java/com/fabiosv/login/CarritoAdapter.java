package com.fabiosv.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CartVH> {

    public interface Listener {
        void onQuantityChanged();
        void onOpenDetails(CartItem item);
    }

    private final LayoutInflater inflater;
    private final Listener listener;
    private List<CartItem> original = new ArrayList<>();
    private List<CartItem> visible = new ArrayList<>();

    public CarritoAdapter(Context ctx, Listener listener) {
        this.inflater = LayoutInflater.from(ctx);
        this.listener = listener;
    }

    public void submit(List<CartItem> items) {
        original = new ArrayList<>(items);
        visible = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void filterByTitle(String q) {
        if (q == null || q.trim().isEmpty()) {
            visible = new ArrayList<>(original);
        } else {
            String needle = q.toLowerCase(Locale.getDefault());
            List<CartItem> out = new ArrayList<>();
            for (CartItem ci : original) {
                if (ci.title.toLowerCase(Locale.getDefault()).contains(needle)) out.add(ci);
            }
            visible = out;
        }
        notifyDataSetChanged();
    }

    public boolean isEmptyVisible() { return visible.isEmpty(); }

    @NonNull @Override public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartVH(inflater.inflate(R.layout.item_carrito, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull CartVH h, int pos) {
        CartItem it = visible.get(pos);
        h.cover.setImageResource(it.imageRes);
        h.title.setText(it.title);
        h.price.setText(String.format(Locale.getDefault(), "$%.2f", it.unitPrice));
        h.qty.setText(String.valueOf(it.quantity));

        h.btnMinus.setOnClickListener(v -> { CartManager.get().setQuantity(it.productId, it.quantity - 1); refresh(); });
        h.btnPlus.setOnClickListener(v -> { CartManager.get().setQuantity(it.productId, it.quantity + 1); refresh(); });
        h.btnRemove.setOnClickListener(v -> { CartManager.get().remove(it.productId); refresh(); });
        h.btnDetails.setOnClickListener(v -> listener.onOpenDetails(it));
    }

    @Override public int getItemCount() { return visible.size(); }

    public void refresh() {
        submit(CartManager.get().all());
        listener.onQuantityChanged();
    }

    static class CartVH extends RecyclerView.ViewHolder {
        ImageView cover; TextView title, price, qty;
        Button btnMinus, btnPlus; ImageButton btnDetails, btnRemove;
        CartVH(@NonNull View v) {
            super(v);
            cover = v.findViewById(R.id.img_cover);
            title = v.findViewById(R.id.tv_title);
            price = v.findViewById(R.id.tv_price);
            qty = v.findViewById(R.id.tv_qty);
            btnMinus = v.findViewById(R.id.btn_minus);
            btnPlus = v.findViewById(R.id.btn_plus);
            btnDetails = v.findViewById(R.id.btn_details);
            btnRemove = v.findViewById(R.id.btn_remove);
        }
    }
}
