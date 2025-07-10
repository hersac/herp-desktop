package com.hersac.ui.views.comercial.clientes.constantes;

public enum TiposClienteEnum {
    NATURAL(1, "Natural"),
    JURIDICA(2, "Jurídica"),
    EXTRANJERA(3, "Extranjera"),
    GOBIERNO(4, "Gobierno"),
    ONG(5, "ONG"),
    OTRO(6, "Otro");

    private final int id;
    private final String nombre;

    TiposClienteEnum(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static TiposClienteEnum fromId(int id) {
        for (TiposClienteEnum tipo : values()) {
            if (tipo.id == id) return tipo;
        }
        return null;
    }

    public static TiposClienteEnum fromNombre(String nombre) {
        for (TiposClienteEnum tipo : values()) {
            if (tipo.nombre.equalsIgnoreCase(nombre)) return tipo;
        }
        return null;
    }
}

