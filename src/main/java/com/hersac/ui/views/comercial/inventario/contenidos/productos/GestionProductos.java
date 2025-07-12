package com.hersac.ui.views.comercial.inventario.contenidos.productos;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.ui.controllers.productos.ProductosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltrosProductosForm;
import com.hersac.ui.views.comercial.inventario.listeners.ProductosListeners;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarProducto;
import com.hersac.ui.views.comercial.inventario.tablas.ProductosTable;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class GestionProductos extends JPanel implements ProductosListeners {
    private JFrame frame = new JFrame("Registrar Producto");
    private final ProductosController productosController;
    private final ProductosTable tablaProductosTable = new ProductosTable();
    private List<ProductoEntity> listaCompletaProductos;
    private FiltrosProductosForm filtrosForm;
    private JTextField searchField;

    public GestionProductos(DIContainer diContainer) {
        this.productosController = diContainer.getProductosController();
        this.tablaProductosTable.setActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        JLabel titleLabel = new JLabel("Gestión de Productos");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        filtrosForm = new FiltrosProductosForm();

        JButton registrarBtn = new JButton("Registrar Producto");
        FontIcon iconRegistrar = FontIcon.of(FontAwesomeSolid.PLUS, 18, Color.WHITE);
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.setIcon(iconRegistrar);
        registrarBtn.setFocusPainted(false);
        registrarBtn.setBorderPainted(false);
        registrarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrarBtn.setAlignmentX(CENTER_ALIGNMENT);

        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarProductos();
            }
        });
        filtrosForm.setOnFiltrosCambiados(this::filtrarProductos);

        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.setOpaque(false);
        panelBtn.setPreferredSize(new Dimension(1200, 40));
        panelBtn.setMaximumSize(new Dimension(1200, 40));
        panelBtn.add(searchField);
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(registrarBtn);

        JPanel panelBtnExpansible = new JPanel();
        panelBtnExpansible.setLayout(new BoxLayout(panelBtnExpansible, BoxLayout.X_AXIS));
        panelBtnExpansible.setOpaque(false);
        panelBtnExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.add(Box.createHorizontalGlue());
        panelBtnExpansible.add(panelBtn);
        panelBtnExpansible.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(tablaProductosTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosForm);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());

        List<ProductoEntity> listaProductos = obtenerProductos();
        this.listaCompletaProductos = listaProductos;
        tablaProductosTable.setProductos(listaProductos);
        actualizarUnidadesFiltro();

        registrarBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistrarProducto(frame, GestionProductos.this, null);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                registrarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                registrarBtn.setBackground(new Color(25, 118, 210));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registrarBtn.setBackground(new Color(33, 150, 243));
            }
        });
    }

    @Override
    public void crearProducto(ProductoEntity producto) {
        productosController.crear(producto);
        actualizarTabla();
        actualizarUnidadesFiltro();
        JOptionPane.showMessageDialog(this,
                "Producto creado exitosamente:\n\nNombre: " + producto.getNombre() + "\nCódigo: " + producto.getCodigo(),
                "Producto Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verProducto(ProductoEntity producto) {
        new RegistrarProducto(frame, this, producto);
    }

    @Override
    public void actualizarProducto(ProductoEntity producto) {
        productosController.actualizar(producto.getProductoId(), producto);
        actualizarTabla();
        actualizarUnidadesFiltro();
        JOptionPane.showMessageDialog(this,
                "Producto actualizado exitosamente:\n\nNombre: " + producto.getNombre() + "\nCódigo: " + producto.getCodigo(),
                "Producto Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarProducto(ProductoEntity producto) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el producto " + producto.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productosController.eliminar(producto.getProductoId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTabla() {
        this.listaCompletaProductos = obtenerProductos();
        filtrarProductos();
    }

    private void actualizarUnidadesFiltro() {
        List<String> unidades = listaCompletaProductos.stream()
            .map(ProductoEntity::getUnidadMedida)
            .filter(u -> u != null && !u.isEmpty())
            .distinct()
            .sorted()
            .toList();
        filtrosForm.setUnidades(unidades);
    }

    private void filtrarProductos() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String estado = filtrosForm.getEstadoSeleccionado();
        final String unidadSeleccionada = filtrosForm.getUnidadSeleccionada() != null ? filtrosForm.getUnidadSeleccionada() : "Todas";
        List<ProductoEntity> filtrados = listaCompletaProductos.stream()
                .filter(p -> {
                    String productoIdStr = String.valueOf(p.getProductoId());
                    String nombre = p.getNombre() != null ? p.getNombre().toLowerCase() : "";
                    String codigo = p.getCodigo() != null ? p.getCodigo().toLowerCase() : "";
                    String descripcion = p.getDescripcion() != null ? p.getDescripcion().toLowerCase() : "";
                    String estadoProducto = p.isEstadoActivo() ? "activo" : "inactivo";
                    String unidadMedida = p.getUnidadMedida() != null ? p.getUnidadMedida().toLowerCase() : "";
                    boolean coincideTexto = texto.isEmpty()
                        || productoIdStr.contains(texto)
                        || nombre.contains(texto)
                        || codigo.contains(texto)
                        || descripcion.contains(texto)
                        || estadoProducto.contains(texto)
                        || unidadMedida.contains(texto);
                    boolean coincideEstado = estado.equals("Todos")
                            || (estado.equals("Activos") && p.isEstadoActivo())
                            || (estado.equals("Inactivos") && !p.isEstadoActivo());
                    boolean coincideUnidad = unidadSeleccionada.equals("Todas") || unidadSeleccionada.equalsIgnoreCase(unidadMedida);
                    return coincideTexto && coincideEstado && coincideUnidad;
                })
                .collect(Collectors.toList());

        tablaProductosTable.setProductos(filtrados);
    }

    private List<ProductoEntity> obtenerProductos() {
        return productosController.buscarTodos();
    }
}
