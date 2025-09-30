package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

        // Ajuste de insets (barra de estado y navegación)
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
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        contenedorPrincipal.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            Animation fadeInImagen = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            imagenLibro.startAnimation(fadeInImagen);
            Animation slideUpGrid = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
            gridLayoutLogin.startAnimation(slideUpGrid);

        }, 300);
    }

    private void configurarBotonLogin() {
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animación de click en el botón
                Animation escalaClick = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale_click);
                btnIniciar.startAnimation(escalaClick);
                new Handler().postDelayed(() -> {
                    validarLogin();
                }, 200);
            }
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

            // Animación de shake para campo vacío
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            editUsuario.startAnimation(shake);
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Ingrese la contraseña");
            editPassword.requestFocus();

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            editPassword.startAnimation(shake);
            return;
        }

        if (usuario.equals("admin") && password.equals("123")) {
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

            Animation successScale = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            gridLayoutLogin.startAnimation(successScale);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                finish();
            }, 400);

        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            gridLayoutLogin.startAnimation(shake);
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
    }
}