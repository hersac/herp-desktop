package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class FiltrosItemsForm extends JPanel {
    private JComboBox<String> comboEstado;
    private JComboBox<String> comboCategoria;
    private DatePicker selectorFechaDesde;
    private DatePicker selectorFechaHasta;
    private Runnable alCambiarFiltros;

    public FiltrosItemsForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        comboCategoria = new JComboBox<>();
        comboCategoria.setPreferredSize(new Dimension(130, 28));
        comboCategoria.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboCategoria.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 0, "Categoría:", comboCategoria);
        comboEstado = new JComboBox<>(new String[] { "Todos", "Activos", "Inactivos" });
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboEstado.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 1, "Estado:", comboEstado);
        DatePickerSettings desdeSettings = crearConfiguracionFecha();
        selectorFechaDesde = new DatePicker(desdeSettings);
        selectorFechaDesde.setPreferredSize(new Dimension(120, 28));
        selectorFechaDesde.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaDesde.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 2, "Desde:", selectorFechaDesde);
        DatePickerSettings hastaSettings = crearConfiguracionFecha();
        selectorFechaHasta = new DatePicker(hastaSettings);
        selectorFechaHasta.setPreferredSize(new Dimension(120, 28));
        selectorFechaHasta.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaHasta.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 3, "Hasta:", selectorFechaHasta);
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
        if (componente instanceof JTextField) {
            componente.setFont(new Font("Roboto", Font.PLAIN, 14));
        }
        panel.add(componente, gbc);
    }

    private DatePickerSettings crearConfiguracionFecha() {
        DatePickerSettings configuracion = new DatePickerSettings();
        configuracion.setAllowEmptyDates(true);
        configuracion.setFormatForDatesCommonEra("yyyy-MM-dd");
        configuracion.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        return configuracion;
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

    public String obtenerCategoriaSeleccionada() {
        return (String) comboCategoria.getSelectedItem();
    }

    public String obtenerFechaDesde() {
        LocalDate fecha = selectorFechaDesde.getDate();
        return (fecha != null) ? fecha.toString() : null;
    }

    public String obtenerFechaHasta() {
        LocalDate fecha = selectorFechaHasta.getDate();
        return (fecha != null) ? fecha.toString() : null;
    }

    public void establecerCategorias(List<String> categorias) {
        comboCategoria.removeAllItems();
        comboCategoria.addItem("Todas");
        for (String cat : categorias) {
            comboCategoria.addItem(cat);
        }
    }
}
