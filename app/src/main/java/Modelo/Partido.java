package Modelo;

import java.io.Serializable;
import Excepciones.DatosIncompletosException;


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

    public Partido(String id, String fase, String fecha, String hora, String estadio, String seleccion1, String seleccion2) {
        this.id = id;
        this.fase = fase;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.estado = EstadoPartido.ABIERTO;

        this.golesSeleccion1 = 0;
        this.golesSeleccion2 = 0;
    }

    public void cerrarPronosticos() {
        this.estado = EstadoPartido.CERRADO;
    }

    public void registrarResultadoOficial (int goles1, int goles2) throws DatosIncompletosException {
        if (goles1 < 0 || goles2 < 0) {
            throw new DatosIncompletosException("Los goles deben ser números enteros mayores o iguales a cero.");
        }
        this.golesSeleccion1 = goles1;
        this.golesSeleccion2 = goles2;
        this.estado = EstadoPartido.FINALIZADO;
    }

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
