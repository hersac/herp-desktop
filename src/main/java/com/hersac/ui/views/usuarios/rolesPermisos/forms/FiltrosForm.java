package com.hersac.ui.views.usuarios.rolesPermisos.forms;

import javax.swing.*;
import java.awt.*;
import com.hersac.ui.views.usuarios.rolesPermisos.constantes.RolesPermisosConstantes;

public class FiltrosForm extends JPanel {
    private JComboBox<String> estadoCombo;
    private Runnable onFiltrosCambiados;

    public FiltrosForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
        estadoCombo = new JComboBox<>(RolesPermisosConstantes.ESTADOS);
        estadoCombo.addActionListener(e -> {
            if (onFiltrosCambiados != null) onFiltrosCambiados.run();
        });
        add(new JLabel("Estado:"));
        add(estadoCombo);
    }

    public String getEstadoSeleccionado() {
        return (String) estadoCombo.getSelectedItem();
    }

    public void setOnFiltrosCambiados(Runnable r) {
        this.onFiltrosCambiados = r;
    }
}

