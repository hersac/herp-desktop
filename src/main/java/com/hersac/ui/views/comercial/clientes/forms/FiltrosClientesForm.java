package com.hersac.ui.views.comercial.clientes.forms;

import javax.swing.*;
import java.awt.*;

public class FiltrosClientesForm extends JPanel {
    private JComboBox<String> comboEstado;
    private Runnable alCambiarFiltros;

    public FiltrosClientesForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 10, 5, 10);
        restricciones.gridy = 0;
        comboEstado = new JComboBox<>(new String[] {"Todos", "Activo", "Inactivo"});
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.addActionListener(e -> notificarCambio());
        establecerFuenteRoboto(comboEstado);
        agregarFiltro(panelFiltros, restricciones, 0, "Estado:", comboEstado);
        add(Box.createVerticalStrut(10));
        add(panelFiltros);
    }

    private void agregarFiltro(JPanel panel, GridBagConstraints restricciones, int x, String textoEtiqueta, JComponent componente) {
        restricciones.gridx = x * 2;
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setPreferredSize(new Dimension(90, 28));
        establecerFuenteRoboto(etiqueta);
        panel.add(etiqueta, restricciones);
        restricciones.gridx = x * 2 + 1;
        panel.add(componente, restricciones);
    }

    private void notificarCambio() {
        if (alCambiarFiltros != null) {
            alCambiarFiltros.run();
        }
    }

    public String obtenerEstadoSeleccionado() {
        return (String) comboEstado.getSelectedItem();
    }

    public void establecerAlCambiarFiltros(Runnable alCambiarFiltros) {
        this.alCambiarFiltros = alCambiarFiltros;
    }

    private void establecerFuenteRoboto(JComponent componente) {
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);
        componente.setFont(fuenteRoboto);
    }
}
