package com.hersac.ui.views.usuarios.gestion.forms;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
    private JComboBox<String> estadoCombo;
    private JComboBox<String> departamentoCombo;
    private DatePicker fechaDesdePicker;
    private DatePicker fechaHastaPicker;
    private Runnable onFiltrosCambiados;

    public FiltrosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        departamentoCombo = new JComboBox<>();
        departamentoCombo.setPreferredSize(new Dimension(130, 28));
        departamentoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 0, "Departamento:", departamentoCombo);
        estadoCombo = new JComboBox<>(new String[] {
                "Todos", "Activos", "Inactivos", "Bloqueados", "Suspendidos"
        });
        estadoCombo.setPreferredSize(new Dimension(120, 28));
        estadoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 1, "Estado:", estadoCombo);
        DatePickerSettings desdeSettings = createDateSettings();
        fechaDesdePicker = new DatePicker(desdeSettings);
        fechaDesdePicker.setPreferredSize(new Dimension(120, 28));
        fechaDesdePicker.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        fechaDesdePicker.addDateChangeListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 2, "Desde:", fechaDesdePicker);
        DatePickerSettings hastaSettings = createDateSettings();
        fechaHastaPicker = new DatePicker(hastaSettings);
        fechaHastaPicker.setPreferredSize(new Dimension(120, 28));
        fechaHastaPicker.getSettings().setDateRangeLimits(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
        fechaHastaPicker.addDateChangeListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 3, "Hasta:", fechaHastaPicker);
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
        DatePickerSettings settings = new DatePickerSettings(new Locale("es"));
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

    public String getEstadoSeleccionado() {
        return (String) estadoCombo.getSelectedItem();
    }

    public String getDepartamentoSeleccionado() {
        return (String) departamentoCombo.getSelectedItem();
    }

    public String getFechaDesde() {
        LocalDate date = fechaDesdePicker.getDate();
        return (date != null) ? date.toString() : null;
    }

    public String getFechaHasta() {
        LocalDate date = fechaHastaPicker.getDate();
        return (date != null) ? date.toString() : null;
    }

    public void setDepartamentos(List<DepartamentoEntity> departamentos) {
        departamentoCombo.removeAllItems();
        departamentoCombo.addItem("Todos");
        for (DepartamentoEntity dep : departamentos) {
            departamentoCombo.addItem(dep.getNombre());
        }
    }
}
