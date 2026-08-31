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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Excepciones.DatosIncompletosException;
import Modelo.EstadoPartido;
import Modelo.Partido;
import Modelo.Pronostico;
import Modelo.ProyectoRepo;
import Modelo.Usuario;

public class PronosticosActivity extends AppCompatActivity {
    private TextView nombreUsuario;
    private Usuario usuarioLogueado;
    private TextView rolUsuario;
    private Spinner spFase;
    private ArrayList<Partido> partidos;
    LinearLayout contenedorPartidos;
    private ProyectoRepo pro;

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
            pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");
            partidos = pro.getPartidos();
            if (pro != null && pro.getUsuarioLogueado() != null) {
                Usuario usuarioActual = pro.getUsuarioLogueado();
                usuarioLogueado = usuarioActual;
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

                TextView textoSeleccionado = (TextView) view;
                textoSeleccionado.setTextColor(Color.WHITE);

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
                View vistaPartidos = inflater.inflate(R.layout.item_partido_pronostico, contenedorPartidos, false);

                llenarDatosPartido(vistaPartidos, p);
                cargarPronosticoExistente(vistaPartidos, p);
                configurarBotonGuardar(vistaPartidos, p);
                configurarEstadoPartido(vistaPartidos, p);

                contenedorPartidos.addView(vistaPartidos);
            }
        }
    }

    private void llenarDatosPartido(View vista, Partido p) {
        TextView lblFechaHoraPartido = vista.findViewById(R.id.lblFechaHoraPartido);
        TextView lblEstadioPartido = vista.findViewById(R.id.lblEstadioPartido);
        TextView lblSeleccion1 = vista.findViewById(R.id.lblSeleccion1);
        TextView lblSeleccion2 = vista.findViewById(R.id.lblSeleccion2);

        lblFechaHoraPartido.setText(p.getFecha() + " - " + p.getHora());
        lblEstadioPartido.setText(p.getEstadio());
        lblSeleccion1.setText(p.getSeleccion1());
        lblSeleccion2.setText(p.getSeleccion2());
    }

    private void cargarPronosticoExistente(View vista, Partido p) {
        EditText txtGolesSel1 = vista.findViewById(R.id.txtGolesSel1);
        EditText txtGolesSel2 = vista.findViewById(R.id.txtGolesSel2);
        Button btnGuardar = vista.findViewById(R.id.btnGuardarPronostico);

        if (pro != null && pro.getPronosticos() != null) {
            for (Pronostico pron : pro.getPronosticos()) {
                if (pron.getIdPartido().equals(p.getId())) {
                    txtGolesSel1.setText(String.valueOf(pron.getGolesSel1()));
                    txtGolesSel2.setText(String.valueOf(pron.getGolesSel2()));
                    btnGuardar.setText("Actualizar pronóstico");
                    return;
                }
            }
        }
    }

    private void configurarBotonGuardar(View vista, Partido p) {
        EditText txtGolesSel1 = vista.findViewById(R.id.txtGolesSel1);
        EditText txtGolesSel2 = vista.findViewById(R.id.txtGolesSel2);
        Button btnGuardar = vista.findViewById(R.id.btnGuardarPronostico);

        btnGuardar.setOnClickListener(v -> {
            try {
                String goles1Str = txtGolesSel1.getText().toString().trim();
                String goles2Str = txtGolesSel2.getText().toString().trim();

                // Lanzar la excepción si algún campo está vacío
                if (goles1Str.isEmpty() || goles2Str.isEmpty()) {
                    throw new DatosIncompletosException("Debe ingresar ambos marcadores para guardar el pronóstico.");
                }

                int goles1 = Integer.parseInt(goles1Str);
                int goles2 = Integer.parseInt(goles2Str);

                String idPronostico = usuarioLogueado.getIdUsuario() + "_" + p.getId();

                Pronostico nuevoPronostico = new Pronostico(
                        idPronostico,
                        usuarioLogueado.getIdUsuario(),
                        p.getId(),
                        goles1,
                        goles2
                );


                String faseSeleccionada = p.getFase();
                boolean exito = pro.guardarPronostico(PronosticosActivity.this, nuevoPronostico, faseSeleccionada);

                if (exito) {
                    Toast.makeText(PronosticosActivity.this, "Pronóstico guardado exitosamente", Toast.LENGTH_SHORT).show();
                }

            } catch (DatosIncompletosException e) {
                // Captura de la excepción propia
                Toast.makeText(PronosticosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(PronosticosActivity.this, "Ingrese únicamente valores numéricos enteros", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void volver(View view) {
        Intent intent = new Intent(PronosticosActivity.this, ParticipantHomeActivity.class);

        if (pro != null) {
            intent.putExtra("MI_REPOSITORIO", pro);
        }

        startActivity(intent);

        // Cerramos la actividad actual
        finish();
    }
}