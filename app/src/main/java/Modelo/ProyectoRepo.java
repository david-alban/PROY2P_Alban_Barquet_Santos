package Modelo;

import android.content.Context;
import android.provider.Telephony;

import java.io.Serializable;
import java.util.ArrayList;

import Excepciones.CredencialesInvalidasException;

public class ProyectoRepo implements Serializable {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Partido> partidos;
    private Usuario usuarioLogueado;
    private ManejadorArchivos maArchivos = new ManejadorArchivos();

    public ProyectoRepo(Context context){
        cargarDatosLogin(context);
    }
    public void cargarDatosLogin(Context context){
        usuarios = maArchivos.leerUsuarios(context);
    }
    public void cargarDatosMenu(Context context){
        partidos = maArchivos.leerPartidos(context);
        maArchivos.leerResultados(context);
    }
    public void autenticar(String usuarioIngresado, String contrasenaIngresada) throws CredencialesInvalidasException {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equalsIgnoreCase(usuarioIngresado) &&
                    usuario.getContrasena().equals(contrasenaIngresada)) {
                this.usuarioLogueado = usuario;
                return;
            }
        }
        //Si no hubo conincidencias se lanza la siguinete excepcion personalizada.
        throw new CredencialesInvalidasException();
    }

    public void actualizarDatosParticipantes(Context context){
        ArrayList<String> datos = new ArrayList<>();
        datos.add("idUsuario;puntajeAcumulado");
        for (Usuario usuario : usuarios) {
            if (usuario != null && usuario.getTipoUsuario() == TipoUsuario.PARTICIPANTE) {
                Participante pa = (Participante) usuario;

                String id = (pa.getIdUsuario() != null) ? pa.getIdUsuario() : "N/A";
                String str = id + ";" + pa.getPuntajeAcumulado();
                datos.add(str);
            }
        }

        maArchivos.escribirLineas(context, "participantes.txt", datos, false);
    }
    public ArrayList<Usuario> getUsuarios(){
        return usuarios;
    }
    public ArrayList<Partido> getPartidos(){ return partidos; }
    public Usuario getUsuarioLogueado(){ return usuarioLogueado; }
    public void setUsuarios(ArrayList<Usuario> usuarios) { this.usuarios = usuarios; }
    public void setPartidos(ArrayList<Partido> partidos) { this.partidos = partidos; }
    public void setUsuarioLogueado(Usuario usuarioLogueado) { this.usuarioLogueado = usuarioLogueado; }

}
