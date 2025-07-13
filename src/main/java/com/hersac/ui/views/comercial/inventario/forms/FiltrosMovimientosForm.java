package com.hersac.ui.views.comercial.inventario.forms;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class FiltrosMovimientosForm extends JPanel {
    private JComboBox<String> comboTipoMovimiento;
    private JComboBox<String> comboProducto;
    private JComboBox<String> comboBodega;
    private JComboBox<String> comboUsuario;
    private JComboBox<String> comboTipoReferencia;
    private DatePicker selectorFechaDesde;
    private DatePicker selectorFechaHasta;
    private Runnable alCambiarFiltros;

    public FiltrosMovimientosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        DatePickerSettings desdeSettings = crearConfiguracionFecha();
        selectorFechaDesde = new DatePicker(desdeSettings);
        selectorFechaDesde.setPreferredSize(new Dimension(120, 28));
        selectorFechaDesde.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaDesde.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 0, "Desde:", selectorFechaDesde);
        DatePickerSettings hastaSettings = crearConfiguracionFecha();
        selectorFechaHasta = new DatePicker(hastaSettings);
        selectorFechaHasta.setPreferredSize(new Dimension(120, 28));
        selectorFechaHasta.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaHasta.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 1, "Hasta:", selectorFechaHasta);
        comboTipoMovimiento = new JComboBox<>(new String[] {"Todos", "ENTRADA", "SALIDA"});
        comboTipoMovimiento.setPreferredSize(new Dimension(120, 28));
        comboTipoMovimiento.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboTipoMovimiento.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 2, "Tipo Movimiento:", comboTipoMovimiento);
        comboProducto = new JComboBox<>(new String[] {"Todos"});
        comboProducto.setPreferredSize(new Dimension(180, 28));
        comboProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboProducto.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 3, "Producto:", comboProducto);
        comboBodega = new JComboBox<>(new String[] {"Todas"});
        comboBodega.setPreferredSize(new Dimension(180, 28));
        comboBodega.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboBodega.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 4, "Bodega:", comboBodega);
        comboUsuario = new JComboBox<>(new String[] {"Todos"});
        comboUsuario.setPreferredSize(new Dimension(180, 28));
        comboUsuario.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboUsuario.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 5, "Usuario:", comboUsuario);
        comboTipoReferencia = new JComboBox<>(new String[] {"Todos", "COMPRA", "VENTA"});
        comboTipoReferencia.setPreferredSize(new Dimension(120, 28));
        comboTipoReferencia.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboTipoReferencia.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 6, "Referencia:", comboTipoReferencia);
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

    public LocalDate obtenerFechaDesde() {
        return selectorFechaDesde.getDate();
    }

    public LocalDate obtenerFechaHasta() {
        return selectorFechaHasta.getDate();
    }

    public String obtenerTipoMovimientoSeleccionado() {
        return (String) comboTipoMovimiento.getSelectedItem();
    }

    public String obtenerProductoSeleccionado() {
        return (String) comboProducto.getSelectedItem();
    }

    public String obtenerBodegaSeleccionada() {
        return (String) comboBodega.getSelectedItem();
    }

    public String obtenerUsuarioSeleccionado() {
        return (String) comboUsuario.getSelectedItem();
    }

    public String obtenerTipoReferenciaSeleccionado() {
        return (String) comboTipoReferencia.getSelectedItem();
    }

    public void establecerProductos(List<String> productos) {
        comboProducto.setModel(new DefaultComboBoxModel<>(productos.toArray(new String[0])));
        comboProducto.insertItemAt("Todos", 0);
        comboProducto.setSelectedIndex(0);
    }

    public void establecerBodegas(List<String> bodegas) {
        comboBodega.setModel(new DefaultComboBoxModel<>(bodegas.toArray(new String[0])));
        comboBodega.insertItemAt("Todas", 0);
        comboBodega.setSelectedIndex(0);
    }

    public void establecerUsuarios(List<String> usuarios) {
        comboUsuario.setModel(new DefaultComboBoxModel<>(usuarios.toArray(new String[0])));
        comboUsuario.insertItemAt("Todos", 0);
        comboUsuario.setSelectedIndex(0);
    }

    public void establecerReferencias(List<String> referencias) {
        comboTipoReferencia.setModel(new DefaultComboBoxModel<>(referencias.toArray(new String[0])));
        comboTipoReferencia.insertItemAt("Todos", 0);
        comboTipoReferencia.setSelectedIndex(0);
    }
}
