package Modelo;

import java.io.Serializable;

public class Administrador extends Usuario {
    private String cargo;

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
    }

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
