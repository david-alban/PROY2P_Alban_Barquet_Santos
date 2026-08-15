package Excepciones;

public class DatosIncompletosException extends Exception {
    public DatosIncompletosException() {
        super("No se han ingresado todos los datos necesarios para registrar el pronóstico.");
    }

    public DatosIncompletosException(String mensajePersonalizado) {
        super(mensajePersonalizado);
    }
}
