package com.fabiosv.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    // Botones de libros
    private Button botonVerDetalles1, botonVerDetalles2, botonVerDetalles3, botonVerDetalles4;
    private Button botonAgregar1, botonAgregar2, botonAgregar3, botonAgregar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_main);

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

        // Inicializar botones de libros
        botonVerDetalles1 = findViewById(R.id.boton_ver_detalles_libro_1);
        botonVerDetalles2 = findViewById(R.id.boton_ver_detalles_libro_2);
        botonVerDetalles3 = findViewById(R.id.boton_ver_detalles_libro_3);
        botonVerDetalles4 = findViewById(R.id.boton_ver_detalles_libro_4);

        botonAgregar1 = findViewById(R.id.boton_agregar_libro_1);
        botonAgregar2 = findViewById(R.id.boton_agregar_libro_2);
        botonAgregar3 = findViewById(R.id.boton_agregar_libro_3);
        botonAgregar4 = findViewById(R.id.boton_agregar_libro_4);
    }

    private void configurarAnimacionesEntrada() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorPrincipal.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
            tituloBienvenida.startAnimation(slideDown);

            new Handler().postDelayed(() -> {
                Animation fadeInSubtitulo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                subtituloBienvenida.startAnimation(fadeInSubtitulo);
            }, 200);

            new Handler().postDelayed(() -> {
                Animation slideUpSearch = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
                campoBuscar.startAnimation(slideUpSearch);
                botonBuscar.startAnimation(slideUpSearch);
            }, 400);

            new Handler().postDelayed(() -> {
                Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                botonVolverInicio.startAnimation(scaleUp);
                botonVerCarrito.startAnimation(scaleUp);
            }, 600);

        }, 300);
    }

    private void configurarBotones() {
        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                botonVolverInicio.startAnimation(scaleClick);

                new Handler().postDelayed(() -> {
                    volverAPrincipal();
                }, 200);
            }
        });

        botonVerCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleClick = AnimationUtils.loadAnimation(CatalogoActivity.this, R.anim.scale_click);
                botonVerCarrito.startAnimation(scaleClick);

                new Handler().postDelayed(() -> {
                    Toast.makeText(CatalogoActivity.this, "Funcionalidad de carrito próximamente", Toast.LENGTH_SHORT).show();
                }, 200);
            }
        });

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

        configurarBotonesCategorias();
        configurarBotonesLibros();
    }

    private void configurarBotonesCategorias() {
        int[] idsBotonesCategorias = {
                R.id.boton_ficcion, R.id.boton_poesia, R.id.boton_misterio,
                R.id.boton_romance, R.id.boton_biografias
        };

        for (int i = 0; i < idsBotonesCategorias.length; i++) {
            Button boton = findViewById(idsBotonesCategorias[i]);
            final int delay = i * 100;

            new Handler().postDelayed(() -> {
                Animation fadeInBoton = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                boton.startAnimation(fadeInBoton);
            }, 600 + delay);

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

    private void configurarBotonesLibros() {
        // Configurar botones "Ver Detalles"
        botonVerDetalles1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDetallesLibro(
                        "Las Aventuras de Nico y Nacho: El Misterio de la Laguna",
                        "Pablo Herrera R.",
                        "$18.99",
                        "Únete a Nico y Nacho en esta emocionante aventura donde descubrirán los secretos ocultos en la laguna misteriosa. Una historia llena de amistad, valentía y descubrimientos que cautivará a lectores de todas las edades.",
                        R.drawable.libro1
                );
            }
        });

        botonVerDetalles2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDetallesLibro(
                        "Visions Of Tomorrow",
                        "Leo Waen",
                        "$22.50",
                        "Una visión futurista de la humanidad y la tecnología. Explora los límites de la innovación y el impacto de las decisiones humanas en el destino de nuestra civilización.",
                        R.drawable.libro2
                );
            }
        });

        botonVerDetalles3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDetallesLibro(
                        "El Reto",
                        "Maria Serrano Burgos y David Sierra Liston",
                        "$19.99",
                        "Una historia inspiradora sobre superación personal y trabajo en equipo. Descubre cómo los desafíos pueden convertirse en las mayores oportunidades de crecimiento.",
                        R.drawable.libro3
                );
            }
        });

        botonVerDetalles4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDetallesLibro(
                        "Dracula",
                        "Bram Stoker",
                        "$19.99",
                        "El clásico de terror que definió el género vampírico. Una obra maestra de la literatura gótica que continúa fascinando a generaciones de lectores.",
                        R.drawable.libro4
                );
            }
        });

        // Configurar botones "Agregar" (funcionalidad básica por ahora)
        botonAgregar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarritoRapido("Las Aventuras de Nico y Nacho");
            }
        });

        botonAgregar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarritoRapido("Visions Of Tomorrow");
            }
        });

        botonAgregar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarritoRapido("El Reto");
            }
        });

        botonAgregar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarritoRapido("Dracula");
            }
        });
    }

    private void mostrarDetallesLibro(String titulo, String autor, String precio, String descripcion, int imagenRes) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detalles_libro);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.95),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        // Obtener referencias de las vistas del modal
        ImageView imagenLibro = dialog.findViewById(R.id.imagen_libro_detalles);
        TextView textoTitulo = dialog.findViewById(R.id.texto_titulo_libro_detalles);
        TextView textoAutor = dialog.findViewById(R.id.texto_autor_detalles);
        TextView textoPrecio = dialog.findViewById(R.id.texto_precio_detalles);
        TextView textoDescripcion = dialog.findViewById(R.id.texto_descripcion_detalles);
        final TextView textoCantidad = dialog.findViewById(R.id.texto_cantidad_detalles);

        ImageButton botonCerrar = dialog.findViewById(R.id.boton_cerrar_detalles);
        Button botonDisminuir = dialog.findViewById(R.id.boton_disminuir_cantidad);
        Button botonAumentar = dialog.findViewById(R.id.boton_aumentar_cantidad);
        Button botonAgregarCarrito = dialog.findViewById(R.id.boton_agregar_carrito_detalles);
        Button botonComprarAhora = dialog.findViewById(R.id.boton_comprar_ahora_detalles);

        // Configurar los datos del libro
        imagenLibro.setImageResource(imagenRes);
        textoTitulo.setText(titulo);
        textoAutor.setText(autor);
        textoPrecio.setText(precio);
        textoDescripcion.setText(descripcion);

        final int[] cantidad = {1};

        // Configurar listeners
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        botonDisminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad[0] > 1) {
                    cantidad[0]--;
                    textoCantidad.setText(String.valueOf(cantidad[0]));
                }
            }
        });

        botonAumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad[0] < 99) {
                    cantidad[0]++;
                    textoCantidad.setText(String.valueOf(cantidad[0]));
                }
            }
        });

        botonAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CatalogoActivity.this,
                        cantidad[0] + "x " + titulo + " agregado al carrito",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        botonComprarAhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CatalogoActivity.this,
                        "Comprando " + cantidad[0] + "x " + titulo,
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void agregarAlCarritoRapido(String nombreLibro) {
        Toast.makeText(this, nombreLibro + " agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    private void configurarBotonAtras() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAPrincipal();
            }
        });
    }

    private void volverAPrincipal() {
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