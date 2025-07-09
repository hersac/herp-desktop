package com.hersac.ui.views.terceros.modales;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;

import javax.swing.*;
import java.awt.*;

public class RegistrarTercero extends JDialog {
    private JTextField nombreField;
    private JTextField tipoField;
    private JCheckBox activoCheckBox;
    private TerceroEntity terceroRegistrado;
    private final TercerosListeners listener;
    private final boolean esEdicion;

    public RegistrarTercero(JFrame parent, TercerosListeners listener, TerceroEntity terceroParaEditar) {
        super(parent, terceroParaEditar != null ? "Actualizar Tercero" : "Registrar Tercero", true);
        this.listener = listener;
        this.terceroRegistrado = terceroParaEditar;
        this.esEdicion = terceroParaEditar != null;
        initComponents();
    }

    private void initComponents() {
        setSize(400, 250);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        nombreField = new JTextField();
        tipoField = new JTextField();
        activoCheckBox = new JCheckBox("Activo", true);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(tipoField);
        formPanel.add(new JLabel("¿Está activo?:"));
        formPanel.add(activoCheckBox);
        if (esEdicion && terceroRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.addActionListener(e -> guardarTercero());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void cargarDatosEdicion() {
        nombreField.setText(terceroRegistrado.getNombre());
        tipoField.setText(terceroRegistrado.getTipo());
        activoCheckBox.setSelected("Activo".equalsIgnoreCase(terceroRegistrado.getEstado()));
    }

    private void guardarTercero() {
        if (terceroRegistrado == null) {
            terceroRegistrado = new TerceroEntity();
        }
        terceroRegistrado.setNombre(nombreField.getText());
        terceroRegistrado.setTipo(tipoField.getText());
        terceroRegistrado.setEstado(activoCheckBox.isSelected() ? "Activo" : "Inactivo");
        if (!esEdicion) {
            listener.crearTercero(terceroRegistrado);
        } else {
            listener.actualizarTercero(terceroRegistrado);
        }
        dispose();
    }

    public TerceroEntity getTerceroRegistrado() {
        return terceroRegistrado;
    }
}
