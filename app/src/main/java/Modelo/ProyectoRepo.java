package Modelo;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class ProyectoRepo implements Serializable {
    private Context context;
    private ArrayList<Usuario> usuarios;

    public ProyectoRepo(Context context){
        cargarDatos(context);
    }
    public void cargarDatos(Context context){
        usuarios = ManejadorArchivos.leerUsuarios(context);
    }

    public ArrayList<Usuario> getUsuarios(){
        return usuarios;
    }
}
