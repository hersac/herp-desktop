package com.hersac.core.modules.movimientosinventarios.constants;

public enum TipoReferencia {
    COMPRA(1),
    VENTA(2);

    private final int value;

    TipoReferencia(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
