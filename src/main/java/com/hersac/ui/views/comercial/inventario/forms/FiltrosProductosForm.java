package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FiltrosProductosForm extends JPanel {
    private JComboBox<String> comboEstado;
    private JComboBox<String> comboUnidad;
    private Runnable alCambiarFiltros;

    public FiltrosProductosForm() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        comboEstado = new JComboBox<>(new String[] { "Todos", "Activos", "Inactivos" });
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboEstado.addActionListener(e -> notificarCambio());
        comboUnidad = new JComboBox<>();
        comboUnidad.setPreferredSize(new Dimension(120, 28));
        comboUnidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboUnidad.addItem("Todas");
        comboUnidad.addActionListener(e -> notificarCambio());
        agregarEtiqueta("Estado:");
        add(Box.createHorizontalStrut(8));
        add(comboEstado);
        add(Box.createHorizontalStrut(20));
        agregarEtiqueta("Unidad de Medida:");
        add(Box.createHorizontalStrut(8));
        add(comboUnidad);
    }

    private void agregarEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiqueta);
    }

    private void notificarCambio() {
        if (alCambiarFiltros != null) {
            alCambiarFiltros.run();
        }
    }

    public void establecerAlCambiarFiltros(Runnable listener) {
        this.alCambiarFiltros = listener;
    }

    public String obtenerEstadoSeleccionado() {
        return (String) comboEstado.getSelectedItem();
    }

    public String obtenerUnidadSeleccionada() {
        return (String) comboUnidad.getSelectedItem();
    }

    public void establecerUnidades(List<String> unidades) {
        comboUnidad.removeAllItems();
        comboUnidad.addItem("Todas");
        for (String unidad : unidades) {
            comboUnidad.addItem(unidad);
        }
    }
}
