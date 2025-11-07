package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsuario;
    private EditText editPassword;
    private Button btnIniciar;
    private LinearLayout contenedorPrincipal;
    private ImageView imagenLibro;
    private androidx.gridlayout.widget.GridLayout gridLayoutLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarVistas();
        configurarAnimacionesEntrada();
        configurarBotonLogin();
        configurarBotonAtras();
    }

    private void inicializarVistas() {
        editUsuario = findViewById(R.id.username_input);
        editPassword = findViewById(R.id.password_input);
        btnIniciar = findViewById(R.id.loginbtn);
        contenedorPrincipal = findViewById(R.id.splash);
        imagenLibro = findViewById(R.id.imageView);
        gridLayoutLogin = findViewById(R.id.gridLayoutLogin);
    }

    private void configurarAnimacionesEntrada() {
        // 1) Fondo se desvanece
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorPrincipal.startAnimation(fadeIn);

        // 2) El grid sube (entrada principal)
        new Handler().postDelayed(() -> {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
            gridLayoutLogin.startAnimation(slideUp);
        }, 150);

        // 3) Pop secuencial para cada hijo del grid (imagen, inputs, checkbox, botón, etc.)
        new Handler().postDelayed(() -> {
            Animation pop = AnimationUtils.loadAnimation(this, R.anim.card_pop_in);
            imagenLibro.startAnimation(pop);

            // Anima a todos los hijos del grid con un pequeño offset
            int count = gridLayoutLogin.getChildCount();
            long delayStep = 90;
            for (int i = 0; i < count; i++) {
                final int index = i;
                new Handler().postDelayed(() -> {
                    if (gridLayoutLogin.getChildAt(index) != imagenLibro) {
                        gridLayoutLogin.getChildAt(index).startAnimation(
                                AnimationUtils.loadAnimation(this, R.anim.card_pop_in)
                        );
                    }
                }, (index + 1) * delayStep);
            }
        }, 280);
    }

    private void configurarBotonLogin() {
        btnIniciar.setOnClickListener(v -> {
            Animation click = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale_click);
            btnIniciar.startAnimation(click);
            new Handler().postDelayed(this::validarLogin, 200);
        });
    }

    private void configurarBotonAtras() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Animation slideOut = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_out_right);
                contenedorPrincipal.startAnimation(slideOut);
                new Handler().postDelayed(() -> {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 300);
            }
        });
    }

    private void validarLogin() {
        String usuario = editUsuario.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (usuario.isEmpty()) {
            editUsuario.setError("Ingrese el usuario");
            editUsuario.requestFocus();
            editUsuario.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Ingrese la contraseña");
            editPassword.requestFocus();
            editPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }

        if (usuario.equals("admin") && password.equals("123")) {
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
            gridLayoutLogin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up));

            new Handler().postDelayed(() -> {
                Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 420);
        }  else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            gridLayoutLogin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        limpiarAnimaciones();
    }

    private void limpiarAnimaciones() {
        contenedorPrincipal.clearAnimation();
        imagenLibro.clearAnimation();
        gridLayoutLogin.clearAnimation();
        btnIniciar.clearAnimation();
        editUsuario.clearAnimation();
        editPassword.clearAnimation();
    }
}
