package com.hersac.ui.views.comercial.inventario.contenidos.proveedores;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.ui.controllers.proveedores.ProveedoresController;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltroProveedoresForm;
import com.hersac.ui.views.comercial.inventario.listeners.ProveedorListeners;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarProveedor;
import com.hersac.ui.views.comercial.inventario.tablas.ProveedoresTable;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionProveedores extends JPanel implements ProveedorListeners {
    private final ProveedoresController controladorProveedores;
    private final TercerosController controladorTerceros;
    private final ProveedoresTable tablaProveedores;
    private List<ProveedorEntity> listaProveedores;
    private FiltroProveedoresForm filtros;
    private JTextField campoBusqueda;

    public GestionProveedores(DIContainer contenedorDI) {
        this.controladorProveedores = contenedorDI.getProveedoresController();
        this.controladorTerceros = contenedorDI.getTercerosController();
        this.tablaProveedores = new ProveedoresTable();
        this.tablaProveedores.setActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Proveedores");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        filtros = new FiltroProveedoresForm();
        filtros.establecerAlCambiarFiltros(this::filtrarProveedores);
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarProveedores();
            }
        });
        JButton botonRegistrar = new JButton("Registrar Proveedor");
        FontIcon icono = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(icono);
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.addActionListener(e -> mostrarRegistrarProveedor());
        add(etiquetaTitulo);
        add(Box.createVerticalStrut(10));
        add(filtros);
        add(Box.createVerticalStrut(10));
        JPanel panelBusquedaBoton = new JPanel();
        panelBusquedaBoton.setLayout(new BoxLayout(panelBusquedaBoton, BoxLayout.X_AXIS));
        panelBusquedaBoton.setOpaque(false);
        panelBusquedaBoton.setMaximumSize(new Dimension(1200, 40));
        panelBusquedaBoton.setPreferredSize(new Dimension(1200, 40));
        panelBusquedaBoton.add(campoBusqueda);
        panelBusquedaBoton.add(Box.createHorizontalGlue());
        panelBusquedaBoton.add(botonRegistrar);
        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(panelBusquedaBoton);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        cargarProveedores();
    }

    private void mostrarRegistrarProveedor() {
        RegistrarProveedor dialogo = new RegistrarProveedor(null, this, null, controladorTerceros);
        dialogo.setVisible(true);
    }

    private void cargarProveedores() {
        listaProveedores = controladorProveedores.buscarTodos();
        filtrarProveedores();
    }

    private void filtrarProveedores() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = filtros.obtenerEstadoSeleccionado();
        List<ProveedorEntity> filtrados = listaProveedores.stream()
            .filter(p -> {
                String nombre = p.getTercero() != null && p.getTercero().getNombre() != null ? p.getTercero().getNombre().toLowerCase() : "";
                String id = p.getProveedorId() != null ? p.getProveedorId().toString() : "";
                boolean coincideTexto = texto.isEmpty() || nombre.contains(texto) || id.contains(texto);
                boolean coincideEstado = estado.equals("Todos")
                    || (estado.equals("Activo") && p.isEsta_activo())
                    || (estado.equals("Inactivo") && !p.isEsta_activo());
                return coincideTexto && coincideEstado;
            })
            .toList();
        tablaProveedores.setProveedores(filtrados);
    }

    @Override
    public void crearProveedor(ProveedorEntity proveedor) {
        controladorProveedores.crear(proveedor);
        cargarProveedores();
    }
    @Override
    public void verProveedor(ProveedorEntity proveedor) {
        RegistrarProveedor dialogo = new RegistrarProveedor(null, this, proveedor, controladorTerceros);
        dialogo.setVisible(true);
    }

    @Override
    public void actualizarProveedor(ProveedorEntity proveedor) {
        controladorProveedores.actualizar(proveedor.getProveedorId(), proveedor);
        cargarProveedores();
        JOptionPane.showMessageDialog(this,
                "Proveedor actualizado exitosamente.",
                "Proveedor Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarProveedor(ProveedorEntity proveedor) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al proveedor?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorProveedores.eliminar(proveedor.getProveedorId());
            cargarProveedores();
            JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
