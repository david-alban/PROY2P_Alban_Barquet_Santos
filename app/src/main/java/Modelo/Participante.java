package Modelo;

import java.io.Serializable;

public class Participante extends Usuario implements Comparable<Participante> {
    private int puntajeAcumulado;

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
    }

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public void setPuntajeAcumulado(int puntajeAcumulado) {
        this.puntajeAcumulado = puntajeAcumulado;
    }

    @Override
    public int compareTo(Participante otro) {
        int comparacionPuntaje = Integer.compare(otro.getPuntajeAcumulado(), this.puntajeAcumulado);
        if (comparacionPuntaje == 0) {
            return this.getNombreUsuario().compareToIgnoreCase(otro.getNombreUsuario());
        }
        return comparacionPuntaje;
    }
}