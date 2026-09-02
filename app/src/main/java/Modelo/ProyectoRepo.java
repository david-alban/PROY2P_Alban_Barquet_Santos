package Modelo;

/**
 * Repositorio central que gestiona el estado global y los datos en memoria de la aplicación.
 * Actúa como intermediario entre la interfaz de usuario y el sistema de almacenamiento de archivos.
 * Implementa {@link Serializable} para facilitar la transferencia de datos entre componentes del sistema.
 */

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

    /**
     * Crea un nuevo repositorio e inicializa la carga de datos necesarios para el inicio de sesión.
     *
     * @param context Contexto de la aplicación, necesario para acceder a los archivos internos.
     */

    public ProyectoRepo(Context context) {
        cargarDatosLogin(context);
    }

    /**
     * Carga exclusivamente la lista de usuarios en memoria para validar credenciales.
     *
     * @param context Contexto de la aplicación.
     */

    public void cargarDatosLogin(Context context) {

        usuarios = maArchivos.leerUsuarios(context);
    }

    /**
     * Carga en memoria los datos requeridos por los menús principales del sistema.
     * Inicializa los partidos, resultados oficiales y los pronósticos específicos del usuario actual.
     *
     * @param context Contexto de la aplicación.
     */

    public void cargarDatosMenu(Context context) {
        partidos = maArchivos.leerPartidos(context);
        pronosticos = maArchivos.leerTodosLosPronosticos(context, usuarioLogueado.getIdUsuario());
        maArchivos.leerResultados(context, partidos);
    }

    /**
     * Verifica las credenciales de un usuario contra los registros cargados en memoria.
     * Si las credenciales son válidas, establece el usuario como logueado en el sistema.
     *
     * @param usuarioIngresado    Nombre de usuario digitado.
     * @param contrasenaIngresada Contraseña digitada.
     * @throws CredencialesInvalidasException Si no se encuentra un usuario con dichas credenciales.
     */

    public void autenticar(String usuarioIngresado, String contrasenaIngresada) throws CredencialesInvalidasException {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equalsIgnoreCase(usuarioIngresado) && usuario.getContrasena().equals(contrasenaIngresada)) {
                this.usuarioLogueado = usuario;
                return;
            }
        }
        throw new CredencialesInvalidasException();
    }

    /**
     * Busca un partido específico dentro de la lista de partidos cargados en memoria.
     *
     * @param idPartido Identificador del partido a buscar.
     * @return El objeto {@link Partido} correspondiente, o null si no se encuentra.
     */

    public Partido buscarPartidoPorId(String idPartido) {
        for (Partido p : partidos) {
            if (p.getId().equals(idPartido)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Actualiza un pronóstico existente o agrega uno nuevo a la lista en memoria del usuario actual.
     *
     * @param nuevoPronostico Objeto con los datos del pronóstico ingresado.
     */

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

    /**
     * Guarda un pronóstico recién creado tanto en memoria como persistido en su respectivo archivo serializado,
     * agrupándolo por su fase correspondiente.
     *
     * @param context         Contexto de la aplicación.
     * @param nuevoPronostico Pronóstico que se desea guardar.
     * @param claveFase       Nombre de la fase del torneo (ej. "Fase de grupos") usada para nombrar el archivo.
     * @return true si la persistencia fue exitosa; false si hubo un error o no hay usuario logueado.
     */

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

    /**
     * Distribuye y serializa un listado completo de pronósticos en distintos archivos
     * agrupándolos según la fase del torneo a la que pertenecen.
     *
     * @param context                 Contexto de la aplicación.
     * @param idUsuario               Identificador del usuario propietario de los pronósticos.
     * @param pronosticosParticipante Lista de pronósticos a persistir.
     */

    public void guardarPronosticosActualizados(Context context, String idUsuario, ArrayList<Pronostico> pronosticosParticipante) {

        String[] fases = {"fase_de_grupos", "dieciseisavos_de_final", "octavos_de_final", "cuartos_de_final", "semifinales", "tercer_lugar", "final"};

        for (String fase : fases) {
            ArrayList<Pronostico> pronosticosFase = new ArrayList<>();

            for (Pronostico pronostico : pronosticosParticipante) {
                Partido partido = buscarPartidoPorId(pronostico.getIdPartido());

                if (partido != null && partido.getFase().equalsIgnoreCase(fase)) {

                    pronosticosFase.add(pronostico);
                }
            }

            if (!pronosticosFase.isEmpty()) {
                String nombreArchivo = "pronostico_" + idUsuario + "_" + fase + ".dat";

                maArchivos.serializar(context, pronosticosFase, nombreArchivo);
            }
        }


    }

    /**
     * Proceso central que recalcula y actualiza el puntaje acumulado de todos los participantes.
     * Compara los pronósticos guardados contra los resultados de los partidos ya finalizados
     * y guarda tanto los pronósticos valorados como la tabla de posiciones actualizada.
     *
     * @param context Contexto de la aplicación.
     */

    public void actualizarPuntajes(Context context) {

        partidos = maArchivos.leerPartidos(context);
        maArchivos.leerResultados(context, partidos);

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Participante) {
                Participante participante = (Participante) usuario;
                participante.setPuntajeAcumulado(0);
            }
        }

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Participante) {
                Participante participante = (Participante) usuario;

                ArrayList<Pronostico> pronosticoParticipante = maArchivos.leerTodosLosPronosticos(context, participante.getIdUsuario());

                for (Pronostico pronostico : pronosticoParticipante) {

                    Partido partido = buscarPartidoPorId(pronostico.getIdPartido());

                    if (partido != null && partido.getEstado() == EstadoPartido.FINALIZADO) {

                        pronostico.calcularPuntos(partido.getGolesSeleccion1(), partido.getGolesSeleccion2());

                        int nuevoPuntaje = participante.getPuntajeAcumulado() + pronostico.getPuntosObtenidos();

                        participante.setPuntajeAcumulado(nuevoPuntaje);
                    }
                }
                guardarPronosticosActualizados(context, participante.getIdUsuario(), pronosticoParticipante);
            }
        }
        ManejadorArchivos.guardarPuntajesParticipantes(context, usuarios);
    }

    /**
     * Setters y Getters
     * @return
     */

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void setPartidos(ArrayList<Partido> partidos) {
        this.partidos = partidos;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public ArrayList<Pronostico> getPronosticos() {
        return pronosticos;
    }

    /**
     * Establece la lista de pronósticos del usuario actual en memoria.
     *
     * @param pronosticos Nueva lista de pronósticos.
     */

    public void setPronosticos(ArrayList<Pronostico> pronosticos) {
        this.pronosticos = pronosticos;
    }
}
