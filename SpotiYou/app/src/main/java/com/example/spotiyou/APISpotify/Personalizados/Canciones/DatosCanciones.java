package com.example.spotiyou.APISpotify.Personalizados.Canciones;

public class DatosCanciones {

    private String imagen;
    private String cancion;
    private String cantante;
    private String id;

    public DatosCanciones(String imagen, String cancion, String cantante, String id) {
        this.imagen = imagen;
        this.cancion = cancion;
        this.cantante = cantante;
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCancion() {
        return cancion;
    }

    public String getCantante() {
        return cantante;
    }

    public String getId() {
        return id;
    }
}
