package Excepciones;

/**
 * Excepción personalizada que se lanza cuando un usuario intenta registrar o modificar un pronóstico
 * para un partido que ya no acepta interacciones (estado CERRADO o FINALIZADO).
 */

public class PronosticoFueraDeTiempoException extends Exception {

    /**
     * Crea una nueva excepción con un mensaje predeterminado advirtiendo que
     * el tiempo permitido para realizar pronósticos en ese partido ha concluido.
     */

    public PronosticoFueraDeTiempoException() {
        super("El período para registrar pronósticos de ese partido ya ha finalizado.");
    }
}
