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
    private final PermissionService servicioPermisos;
    private final ClientesController controladorClientes;
    private final TercerosController controladorTerceros;
    private final ClientesTable tablaClientes;
    private List<ClienteEntity> listaClientes;
    private FiltrosClientesForm formularioFiltros;
    private JTextField campoBusqueda;

    public GestionClientes(DIContainer diContainer) {
        this.servicioPermisos = diContainer.getPermissionService();
        this.controladorClientes = diContainer.getClientesController();
        this.controladorTerceros = diContainer.getTercerosController();
        this.tablaClientes = new ClientesTable(servicioPermisos);
        this.tablaClientes.setActionListener(this);
        inicializarInterfaz();
        cargarClientes();
    }

    private void inicializarInterfaz() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Clientes");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosClientesForm();
        formularioFiltros.establecerAlCambiarFiltros(this::filtrarClientes);
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarClientes();
            }
        });
        JButton botonRegistrar = new JButton("Registrar Cliente");
        FontIcon icono = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(icono);
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.addActionListener(e -> mostrarRegistrarCliente());
        add(etiquetaTitulo);
        add(Box.createVerticalStrut(10));
        add(formularioFiltros);
        add(Box.createVerticalStrut(10));
        JPanel panelBotonTabla = new JPanel();
        panelBotonTabla.setLayout(new BoxLayout(panelBotonTabla, BoxLayout.X_AXIS));
        panelBotonTabla.setOpaque(false);
        panelBotonTabla.setMaximumSize(new Dimension(900, 40));
        panelBotonTabla.setPreferredSize(new Dimension(900, 40));
        panelBotonTabla.add(campoBusqueda);
        panelBotonTabla.add(Box.createHorizontalGlue());
        panelBotonTabla.add(botonRegistrar);
        JScrollPane panelScroll = new JScrollPane(tablaClientes);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll.setPreferredSize(new Dimension(900, 400));
        panelScroll.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(panelBotonTabla);
        add(Box.createVerticalStrut(10));
        add(panelScroll);
    }

    private void mostrarRegistrarCliente() {
        RegistrarCliente dialogo = new RegistrarCliente(null, this, null, servicioPermisos, controladorTerceros);
        dialogo.setVisible(true);
    }

    private void cargarClientes() {
        listaClientes = controladorClientes.buscarTodos();
        filtrarClientes();
    }

    private void filtrarClientes() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.obtenerEstadoSeleccionado();
        List<ClienteEntity> filtrados = listaClientes.stream()
            .filter(cliente -> {
                String nombre = cliente.getTercero() != null && cliente.getTercero().getNombre() != null ? cliente.getTercero().getNombre().toLowerCase() : "";
                String id = cliente.getClienteId() != null ? cliente.getClienteId().toString() : "";
                boolean coincideTexto = texto.isEmpty() || nombre.contains(texto) || id.contains(texto);
                boolean activo = cliente.isEsta_activo();
                if (estado.equals("Todos")) return coincideTexto;
                if (estado.equals("Activo") && activo) return coincideTexto;
                if (estado.equals("Inactivo") && !activo) return coincideTexto;
                return false;
            })
            .toList();
        tablaClientes.setClientes(filtrados);
    }

    @Override
    public void crearCliente(ClienteEntity cliente) {
        controladorClientes.crear(cliente);
        cargarClientes();
    }

    @Override
    public void verCliente(ClienteEntity cliente) {
        RegistrarCliente dialogo = new RegistrarCliente(null, this, cliente, servicioPermisos, controladorTerceros);
        dialogo.setVisible(true);
    }

    @Override
    public void actualizarCliente(ClienteEntity cliente) {
        controladorClientes.actualizar(cliente.getClienteId(), cliente);
        cargarClientes();
        JOptionPane.showMessageDialog(this,
                "Cliente actualizado exitosamente.",
                "Cliente Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarCliente(ClienteEntity cliente) {
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al cliente?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            controladorClientes.eliminar(cliente.getClienteId());
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
