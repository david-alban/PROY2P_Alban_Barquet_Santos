package Modelo;

import java.io.Serializable;

public class Pronostico implements Serializable {

    private String idPronostico;
    private String idUsuario;
    private String idPartido;
    private int golesSel1;
    private int golesSel2;
    private int puntosObtenidos;

    public Pronostico(String idPronostico, String idUsuario, String idPartido, int golesSel1, int golesSel2) {
        this.idPronostico = idPronostico;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.golesSel1 = golesSel1;
        this.golesSel2 = golesSel2;
        this.puntosObtenidos = 0;
    }

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

    @Override
    public String toString() {
        return idPronostico + "," + idUsuario + "," + idPartido + "," + golesSel1 + "," + golesSel2 + "," + puntosObtenidos;
    }

    public void calcularPuntos (int gRealSel1, int gRealSel2){
        if(golesSel1 == gRealSel1 && golesSel2==gRealSel2){
            puntosObtenidos = 3;
        } else if (golesSel1==golesSel2 && gRealSel1== gRealSel2) {
            puntosObtenidos=2;


        } else if (golesSel1 > golesSel2 && gRealSel1 > gRealSel2) { // gana sel 1

            // diferencia de goles
            if ((golesSel1 - golesSel2) == (gRealSel1 - gRealSel2)) {
                puntosObtenidos = 2;
            } else {
                puntosObtenidos = 1;
            }


        } else if (golesSel2 > golesSel1 && gRealSel2 > gRealSel1) { // gana sel 2

            // diferencia de goles
            if ((golesSel2 - golesSel1) == (gRealSel2 - gRealSel1)) {
                puntosObtenidos = 2;
            } else {
                puntosObtenidos = 1;
            }


        } else { // no le pego a nada jaja
            puntosObtenidos = 0;
        }
    }
}



