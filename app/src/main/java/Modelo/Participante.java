package Modelo;

public class Participante extends Usuario implements Comparable<Participante> {
    private int puntajeAcumulado;

    public Participante() {
        super();
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
        return Integer.compare(otro.getPuntajeAcumulado(), this.puntajeAcumulado);
    }
}
