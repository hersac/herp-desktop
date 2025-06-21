package com.hersac.ui.views.usuarios.gestion;

import javax.swing.*;

public class GestionUsuarios extends JPanel {
    public GestionUsuarios() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(800, 600));

        JLabel titleLabel = new JLabel("Gestión de Usuarios");
        titleLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
    }
}
