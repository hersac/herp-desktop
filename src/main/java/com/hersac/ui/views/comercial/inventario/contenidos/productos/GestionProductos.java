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
    private JFrame ventana;
    private final ProductosController controladorProductos;
    private final ProductosTable tablaProductos;
    private List<ProductoEntity> listaProductos;
    private FiltrosProductosForm filtros;
    private JTextField campoBusqueda;

    public GestionProductos(DIContainer contenedorDI) {
        this.ventana = new JFrame("Registrar Producto");
        this.controladorProductos = contenedorDI.getProductosController();
        this.tablaProductos = new ProductosTable();
        this.tablaProductos.setActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Productos");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        filtros = new FiltrosProductosForm();
        JButton botonRegistrar = new JButton("Registrar Producto");
        FontIcon icono = FontIcon.of(FontAwesomeSolid.PLUS, 18, Color.WHITE);
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setIcon(icono);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setBorderPainted(false);
        botonRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonRegistrar.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarProductos();
            }
        });
        filtros.establecerAlCambiarFiltros(this::filtrarProductos);
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new BoxLayout(panelBoton, BoxLayout.X_AXIS));
        panelBoton.setOpaque(false);
        panelBoton.setPreferredSize(new Dimension(1200, 40));
        panelBoton.setMaximumSize(new Dimension(1200, 40));
        panelBoton.add(campoBusqueda);
        panelBoton.add(Box.createHorizontalGlue());
        panelBoton.add(botonRegistrar);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtros);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(panelBoton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());
        listaProductos = obtenerProductos();
        tablaProductos.setProductos(listaProductos);
        actualizarUnidadesFiltro();
        botonRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistrarProducto(ventana, GestionProductos.this, null);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                botonRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    @Override
    public void crearProducto(ProductoEntity producto) {
        controladorProductos.crear(producto);
        actualizarTabla();
        actualizarUnidadesFiltro();
        JOptionPane.showMessageDialog(this,
                "Producto creado exitosamente:\n\nNombre: " + producto.getNombre() + "\nCódigo: " + producto.getCodigo(),
                "Producto Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verProducto(ProductoEntity producto) {
        new RegistrarProducto(ventana, this, producto);
    }

    @Override
    public void actualizarProducto(ProductoEntity producto) {
        controladorProductos.actualizar(producto.getProductoId(), producto);
        actualizarTabla();
        actualizarUnidadesFiltro();
        JOptionPane.showMessageDialog(this,
                "Producto actualizado exitosamente:\n\nNombre: " + producto.getNombre() + "\nCódigo: " + producto.getCodigo(),
                "Producto Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarProducto(ProductoEntity producto) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el producto " + producto.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorProductos.eliminar(producto.getProductoId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTabla() {
        listaProductos = obtenerProductos();
        filtrarProductos();
    }

    private void actualizarUnidadesFiltro() {
        List<String> unidades = listaProductos.stream()
            .map(ProductoEntity::getUnidadMedida)
            .filter(u -> u != null && !u.isEmpty())
            .distinct()
            .sorted()
            .toList();
        filtros.establecerUnidades(unidades);
    }

    private void filtrarProductos() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = filtros.obtenerEstadoSeleccionado();
        String unidadSeleccionada = filtros.obtenerUnidadSeleccionada() != null ? filtros.obtenerUnidadSeleccionada() : "Todas";
        List<ProductoEntity> filtrados = listaProductos.stream()
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
        tablaProductos.setProductos(filtrados);
    }

    private List<ProductoEntity> obtenerProductos() {
        return controladorProductos.buscarTodos();
    }
}
