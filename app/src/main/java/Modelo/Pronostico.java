package Modelo;

import java.io.Serializable;

/**
 * Representa el pronóstico realizado por un participante para un partido específico.
 * Implementa {@link Serializable} para permitir su almacenamiento en archivos.
 */

public class Pronostico implements Serializable {

    private String idPronostico;
    private String idUsuario;
    private String idPartido;
    private int golesSel1;
    private int golesSel2;
    private int puntosObtenidos;

    /**
     * Crea un nuevo pronóstico con los marcadores previstos por el usuario.
     * Inicializa los puntos obtenidos en 0 por defecto.
     *
     * @param idPronostico Identificador único del pronóstico.
     * @param idUsuario    Identificador del participante.
     * @param idPartido    Identificador del partido.
     * @param golesSel1    Goles pronosticados para el primer equipo.
     * @param golesSel2    Goles pronosticados para el segundo equipo.
     */

    public Pronostico(String idPronostico, String idUsuario, String idPartido, int golesSel1, int golesSel2) {
        this.idPronostico = idPronostico;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.golesSel1 = golesSel1;
        this.golesSel2 = golesSel2;
        this.puntosObtenidos = 0;
    }

    /**
     * Setters y Getters
     * @return
     */

    public String getIdPronostico() {
        return idPronostico;
    }

    public void setIdPronostico(String idPronostico) {
        this.idPronostico = idPronostico;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }

    public int getGolesSel1() {
        return golesSel1;
    }

    public void setGolesSel1(int golesSel1) {
        this.golesSel1 = golesSel1;
    }

    public int getGolesSel2() {
        return golesSel2;
    }

    public void setGolesSel2(int golesSel2) {
        this.golesSel2 = golesSel2;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }

    /**
     * Devuelve una representación en formato de texto (separada por comas) con los datos del pronóstico.
     *
     * @return Cadena de texto con formato: idPronostico,idUsuario,idPartido,golesSel1,golesSel2,puntosObtenidos
     */

    @Override
    public String toString() {
        return idPronostico + "," + idUsuario + "," + idPartido + "," + golesSel1 + "," + golesSel2 + "," + puntosObtenidos;
    }

    /**
     * Calcula los puntos obtenidos comparando el pronóstico del usuario con los goles reales del partido.
     * Las reglas de puntuación son:
     * - 3 puntos: Acertar el marcador exacto.
     * - 2 puntos: Acertar un empate (sin marcador exacto) o acertar al ganador con la misma diferencia de goles.
     * - 1 punto: Acertar únicamente al ganador del partido.
     * - 0 puntos: Pronóstico completamente incorrecto.
     *
     * @param gRealSel1 Goles oficiales anotados por la primera selección.
     * @param gRealSel2 Goles oficiales anotados por la segunda selección.
     */

    public void calcularPuntos (int gRealSel1, int gRealSel2){
        if(golesSel1 == gRealSel1 && golesSel2==gRealSel2){
            puntosObtenidos = 3;
        } else if (golesSel1==golesSel2 && gRealSel1== gRealSel2) {
            puntosObtenidos=2;


        } else if (golesSel1 > golesSel2 && gRealSel1 > gRealSel2) {

            if ((golesSel1 - golesSel2) == (gRealSel1 - gRealSel2)) {
                puntosObtenidos = 2;
            } else {
                puntosObtenidos = 1;
            }


        } else if (golesSel2 > golesSel1 && gRealSel2 > gRealSel1) {
            if ((golesSel2 - golesSel1) == (gRealSel2 - gRealSel1)) {
                puntosObtenidos = 2;
            } else {
                puntosObtenidos = 1;
            }

        } else {
            puntosObtenidos = 0;
        }
    }
}



