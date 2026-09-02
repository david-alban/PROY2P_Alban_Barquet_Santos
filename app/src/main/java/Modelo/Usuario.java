package Modelo;

import java.io.Serializable;

/**
 * Representa la entidad base y abstracta para todos los usuarios del sistema.
 * Sirve como plantilla principal que define los atributos y comportamientos comunes
 * que heredarán tipos específicos de usuarios (como Participante o Administrador).
 * Implementa {@link Serializable} para facilitar su almacenamiento y transmisión en Android.
 */

public abstract class Usuario implements Serializable {
    protected String idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombreCompleto;
    protected TipoUsuario tipoUsuario;

    /**
     * Crea una instancia vacía de Usuario.
     * Requerido en algunos contextos de serialización o inicialización sin datos previos.
     */

    public Usuario() {
    }

    /**
     * Crea una instancia de Usuario con todos sus atributos básicos inicializados.
     *
     * @param idUsuario      Identificador único del usuario.
     * @param nombreUsuario  Nombre de credencial para iniciar sesión.
     * @param contrasena     Contraseña de acceso.
     * @param nombreCompleto Nombre real del usuario.
     * @param tipoUsuario    Rol asignado en el sistema.
     */

    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Setters y Getters
     * @return
     */

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Establece el tipo o rol del usuario en el sistema.
     *
     * @param tipoUsuario Nuevo tipo de usuario.
     */

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Devuelve una representación en texto con la información principal del usuario,
     * excluyendo la contraseña por motivos de seguridad.
     *
     * @return Cadena de texto detallando el estado actual del objeto Usuario.
     */

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
