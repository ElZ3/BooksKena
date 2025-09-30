package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int DURACION_SPLASH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ImageView logo = findViewById(R.id.imageView2);
        LinearLayout contenedorSplash = findViewById(R.id.splash);

        iniciarAnimacionesMejoradas(logo, contenedorSplash);
    }

    private void iniciarAnimacionesMejoradas(ImageView logo, LinearLayout contenedor) {
        Animation fadeInContenedor = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedor.startAnimation(fadeInContenedor);

        new Handler().postDelayed(() -> {
            Animation escalaLogo = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            Animation fadeInLogo = AnimationUtils.loadAnimation(this, R.anim.fade_in);

            logo.startAnimation(escalaLogo);
            logo.startAnimation(fadeInLogo);

        }, 300);

        new Handler().postDelayed(() -> {
            navegarAMainActivity();
        }, DURACION_SPLASH);
    }

    private void navegarAMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        findViewById(R.id.imageView2).clearAnimation();
        findViewById(R.id.splash).clearAnimation();
    }
}