package com.fabiosv.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;   // <-- IMPORTANTE
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private ViewPager2 vpCarrusel;
    private ImageButton btnPrev, btnNext, btnContact;
    private LinearLayout dotsContainer, cardPrincipal;
    private GridLayout blobLeft, blobRight;     // <-- tipos correctos
    private TextView titulo, descripcion;

    private CarouselAdapter adapter;
    private final Handler autoHandler = new Handler();
    private static final long AUTO_DELAY = 3000L; // 3s

    private final Runnable autoRunnable = new Runnable() {
        @Override public void run() {
            if (adapter == null || adapter.getItemCount() == 0) return;
            int next = (vpCarrusel.getCurrentItem() + 1) % adapter.getItemCount();
            vpCarrusel.setCurrentItem(next, true);
            autoHandler.postDelayed(this, AUTO_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.principal_main);

        initViews();
        setupBlobsFloat();
        setupCarousel();
        setupButtons();
        enterAnimations();
    }

    private void initViews() {
        vpCarrusel   = findViewById(R.id.vpCarrusel);
        btnPrev      = findViewById(R.id.btnPrev);
        btnNext      = findViewById(R.id.btnNext);
        btnContact   = findViewById(R.id.btnContact);
        dotsContainer= findViewById(R.id.dotsContainer);
        cardPrincipal= findViewById(R.id.cardPrincipal);
        blobLeft     = findViewById(R.id.blobLeft);   // <-- ya no castea a LinearLayout
        blobRight    = findViewById(R.id.blobRight);  // <--
        titulo       = findViewById(R.id.titulo);
        descripcion  = findViewById(R.id.descripcion);
    }

    private void setupBlobsFloat() {
        Animation floatSoft = AnimationUtils.loadAnimation(this, R.anim.float_soft);
        blobLeft.startAnimation(floatSoft);

        Animation floatSoft2 = AnimationUtils.loadAnimation(this, R.anim.float_soft);
        floatSoft2.setStartOffset(600);
        blobRight.startAnimation(floatSoft2);

        Animation floatCard = AnimationUtils.loadAnimation(this, R.anim.float_soft);
        floatCard.setDuration(2800);
        cardPrincipal.startAnimation(floatCard);
    }

    private void setupCarousel() {
        // carrusel1 es VIDEO: debe estar en /res/raw como carrusel1.mp4 (R.raw.carrusel1)
        List<CarouselAdapter.CarouselItem> items = new ArrayList<>();
        items.add(CarouselAdapter.CarouselItem.video(resToUri(R.raw.carrusel1)));
        items.add(CarouselAdapter.CarouselItem.image(R.drawable.carrusel2));
        items.add(CarouselAdapter.CarouselItem.image(R.drawable.carrusel3));
        items.add(CarouselAdapter.CarouselItem.image(R.drawable.carrusel4));
        items.add(CarouselAdapter.CarouselItem.image(R.drawable.carrusel5));

        adapter = new CarouselAdapter(this, items);
        vpCarrusel.setAdapter(adapter);
        vpCarrusel.setOffscreenPageLimit(1);

        buildDots(items.size());
        vpCarrusel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) { selectDot(position); }
        });

        autoHandler.postDelayed(autoRunnable, AUTO_DELAY);

        btnPrev.setOnClickListener(v -> {
            pauseAuto();
            int prev = vpCarrusel.getCurrentItem() - 1;
            if (prev < 0) prev = adapter.getItemCount() - 1;
            vpCarrusel.setCurrentItem(prev, true);
            resumeAuto();
        });

        btnNext.setOnClickListener(v -> {
            pauseAuto();
            int next = (vpCarrusel.getCurrentItem() + 1) % adapter.getItemCount();
            vpCarrusel.setCurrentItem(next, true);
            resumeAuto();
        });
    }

    private Uri resToUri(int resId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + resId);
    }

    private void buildDots(int count) {
        dotsContainer.removeAllViews();
        for (int i = 0; i < count; i++) {
            android.view.View dot = new android.view.View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
            lp.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(R.drawable.dot_inactive);
            dotsContainer.addView(dot);
        }
        selectDot(0);
    }

    private void selectDot(int index) {
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            dotsContainer.getChildAt(i).setBackgroundResource(
                    i == index ? R.drawable.boton_agregar_redondeado : R.drawable.dot_inactive
            );
        }
    }

    private void setupButtons() {
        findViewById(R.id.btnCatalogo).setOnClickListener(v -> {
            pauseAuto();
            startActivity(new Intent(this, CatalogoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            pauseAuto();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        findViewById(R.id.btnAcerca).setOnClickListener(v -> {
            pauseAuto();
            // Si aún no tienes ContactoActivity, comenta estas dos líneas para evitar crash.
            startActivity(new Intent(this, ContactoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnContact.setOnClickListener(v -> {
            pauseAuto();
            // Igual que arriba: solo si ya existe ContactoActivity.
            startActivity(new Intent(this, ContactoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void enterAnimations() {
        cardPrincipal.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
        titulo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
        descripcion.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
    }

    private void pauseAuto() { autoHandler.removeCallbacks(autoRunnable); }
    private void resumeAuto() { autoHandler.postDelayed(autoRunnable, AUTO_DELAY); }

    @Override protected void onResume() { super.onResume(); resumeAuto(); }
    @Override protected void onPause()  { super.onPause();  pauseAuto();  }
}
