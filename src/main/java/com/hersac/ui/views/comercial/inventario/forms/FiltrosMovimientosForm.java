package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class FiltrosMovimientosForm extends JPanel {
    private JComboBox<String> tipoMovimientoCombo;
    private JComboBox<String> productoCombo;
    private JComboBox<String> bodegaCombo;
    private JComboBox<String> usuarioCombo;
    private JComboBox<String> tipoReferenciaCombo;
    private DatePicker fechaDesdePicker;
    private DatePicker fechaHastaPicker;
    private Runnable onFiltrosCambiados;

    public FiltrosMovimientosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        // Fecha desde
        DatePickerSettings desdeSettings = createDateSettings();
        fechaDesdePicker = new DatePicker(desdeSettings);
        fechaDesdePicker.setPreferredSize(new Dimension(120, 28));
        fechaDesdePicker.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        fechaDesdePicker.addDateChangeListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 0, "Desde:", fechaDesdePicker);
        // Fecha hasta
        DatePickerSettings hastaSettings = createDateSettings();
        fechaHastaPicker = new DatePicker(hastaSettings);
        fechaHastaPicker.setPreferredSize(new Dimension(120, 28));
        fechaHastaPicker.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        fechaHastaPicker.addDateChangeListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 1, "Hasta:", fechaHastaPicker);
        // Tipo movimiento
        tipoMovimientoCombo = new JComboBox<>(new String[] {"Todos", "ENTRADA", "SALIDA"});
        tipoMovimientoCombo.setPreferredSize(new Dimension(120, 28));
        tipoMovimientoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 2, "Tipo Movimiento:", tipoMovimientoCombo);
        // Producto
        productoCombo = new JComboBox<>(new String[] {"Todos"});
        productoCombo.setPreferredSize(new Dimension(180, 28));
        productoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 3, "Producto:", productoCombo);
        // Bodega
        bodegaCombo = new JComboBox<>(new String[] {"Todas"});
        bodegaCombo.setPreferredSize(new Dimension(180, 28));
        bodegaCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 4, "Bodega:", bodegaCombo);
        // Usuario
        usuarioCombo = new JComboBox<>(new String[] {"Todos"});
        usuarioCombo.setPreferredSize(new Dimension(180, 28));
        usuarioCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 5, "Usuario:", usuarioCombo);
        // Tipo referencia
        tipoReferenciaCombo = new JComboBox<>(new String[] {"Todos", "COMPRA", "VENTA"});
        tipoReferenciaCombo.setPreferredSize(new Dimension(120, 28));
        tipoReferenciaCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 6, "Referencia:", tipoReferenciaCombo);
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

    private DatePickerSettings createDateSettings() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setAllowEmptyDates(true);
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        return settings;
    }

    private void notificarCambio() {
        if (onFiltrosCambiados != null) {
            onFiltrosCambiados.run();
        }
    }

    public void setOnFiltrosCambiados(Runnable listener) {
        this.onFiltrosCambiados = listener;
    }

    public LocalDate getFechaDesde() {
        return fechaDesdePicker.getDate();
    }

    public LocalDate getFechaHasta() {
        return fechaHastaPicker.getDate();
    }

    public String getTipoMovimientoSeleccionado() {
        return (String) tipoMovimientoCombo.getSelectedItem();
    }

    public String getProductoSeleccionado() {
        return (String) productoCombo.getSelectedItem();
    }

    public String getBodegaSeleccionada() {
        return (String) bodegaCombo.getSelectedItem();
    }

    public String getUsuarioSeleccionado() {
        return (String) usuarioCombo.getSelectedItem();
    }

    public String getTipoReferenciaSeleccionado() {
        return (String) tipoReferenciaCombo.getSelectedItem();
    }

    public void setProductos(List<String> productos) {
        productoCombo.setModel(new DefaultComboBoxModel<>(productos.toArray(new String[0])));
        productoCombo.insertItemAt("Todos", 0);
        productoCombo.setSelectedIndex(0);
    }

    public void setBodegas(List<String> bodegas) {
        bodegaCombo.setModel(new DefaultComboBoxModel<>(bodegas.toArray(new String[0])));
        bodegaCombo.insertItemAt("Todas", 0);
        bodegaCombo.setSelectedIndex(0);
    }

    public void setUsuarios(List<String> usuarios) {
        usuarioCombo.setModel(new DefaultComboBoxModel<>(usuarios.toArray(new String[0])));
        usuarioCombo.insertItemAt("Todos", 0);
        usuarioCombo.setSelectedIndex(0);
    }

    public void setReferencias(List<String> referencias) {
        tipoReferenciaCombo.setModel(new DefaultComboBoxModel<>(referencias.toArray(new String[0])));
        tipoReferenciaCombo.insertItemAt("Todos", 0);
        tipoReferenciaCombo.setSelectedIndex(0);
    }
}
