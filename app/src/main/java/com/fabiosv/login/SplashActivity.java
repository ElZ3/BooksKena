package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // ~3s totales como pediste
    private static final int DURACION_SPLASH = 3000;

    private LinearLayout contenedorGrid;   // grid grande redondeado (aparece desde arriba)
    private LinearLayout cardLogo;         // card pequeña donde vive el logo
    private ImageView logo;                // tu logo
    private View bubble1, bubble2, bubble3;// burbujas estilo lava

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        contenedorGrid = findViewById(R.id.splashGrid);
        cardLogo       = findViewById(R.id.logoCard);
        logo           = findViewById(R.id.logoImage);

        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);

        iniciarAnimaciones();
    }

    private void iniciarAnimaciones() {
        // 1) Fondo y grid entran (slide down + fade)
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
        Animation fadeIn    = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorGrid.startAnimation(slideDown);

        // 2) Pequeño delay y aparece la card del logo (pop-in)
        new Handler().postDelayed(() -> {
            Animation popIn = AnimationUtils.loadAnimation(this, R.anim.card_pop_in);
            cardLogo.startAnimation(popIn);

            // 3) El logo hace scale up + fade-in sutil
            Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            Animation fadeInLogo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            logo.startAnimation(scaleUp);
            logo.startAnimation(fadeInLogo);

            // 4) Burbujas “lava lamp”
            lanzarBurbujas();

        }, 250);

        // 5) Salida: zoom al logo + fade y pasamos a MainActivity
        new Handler().postDelayed(this::salirConZoom, DURACION_SPLASH);
    }

    private void lanzarBurbujas() {
        Animation bubbleAnim1 = AnimationUtils.loadAnimation(this, R.anim.bubble_float_up);
        Animation bubbleAnim2 = AnimationUtils.loadAnimation(this, R.anim.bubble_float_up);
        Animation bubbleAnim3 = AnimationUtils.loadAnimation(this, R.anim.bubble_float_up);

        // Desfase para que se sientan orgánicas
        bubbleAnim1.setStartOffset(0);
        bubbleAnim2.setStartOffset(180);
        bubbleAnim3.setStartOffset(350);

        // Duraciones ligeramente distintas
        bubbleAnim1.setDuration(1200);
        bubbleAnim2.setDuration(1400);
        bubbleAnim3.setDuration(1600);

        bubble1.startAnimation(bubbleAnim1);
        bubble2.startAnimation(bubbleAnim2);
        bubble3.startAnimation(bubbleAnim3);
    }

    private void salirConZoom() {
        Animation zoomOutFade = AnimationUtils.loadAnimation(this, R.anim.zoom_out_fade);
        zoomOutFade.setAnimationListener(new SimpleAnimationEnd(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            // dejamos la transición propia del zoom del logo
            overridePendingTransition(0, 0);
            finish();
        }));
        logo.startAnimation(zoomOutFade);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Limpia animaciones para evitar fugas
        if (logo != null) logo.clearAnimation();
        if (contenedorGrid != null) contenedorGrid.clearAnimation();
        if (cardLogo != null) cardLogo.clearAnimation();
        if (bubble1 != null) bubble1.clearAnimation();
        if (bubble2 != null) bubble2.clearAnimation();
        if (bubble3 != null) bubble3.clearAnimation();
    }

    // Listener cortito para ejecutar algo al finalizar una animación
    private static class SimpleAnimationEnd implements android.view.animation.Animation.AnimationListener {
        private final Runnable end;
        SimpleAnimationEnd(Runnable end) { this.end = end; }
        @Override public void onAnimationStart(Animation animation) {}
        @Override public void onAnimationEnd(Animation animation) { if (end != null) end.run(); }
        @Override public void onAnimationRepeat(Animation animation) {}
    }
}
