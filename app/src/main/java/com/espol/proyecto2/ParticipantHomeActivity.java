package com.espol.proyecto2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Modelo.Participante;
import Modelo.ProyectoRepo;
import Modelo.TipoUsuario;
import Modelo.Usuario;

/**
 * Actividad que representa el menú principal (Home) para los usuarios con rol de Participante.
 * Sirve como el centro de navegación hacia las funcionalidades principales como visualizar
 * la tabla de posiciones, registrar nuevos pronósticos o revisar el historial personal.
 */

public class ParticipantHomeActivity extends AppCompatActivity {

    /**
     * Componente de texto para mostrar el nombre completo del participante en el encabezado.
     */

    private TextView nombreUsuario;
    private TextView rolUsuario;

    /**
     * Método del ciclo de vida de la actividad llamado al momento de su creación.
     * Configura el diseño de la pantalla, aplica los márgenes del sistema, extrae el
     * repositorio de datos desde el Intent y carga la información inicial en la interfaz.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_participant_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombreUsuario = findViewById(R.id.lblNombreHeader);
        rolUsuario = findViewById(R.id.lblRolHeader);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");

            if (pro != null && pro.getUsuarioLogueado() != null) {
                Participante usuarioActual = (Participante) pro.getUsuarioLogueado();
                pro.cargarDatosMenu(this);
                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
    }

    /**
     * Manejador de evento (onClick) para el botón de "Salir".
     * Cierra todas las actividades asociadas a la aplicación y finaliza el proceso de la interfaz.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void salir(View view) {

        finishAffinity();
    }

    /**
     * Manejador de evento (onClick) para el botón de "Tabla de Posiciones".
     * Inicia la actividad {@link TablaPosicionesActivity} transfiriendo el repositorio de datos serializado.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void tablaPosiciones(View view){
        Intent in = getIntent();
        Intent intent = new Intent(ParticipantHomeActivity.this, TablaPosicionesActivity.class);
        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");
            intent.putExtra("MI_REPOSITORIO",pro);
        }
        startActivity(intent);
    }

    /**
     * Manejador de evento (onClick) para el botón de "Pronósticos".
     * Inicia la actividad {@link PronosticosActivity} para registrar nuevos pronósticos,
     * transfiriendo el repositorio de datos serializado.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void pronosticos(View view){
        Intent in = getIntent();
        Intent intent = new Intent(ParticipantHomeActivity.this, PronosticosActivity.class);
        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");
            intent.putExtra("MI_REPOSITORIO",pro);
        }
        startActivity(intent);
    }

    /**
     * Manejador de evento (onClick) para el botón de "Mis Pronósticos".
     * Inicia la actividad {@link MisPronosticosActivity} para consultar el historial personal,
     * transfiriendo el repositorio de datos serializado.
     *
     * @param view La vista (botón) que fue presionada.
     */

    public void misPronosticos(View view){
        Intent in = getIntent();
        Intent intent = new Intent(ParticipantHomeActivity.this, MisPronosticosActivity.class);
        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");
            intent.putExtra("MI_REPOSITORIO",pro);
        }
        startActivity(intent);
    }
}