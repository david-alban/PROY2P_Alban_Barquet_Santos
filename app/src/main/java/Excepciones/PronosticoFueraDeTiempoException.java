package Excepciones;

public class PronosticoFueraDeTiempoException extends Exception {
    public PronosticoFueraDeTiempoException() {
        super("El período para registrar pronósticos de ese partido ya ha finalizado.");
    }
}
