package com.hersac.ui.views.comercial.clientes.forms;

import javax.swing.*;
import java.awt.*;

public class FiltrosClientesForm extends JPanel {
    private JComboBox<String> estadoCombo;
    private Runnable onFiltrosCambiados;

    public FiltrosClientesForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        estadoCombo = new JComboBox<>(new String[] {"Todos", "Activo", "Inactivo"});
        estadoCombo.setPreferredSize(new Dimension(120, 28));
        estadoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 0, "Estado:", estadoCombo);
        add(Box.createVerticalStrut(10));
        add(filtrosPanel);
    }

    private void addFiltro(JPanel panel, GridBagConstraints gbc, int x, String labelText, JComponent component) {
        gbc.gridx = x * 2;
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(90, 28));
        panel.add(label, gbc);
        gbc.gridx = x * 2 + 1;
        panel.add(component, gbc);
    }

    private void notificarCambio() {
        if (onFiltrosCambiados != null) {
            onFiltrosCambiados.run();
        }
    }

    public String getEstadoSeleccionado() {
        return (String) estadoCombo.getSelectedItem();
    }

    public void setOnFiltrosCambiados(Runnable onFiltrosCambiados) {
        this.onFiltrosCambiados = onFiltrosCambiados;
    }
}
