package com.hersac.ui.views.terceros.constantes;

public enum TiposTerceroEnum {
    NATURAL(1, "Natural"),
    JURIDICA(2, "Jurídica"),
    EXTRANJERA(3, "Extranjera"),
    GOBIERNO(4, "Gobierno"),
    ONG(5, "ONG"),
    OTRO(6, "Otro");

    private final int id;
    private final String nombre;

    TiposTerceroEnum(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static TiposTerceroEnum fromId(int id) {
        for (TiposTerceroEnum tipo : values()) {
            if (tipo.id == id) return tipo;
        }
        return null;
    }

    public static TiposTerceroEnum fromNombre(String nombre) {
        for (TiposTerceroEnum tipo : values()) {
            if (tipo.nombre.equalsIgnoreCase(nombre)) return tipo;
        }
        return null;
    }
}
