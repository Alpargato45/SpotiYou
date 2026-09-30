package com.example.spotiyou.BBDD;

public class DatosUsuarios {

    private int codigo;
    private String nombre;
    private String imagen;
    private int estado;
    private String token;
    private String refreshToken;

    public DatosUsuarios(int codigo, String nombre, String imagen, int estado, String token, String refreshToken) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.imagen = imagen;
        this.estado = estado;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public int getEstado() {
        return estado;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
