package com.fabiosv.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Locale;
import java.util.regex.Pattern;

public class CatalogoActivity extends AppCompatActivity {

    // ==== IDs fijos de tus 4 libros ) ====
    private static final int ID_LIBRO_1 = 1; // Nico y Nacho
    private static final int ID_LIBRO_2 = 2; // Visions Of Tomorrow
    private static final int ID_LIBRO_3 = 3; // El Reto
    private static final int ID_LIBRO_4 = 4; // Dracula


    private LinearLayout contenedorPrincipal;
    private ImageView logoApp, contactoCreador;
    private EditText campoBuscar;
    private ImageButton botonBuscar;
    private Button botonVolverInicio, botonVerCarrito;
    private Button botonFiccion, botonPoesia, botonMisterio, botonRomance, botonBiografias;
    private ImageButton botonAgregar1, botonAgregar2, botonAgregar3, botonAgregar4;
    private ImageButton botonVerDetalles1, botonVerDetalles2, botonVerDetalles3, botonVerDetalles4;
    private TextView titulo1, titulo2, titulo3, titulo4;
    private TextView noResultsView;
    private ExoPlayer videoPlayer;

    // Validación de búsqueda (letras, números, espacios y guiones)
    private static final Pattern QUERY_OK = Pattern.compile("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9\\-\\s]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_main);

        // Inset para status/nav bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.catalogo_libros_principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === 1) Vistas / referencias ===
        bindViews();

        // === 2) Acciones principales ===
        setupHeaderActions();
        setupSearchActions();
        setupCategoryFilters();
        setupBookButtons();
        setupFooterButtons();
        setupBackPress();

        // === 3) Animaciones (al final del flujo) ===
        runEntryAnimations();
    }

    // ------------------------------------------------------------
    // VISTAS
    // ------------------------------------------------------------
    private void bindViews() {
        contenedorPrincipal = findViewById(R.id.catalogo_libros_principal);

        logoApp = findViewById(R.id.logo_app);

        campoBuscar = findViewById(R.id.campo_buscar_libros);
        botonBuscar = findViewById(R.id.boton_buscar);

        botonVolverInicio = findViewById(R.id.boton_volver_inicio);
        botonVerCarrito = findViewById(R.id.boton_ver_carrito);

        botonFiccion = findViewById(R.id.boton_ficcion);
        botonPoesia = findViewById(R.id.boton_poesia);
        botonMisterio = findViewById(R.id.boton_misterio);
        botonRomance = findViewById(R.id.boton_romance);
        botonBiografias = findViewById(R.id.boton_biografias);

        botonAgregar1 = findViewById(R.id.boton_agregar_libro_1);
        botonAgregar2 = findViewById(R.id.boton_agregar_libro_2);
        botonAgregar3 = findViewById(R.id.boton_agregar_libro_3);
        botonAgregar4 = findViewById(R.id.boton_agregar_libro_4);

        botonVerDetalles1 = findViewById(R.id.boton_ver_detalles_libro_1);
        botonVerDetalles2 = findViewById(R.id.boton_ver_detalles_libro_2);
        botonVerDetalles3 = findViewById(R.id.boton_ver_detalles_libro_3);
        botonVerDetalles4 = findViewById(R.id.boton_ver_detalles_libro_4);

        titulo1 = findViewById(R.id.titulo_libro_1);
        titulo2 = findViewById(R.id.titulo_libro_2);
        titulo3 = findViewById(R.id.titulo_libro_3);
        titulo4 = findViewById(R.id.titulo_libro_4);

        // Vista de “no encontrado” (overlay ligera). Se crea una sola vez.
        noResultsView = new TextView(this);
        noResultsView.setText("Producto no encontrado");
        noResultsView.setTextColor(0xFFFFFFFF);
        noResultsView.setTextSize(16f);
        noResultsView.setPadding(0, 40, 0, 0);
        noResultsView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        noResultsView.setVisibility(View.GONE);
        contenedorPrincipal.addView(noResultsView);
    }

    // ------------------------------------------------------------
    // HEADER
    // ------------------------------------------------------------
    private void setupHeaderActions() {
        logoApp.setOnClickListener(v -> Toast.makeText(this, "Books Kena", Toast.LENGTH_SHORT).show());
        contactoCreador.setOnClickListener(v -> {
            // abre Contacto (cuando la tengas)
            Toast.makeText(this, "Contactos (próximamente)", Toast.LENGTH_SHORT).show();
        });
    }

    // ------------------------------------------------------------
    // BÚSQUEDA
    // ------------------------------------------------------------
    private void setupSearchActions() {
        botonBuscar.setOnClickListener(v -> {
            String q = campoBuscar.getText().toString().trim();
            if (TextUtils.isEmpty(q)) {
                // limpiar filtros
                showAllCards();
                noResultsView.setVisibility(View.GONE);
                return;
            }
            if (!QUERY_OK.matcher(q).matches()) {
                Toast.makeText(this, "Solo letras, números, espacios y guiones", Toast.LENGTH_SHORT).show();
                return;
            }
            filterByTitle(q);
        });

        // Forzar uso del botón (no buscar con Enter)
        campoBuscar.setOnEditorActionListener((tv, a, e) -> true);
    }

    // ------------------------------------------------------------
    // CATEGORÍAS
    // ------------------------------------------------------------
    private void setupCategoryFilters() {
        botonFiccion.setOnClickListener(v -> filterByCategory("ficcion"));
        botonPoesia.setOnClickListener(v -> filterByCategory("poesia"));
        botonMisterio.setOnClickListener(v -> filterByCategory("misterio"));
        botonRomance.setOnClickListener(v -> filterByCategory("romance"));
        botonBiografias.setOnClickListener(v -> filterByCategory("biografias"));
    }

    // ------------------------------------------------------------
    // BOTONES DE LIBRO (Agregar / Ver Detalles)
    // ------------------------------------------------------------
    private void setupBookButtons() {
        // Agregar rápido
        botonAgregar1.setOnClickListener(v -> addToCart(ID_LIBRO_1, "Las Aventuras de Nico y Nacho", 18.99, R.drawable.libro1));
        botonAgregar2.setOnClickListener(v -> addToCart(ID_LIBRO_2, "Visions Of Tomorrow", 22.50, R.drawable.libro2));
        botonAgregar3.setOnClickListener(v -> addToCart(ID_LIBRO_3, "El Reto", 19.99, R.drawable.libro3));
        botonAgregar4.setOnClickListener(v -> addToCart(ID_LIBRO_4, "Drácula", 19.99, R.drawable.libro4));

        // Ver detalles (modal con video + agregar desde modal)
        botonVerDetalles1.setOnClickListener(v ->
                mostrarDetallesLibro("Las Aventuras de Nico y Nacho: El Misterio de la Laguna",
                        "Pablo Herrera R.", "$18.99", R.drawable.libro1,
                        ID_LIBRO_1, 18.99));
        botonVerDetalles2.setOnClickListener(v ->
                mostrarDetallesLibro("Visions Of Tomorrow",
                        "Leo Waen", "$22.50", R.drawable.libro2,
                        ID_LIBRO_2, 22.50));
        botonVerDetalles3.setOnClickListener(v ->
                mostrarDetallesLibro("El Reto",
                        "Maria Serrano Burgos y David Sierra Liston", "$19.99", R.drawable.libro3,
                        ID_LIBRO_3, 19.99));
        botonVerDetalles4.setOnClickListener(v ->
                mostrarDetallesLibro("Drácula",
                        "Bram Stoker", "$19.99", R.drawable.libro4,
                        ID_LIBRO_4, 19.99));
    }

    // ------------------------------------------------------------
    // FOOTER
    // ------------------------------------------------------------
    private void setupFooterButtons() {
        botonVolverInicio.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        botonVerCarrito.setOnClickListener(v -> {
            startActivity(new Intent(this, CarritoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    // ------------------------------------------------------------
    // BACK HARDWARE
    // ------------------------------------------------------------
    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    // ------------------------------------------------------------
    // FILTROS (por título y por categoría)
    // ------------------------------------------------------------
    private void filterByTitle(String q) {
        String needle = q.toLowerCase(Locale.getDefault());

        boolean m1 = titulo1.getText().toString().toLowerCase(Locale.getDefault()).contains(needle);
        boolean m2 = titulo2.getText().toString().toLowerCase(Locale.getDefault()).contains(needle);
        boolean m3 = titulo3.getText().toString().toLowerCase(Locale.getDefault()).contains(needle);
        boolean m4 = titulo4.getText().toString().toLowerCase(Locale.getDefault()).contains(needle);

        setCardVisible(titulo1, m1);
        setCardVisible(titulo2, m2);
        setCardVisible(titulo3, m3);
        setCardVisible(titulo4, m4);

        boolean any = m1 || m2 || m3 || m4;
        noResultsView.setVisibility(any ? View.GONE : View.VISIBLE);
    }

    private void filterByCategory(String cat) {
        boolean c1 = "ficcion".equals(cat) || "biografias".equals(cat); // ejemplo de pertenencia
        boolean c2 = "ficcion".equals(cat) || "misterio".equals(cat);
        boolean c3 = "ficcion".equals(cat) || "poesia".equals(cat);
        boolean c4 = "misterio".equals(cat) || "romance".equals(cat);

        setCardVisible(titulo1, c1);
        setCardVisible(titulo2, c2);
        setCardVisible(titulo3, c3);
        setCardVisible(titulo4, c4);

        boolean any = c1 || c2 || c3 || c4;
        noResultsView.setVisibility(any ? View.GONE : View.VISIBLE);
    }

    private void showAllCards() {
        setCardVisible(titulo1, true);
        setCardVisible(titulo2, true);
        setCardVisible(titulo3, true);
        setCardVisible(titulo4, true);
    }

    private void setCardVisible(TextView titleView, boolean visible) {
        View parent1 = (View) titleView.getParent();            // LinearLayout vertical
        if (parent1 == null) return;
        View card = (View) parent1.getParent();                  // LinearLayout horizontal (tarjeta)
        if (card != null) card.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    // ------------------------------------------------------------
    // CARRITO
    // ------------------------------------------------------------
    private void addToCart(int id, String title, double price, int imageRes) {
        boolean ok = CartManager.get().addOrIncrement(id, title, price, imageRes);
        Toast.makeText(this, ok ? "Agregado al carrito" : "Máximo 5 unidades por producto", Toast.LENGTH_SHORT).show();
    }

    // ------------------------------------------------------------
    // MODAL DETALLES (con video + agregar al carrito)
    // ------------------------------------------------------------
    private void mostrarDetallesLibro(String titulo, String autor, String precioTexto,
                                      int imagenRes, int productId, double unitPrice) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detalles_libro);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.92),
                    (int)(getResources().getDisplayMetrics().heightPixels * 0.86)
            );
        }

        // VIDEO
        PlayerView videoFondo = dialog.findViewById(R.id.video_fondo);
        videoPlayer = new ExoPlayer.Builder(this).build();
        videoFondo.setPlayer(videoPlayer);
        try {
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fondodetalle);
            videoPlayer.setMediaItem(MediaItem.fromUri(videoUri));
            videoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            videoPlayer.setVolume(0f);
            videoPlayer.prepare();
            videoPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Views del modal
        ImageView imagenLibro = dialog.findViewById(R.id.imagen_libro_detalles);
        TextView textoTitulo = dialog.findViewById(R.id.texto_titulo_libro_detalles);
        TextView textoAutor  = dialog.findViewById(R.id.texto_autor_detalles);
        TextView textoPrecio = dialog.findViewById(R.id.texto_precio_detalles);
        final TextView textoCantidad = dialog.findViewById(R.id.texto_cantidad_detalles);

        ImageButton botonCerrar = dialog.findViewById(R.id.boton_cerrar_detalles);
        Button botonDisminuir = dialog.findViewById(R.id.boton_disminuir_cantidad);
        Button botonAumentar  = dialog.findViewById(R.id.boton_aumentar_cantidad);
        Button botonAgregar   = dialog.findViewById(R.id.boton_agregar_carrito_detalles);
        Button botonComprar   = dialog.findViewById(R.id.boton_comprar_ahora_detalles);

        imagenLibro.setImageResource(imagenRes);
        textoTitulo.setText(titulo);
        textoAutor.setText(autor);
        textoPrecio.setText(precioTexto);

        final int[] cantidad = {1};
        textoCantidad.setText(String.valueOf(cantidad[0]));

        botonCerrar.setOnClickListener(v -> { liberarReproductorVideo(); dialog.dismiss(); });

        botonDisminuir.setOnClickListener(v -> {
            if (cantidad[0] > 1) cantidad[0]--;
            textoCantidad.setText(String.valueOf(cantidad[0]));
        });

        botonAumentar.setOnClickListener(v -> {
            if (cantidad[0] < CartManager.get().maxPerProduct()) cantidad[0]++;
            textoCantidad.setText(String.valueOf(cantidad[0]));
        });

        botonAgregar.setOnClickListener(v -> {
            boolean ok = true;
            for (int i = 0; i < cantidad[0]; i++) {
                if (!CartManager.get().addOrIncrement(productId, titulo, unitPrice, imagenRes)) {
                    ok = false; break;
                }
            }
            Toast.makeText(this, ok ? "Agregado al carrito" : "Máximo 5 unidades por producto", Toast.LENGTH_SHORT).show();
            liberarReproductorVideo();
            dialog.dismiss();
        });

        botonComprar.setOnClickListener(v -> {
            // Futuro: ir directo a flujo de compra; por ahora solo agregar y cerrar
            boolean ok = true;
            for (int i = 0; i < cantidad[0]; i++) {
                if (!CartManager.get().addOrIncrement(productId, titulo, unitPrice, imagenRes)) {
                    ok = false; break;
                }
            }
            Toast.makeText(this, ok ? "Producto listo en tu carrito" : "Máximo 5 unidades por producto", Toast.LENGTH_SHORT).show();
            liberarReproductorVideo();
            dialog.dismiss();
        });

        // Liberar recursos al cerrar
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialogInterface) {
                liberarReproductorVideo();
            }
        });

        // Animación ligera del modal
        dialog.show();
        View decor = dialog.getWindow() != null ? dialog.getWindow().getDecorView() : null;
        if (decor != null) {
            decor.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
        }
    }

    private void liberarReproductorVideo() {
        if (videoPlayer != null) {
            videoPlayer.stop();
            videoPlayer.release();
            videoPlayer = null;
        }
    }

    // ------------------------------------------------------------
    // ANIMACIONES DE ENTRADA (usa las que ya tienes en /res/anim)
    // ------------------------------------------------------------
    private void runEntryAnimations() {
        // raíz
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorPrincipal.startAnimation(fadeIn);

        // header
        new Handler().postDelayed(() -> {
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
            logoApp.startAnimation(slideDown);
            contactoCreador.startAnimation(slideDown);
        }, 150);

        // buscador
        new Handler().postDelayed(() -> {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
            campoBuscar.startAnimation(slideUp);
            botonBuscar.startAnimation(slideUp);
        }, 300);

        // footer (botones)
        new Handler().postDelayed(() -> {
            Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            botonVolverInicio.startAnimation(scaleUp);
            botonVerCarrito.startAnimation(scaleUp);
        }, 450);
    }

    @Override
    protected void onPause() {
        super.onPause();
        liberarReproductorVideo();
        limpiarAnimaciones();
    }

    private void limpiarAnimaciones() {
        contenedorPrincipal.clearAnimation();
        logoApp.clearAnimation();
        contactoCreador.clearAnimation();
        campoBuscar.clearAnimation();
        botonBuscar.clearAnimation();
        botonVolverInicio.clearAnimation();
        botonVerCarrito.clearAnimation();
    }
}
