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
 * Actividad que representa el menú principal (Home) para los usuarios con rol de Administrador.
 * Proporciona la interfaz y la navegación hacia las opciones de gestión del sistema, como
 * administrar partidos y actualizar los puntajes de los participantes.
 */

public class AdminHomeActivity extends AppCompatActivity {
    private TextView userName;
    private TextView userRol;

    /**
     * Método del ciclo de vida de la actividad llamado al momento de su creación.
     * Configura el diseño de la pantalla, aplica los márgenes del sistema, extrae el
     * repositorio de datos desde el Intent y carga la información inicial en el menú.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.lblNombreHeader);
        userRol = findViewById(R.id.lblRolHeader);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");

            if (pro != null && pro.getUsuarioLogueado() != null) {
                Administrador usuarioActual = (Administrador) pro.getUsuarioLogueado();
                pro.cargarDatosMenu(this);

                userName.setText(usuarioActual.getNombreCompleto());
                userRol.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
    }

    /**
     * Manejador de evento (onClick) para el botón de "Administrar Partidos".
     * Inicia la actividad {@link AdministrarPartidosActivity} transfiriendo el repositorio de datos serializado.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void administrarPartidos(View view) {
        Intent in = getIntent();
        Intent intent = new Intent(AdminHomeActivity.this, AdministrarPartidosActivity.class);

        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");

            intent.putExtra("MI_REPOSITORIO", pro);
        }

        startActivity(intent);

    }

    /**
     * Manejador de evento (onClick) para el botón de "Actualizar Puntajes".
     * Inicia la actividad {@link ActualizarPuntajesActivity} transfiriendo el repositorio de datos serializado.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void actualizarPuntajes(View view) {
        Intent in = getIntent();
        Intent intent = new Intent(AdminHomeActivity.this, ActualizarPuntajesActivity.class);

        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");

            intent.putExtra("MI_REPOSITORIO", pro);
        }

        startActivity(intent);

    }

    /**
     * Manejador de evento (onClick) para el botón de "Salir".
     * Cierra todas las actividades asociadas a la aplicación y la finaliza por completo.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void salir(View view) {
        finishAffinity();

    }
}


