package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrincipalActivity extends AppCompatActivity {

    private TextView tvBienveUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.principal_main);

        // Aplica el padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Encontrar el TextView por su ID
        tvBienveUsuario = findViewById(R.id.BienveUsuario);

        // 2. Obtener la Intent que inició esta actividad
        Intent intent = new Intent(PrincipalActivity.this, CatalogoActivity.class);
        startActivity(intent);

        // 3. Obtener los datos ("extras") de la Intent
        if (intent != null && intent.hasExtra("usuario")) {
            String nombreUsuario = intent.getStringExtra("usuario");
            // 4. Actualizar el texto del TextView con el nombre del usuario
            tvBienveUsuario.setText("Bienvenido " + nombreUsuario);
        }
    }
}
