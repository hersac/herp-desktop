package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.ui.views.comercial.inventario.listeners.ProveedorListeners;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.controllers.terceros.TercerosController;

import javax.swing.*;
import java.awt.*;

public class RegistrarProveedor extends JDialog {
    private JTextField nombreField;
    private JCheckBox activoCheckBox;
    private ProveedorEntity proveedorRegistrado;
    private final ProveedorListeners listener;
    private final boolean esEdicion;
    private JTextField terceroIdField;
    private JTextField terceroNombreField;
    private TercerosController tercerosController;

    public RegistrarProveedor(JFrame parent, ProveedorListeners listener, ProveedorEntity proveedorParaEditar, TercerosController tercerosController) {
        super(parent, proveedorParaEditar != null ? "Actualizar Proveedor" : "Registrar Proveedor", true);
        this.listener = listener;
        this.proveedorRegistrado = proveedorParaEditar;
        this.esEdicion = proveedorParaEditar != null;
        this.tercerosController = tercerosController;
        initComponents();
    }

    private void initComponents() {
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(420, 340));
        setResizable(false);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setPreferredSize(new Dimension(360, 180));
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        terceroIdField = new JTextField();
        terceroNombreField = new JTextField();
        terceroNombreField.setEditable(false);
        nombreField = new JTextField();
        activoCheckBox = new JCheckBox("Activo");
        if (esEdicion && proveedorRegistrado != null) {
            if (proveedorRegistrado.getTercero() != null) {
                terceroIdField.setText(proveedorRegistrado.getTercero().getTerceroId());
                terceroNombreField.setText(proveedorRegistrado.getTercero().getNombre());
                nombreField.setText(proveedorRegistrado.getTercero().getNombre());
            }
            activoCheckBox.setSelected(proveedorRegistrado.isEsta_activo());
        }
        terceroIdField.addActionListener(e -> buscarTerceroPorId());
        formPanel.add(new JLabel("ID Tercero:"));
        formPanel.add(terceroIdField);
        formPanel.add(new JLabel("Nombre Tercero:"));
        formPanel.add(terceroNombreField);
        formPanel.add(new JLabel("Nombre Proveedor:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Activo:"));
        formPanel.add(activoCheckBox);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Registrar");
        guardarBtn.setPreferredSize(new Dimension(120, 36));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(guardarBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        guardarBtn.addActionListener(e -> guardarProveedor());
    }

    private void buscarTerceroPorId() {
        String terceroId = terceroIdField.getText();
        TerceroEntity tercero = tercerosController.buscarPorId(terceroId);
        if (tercero != null) {
            nombreField.setText(tercero.getNombre());
            terceroNombreField.setText(tercero.getNombre());
        } else {
            nombreField.setText("");
            terceroNombreField.setText("");
            JOptionPane.showMessageDialog(this, "Tercero no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarProveedor() {
        ProveedorEntity nuevoProveedor = new ProveedorEntity();
        String terceroId = terceroIdField.getText();
        TerceroEntity tercero = tercerosController.buscarPorId(terceroId);
        nuevoProveedor.setTercero(tercero);
        nuevoProveedor.setEsta_activo(activoCheckBox.isSelected());
        if (listener != null) {
            listener.crearProveedor(nuevoProveedor);
        }
        dispose();
    }
}
