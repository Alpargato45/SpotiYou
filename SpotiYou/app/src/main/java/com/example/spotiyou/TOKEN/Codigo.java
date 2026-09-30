package com.example.spotiyou.TOKEN;

public class Codigo {

    private static int codigo;

    public Codigo() {
    }

    public static int getCodigo() {
        return codigo;
    }

    public static void setCodigo(int codigo) {
        Codigo.codigo = codigo;
    }
}
