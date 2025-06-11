package com.hersac.ui.views.globals.enums;

import java.awt.*;

public enum ColorsTheme {
    PRIMARY(new Color(255, 0, 77)),
    PRIMARY_LIGTH(new Color(255, 41, 105)),
    SECONDARY(new Color(250, 239, 93)),
    TERTIARY(new Color(126, 37, 83)),
    BACKGROUND(new Color(29, 43, 83)),
    TEXT_PRIMARY(new Color(251, 251, 251)),
    TEXT_SECONDARY(new Color(214, 214, 214)),
    SUCCESS(new Color(76, 175, 80)),
    WARNING(new Color(255, 193, 7)),
    ERROR(new Color(244, 67, 54));

    private final Color color;

    ColorsTheme(Color color) {
        this.color = color;
    }

    public Color get() {
        return color;
    }
}
