package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout contenedorPrincipal;
    private LinearLayout gridContainer;
    private ImageView imagenLibro;
    private TextView tituloBienvenida;
    private TextView descripcion;
    private Button botonIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarVistas();
        configurarAnimacionesEntrada();
        configurarBotonIniciar();
    }

    private void inicializarVistas() {
        contenedorPrincipal = findViewById(R.id.main);
        gridContainer = findViewById(R.id.gridContainer);
        imagenLibro = findViewById(R.id.imageView5);
        tituloBienvenida = findViewById(R.id.textView2);
        descripcion = findViewById(R.id.textView3);
        botonIniciar = findViewById(R.id.iniciarnbtn);
    }

    private void configurarAnimacionesEntrada() {
        // Animación flotante para el grid (se repite suavemente)
        Animation floatAnim = AnimationUtils.loadAnimation(this, R.anim.float_soft);
        gridContainer.startAnimation(floatAnim);

        // Entrada tipo pop para los elementos
        new Handler().postDelayed(() -> {
            Animation popIn = AnimationUtils.loadAnimation(this, R.anim.card_pop_in);
            imagenLibro.startAnimation(popIn);
        }, 150);

        new Handler().postDelayed(() -> {
            Animation popIn = AnimationUtils.loadAnimation(this, R.anim.card_pop_in);
            tituloBienvenida.startAnimation(popIn);
        }, 300);

        new Handler().postDelayed(() -> {
            Animation popIn = AnimationUtils.loadAnimation(this, R.anim.card_pop_in);
            descripcion.startAnimation(popIn);
        }, 450);

        new Handler().postDelayed(() -> {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            botonIniciar.startAnimation(fadeIn);
        }, 600);
    }

    private void configurarBotonIniciar() {
        botonIniciar.setOnClickListener(v -> {
            Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_click);
            botonIniciar.startAnimation(clickAnim);
            new Handler().postDelayed(this::navegarALogin, 200);
        });
    }

    private void navegarALogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        limpiarAnimaciones();
    }

    private void limpiarAnimaciones() {
        contenedorPrincipal.clearAnimation();
        gridContainer.clearAnimation();
        imagenLibro.clearAnimation();
        tituloBienvenida.clearAnimation();
        descripcion.clearAnimation();
        botonIniciar.clearAnimation();
    }
}
