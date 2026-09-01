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
import Modelo.Administrador;
import Modelo.ManejadorArchivos;
import Modelo.Partido;
import Modelo.ProyectoRepo;

public class AdministrarPartidosActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView rolUsuario;
    private Spinner spFase;
    private ArrayList<Partido> partidos;
    private LinearLayout contenedorPartidos;
    private ProyectoRepo pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_partidos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombreUsuario = findViewById(R.id.lblNombreHeader);
        rolUsuario = findViewById(R.id.lblRolHeader);
        spFase = findViewById(R.id.spFaseAdministrar);
        contenedorPartidos = findViewById(R.id.contenedorAdministrarPartidos);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("MI_REPOSITORIO")) {
            pro = (ProyectoRepo) intent.getSerializableExtra("MI_REPOSITORIO");

            if (pro != null && pro.getUsuarioLogueado() != null) {
                Administrador usuarioActual = (Administrador) pro.getUsuarioLogueado();

                partidos = pro.getPartidos();

                nombreUsuario.setText(usuarioActual.getNombreCompleto());
                rolUsuario.setText(usuarioActual.getTipoUsuario().toString());
            }
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fases_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFase.setAdapter(adapter);

        mostrarPartidos("FASE_DE_GRUPOS");

        spFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                mostrarPartidos(claveFase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });


    }

    public void mostrarPartidos(String fase) {
        if (partidos == null) {
            return;
        }
        contenedorPartidos.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Partido p : partidos) {
            if (p != null && p.getFase() != null && p.getFase().equals(fase)) {
                View vistaPartidos = inflater.inflate(R.layout.item_administrar_partido, contenedorPartidos, false);

                llenarDatosPartido(vistaPartidos, p);
                configurarEstadoPartido(vistaPartidos, p);

                contenedorPartidos.addView(vistaPartidos);
            }
        }


    }

    public void configurarEstadoPartido(View vista, Partido p) {
        EditText txtGolesSel1 = vista.findViewById(R.id.txtGolesRealSel1);
        EditText txtGolesSel2 = vista.findViewById(R.id.txtGolesRealSel2);
        TextView lblMensaje = vista.findViewById(R.id.lblMensajeAdministrarPartido);


        Button btnCerrar = vista.findViewById(R.id.btnCerrarPartido);
        Button btnGuardar = vista.findViewById(R.id.btnGuardarResultado);
        Button btnRegistrar = vista.findViewById(R.id.btnRegistrarResultado);



        switch (p.getEstado()) {
            case ABIERTO:
                txtGolesSel1.setEnabled(false);
                txtGolesSel2.setEnabled(false);
                lblMensaje.setVisibility(View.GONE);

                btnCerrar.setVisibility(View.VISIBLE);
                btnRegistrar.setVisibility(View.GONE);
                btnGuardar.setVisibility(View.GONE);
                break;

            case CERRADO:
                txtGolesSel1.setEnabled(false);
                txtGolesSel2.setEnabled(false);
                lblMensaje.setVisibility(View.VISIBLE);

                btnCerrar.setVisibility(View.GONE);
                btnRegistrar.setVisibility(View.VISIBLE);
                btnGuardar.setVisibility(View.GONE);


                btnRegistrar.setOnClickListener(v -> {

                    txtGolesSel1.setEnabled(true);
                    txtGolesSel2.setEnabled(true);

                    btnRegistrar.setVisibility(View.GONE);
                    btnGuardar.setVisibility(View.VISIBLE);

                });
                break;

            case FINALIZADO:
                txtGolesSel1.setText(String.valueOf(p.getGolesSeleccion1()));
                txtGolesSel2.setText(String.valueOf(p.getGolesSeleccion2()));
                txtGolesSel1.setEnabled(false);
                txtGolesSel2.setEnabled(false);
                lblMensaje.setVisibility(View.GONE);

                btnCerrar.setVisibility(View.GONE);
                btnRegistrar.setVisibility(View.GONE);
                btnGuardar.setVisibility(View.GONE);

                break;
        }

        btnGuardar.setOnClickListener(v -> {
            try {
                String goles1Str = txtGolesSel1.getText().toString().trim();
                String goles2Str = txtGolesSel2.getText().toString().trim();

                if (goles1Str.isEmpty() || goles2Str.isEmpty()) {
                    throw new DatosIncompletosException("Debe ingresar ambos marcadores.");
                }

                int goles1 = Integer.parseInt(goles1Str);
                int goles2 = Integer.parseInt(goles2Str);
                if (goles1 < 0 || goles2 < 0) {
                    throw new DatosIncompletosException("Los goles no pueden ser negativos.");
                }
                p.registrarResultadoOficial(goles1, goles2);

                ManejadorArchivos.guardarResultadoOficial(AdministrarPartidosActivity.this, Integer.parseInt(p.getId()), goles1, goles2);
                ManejadorArchivos.actualizarEstadoPartido(AdministrarPartidosActivity.this, p);
                Toast.makeText(AdministrarPartidosActivity.this, "Resultado guardado exitosamente", Toast.LENGTH_SHORT).show();
                mostrarPartidos(p.getFase());

            } catch (DatosIncompletosException e) {
                Toast.makeText(AdministrarPartidosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(AdministrarPartidosActivity.this, "Ingrese únicamente valores numéricos enteros", Toast.LENGTH_SHORT).show();
            }
        });

        btnCerrar.setOnClickListener(v -> {
            p.cerrarPronosticos();
            ManejadorArchivos.actualizarEstadoPartido(AdministrarPartidosActivity.this, p);

            Toast.makeText(AdministrarPartidosActivity.this, "Pronósticos cerrados exitosamente", Toast.LENGTH_SHORT).show();

            mostrarPartidos(p.getFase());
        });


    }

    private void llenarDatosPartido(View vista, Partido p) {
        TextView lblFechaHoraPartido = vista.findViewById(R.id.lblFechaHoraAdministrarPartido);

        TextView lblEstadoPartido = vista.findViewById(R.id.lblEstadoAdministrarPartido);

        TextView lblEstadioPartido = vista.findViewById(R.id.lblEstadioAdministrarPartido);

        TextView lblSeleccion1 = vista.findViewById(R.id.lblSeleccion1Administrar);

        TextView lblSeleccion2 = vista.findViewById(R.id.lblSeleccion2Administrar);

        lblFechaHoraPartido.setText(p.getFecha() + " - " + p.getHora());

        lblEstadoPartido.setText(p.getEstado().toString());

        lblEstadioPartido.setText(p.getEstadio());

        lblSeleccion1.setText(p.getSeleccion1());

        lblSeleccion2.setText(p.getSeleccion2());
    }

    public void volver(View view) {
        finish();
    }

}