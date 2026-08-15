package Modelo;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class ProyectoRepo implements Serializable {
    private ArrayList<Usuario> usuarios;
    private ManejadorArchivos maArchivos = new ManejadorArchivos();

    public ProyectoRepo(Context context){
        cargarDatos(context);
    }
    public void cargarDatos(Context context){
        usuarios = maArchivos.leerUsuarios(context);
    }

    public ArrayList<Usuario> getUsuarios(){
        return usuarios;
    }
}
