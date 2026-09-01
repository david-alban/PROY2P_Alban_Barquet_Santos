package com.espol.proyecto2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Modelo.Administrador;
import Modelo.ProyectoRepo;

public class ActualizarPuntajesActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userRol;
    private ProyectoRepo pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_puntajes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.lblNombreHeader);
        userRol = findViewById(R.id.lblRolHeader);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");

            if (pro != null && pro.getUsuarioLogueado() != null) {
                Administrador usuarioActual = (Administrador) pro.getUsuarioLogueado();

                userName.setText(usuarioActual.getNombreCompleto());
                userRol.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
    }


    public void actualizarPuntajes(View view) {
        if (pro != null) {
            pro.actualizarPuntajes(this);

            Toast.makeText(this, "Puntajes actualizados correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void volver(View view) {
        finish();
    }
}