package com.hersac.ui.views.components;

import com.hersac.ui.views.globals.enums.ColorsTheme;

import javax.swing.*;
import java.awt.*;

public class SidebarComponent extends JPanel {
    private final Color BACKGROUND_COLOR = ColorsTheme.BACKGROUND.get();
    private final Color TEXT_COLOR = ColorsTheme.TEXT_PRIMARY.get();

    public SidebarComponent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(150, 0));
        setBackground(BACKGROUND_COLOR);

        Font labelFont = new Font("Roboto", Font.BOLD, 14);

        add(createOptionPanel("Módulos", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Compañía", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Terceros", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Reportes", labelFont));
    }

    private JPanel createOptionPanel(String text, Font font) {
        JPanel optionPanel = new JPanel(new GridBagLayout());
        optionPanel.setPreferredSize(new Dimension(150, 50));
        optionPanel.setMaximumSize(new Dimension(150, 50));
        optionPanel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        optionPanel.add(label);

        return optionPanel;
    }
}
