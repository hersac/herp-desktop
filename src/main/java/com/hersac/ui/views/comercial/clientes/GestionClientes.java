package com.hersac.ui.views.comercial.clientes;

import com.hersac.core.di.DIContainer;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.views.comercial.clientes.forms.FiltrosClientesForm;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;
import com.hersac.ui.views.comercial.clientes.modales.RegistrarCliente;
import com.hersac.ui.views.comercial.clientes.tablas.ClientesTable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionClientes extends JPanel implements ClientesListeners {
    private final PermissionService permissionService;
    private final ClientesTable tablaClientesTable;
    private List<ClienteEntity> listaCompletaClientes;
    private FiltrosClientesForm filtrosForm;
    private JTextField searchField;

    public GestionClientes(DIContainer diContainer) {
        this.permissionService = diContainer.getPermissionService();
        this.tablaClientesTable = new ClientesTable(permissionService);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosForm = new FiltrosClientesForm();
        JButton registrarBtn = new JButton("Registrar Cliente");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(new Color(33, 150, 243));
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.addActionListener(e -> mostrarRegistrarCliente());
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(filtrosForm);
        add(Box.createVerticalStrut(10));
        add(registrarBtn);
        add(Box.createVerticalStrut(10));
        add(tablaClientesTable);
    }

    private void mostrarRegistrarCliente() {
        RegistrarCliente dialog = new RegistrarCliente(null, this, null, permissionService);
        dialog.setVisible(true);
    }

    @Override
    public void crearCliente(ClienteEntity cliente) {
        // Implementar lógica para crear cliente
    }
    @Override
    public void verCliente(ClienteEntity cliente) {
        // Implementar lógica para ver cliente
    }
    @Override
    public void actualizarCliente(ClienteEntity cliente) {
        // Implementar lógica para actualizar cliente
    }
    @Override
    public void eliminarCliente(ClienteEntity cliente) {
        // Implementar lógica para eliminar cliente
    }
}
