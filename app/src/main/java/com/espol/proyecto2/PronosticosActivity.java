package com.espol.proyecto2;

import static Modelo.EstadoPartido.ABIERTO;
import static Modelo.EstadoPartido.CERRADO;
import static Modelo.EstadoPartido.FINALIZADO;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Modelo.EstadoPartido;
import Modelo.Partido;
import Modelo.ProyectoRepo;
import Modelo.Usuario;

public class PronosticosActivity extends AppCompatActivity {
    private TextView nombreUsuario;
    private TextView rolUsuario;
    private Spinner spFase;
    private ArrayList<Partido> partidos;
    LinearLayout contenedorPartidos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pronosticos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        contenedorPartidos = findViewById(R.id.contenedorPartidos);
        nombreUsuario = findViewById(R.id.lblNombreHeader);
        rolUsuario = findViewById(R.id.lblRolHeader);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            ProyectoRepo pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");
            partidos = pro.getPartidos();
            if (pro != null && pro.getUsuarioLogueado() != null) {
                Usuario usuarioActual = pro.getUsuarioLogueado();

                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
        spFase = findViewById(R.id.spFase);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.fases_spinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFase.setAdapter(adapter);

        // Cargar por defecto la primera fase al abrir la pantalla
        mostrarPronosticos("FASE_DE_GRUPOS");

        spFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String faseSeleccionada = parent.getItemAtPosition(position).toString();

                // Convertir texto a formato
                String claveFase = faseSeleccionada.toUpperCase()
                        .replace(" ", "_")
                        .replace("Í", "I")
                        .replace("É", "E");

                // Ajuste para la fase de 16avos
                if (claveFase.equals("DIECISEISAVOS")) {
                    claveFase = "DIECISEISAVOS_DE_FINAL";
                }
                // Ajuste para la fase 8vos
                if (claveFase.equals("OCTAVOS")) {
                    claveFase = "OCTAVOS_DE_FINAL";
                }

                mostrarPronosticos(claveFase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

    }

    public void mostrarPronosticos(String fase){
        if (partidos == null) return;

        contenedorPartidos.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        // Creando los views de los partidos
        for(Partido p: partidos){
            if(p != null && p.getFase() != null && p.getFase().equals(fase)){
                View vistaPartidos = inflater.inflate(R.layout.item_partido_pronostico,contenedorPartidos,false);

                TextView lblFechaHoraPartido = vistaPartidos.findViewById(R.id.lblFechaHoraPartido);
                TextView lblEstadioPartido = vistaPartidos.findViewById(R.id.lblEstadioPartido);
                TextView lblEstadoPartido = vistaPartidos.findViewById(R.id.lblEstadoPartido);
                TextView lblSeleccion1 = vistaPartidos.findViewById(R.id.lblSeleccion1);
                TextView lblSeleccion2 = vistaPartidos.findViewById(R.id.lblSeleccion2);

                lblFechaHoraPartido.setText(p.getFecha() + " - " + p.getHora());
                lblEstadioPartido.setText(p.getEstadio());
                lblSeleccion1.setText(p.getSeleccion1());
                lblSeleccion2.setText(p.getSeleccion2());

                configurarEstadoPartido(vistaPartidos, p);
                contenedorPartidos.addView(vistaPartidos);
            }
        }

    }

    private void configurarEstadoPartido(View vista, Partido p) {
        TextView lblEstado = vista.findViewById(R.id.lblEstadoPartido);
        EditText txtGoles1 = vista.findViewById(R.id.txtGolesSel1);
        EditText txtGoles2 = vista.findViewById(R.id.txtGolesSel2);
        Button btnGuardar = vista.findViewById(R.id.btnGuardarPronostico);
        TextView lblMensaje = vista.findViewById(R.id.lblMensajeEstado);

        EstadoPartido estado = p.getEstado();

        if (estado != null) {
            lblEstado.setText(estado.toString());

            switch (estado) {
                case ABIERTO:
                    lblEstado.setTextColor(Color.parseColor("#2E7D32"));
                    txtGoles1.setEnabled(true);
                    txtGoles2.setEnabled(true);
                    btnGuardar.setVisibility(View.VISIBLE);
                    lblMensaje.setVisibility(View.GONE);
                    break;

                case CERRADO:
                    lblEstado.setTextColor(Color.parseColor("#C62828"));
                    txtGoles1.setEnabled(false);
                    txtGoles2.setEnabled(false);
                    btnGuardar.setVisibility(View.GONE);
                    lblMensaje.setVisibility(View.VISIBLE);
                    lblMensaje.setText("Los pronósticos para este partido están cerrados.");
                    lblMensaje.setBackgroundColor(Color.parseColor("#FFF3CD"));
                    lblMensaje.setTextColor(Color.parseColor("#856404"));
                    break;

                case FINALIZADO:
                    lblEstado.setTextColor(Color.parseColor("#1565C0"));
                    txtGoles1.setEnabled(false);
                    txtGoles2.setEnabled(false);
                    btnGuardar.setVisibility(View.GONE);
                    lblMensaje.setVisibility(View.VISIBLE);
                    lblMensaje.setText("¡Partido finalizado! Ya conoces tus puntos.");
                    lblMensaje.setBackgroundColor(Color.parseColor("#D4EDDA"));
                    lblMensaje.setTextColor(Color.parseColor("#155724"));
                    break;
            }
        }
    }

}