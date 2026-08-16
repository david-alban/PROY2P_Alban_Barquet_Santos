package com.espol.proyecto2;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Excepciones.CredencialesInvalidasException;
import Modelo.ProyectoRepo;
import Modelo.TipoUsuario;
import Modelo.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtPasword;
    private Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            txtUsuario = findViewById(R.id.txtUsuario);
            txtPasword = findViewById(R.id.txtPassword);
            btnIngresar = findViewById(R.id.btnIngresar);
            ProyectoRepo pro = new ProyectoRepo(this);
            btnIngresar.setOnClickListener(view -> {
                String usuarioIngresado = txtUsuario.getText().toString().trim();
                String contrasenaIngrasada = txtPasword.getText().toString().trim();
                try{
                    pro.autenticar(usuarioIngresado,contrasenaIngrasada);
                    Toast.makeText(this, "Bienvenido " + pro.getUsuarioLogueado().getNombreCompleto(), Toast.LENGTH_SHORT).show();
                    if(pro.getUsuarioLogueado().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)){

                    } else{
                        Intent intent = new Intent(LoginActivity.this, ParticipantHomeActivity.class);
                        intent.putExtra("MI_REPOSITORIO", pro);
                        startActivity(intent);
                    }
                    finish();
                } catch(CredencialesInvalidasException e){
                    Toast.makeText(this,e.getMessage(),LENGTH_SHORT).show();
                }
            });
            return insets;
        });
    }
}