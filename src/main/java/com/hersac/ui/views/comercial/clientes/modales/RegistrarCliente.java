package com.hersac.ui.views.comercial.clientes.modales;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.controllers.terceros.TercerosController;

import javax.swing.*;
import java.awt.*;

public class RegistrarCliente extends JDialog {
    private JTextField nombreField;
    private JCheckBox activoCheckBox;
    private ClienteEntity clienteRegistrado;
    private final ClientesListeners listener;
    private final boolean esEdicion;
    private final PermissionService permissionService;
    private JTextField terceroIdField;
    private JTextField terceroNombreField;
    private TercerosController tercerosController;

    public RegistrarCliente(JFrame parent, ClientesListeners listener, ClienteEntity clienteParaEditar, PermissionService permissionService, TercerosController tercerosController) {
        super(parent, clienteParaEditar != null ? "Actualizar Cliente" : "Registrar Cliente", true);
        this.listener = listener;
        this.clienteRegistrado = clienteParaEditar;
        this.esEdicion = clienteParaEditar != null;
        this.permissionService = permissionService;
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
        if (esEdicion && clienteRegistrado != null) {
            if (clienteRegistrado.getTercero() != null) {
                terceroIdField.setText(clienteRegistrado.getTercero().getTerceroId());
                terceroNombreField.setText(clienteRegistrado.getTercero().getNombre());
                nombreField.setText(clienteRegistrado.getTercero().getNombre());
            }
            activoCheckBox.setSelected(clienteRegistrado.isEsta_activo());
        }
        terceroIdField.addActionListener(e -> buscarTerceroPorId());
        formPanel.add(new JLabel("ID Tercero:"));
        formPanel.add(terceroIdField);
        formPanel.add(new JLabel("Nombre Tercero:"));
        formPanel.add(terceroNombreField);
        formPanel.add(new JLabel("Nombre Cliente:"));
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
        guardarBtn.addActionListener(e -> guardarCliente());
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

    private void guardarCliente() {
        ClienteEntity nuevoCliente = new ClienteEntity();
        String terceroId = terceroIdField.getText();
        TerceroEntity tercero = tercerosController.buscarPorId(terceroId);
        nuevoCliente.setTercero(tercero);
        nuevoCliente.setEsta_activo(activoCheckBox.isSelected());
        if (listener != null) {
            listener.crearCliente(nuevoCliente);
        }
        dispose();
    }
}
