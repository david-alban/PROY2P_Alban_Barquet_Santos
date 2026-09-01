package com.espol.proyecto2;

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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Modelo.EstadoPartido;
import Modelo.Partido;
import Modelo.Pronostico;
import Modelo.ProyectoRepo;
import Modelo.Usuario;

public class MisPronosticosActivity extends AppCompatActivity {
    private Usuario usuarioLogueado;
    private TextView nombreUsuario;
    private TextView rolUsuario;
    private ProyectoRepo pro;
    private ArrayList<Pronostico> misPronosticos;
    private Spinner spFaseMisPronosticos;
    private ArrayList<Partido> partidos;
    LinearLayout contenedorMisPronosticos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_pronosticos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contenedorMisPronosticos = findViewById(R.id.contenedorMisPronosticos);
        nombreUsuario = findViewById(R.id.lblNombreHeader);
        rolUsuario = findViewById(R.id.lblRolHeader);
        spFaseMisPronosticos = findViewById(R.id.spFaseMisPronosticos);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");

            if (pro != null && pro.getUsuarioLogueado() != null) {
                misPronosticos = pro.getPronosticos();
                Usuario usuarioActual = pro.getUsuarioLogueado();
                usuarioLogueado = usuarioActual;
                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
        obtenerPartidos(misPronosticos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fases_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFaseMisPronosticos.setAdapter(adapter);
        mostrarPronosticos("FASE_DE_GRUPOS");

        spFaseMisPronosticos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String faseSeleccionada = parent.getItemAtPosition(position).toString();

                TextView textoSeleccionado = (TextView) view;
                textoSeleccionado.setTextColor(Color.WHITE);

                // Convertir texto a formato
                String claveFase = faseSeleccionada.toUpperCase().replace(" ", "_").replace("Í", "I").replace("É", "E");

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

    private void obtenerPartidos(ArrayList<Pronostico> pronosticos) {
        partidos = new ArrayList<>();
        for (Pronostico p : pronosticos) {
            Partido partido = pro.buscarPartidoPorId(p.getIdPartido());
            partidos.add(partido);
        }
    }

    public void mostrarPronosticos(String fase) {
        if (partidos == null) return;

        contenedorMisPronosticos.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        // Creando los views de los partidos
        for (Partido p : partidos) {
            if (p != null && p.getFase() != null && p.getFase().equals(fase)) {
                View vistaPartidos = inflater.inflate(R.layout.item_partido_pronostico, contenedorMisPronosticos, false);

                llenarDatosPartido(vistaPartidos, p);
                cargarPronosticoExistente(vistaPartidos, p);
                configurarEstadoPartido(vistaPartidos, p);

                contenedorMisPronosticos.addView(vistaPartidos);
            }
        }
    }

    private void llenarDatosPartido(View vista, Partido p) {
        TextView lblFechaHoraPartido = vista.findViewById(R.id.lblFechaHoraPartido);
        TextView lblEstadioPartido = vista.findViewById(R.id.lblEstadioPartido);
        TextView lblSeleccion1 = vista.findViewById(R.id.lblSeleccion1);
        TextView lblSeleccion2 = vista.findViewById(R.id.lblSeleccion2);
        Button btnExtra = vista.findViewById(R.id.btnGuardarPronostico);
        android.widget.ImageView imgSeleccion1 = vista.findViewById(R.id.imgSeleccion1);
        android.widget.ImageView imgSeleccion2 = vista.findViewById(R.id.imgSeleccion2);

        int idBandera1 = obtenerIdBandera(p.getSeleccion1(), vista);
        if (idBandera1 != 0) {
            imgSeleccion1.setImageResource(idBandera1);
        }
        int idBandera2 = obtenerIdBandera(p.getSeleccion2(), vista);
        if (idBandera2 != 0) {
            imgSeleccion2.setImageResource(idBandera2);
        }
        btnExtra.setVisibility(View.GONE);
        lblFechaHoraPartido.setText(p.getFecha() + " - " + p.getHora());
        lblEstadioPartido.setText(p.getEstadio());
        lblSeleccion1.setText(p.getSeleccion1());
        lblSeleccion2.setText(p.getSeleccion2());
    }

    private void cargarPronosticoExistente(View vista, Partido p) {
        EditText txtGolesSel1 = vista.findViewById(R.id.txtGolesSel1);
        EditText txtGolesSel2 = vista.findViewById(R.id.txtGolesSel2);

        for (Pronostico pron : misPronosticos) {
            if (pron.getIdPartido().equals(p.getId())) {
                txtGolesSel1.setText(String.valueOf(pron.getGolesSel1()));
                txtGolesSel2.setText(String.valueOf(pron.getGolesSel2()));
                return;
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
                    int puntosObtenidos = 0;
                    for (Pronostico pronostico : misPronosticos) {
                        if (pronostico.getIdPartido().equals(p.getId())) {
                            puntosObtenidos = pronostico.getPuntosObtenidos();
                            break;
                        }
                    }
                    lblMensaje.setText("Partido finalizado\n" + "Resultado oficial: " + p.getGolesSeleccion1() + " - " + p.getGolesSeleccion2() + "\nPuntos obtenidos: " + puntosObtenidos);
                    lblMensaje.setBackgroundColor(Color.parseColor("#D4EDDA"));
                    lblMensaje.setTextColor(Color.parseColor("#155724"));
                    break;
            }
        }
    }

    private int obtenerIdBandera(String nombrePais, View vista) {
        if (nombrePais == null || nombrePais.isEmpty()) return 0;

        String nombreLimpio = nombrePais.toLowerCase().replace(" ", "_").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ñ", "n");

        String nombreRecurso = "bandera_" + nombreLimpio;

        return vista.getContext().getResources().getIdentifier(nombreRecurso, "drawable", vista.getContext().getPackageName());
    }

    public void volver(View view) {
        Intent intent = new Intent(MisPronosticosActivity.this, ParticipantHomeActivity.class);

        if (pro != null) {
            intent.putExtra("MI_REPOSITORIO", pro);
        }

        startActivity(intent);

        // Cerramos la actividad actual
        finish();
    }
}