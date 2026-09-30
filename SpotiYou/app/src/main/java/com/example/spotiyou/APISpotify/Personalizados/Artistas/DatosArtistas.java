package com.example.spotiyou.APISpotify.Personalizados.Artistas;

public class DatosArtistas {
    private String imagen;
    private String nombre;
    private String id;

    public DatosArtistas(String imagen, String nombre, String id) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }
}