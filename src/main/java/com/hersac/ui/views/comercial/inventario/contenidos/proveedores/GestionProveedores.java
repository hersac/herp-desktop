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
    private final ProveedoresController proveedoresController;
    private final TercerosController tercerosController;
    private final ProveedoresTable tablaProveedoresTable;
    private List<ProveedorEntity> listaCompletaProveedores;
    private FiltroProveedoresForm filtrosForm;
    private JTextField searchField;

    public GestionProveedores(DIContainer diContainer) {
        this.proveedoresController = diContainer.getProveedoresController();
        this.tercerosController = diContainer.getTercerosController();
        this.tablaProveedoresTable = new ProveedoresTable();
        this.tablaProveedoresTable.setActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Proveedores");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosForm = new FiltroProveedoresForm();
        filtrosForm.setOnFiltrosCambiados(this::filtrarProveedores);
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarProveedores();
            }
        });
        JButton registrarBtn = new JButton("Registrar Proveedor");
        FontIcon icono = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(icono);
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.addActionListener(e -> mostrarRegistrarProveedor());
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
        JScrollPane scrollPane = new JScrollPane(tablaProveedoresTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(panelBtnTabla);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        cargarProveedores();
    }

    private void mostrarRegistrarProveedor() {
        RegistrarProveedor dialog = new RegistrarProveedor(null, this, null, tercerosController);
        dialog.setVisible(true);
    }

    private void cargarProveedores() {
        listaCompletaProveedores = proveedoresController.buscarTodos();
        filtrarProveedores();
    }

    private void filtrarProveedores() {
        String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        String estado = filtrosForm.getEstadoSeleccionado();
        List<ProveedorEntity> filtrados = listaCompletaProveedores.stream()
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
        tablaProveedoresTable.setProveedores(filtrados);
    }

    @Override
    public void crearProveedor(ProveedorEntity proveedor) {
        proveedoresController.crear(proveedor);
        cargarProveedores();
    }
    @Override
    public void verProveedor(ProveedorEntity proveedor) {
        RegistrarProveedor dialog = new RegistrarProveedor(null, this, proveedor, tercerosController);
        dialog.setVisible(true);
    }

    @Override
    public void actualizarProveedor(ProveedorEntity proveedor) {
        proveedoresController.actualizar(proveedor.getProveedorId(), proveedor);
        cargarProveedores();
        JOptionPane.showMessageDialog(this,
                "Proveedor actualizado exitosamente.",
                "Proveedor Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarProveedor(ProveedorEntity proveedor) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al proveedor?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            proveedoresController.eliminar(proveedor.getProveedorId());
            cargarProveedores();
            JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
