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

public class ParticipantHomeActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView rolUsuario;

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

                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
    }

    public void tablaPosiciones(View view){
        Intent in = getIntent();
        Intent intent = new Intent(ParticipantHomeActivity.this, TablaPosicionesActivity.class);
        if (in != null && in.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) in.getSerializableExtra("MI_REPOSITORIO");
            intent.putExtra("MI_REPOSITORIO",pro);
        }
        startActivity(intent);
    }
}