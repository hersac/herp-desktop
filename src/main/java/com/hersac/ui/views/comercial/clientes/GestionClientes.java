package com.hersac.ui.views.comercial.clientes;

import com.hersac.core.di.DIContainer;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.controllers.clientes.ClientesController;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.clientes.forms.FiltrosClientesForm;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;
import com.hersac.ui.views.comercial.clientes.modales.RegistrarCliente;
import com.hersac.ui.views.comercial.clientes.tablas.ClientesTable;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionClientes extends JPanel implements ClientesListeners {
    private final PermissionService permissionService;
    private final ClientesController clientesController;
    private final TercerosController tercerosController;
    private final ClientesTable tablaClientesTable;
    private List<ClienteEntity> listaCompletaClientes;
    private FiltrosClientesForm filtrosForm;
    private JTextField searchField;

    public GestionClientes(DIContainer diContainer) {
        this.permissionService = diContainer.getPermissionService();
        this.clientesController = diContainer.getClientesController();
        this.tercerosController = diContainer.getTercerosController();
        this.tablaClientesTable = new ClientesTable(permissionService);
        this.tablaClientesTable.setActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosForm = new FiltrosClientesForm();
        filtrosForm.setOnFiltrosCambiados(this::filtrarClientes);
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarClientes();
            }
        });
        JButton registrarBtn = new JButton("Registrar Cliente");
        FontIcon icono = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(icono);
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.addActionListener(e -> mostrarRegistrarCliente());
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(filtrosForm);
        add(Box.createVerticalStrut(10));
        JPanel panelBtnTabla = new JPanel();
        panelBtnTabla.setLayout(new BoxLayout(panelBtnTabla, BoxLayout.X_AXIS));
        panelBtnTabla.setOpaque(false);
        panelBtnTabla.setMaximumSize(new Dimension(900, 40));
        panelBtnTabla.setPreferredSize(new Dimension(900, 40));
        panelBtnTabla.add(searchField);
        panelBtnTabla.add(Box.createHorizontalGlue());
        panelBtnTabla.add(registrarBtn);
        JScrollPane scrollPane = new JScrollPane(tablaClientesTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(panelBtnTabla);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        cargarClientes();
    }

    private void mostrarRegistrarCliente() {
        RegistrarCliente dialog = new RegistrarCliente(null, this, null, permissionService, tercerosController);
        dialog.setVisible(true);
    }

    private void cargarClientes() {
        listaCompletaClientes = clientesController.buscarTodos();
        filtrarClientes();
    }

    private void filtrarClientes() {
        String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        String estado = filtrosForm.getEstadoSeleccionado();
        List<ClienteEntity> filtrados = listaCompletaClientes.stream()
            .filter(c -> {
                String nombre = c.getTercero() != null && c.getTercero().getNombre() != null ? c.getTercero().getNombre().toLowerCase() : "";
                String id = c.getClienteId() != null ? c.getClienteId().toString() : "";
                boolean coincideTexto = texto.isEmpty() || nombre.contains(texto) || id.contains(texto);
                boolean coincideEstado = estado.equals("Todos")
                    || (estado.equals("Activo") && c.isEsta_activo())
                    || (estado.equals("Inactivo") && !c.isEsta_activo());
                return coincideTexto && coincideEstado;
            })
            .toList();
        tablaClientesTable.setClientes(filtrados);
    }

    @Override
    public void crearCliente(ClienteEntity cliente) {
        clientesController.crear(cliente);
        cargarClientes();
    }
    @Override
    public void verCliente(ClienteEntity cliente) {
        RegistrarCliente dialog = new RegistrarCliente(null, this, cliente, permissionService, tercerosController);
        dialog.setVisible(true);
    }

    @Override
    public void actualizarCliente(ClienteEntity cliente) {
        clientesController.actualizar(cliente.getClienteId(), cliente);
        cargarClientes();
        JOptionPane.showMessageDialog(this,
                "Cliente actualizado exitosamente.",
                "Cliente Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarCliente(ClienteEntity cliente) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al cliente?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clientesController.eliminar(cliente.getClienteId());
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
