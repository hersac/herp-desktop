package com.hersac.ui.views.components;

import com.hersac.ui.views.globals.enums.ColorsTheme;

import javax.swing.*;
import java.awt.*;

public class NavbarComponent extends JPanel {
    private static final Color BACKGROUND_COLOR = ColorsTheme.BACKGROUND.get();
    private static final Color TEXT_COLOR = ColorsTheme.TEXT_PRIMARY.get();

    public NavbarComponent() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
        setPreferredSize(new Dimension(0, 25));
        setBackground(BACKGROUND_COLOR);

        Font labelFont = new Font("Roboto", Font.PLAIN, 11);

        add(createLabel("Módulos", labelFont));
        add(createLabel("Compañía", labelFont));
        add(createLabel("Terceros", labelFont));
        add(createLabel("Reportes", labelFont));
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);

        return label;
    }


}
