package Modelo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManejadorArchivos {
    public static ArrayList<Usuario> leerUsuarios(Context context) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("usuarios.txt")));) {
            String linea;
            while((linea = reader.readLine())!=null){
                linea = reader.readLine();
                String datos[] = linea.split(";");
                String idUsuario = datos[0];
                String nombreUsuario = datos[1];
                String contrasena = datos[2];
                String nombreCompleto = datos[3];
                Modelo.TipoUsuario tipoUsuario = Modelo.TipoUsuario.valueOf(datos[4]);
                Usuario usuario;
                if(tipoUsuario == TipoUsuario.PARTICIPANTE){
                    usuario = new Participante(idUsuario,nombreUsuario,contrasena,nombreCompleto,tipoUsuario);
                }else{
                    usuario = new Administrador(idUsuario,nombreUsuario,contrasena,nombreCompleto,tipoUsuario);
                }
                    usuarios.add(usuario);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return usuarios;
    }
}
