package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class FiltrosProductosForm extends JPanel {
    private JComboBox<String> estadoCombo;
    private JComboBox<String> unidadCombo;
    private Runnable onFiltrosCambiados;

    public FiltrosProductosForm() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        estadoCombo = new JComboBox<>(new String[] { "Todos", "Activos", "Inactivos" });
        estadoCombo.setPreferredSize(new Dimension(120, 28));
        estadoCombo.addActionListener(e -> notificarCambio());
        unidadCombo = new JComboBox<>();
        unidadCombo.setPreferredSize(new Dimension(120, 28));
        unidadCombo.addItem("Todas");
        unidadCombo.addActionListener(e -> notificarCambio());
        add(new JLabel("Estado:"));
        add(Box.createHorizontalStrut(8));
        add(estadoCombo);
        add(Box.createHorizontalStrut(20));
        add(new JLabel("Unidad de Medida:"));
        add(Box.createHorizontalStrut(8));
        add(unidadCombo);
    }

    private void notificarCambio() {
        if (onFiltrosCambiados != null) {
            onFiltrosCambiados.run();
        }
    }

    public void setOnFiltrosCambiados(Runnable listener) {
        this.onFiltrosCambiados = listener;
    }

    public String getEstadoSeleccionado() {
        return (String) estadoCombo.getSelectedItem();
    }

    public String getUnidadSeleccionada() {
        return (String) unidadCombo.getSelectedItem();
    }

    public void setUnidades(List<String> unidades) {
        unidadCombo.removeAllItems();
        unidadCombo.addItem("Todas");
        for (String unidad : unidades) {
            unidadCombo.addItem(unidad);
        }
    }
}
