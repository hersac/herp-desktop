package com.hersac.ui.views.comercial.inventario;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.comercial.inventario.parciales.PestanasComponent;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GestionInventario extends JPanel {
    public GestionInventario(DIContainer diContainer) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Gestión de Inventario");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(titleLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        PestanasComponent pestanas = new PestanasComponent(diContainer);
        pestanas.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(pestanas);
        add(topPanel, BorderLayout.CENTER);
    }
}
