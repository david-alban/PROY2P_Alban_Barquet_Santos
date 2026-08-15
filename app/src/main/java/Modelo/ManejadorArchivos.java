package Modelo;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class ManejadorArchivos {

    public ArrayList<String> leerLineas(Context context, String nombreArchivo) {
        ArrayList<String> lineas = new ArrayList<>();
        File archivoInterno = new File(context.getFilesDir(), nombreArchivo);

        if (!archivoInterno.exists()) {
            try (
                    InputStream entrada = context.getAssets().open(nombreArchivo);
                    OutputStream salida = context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)
            ) {
                byte[] buffer = new byte[1024];
                int cantidadBytes;
                while ((cantidadBytes = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, cantidadBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return lineas;
            }
        }

        try (
                FileInputStream fis = new FileInputStream(archivoInterno);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
        ) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = reader.readLine();
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;
    }
    public ArrayList<Usuario> leerUsuarios(Context context) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        ArrayList<String> lineas = leerLineas(context,"usuarios.txt");

        for(String linea: lineas){
            String[] datos = linea.split(";");
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
        return usuarios;
    }



}
