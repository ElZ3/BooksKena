package com.fabiosv.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactoActivity extends AppCompatActivity {

    private LinearLayout cardFormulario;
    private ScrollView contactoRoot;
    private ImageView imgLogo;
    private EditText edtNombre, edtCorreo, edtAsunto, edtMensaje;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.contacto_main);

        // Insets para barra de estado/navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contacto_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        animacionesEntrada();
        configurarBotonEnviar();
        configurarBotonAtras();
    }

    private void initViews() {
        contactoRoot   = findViewById(R.id.contacto_root);
        cardFormulario = findViewById(R.id.cardFormulario);
        imgLogo        = findViewById(R.id.imgLogoContacto);
        edtNombre      = findViewById(R.id.edtNombre);
        edtCorreo      = findViewById(R.id.edtCorreo);
        edtAsunto      = findViewById(R.id.edtAsunto);
        edtMensaje     = findViewById(R.id.edtMensaje);
        btnEnviar      = findViewById(R.id.btnEnviar);
    }

    private void animacionesEntrada() {
        contactoRoot.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        new Handler().postDelayed(() -> {
            imgLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in));
            cardFormulario.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_up));
        }, 200);
    }

    private void configurarBotonEnviar() {
        btnEnviar.setOnClickListener(v -> {
            btnEnviar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_click));

            String nombre  = edtNombre.getText().toString().trim();
            String correo  = edtCorreo.getText().toString().trim();
            String asunto  = edtAsunto.getText().toString().trim();
            String mensaje = edtMensaje.getText().toString().trim();

            // Validaciones básicas
            if (nombre.isEmpty()) {
                edtNombre.setError("Ingrese su nombre");
                edtNombre.requestFocus();
                edtNombre.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                return;
            }

            if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                edtCorreo.setError("Ingrese un correo válido");
                edtCorreo.requestFocus();
                edtCorreo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                return;
            }

            if (asunto.isEmpty()) {
                edtAsunto.setError("Ingrese un asunto");
                edtAsunto.requestFocus();
                edtAsunto.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                return;
            }

            if (mensaje.isEmpty()) {
                edtMensaje.setError("Escriba un mensaje");
                edtMensaje.requestFocus();
                edtMensaje.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                return;
            }

            // Construir el correo
            enviarCorreo(nombre, correo, asunto, mensaje);
        });
    }

    private void enviarCorreo(String nombre, String correo, String asunto, String mensaje) {
        String destinatario = "soporte@bookskena.com"; // cámbialo por tu correo real

        String cuerpo = "Nombre: " + nombre +
                "\nCorreo: " + correo +
                "\n\nMensaje:\n" + mensaje;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + destinatario));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde Books Kena - " + asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        try {
            startActivity(Intent.createChooser(intent, "Enviar correo con..."));
            Toast.makeText(this, "Abriendo tu app de correo…", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Si no hay app de correo, al menos notificamos
            Toast.makeText(this, "Mensaje registrado. No se encontró app de correo.", Toast.LENGTH_LONG).show();
        }
    }

    private void configurarBotonAtras() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                contactoRoot.startAnimation(AnimationUtils.loadAnimation(ContactoActivity.this, R.anim.slide_out_right));
                new Handler().postDelayed(() -> {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 250);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // limpieza simple de animaciones
        contactoRoot.clearAnimation();
        cardFormulario.clearAnimation();
        imgLogo.clearAnimation();
        btnEnviar.clearAnimation();
    }
}
