package com.hersac.ui.views.usuarios.auditorias;

import javax.swing.*;

public class Auditorias extends JPanel {
    public Auditorias() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(800, 600));

        JLabel titleLabel = new JLabel("Auditorias");
        titleLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
    }
}
