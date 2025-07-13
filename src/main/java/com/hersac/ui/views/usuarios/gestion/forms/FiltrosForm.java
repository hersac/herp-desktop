package com.hersac.ui.views.usuarios.gestion.forms;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;

public class FiltrosForm extends JPanel {
    private JComboBox<String> comboEstado;
    private JComboBox<String> comboDepartamento;
    private DatePicker selectorFechaDesde;
    private DatePicker selectorFechaHasta;
    private Runnable alCambiarFiltros;
    private final Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);

    public FiltrosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        comboDepartamento = new JComboBox<>();
        comboDepartamento.setPreferredSize(new Dimension(130, 28));
        comboDepartamento.setFont(fuenteRoboto);
        comboDepartamento.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 0, "Departamento:", comboDepartamento);
        comboEstado = new JComboBox<>(new String[] {
                "Todos", "Activos", "Inactivos", "Bloqueados", "Suspendidos"
        });
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.setFont(fuenteRoboto);
        comboEstado.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 1, "Estado:", comboEstado);
        DatePickerSettings ajustesDesde = crearAjustesFecha();
        selectorFechaDesde = new DatePicker(ajustesDesde);
        selectorFechaDesde.setPreferredSize(new Dimension(120, 28));
        selectorFechaDesde.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaDesde.getComponentDateTextField().setFont(fuenteRoboto);
        selectorFechaDesde.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 2, "Desde:", selectorFechaDesde);
        DatePickerSettings ajustesHasta = crearAjustesFecha();
        selectorFechaHasta = new DatePicker(ajustesHasta);
        selectorFechaHasta.setPreferredSize(new Dimension(120, 28));
        selectorFechaHasta.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        selectorFechaHasta.getComponentDateTextField().setFont(fuenteRoboto);
        selectorFechaHasta.addDateChangeListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 3, "Hasta:", selectorFechaHasta);
        add(Box.createVerticalStrut(10));
        add(panelFiltros);
    }

    private void agregarFiltro(JPanel panel, GridBagConstraints gbc, int x, String textoEtiqueta, JComponent componente) {
        gbc.gridx = x * 2;
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setPreferredSize(new Dimension(90, 28));
        etiqueta.setFont(fuenteRoboto);
        panel.add(etiqueta, gbc);
        gbc.gridx = x * 2 + 1;
        panel.add(componente, gbc);
    }

    private DatePickerSettings crearAjustesFecha() {
        DatePickerSettings ajustes = new DatePickerSettings(new Locale("es"));
        ajustes.setAllowEmptyDates(true);
        ajustes.setFormatForDatesCommonEra("yyyy-MM-dd");
        ajustes.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        return ajustes;
    }

    public void setOnFiltrosCambiados(Runnable listener) {
        this.alCambiarFiltros = listener;
    }

    private void notificarCambio() {
        if (alCambiarFiltros != null) {
            alCambiarFiltros.run();
        }
    }

    public String obtenerEstadoSeleccionado() {
        Object seleccionado = comboEstado.getSelectedItem();
        if (seleccionado != null) {
            return seleccionado.toString();
        }
        return null;
    }

    public String obtenerDepartamentoSeleccionado() {
        Object seleccionado = comboDepartamento.getSelectedItem();
        if (seleccionado != null) {
            return seleccionado.toString();
        }
        return null;
    }

    public String obtenerFechaDesde() {
        LocalDate fecha = selectorFechaDesde.getDate();
        if (fecha != null) {
            return fecha.toString();
        }
        return null;
    }

    public String obtenerFechaHasta() {
        LocalDate fecha = selectorFechaHasta.getDate();
        if (fecha != null) {
            return fecha.toString();
        }
        return null;
    }

    public void setDepartamentos(List<DepartamentoEntity> departamentos) {
        comboDepartamento.removeAllItems();
        comboDepartamento.addItem("Todos");
        for (DepartamentoEntity departamento : departamentos) {
            comboDepartamento.addItem(departamento.getNombre());
        }
    }
}
