package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsuario;
    private EditText editPassword;
    private Button btnIniciar;

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

        editUsuario = findViewById(R.id.username_input);
        editPassword = findViewById(R.id.password_input);
        btnIniciar = findViewById(R.id.loginbtn);

        // Animación: Login sube desde abajo al entrar
        View rootLayout = findViewById(R.id.splash); // LinearLayout padre
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
        rootLayout.startAnimation(slideUp);

        btnIniciar.setOnClickListener(v -> validarLogin());
    }

    private void validarLogin() {
        String usuario = editUsuario.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (usuario.isEmpty()) {
            editUsuario.setError("Ingrese el usuario");
            editUsuario.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editPassword.setError("Ingrese la contraseña");
            editPassword.requestFocus();
            return;
        }

        if (usuario.equals("fabio") && password.equals("123")) {
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
