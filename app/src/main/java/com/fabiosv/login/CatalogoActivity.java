package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CatalogoActivity extends AppCompatActivity {

    private LinearLayout contenedorPrincipal;
    private TextView tituloBienvenida;
    private TextView subtituloBienvenida;
    private EditText campoBuscar;
    private Button botonBuscar;
    private Button botonVolverInicio;
    private Button botonVerCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_main);

        // Aplica el padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.catalogo_libros_principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarVistas();
        configurarAnimacionesEntrada();
        configurarBotones();
        configurarBotonAtras();
    }

    private void inicializarVistas() {
        contenedorPrincipal = findViewById(R.id.catalogo_libros_principal);
        tituloBienvenida = findViewById(R.id.titulo_bienvenida);
        subtituloBienvenida = findViewById(R.id.subtitulo_bienvenida);
        campoBuscar = findViewById(R.id.campo_buscar_libros);
        botonBuscar = findViewById(R.id.boton_buscar);
        botonVolverInicio = findViewById(R.id.boton_volver_inicio);
        botonVerCarrito = findViewById(R.id.boton_ver_carrito);
    }

    private void configurarAnimacionesEntrada() {
        // Animación de entrada del contenedor principal
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorPrincipal.startAnimation(fadeIn);

        // Secuencia de animaciones escalonadas
        new Handler().postDelayed(() -> {
            // 1. Título con slide down
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
            tituloBienvenida.startAnimation(slideDown);

            // 2. Subtítulo con fade in
            new Handler().postDelayed(() -> {
                Animation fadeInSubtitulo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                subtituloBienvenida.startAnimation(fadeInSubtitulo);
            }, 200);

            // 3. Campo de búsqueda y botón
            new Handler().postDelayed(() -> {
                Animation slideUpSearch = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
                campoBuscar.startAnimation(slideUpSearch);
                botonBuscar.startAnimation(slideUpSearch);
            }, 400);

            // 4. Botones del pie de página
            new Handler().postDelayed(() -> {
                Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                botonVolverInicio.startAnimation(scaleUp);
                botonVerCarrito.startAnimation(scaleUp);
            }, 600);

        }, 300);
    }

    private void configurarBotones() {
        // Botón Volver al Inicio
        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animación de click
                Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                botonVolverInicio.startAnimation(scaleClick);

                new Handler().postDelayed(() -> {
                    volverAPrincipal();
                }, 200);
            }
        });

        // Botón Ver Carrito
        botonVerCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animación de click
                Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                botonVerCarrito.startAnimation(scaleClick);

                new Handler().postDelayed(() -> {
                    // Aquí puedes navegar a la actividad del carrito
                    Toast.makeText(CatalogoActivity.this, "Funcionalidad de carrito próximamente", Toast.LENGTH_SHORT).show();
                }, 200);
            }
        });

        // Botón Buscar
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                botonBuscar.startAnimation(scaleClick);

                String busqueda = campoBuscar.getText().toString().trim();
                if (!busqueda.isEmpty()) {
                    Toast.makeText(CatalogoActivity.this, "Buscando: " + busqueda, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CatalogoActivity.this, "Escribe algo para buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar botones de categorías
        configurarBotonesCategorias();
    }

    private void configurarBotonesCategorias() {
        int[] idsBotonesCategorias = {
                R.id.boton_ficcion, R.id.boton_poesia, R.id.boton_misterio,
                R.id.boton_romance, R.id.boton_biografias
        };

        for (int i = 0; i < idsBotonesCategorias.length; i++) {
            Button boton = findViewById(idsBotonesCategorias[i]);
            final int delay = i * 100; // Animación escalonada

            new Handler().postDelayed(() -> {
                Animation fadeInBoton = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                boton.startAnimation(fadeInBoton);
            }, 600 + delay);

            // Configurar click listener para cada categoría
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                    boton.startAnimation(scaleClick);

                    String categoria = boton.getText().toString();
                    Toast.makeText(CatalogoActivity.this, "Categoría: " + categoria, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void configurarBotonAtras() {
        // Manejar el gesto de retroceso
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAPrincipal();
            }
        });
    }

    private void volverAPrincipal() {
        // Animación de salida
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        contenedorPrincipal.startAnimation(slideOut);

        new Handler().postDelayed(() -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }, 300);
    }

    @Override
    protected void onPause() {
        super.onPause();
        limpiarAnimaciones();
    }

    private void limpiarAnimaciones() {
        contenedorPrincipal.clearAnimation();
        tituloBienvenida.clearAnimation();
        subtituloBienvenida.clearAnimation();
        campoBuscar.clearAnimation();
        botonBuscar.clearAnimation();
        botonVolverInicio.clearAnimation();
        botonVerCarrito.clearAnimation();
    }
}