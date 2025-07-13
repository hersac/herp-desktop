package com.hersac.ui.views.terceros.forms;

import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;

import javax.swing.*;
import java.awt.*;

public class FiltrosTercerosForm extends JPanel {
    private JComboBox<String> comboEstado;
    private JComboBox<TipoPersonaEntity> comboTipo;
    private Runnable alCambiarFiltros;

    public FiltrosTercerosForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;
        comboTipo = new JComboBox<>();
        comboTipo.setPreferredSize(new Dimension(130, 28));
        comboTipo.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboTipo.addItem(new TipoPersonaEntity(null, "Todos"));
        for (com.hersac.ui.views.terceros.constantes.TiposTerceroEnum tipo : com.hersac.ui.views.terceros.constantes.TiposTerceroEnum.values()) {
            comboTipo.addItem(new TipoPersonaEntity(tipo.getId(), tipo.getNombre()));
        }
        comboTipo.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 0, "Tipo:", comboTipo);
        comboEstado = new JComboBox<>(new String[] {"Todos", "Activo", "Inactivo"});
        comboEstado.setPreferredSize(new Dimension(120, 28));
        comboEstado.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboEstado.addActionListener(e -> notificarCambio());
        agregarFiltro(panelFiltros, gbc, 1, "Estado:", comboEstado);
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

    public void setOnFiltrosCambiados(Runnable listener) {
        this.alCambiarFiltros = listener;
    }

    public String getEstadoSeleccionado() {
        return (String) comboEstado.getSelectedItem();
    }

    public TipoPersonaEntity getTipoSeleccionado() {
        return (TipoPersonaEntity) comboTipo.getSelectedItem();
    }
}
