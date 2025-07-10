package com.hersac.ui.views.terceros.forms;

import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;

import javax.swing.*;
import java.awt.*;

public class FiltrosTercerosForm extends JPanel {
    private JComboBox<String> estadoCombo;
    private JComboBox<TipoPersonaEntity> tipoCombo;
    private Runnable onFiltrosCambiados;

    public FiltrosTercerosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        tipoCombo = new JComboBox<>();
        tipoCombo.setPreferredSize(new Dimension(130, 28));
        tipoCombo.addItem(new TipoPersonaEntity(null, "Todos"));
        for (com.hersac.ui.views.terceros.constantes.TiposTerceroEnum tipo : com.hersac.ui.views.terceros.constantes.TiposTerceroEnum.values()) {
            tipoCombo.addItem(new TipoPersonaEntity(tipo.getId(), tipo.getNombre()));
        }
        tipoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 0, "Tipo:", tipoCombo);
        estadoCombo = new JComboBox<>(new String[] {"Todos", "Activo", "Inactivo"});
        estadoCombo.setPreferredSize(new Dimension(120, 28));
        estadoCombo.addActionListener(e -> notificarCambio());
        addFiltro(filtrosPanel, gbc, 1, "Estado:", estadoCombo);
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

    public void setOnFiltrosCambiados(Runnable listener) {
        this.onFiltrosCambiados = listener;
    }

    public String getEstadoSeleccionado() {
        return (String) estadoCombo.getSelectedItem();
    }

    public TipoPersonaEntity getTipoSeleccionado() {
        return (TipoPersonaEntity) tipoCombo.getSelectedItem();
    }
}
