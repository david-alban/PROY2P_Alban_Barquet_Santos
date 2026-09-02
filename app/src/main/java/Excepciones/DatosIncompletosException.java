package Excepciones;

/**
 * Excepción personalizada que se lanza cuando faltan datos obligatorios o los valores ingresados son inválidos
 * al intentar registrar un pronóstico o un resultado oficial (por ejemplo, ingresar goles en negativo).
 */

public class DatosIncompletosException extends Exception {

    /**
     * Crea una nueva excepción con un mensaje predeterminado indicando que
     * faltan datos para completar el registro del pronóstico.
     */
    public DatosIncompletosException() {
        super("No se han ingresado todos los datos necesarios para registrar el pronóstico.");
    }

    /**
     * Crea una nueva excepción especificando un mensaje de error personalizado.
     *
     * @param mensajePersonalizado Detalle específico sobre el dato faltante o la validación que falló.
     */

    public DatosIncompletosException(String mensajePersonalizado) {
        super(mensajePersonalizado);
    }
}
