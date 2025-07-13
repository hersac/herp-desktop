package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;

public class FiltroProveedoresForm extends JPanel {
    private JComboBox<String> comboEstado;
    private Runnable alCambiarFiltros;

    public FiltroProveedoresForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        comboEstado = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo"});
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboEstado.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 0, "Estado:", comboEstado);
        add(Box.createVerticalStrut(10));
        add(panelFiltros);
    }

    private void agregarFiltro(JPanel panel, GridBagConstraints gbc, int x, String textoEtiqueta, JComponent componente) {
        gbc.gridx = x * 2;
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setPreferredSize(new Dimension(90, 28));
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        panel.add(etiqueta, gbc);
        gbc.gridx = x * 2 + 1;
        panel.add(componente, gbc);
    }

    private void notificarCambio() {
        if (alCambiarFiltros != null) {
            alCambiarFiltros.run();
        }
    }

    public String obtenerEstadoSeleccionado() {
        return (String) comboEstado.getSelectedItem();
    }

    public void establecerAlCambiarFiltros(Runnable listener) {
        this.alCambiarFiltros = listener;
    }
}
