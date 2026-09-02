package Modelo;

import java.io.Serializable;

/**
 * Representa a un usuario con privilegios de administrador dentro del sistema.
 * Hereda los atributos y comportamientos básicos de la clase {@link Usuario}.
 */

public class Administrador extends Usuario {

    /**
     * Cargo o posición específica que ocupa el administrador en la organización.
     */

    private String cargo;

    /**
     * Crea una instancia de Administrador sin especificar un cargo inicial.
     *
     * @param idUsuario      Identificador único del usuario.
     * @param nombreUsuario  Nombre de credencial utilizado para el inicio de sesión.
     * @param contrasena     Contraseña de acceso de la cuenta.
     * @param nombreCompleto Nombre real y completo del administrador.
     * @param tipoUsuario    Rol asignado al usuario (utilizando el enum {@link TipoUsuario}).
     */

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
    }

    /**
     * Crea una instancia de Administrador especificando su cargo.
     *
     * @param idUsuario      Identificador único del usuario.
     * @param nombreUsuario  Nombre de credencial utilizado para el inicio de sesión.
     * @param contrasena     Contraseña de acceso de la cuenta.
     * @param nombreCompleto Nombre real y completo del administrador.
     * @param tipoUsuario    Rol asignado al usuario (utilizando el enum {@link TipoUsuario}).
     * @param cargo          Cargo administrativo específico que ocupa.
     */

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }

    /**
     * Obtiene el cargo actual del administrador.
     *
     * @return Cadena de texto con el cargo.
     */

    public String getCargo() {
        return cargo;
    }

    /**
     * Establece o actualiza el cargo del administrador.
     *
     * @param cargo El nuevo cargo a asignar.
     */

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
