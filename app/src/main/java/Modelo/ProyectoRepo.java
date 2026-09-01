package Modelo;

import android.content.Context;
import android.provider.Telephony;

import java.io.Serializable;
import java.util.ArrayList;

import Excepciones.CredencialesInvalidasException;

public class ProyectoRepo implements Serializable {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Partido> partidos;
    private ArrayList<Pronostico> pronosticos;
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
        pronosticos = maArchivos.leerTodosLosPronosticos(context,usuarioLogueado.getIdUsuario());
        maArchivos.leerResultados(context, partidos);
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

    // Escritura

    public Partido buscarPartidoPorId(String idPartido) {
        for (Partido p : partidos) {
            if (p.getId().equals(idPartido)) {
                return p;
            }
        }
        return null;
    }

    public void guardarEnMemoria(Pronostico nuevoPronostico) {
        boolean encontrado = false;

        for (int i = 0; i < pronosticos.size(); i++) {
            if (pronosticos.get(i).getIdPartido().equals(nuevoPronostico.getIdPartido())) {
                pronosticos.set(i, nuevoPronostico);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            pronosticos.add(nuevoPronostico);
        }
    }

    public boolean guardarPronostico(Context context, Pronostico nuevoPronostico, String claveFase) {
        if (usuarioLogueado == null) return false;

        guardarEnMemoria(nuevoPronostico);

        ArrayList<Pronostico> pronosticosFase = new ArrayList<>();
        for (Pronostico p : pronosticos) {
            Partido partidoAsociado = buscarPartidoPorId(p.getIdPartido());
            if (partidoAsociado != null && partidoAsociado.getFase() != null) {
                if (partidoAsociado.getFase().equalsIgnoreCase(claveFase)) {
                    pronosticosFase.add(p);
                }
            }
        }

        String faseFormateada = claveFase.trim().toLowerCase().replace(" ", "_");
        String idUsuario = usuarioLogueado.getIdUsuario();
        String nombreArchivo = "pronostico_" + idUsuario + "_" + faseFormateada + ".dat";

        return maArchivos.serializar(context, pronosticosFase, nombreArchivo);
    }

    // getters y setters
    public ArrayList<Usuario> getUsuarios(){
        return usuarios;
    }
    public ArrayList<Partido> getPartidos(){ return partidos; }
    public Usuario getUsuarioLogueado(){ return usuarioLogueado; }
    public void setUsuarios(ArrayList<Usuario> usuarios) { this.usuarios = usuarios; }
    public void setPartidos(ArrayList<Partido> partidos) { this.partidos = partidos; }
    public void setUsuarioLogueado(Usuario usuarioLogueado) { this.usuarioLogueado = usuarioLogueado; }
    public ArrayList<Pronostico> getPronosticos() { return pronosticos;}
    public void setPronosticos(ArrayList<Pronostico> pronosticos) { this.pronosticos = pronosticos; }
}
