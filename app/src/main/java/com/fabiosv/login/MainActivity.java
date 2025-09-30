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
    private ImageView imagenLibro;
    private TextView tituloBienvenida;
    private TextView descripcion;
    private Button botonIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aplica el padding para las barras del sistema
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
        imagenLibro = findViewById(R.id.imageView5);
        tituloBienvenida = findViewById(R.id.textView2);
        descripcion = findViewById(R.id.textView3);
        botonIniciar = findViewById(R.id.iniciarnbtn);
    }

    private void configurarAnimacionesEntrada() {
        new Handler().postDelayed(() -> {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            contenedorPrincipal.startAnimation(fadeIn);

            new Handler().postDelayed(() -> {
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
                imagenLibro.startAnimation(slideUp);
            }, 200);

            new Handler().postDelayed(() -> {
                Animation fadeInTitulo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                tituloBienvenida.startAnimation(fadeInTitulo);
            }, 400);

            new Handler().postDelayed(() -> {
                Animation fadeInDesc = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                descripcion.startAnimation(fadeInDesc);
            }, 600);

            new Handler().postDelayed(() -> {
                Animation escalaBoton = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                Animation fadeInBoton = AnimationUtils.loadAnimation(this, R.anim.fade_in);

                botonIniciar.startAnimation(escalaBoton);
                botonIniciar.startAnimation(fadeInBoton);
            }, 800);

        }, 300);
    }

    private void configurarBotonIniciar() {
        botonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation escalaClick = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_click);
                botonIniciar.startAnimation(escalaClick);
                new Handler().postDelayed(() -> {
                    navegarALogin();
                }, 200);
            }
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
        imagenLibro.clearAnimation();
        tituloBienvenida.clearAnimation();
        descripcion.clearAnimation();
        botonIniciar.clearAnimation();
    }
}