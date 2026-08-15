package Excepciones;

public class CredencialesInvalidasException extends Exception{
    public CredencialesInvalidasException() {
        super("El usuario o la contraseña son incorrectos.");
    }
}
