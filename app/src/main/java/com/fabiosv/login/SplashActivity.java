package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ImageView logo = findViewById(R.id.imageView2);

        // Carga la animación de fade-in
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        // Pasa a la siguiente actividad después de la duración del splash
        new Handler().postDelayed(() -> {
            // Corrige la intención para que vaya a ActivityMain
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            // No uses overridePendingTransition aquí, ya que a menudo causa un corte visual
            // Simplemente finaliza esta actividad
            finish();
        }, SPLASH_DURATION);
    }
}