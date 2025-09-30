package com.fabiosv.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrincipalActivity extends AppCompatActivity {

    private TextView tvBienveUsuario;
    private Button btnIrCatalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.principal_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBienveUsuario = findViewById(R.id.BienveUsuario);
        btnIrCatalogo = findViewById(R.id.button);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usuario")) {
            String nombreUsuario = intent.getStringExtra("usuario");

            tvBienveUsuario.setText("Bienvenido " + nombreUsuario);
        }

        configurarBotonCatalogo();
    }

    private void configurarBotonCatalogo() {
        btnIrCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCatalogo = new Intent(PrincipalActivity.this, CatalogoActivity.class);
                String usuario = getIntent().getStringExtra("usuario");
                if (usuario != null) {
                    intentCatalogo.putExtra("usuario", usuario);
                }

                startActivity(intentCatalogo);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}