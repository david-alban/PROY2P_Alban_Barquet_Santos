package Excepciones;

/**
 * Excepción personalizada que se lanza cuando las credenciales de autenticación son incorrectas.
 * Se utiliza principalmente durante el proceso de inicio de sesión en el repositorio del sistema.
 */

public class CredencialesInvalidasException extends Exception{

    /**
     * Crea una nueva excepción con un mensaje predeterminado indicando que
     * el usuario o la contraseña no coinciden con los registros.
     */

    public CredencialesInvalidasException() {
        super("El usuario o la contraseña son incorrectos.");
    }
}
