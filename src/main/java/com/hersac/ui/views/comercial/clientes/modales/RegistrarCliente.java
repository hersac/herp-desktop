package com.hersac.ui.views.comercial.clientes.modales;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.views.comercial.clientes.constantes.TiposClienteEnum;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;
import com.hersac.core.globals.servicios.PermissionService;

import javax.swing.*;
import java.awt.*;

public class RegistrarCliente extends JDialog {
    private JTextField idField;
    private JTextField nombreField;
    private JComboBox<TiposClienteEnum> tipoComboBox;
    private JCheckBox activoCheckBox;
    private ClienteEntity clienteRegistrado;
    private final ClientesListeners listener;
    private final boolean esEdicion;
    private final PermissionService permissionService;
    private static final long PERMISO_INACTIVAR = 55L;

    public RegistrarCliente(JFrame parent, ClientesListeners listener, ClienteEntity clienteParaEditar, PermissionService permissionService) {
        super(parent, clienteParaEditar != null ? "Actualizar Cliente" : "Registrar Cliente", true);
        this.listener = listener;
        this.clienteRegistrado = clienteParaEditar;
        this.esEdicion = clienteParaEditar != null;
        this.permissionService = permissionService;
        initComponents();
    }

    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        idField = new JTextField();
        nombreField = new JTextField();
        tipoComboBox = new JComboBox<>(TiposClienteEnum.values());
        activoCheckBox = new JCheckBox("Activo");
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(tipoComboBox);
        formPanel.add(new JLabel("Activo:"));
        formPanel.add(activoCheckBox);
        add(formPanel, BorderLayout.CENTER);
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Registrar");
        guardarBtn.addActionListener(e -> guardarCliente());
        add(guardarBtn, BorderLayout.SOUTH);
    }

    private void guardarCliente() {
        // Aquí puedes implementar la lógica para guardar o actualizar el cliente
        dispose();
    }
}

