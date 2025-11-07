package com.fabiosv.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CarritoActivity extends AppCompatActivity implements CarritoAdapter.Listener {

    // UI
    private LinearLayout sectionHeader, sectionSearch, sectionList, sectionFooter;
    private ImageView logo; private ImageButton btnContact, btnSearch;
    private EditText etSearch; private RecyclerView rv;
    private TextView tvEmpty, tvItems, tvSubtotal, tvTotal;
    private Button btnBack, btnAddMore, btnFinish;

    private CarritoAdapter adapter;
    private static final Pattern QUERY_OK = Pattern.compile("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9\\-\\s]+$");
    private ExoPlayer videoPlayer; // para modal de detalles

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.carrito_main);
        bindViews();
        setupHeader();
        setupList();
        setupSearch();
        setupFooter();
        setupBack();
        refreshTotals();
        runEntryAnimations();
    }

    // --- INIT / SETUP ---
    private void bindViews() {
        sectionHeader = findViewById(R.id.section_header_cart);
        sectionSearch = findViewById(R.id.section_search_cart);
        sectionList   = findViewById(R.id.section_list_cart);
        sectionFooter = findViewById(R.id.section_footer_cart);
        btnContact = findViewById(R.id.btn_contact_cart);
        etSearch = findViewById(R.id.et_search_cart);
        btnSearch = findViewById(R.id.btn_search_cart);
        rv = findViewById(R.id.rv_cart);
        tvEmpty = findViewById(R.id.cart_empty);
        tvItems = findViewById(R.id.tv_items);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTotal = findViewById(R.id.tv_total);
        btnBack = findViewById(R.id.btn_back_catalog);
        btnAddMore = findViewById(R.id.btn_add_more);
        btnFinish = findViewById(R.id.btn_finish);
    }

    private void setupHeader() {
        logo.setOnClickListener(v -> Toast.makeText(this, "Books Kena", Toast.LENGTH_SHORT).show());
        btnContact.setOnClickListener(v -> Toast.makeText(this, "Contactos (próximo)", Toast.LENGTH_SHORT).show());
    }

    private void setupList() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarritoAdapter(this, this);
        rv.setAdapter(adapter);
        adapter.submit(CartManager.get().all());
        toggleEmpty();
    }

    private void setupSearch() {
        btnSearch.setOnClickListener(v -> {
            String q = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(q)) { adapter.filterByTitle(""); toggleEmpty(); return; }
            if (!QUERY_OK.matcher(q).matches()) {
                Toast.makeText(this, "Solo letras, números, espacios y guiones", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter.filterByTitle(q);
            toggleEmptyOrNotFound();
        });
        etSearch.setOnEditorActionListener((tv, act, ev) -> true); // fuerza uso del botón
    }

    private void setupFooter() {
        btnBack.setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); });
        btnAddMore.setOnClickListener(v -> { startActivity(new Intent(this, CatalogoActivity.class)); overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); });
        btnFinish.setOnClickListener(v -> openConfirmDialog());
    }

    private void setupBack() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    // --- ADAPTER LISTENER ---
    @Override public void onQuantityChanged() { refreshTotals(); toggleEmpty(); }
    @Override public void onOpenDetails(CartItem item) { openDetailsModal(item); }

    // --- TOTALES / ESTADOS ---
    private void refreshTotals() {
        int units = CartManager.get().totalUnits();
        double subtotal = CartManager.get().subtotal();
        tvItems.setText("Items: " + units);
        tvSubtotal.setText(String.format(Locale.getDefault(),"Subtotal: $%.2f", subtotal));
        tvTotal.setText(String.format(Locale.getDefault(),"Total: $%.2f", subtotal));
    }

    private void toggleEmpty() {
        boolean empty = CartManager.get().all().isEmpty();
        tvEmpty.setText(empty ? "Aún no has agregado productos" : "");
        tvEmpty.setVisibility(empty ? TextView.VISIBLE : TextView.GONE);
    }

    private void toggleEmptyOrNotFound() {
        tvEmpty.setText(adapter.isEmptyVisible() ? "Producto no encontrado" : "");
        tvEmpty.setVisibility(adapter.isEmptyVisible() ? TextView.VISIBLE : TextView.GONE);
    }

    // --- MODAL DETALLES (reusa detalles_libro.xml + video) ---
    private void openDetailsModal(CartItem it) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detalles_libro);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.92),
                    (int)(getResources().getDisplayMetrics().heightPixels * 0.86));
        }

        PlayerView video = dialog.findViewById(R.id.video_fondo);
        videoPlayer = new ExoPlayer.Builder(this).build();
        video.setPlayer(videoPlayer);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fondodetalle);
        videoPlayer.setMediaItem(MediaItem.fromUri(uri));
        videoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        videoPlayer.setVolume(0f);
        videoPlayer.prepare();
        videoPlayer.play();

        ImageView iv = dialog.findViewById(R.id.imagen_libro_detalles);
        TextView tTitle = dialog.findViewById(R.id.texto_titulo_libro_detalles);
        TextView tAutor = dialog.findViewById(R.id.texto_autor_detalles);
        TextView tPrecio = dialog.findViewById(R.id.texto_precio_detalles);
        TextView tQty = dialog.findViewById(R.id.texto_cantidad_detalles);
        ImageButton btnClose = dialog.findViewById(R.id.boton_cerrar_detalles);
        Button btnMinus = dialog.findViewById(R.id.boton_disminuir_cantidad);
        Button btnPlus = dialog.findViewById(R.id.boton_aumentar_cantidad);
        Button btnAdd = dialog.findViewById(R.id.boton_agregar_carrito_detalles);
        Button btnBuy = dialog.findViewById(R.id.boton_comprar_ahora_detalles);

        iv.setImageResource(it.imageRes);
        tTitle.setText(it.title);
        tAutor.setText(""); // pon autor si lo tienes
        tPrecio.setText(String.format(Locale.getDefault(),"$%.2f", it.unitPrice));
        tQty.setText(String.valueOf(it.quantity));

        btnClose.setOnClickListener(v -> dialog.dismiss());
        btnMinus.setOnClickListener(v -> { CartManager.get().setQuantity(it.productId, Math.max(1, it.quantity-1)); adapter.refresh(); tQty.setText(String.valueOf(CartManager.get().all().stream().filter(ci->ci.productId==it.productId).findFirst().orElse(it).quantity)); });
        btnPlus.setOnClickListener(v -> { CartManager.get().setQuantity(it.productId, Math.min(CartManager.get().maxPerProduct(), it.quantity+1)); adapter.refresh(); tQty.setText(String.valueOf(CartManager.get().all().stream().filter(ci->ci.productId==it.productId).findFirst().orElse(it).quantity)); });
        btnAdd.setOnClickListener(v -> { boolean ok = CartManager.get().addOrIncrement(it.productId, it.title, it.unitPrice, it.imageRes); Toast.makeText(this, ok?"Agregado":"Máximo 5 unidades", Toast.LENGTH_SHORT).show(); adapter.refresh(); });
        btnBuy.setOnClickListener(v -> { Toast.makeText(this, "Compra rápida desde detalles (continuar en carrito)", Toast.LENGTH_SHORT).show(); dialog.dismiss(); });

        dialog.setOnDismissListener((DialogInterface d) -> releasePlayer());
        dialog.show();
    }

    private void releasePlayer() {
        if (videoPlayer != null) { videoPlayer.stop(); videoPlayer.release(); videoPlayer = null; }
    }

    // --- CONFIRMACIÓN COMPRA ---
    private void openConfirmDialog() {
        List<CartItem> items = CartManager.get().all();
        if (items.isEmpty()) { Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show(); return; }

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.modal_resumen_compra);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.92),
                    (int)(getResources().getDisplayMetrics().heightPixels * 0.70));
        }

        LinearLayout container = dialog.findViewById(R.id.lines_container);
        TextView tvTotalConf = dialog.findViewById(R.id.tv_total_confirm);
        Button btnCancelar = dialog.findViewById(R.id.btn_cancel_confirm);
        Button btnComprar = dialog.findViewById(R.id.btn_buy_confirm);

        container.removeAllViews();
        double subtotal = 0;
        for (CartItem ci : items) {
            subtotal += ci.subtotal();
            TextView line = new TextView(this);
            line.setTextColor(0xFFFFFFFF);
            line.setTextSize(14f);
            line.setText(String.format(Locale.getDefault(), "%dx %s — $%.2f", ci.quantity, ci.title, ci.subtotal()));
            container.addView(line);
        }
        tvTotalConf.setText(String.format(Locale.getDefault(), "Total a pagar: $%.2f", subtotal));

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnComprar.setOnClickListener(v -> {
            CartManager.get().clear();      // cierra “bucle” de compra
            adapter.submit(CartManager.get().all());
            refreshTotals();
            toggleEmpty();
            Toast.makeText(this, "¡Compra realizada!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    // --- ANIMACIONES (usa las que ya tienes) ---
    private void runEntryAnimations() {
        sectionHeader.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_in_down));
        sectionSearch.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.section_slide_in));
        sectionList.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        sectionFooter.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
    }

    @Override protected void onPause() { super.onPause(); releasePlayer(); }
    @Override protected void onDestroy() { super.onDestroy(); releasePlayer(); }
}
