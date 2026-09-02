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

/**
 * Actividad que proporciona la interfaz para que un Administrador actualice
 * los puntajes de todos los participantes del sistema.
 */

public class ActualizarPuntajesActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userRol;
    private ProyectoRepo pro;

    /**
     * Método del ciclo de vida de la actividad llamado al crearse la vista.
     * Configura el diseño de la interfaz, los márgenes del sistema y recupera
     * el repositorio serializado desde el Intent para mostrar los datos del administrador logueado.
     *
     * @param savedInstanceState Contiene el estado previamente guardado de la actividad, si existe.
     */

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

    /**
     * Manejador de evento (onClick) para el botón de actualizar puntajes.
     * Invoca el método correspondiente en el repositorio para recalcular los puntos
     * y muestra un mensaje de confirmación al usuario.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void actualizarPuntajes(View view) {
        if (pro != null) {
            pro.actualizarPuntajes(this);

            Toast.makeText(this, "Puntajes actualizados correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manejador de evento (onClick) para el botón de regresar.
     * Finaliza la actividad actual y devuelve al usuario a la pantalla anterior.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void volver(View view) {
        finish();
    }
}