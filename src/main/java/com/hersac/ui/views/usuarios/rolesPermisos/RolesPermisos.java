package com.hersac.ui.views.usuarios.rolesPermisos;

import javax.swing.*;

public class RolesPermisos extends JPanel {
    public RolesPermisos() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(800, 600));

        JLabel titleLabel = new JLabel("Roles y Permisos");
        titleLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
    }
}
