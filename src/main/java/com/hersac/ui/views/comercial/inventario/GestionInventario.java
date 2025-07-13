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
        JPanel panelSuperior = new JPanel();
        panelSuperior.setOpaque(false);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        JLabel etiquetaTitulo = new JLabel("Gestión de Inventario");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(etiquetaTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        PestanasComponent pestanas = new PestanasComponent(diContainer);
        pestanas.setAlignmentX(CENTER_ALIGNMENT);
        panelSuperior.add(pestanas);
        add(panelSuperior, BorderLayout.CENTER);
    }
}
