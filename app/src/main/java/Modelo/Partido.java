package Modelo;

import java.io.Serializable;
import Excepciones.DatosIncompletosException;

/**
 * Representa un partido deportivo dentro del sistema de pronósticos.
 * Implementa {@link Serializable} para permitir su almacenamiento en archivos o transmisión entre componentes de Android.
 */

public class Partido implements Serializable {
    private String id;
    private String fase;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private EstadoPartido estado;
    private int golesSeleccion1;
    private int golesSeleccion2;

    /**
     * Crea una nueva instancia de un Partido con sus datos básicos.
     * Los goles de ambas selecciones se inicializan automáticamente en 0.
     *
     * @param id         Identificador único del partido.
     * @param fase       Fase del torneo correspondiente al encuentro.
     * @param fecha      Fecha en la que se jugará el partido.
     * @param hora       Hora programada del encuentro.
     * @param estadio    Estadio donde se llevará a cabo.
     * @param estado     Estado inicial del partido.
     * @param seleccion1 Nombre del primer equipo.
     * @param seleccion2 Nombre del segundo equipo.
     */

    public Partido(String id, String fase, String fecha, String hora, String estadio, EstadoPartido estado, String seleccion1, String seleccion2) {
        this.id = id;
        this.fase = fase;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.estado = estado;

        this.golesSeleccion1 = 0;
        this.golesSeleccion2 = 0;
    }

    /**
     * Cambia el estado del partido a {@link EstadoPartido#CERRADO}.
     * Se utiliza para bloquear el ingreso de nuevos pronósticos antes de que comience el encuentro.
     */

    public void cerrarPronosticos() {
        this.estado = EstadoPartido.CERRADO;
    }

    /**
     * Registra el marcador final del partido y cambia su estado a {@link EstadoPartido#FINALIZADO}.
     *
     * @param goles1 Goles oficiales anotados por la primera selección.
     * @param goles2 Goles oficiales anotados por la segunda selección.
     * @throws DatosIncompletosException Si alguno de los valores de goles ingresados es menor a cero.
     */

    public void registrarResultadoOficial (int goles1, int goles2) throws DatosIncompletosException {
        if (goles1 < 0 || goles2 < 0) {
            throw new DatosIncompletosException("Los goles deben ser números enteros mayores o iguales a cero.");
        }
        this.golesSeleccion1 = goles1;
        this.golesSeleccion2 = goles2;
        this.estado = EstadoPartido.FINALIZADO;
    }

    /**
     * Setter y Getters
     * @return
     */

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }

    public String getSeleccion1() { return seleccion1; }
    public void setSeleccion1(String seleccion1) { this.seleccion1 = seleccion1; }

    public String getSeleccion2() { return seleccion2; }
    public void setSeleccion2(String seleccion2) { this.seleccion2 = seleccion2; }

    public EstadoPartido getEstado() { return estado; }
    public void setEstado(EstadoPartido estado) { this.estado = estado; }

    public int getGolesSeleccion1() { return golesSeleccion1; }
    public int getGolesSeleccion2() { return golesSeleccion2; }

    /**
     * Devuelve una representación en formato de texto con todos los atributos del partido.
     *
     * @return Cadena de texto detallando el estado actual del objeto.
     */

    @Override
    public String toString() {
        return "Partido{" +
                "id='" + id + '\'' +
                ", fase='" + fase + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estadio='" + estadio + '\'' +
                ", seleccion1='" + seleccion1 + '\'' +
                ", seleccion2='" + seleccion2 + '\'' +
                ", estado=" + estado +
                ", golesSeleccion1=" + golesSeleccion1 +
                ", golesSeleccion2=" + golesSeleccion2 +
                '}';
    }
}
