package Modelo;

import java.io.Serializable;

/**
 * Representa a un usuario que participa en los pronósticos del sistema.
 * Hereda de la clase {@link Usuario} e implementa la interfaz Comparable para
 * permitir el ordenamiento automático basado en el rendimiento del participante.
 */

public class Participante extends Usuario implements Comparable<Participante> {

    /**
     * Puntos totales obtenidos por el participante en base a sus pronósticos acertados.
     */
    private int puntajeAcumulado;

    /**
     * Crea una instancia de Participante sin un puntaje inicial (por defecto asume 0 o se asigna posteriormente).
     *
     * @param idUsuario      Identificador único del participante.
     * @param nombreUsuario  Nombre de credencial utilizado para el inicio de sesión.
     * @param contrasena     Contraseña de acceso de la cuenta.
     * @param nombreCompleto Nombre real y completo del participante.
     * @param tipoUsuario    Rol asignado al usuario (utilizando el enum {@link TipoUsuario}).
     */

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
    }

    /**
     * Crea una instancia de Participante especificando su puntaje acumulado inicial.
     *
     * @param idUsuario        Identificador único del participante.
     * @param nombreUsuario    Nombre de credencial utilizado para el inicio de sesión.
     * @param contrasena       Contraseña de acceso de la cuenta.
     * @param nombreCompleto   Nombre real y completo del participante.
     * @param tipoUsuario      Rol asignado al usuario (utilizando el enum {@link TipoUsuario}).
     * @param puntajeAcumulado Puntaje total con el que inicia el participante.
     */

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    /**
     * Obtiene los puntos totales acumulados por el participante.
     *
     * @return El puntaje acumulado actual.
     */

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    /**
     * Establece o actualiza el puntaje total del participante.
     *
     * @param puntajeAcumulado El nuevo puntaje a asignar.
     */

    public void setPuntajeAcumulado(int puntajeAcumulado) {
        this.puntajeAcumulado = puntajeAcumulado;
    }

    /**
     * Compara este participante con otro para establecer un ranking (tabla de posiciones).
     * El ordenamiento se realiza de forma descendente por puntaje (de mayor a menor).
     * En caso de empate en puntos, se desempata alfabéticamente por el nombre de usuario (de la A a la Z).
     *
     * @param otro El objeto Participante con el que se va a comparar.
     * @return Un valor negativo si este participante debe ir antes, positivo si debe ir después, o 0 si son iguales.
     */

    @Override
    public int compareTo(Participante otro) {
        int comparacionPuntaje = Integer.compare(otro.getPuntajeAcumulado(), this.puntajeAcumulado);
        if (comparacionPuntaje == 0) {
            return this.getNombreUsuario().compareToIgnoreCase(otro.getNombreUsuario());
        }
        return comparacionPuntaje;
    }
}