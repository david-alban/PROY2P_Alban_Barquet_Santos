package com.espol.proyecto2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

import Modelo.Participante;
import Modelo.ProyectoRepo;
import Modelo.TipoUsuario;
import Modelo.Usuario;

public class TablaPosicionesActivity extends AppCompatActivity {
    private TextView nombreUsuario;
    private TextView rolUsuario;
    private LinearLayout contenedorPuntajes;
    private ArrayList<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tabla_posiciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nombreUsuario = findViewById(R.id.lblNombreHeader);
        rolUsuario = findViewById(R.id.lblRolHeader);
        contenedorPuntajes = findViewById(R.id.contenerdorPuntajes);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");
            usuarios = pro.getUsuarios();
            if (pro != null && pro.getUsuarioLogueado() != null) {
                Usuario usuarioActual = pro.getUsuarioLogueado();

                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }

        mostrarPuntajes();
    }

    public void volver(View view){
        finish();
    }

    public void mostrarPuntajes() {
        contenedorPuntajes.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        // Header
        View headerView = inflater.inflate(R.layout.item_posicion_ranking, contenedorPuntajes, false);
        headerView.setBackgroundResource(R.drawable.bg_header_rounded);

        TextView headerPos = headerView.findViewById(R.id.lblPos);
        TextView headerPart = headerView.findViewById(R.id.lblParticipante);
        TextView headerPuntos = headerView.findViewById(R.id.lblPuntos);

        headerPos.setText("Pos.");
        headerPos.setTextColor(Color.WHITE);

        headerPart.setText("Participante");
        headerPart.setTextColor(Color.WHITE);

        headerPuntos.setText("Puntos");
        headerPuntos.setTextColor(Color.WHITE);

        contenedorPuntajes.addView(headerView);

        ArrayList<Participante> participantes = new ArrayList<>();

        for (Usuario usuario : usuarios){
            if (usuario !=null && usuario.getTipoUsuario() == TipoUsuario.PARTICIPANTE){
                Participante pa = (Participante) usuario;
                participantes.add(pa);
            }
        }

        Collections.sort(participantes);


        int i = 0;
        for (Participante participante : participantes) {
            if (participante != null && participante.getTipoUsuario() == TipoUsuario.PARTICIPANTE) {
                View vistaPuntajes = inflater.inflate(R.layout.item_posicion_ranking, contenedorPuntajes, false);

                TextView posicion = vistaPuntajes.findViewById(R.id.lblPos);
                TextView partcipante = vistaPuntajes.findViewById(R.id.lblParticipante);
                TextView puntajes = vistaPuntajes.findViewById(R.id.lblPuntos);

                posicion.setText(String.valueOf(i + 1));
                partcipante.setText(participante.getNombreUsuario());

                puntajes.setText(String.valueOf(participante.getPuntajeAcumulado()));

                contenedorPuntajes.addView(vistaPuntajes);
                i++;
            }
        }
    }
}