package com.hersac.ui.views.comercial.inventario.contenidos.items;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltrosItemsForm;
import com.hersac.ui.views.comercial.inventario.listeners.ItemsListeners;
import com.hersac.ui.views.comercial.inventario.tablas.ItemsTable;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarItem;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionItems extends JPanel implements ItemsListeners {
    private final ItemsController controladorItems;
    private final ItemsTable tablaItems = new ItemsTable();
    private List<ItemEntity> listaItems;
    private JTextField campoBusqueda;
    private final DIContainer contenedorDI;
    private FiltrosItemsForm formularioFiltros;

    public GestionItems(DIContainer diContainer) {
        this.contenedorDI = diContainer;
        this.controladorItems = diContainer.getItemsController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Items");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosItemsForm();
        formularioFiltros.establecerAlCambiarFiltros(this::filtrarItems);
        JButton botonRegistrar = new JButton("Registrar Item");
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        FontIcon iconoPlus = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(iconoPlus);
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarItems();
            }
        });
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(1200, 40));
        panelBotones.setMaximumSize(new Dimension(1200, 40));
        panelBotones.add(campoBusqueda);
        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(botonRegistrar);
        JPanel panelBotonesExpansible = new JPanel();
        panelBotonesExpansible.setLayout(new BoxLayout(panelBotonesExpansible, BoxLayout.X_AXIS));
        panelBotonesExpansible.setOpaque(false);
        panelBotonesExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        panelBotonesExpansible.add(panelBotones);
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        JScrollPane panelScroll = new JScrollPane(tablaItems);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll.setAlignmentX(CENTER_ALIGNMENT);
        panelScroll.setPreferredSize(new Dimension(1200, 400));
        panelScroll.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formularioFiltros);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBotonesExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelScroll);
        add(Box.createVerticalGlue());
        listaItems = obtenerItems();
        tablaItems.setItems(listaItems);
        tablaItems.setActionListener(this);
        botonRegistrar.addActionListener(e -> {
            RegistrarItem modal = new RegistrarItem((JFrame) SwingUtilities.getWindowAncestor(this), contenedorDI, this);
            ItemEntity nuevoItem = modal.getItemRegistrado();
            if (nuevoItem != null) {
                controladorItems.crear(nuevoItem);
                listaItems = obtenerItems();
                tablaItems.setItems(listaItems);
            }
        });
    }

    private List<ItemEntity> obtenerItems() {
        return controladorItems.buscarTodos();
    }

    private void filtrarItems() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.obtenerEstadoSeleccionado();
        String categoria = formularioFiltros.obtenerCategoriaSeleccionada();
        String desde = formularioFiltros.obtenerFechaDesde();
        String hasta = formularioFiltros.obtenerFechaHasta();
        List<ItemEntity> filtrados = listaItems.stream()
                .filter(i -> {
                    String idStr = String.valueOf(i.getItemId());
                    String codigo = i.getCodigo() != null ? i.getCodigo().toLowerCase() : "";
                    String nombre = i.getNombre() != null ? i.getNombre().toLowerCase() : "";
                    String descripcion = i.getDescripcion() != null ? i.getDescripcion().toLowerCase() : "";
                    String categoriaStr = i.getCategoria() != null ? i.getCategoria().toLowerCase() : "";
                    boolean coincideTexto = texto.isEmpty()
                        || idStr.contains(texto)
                        || codigo.contains(texto)
                        || nombre.contains(texto)
                        || descripcion.contains(texto)
                        || categoriaStr.contains(texto);
                    boolean coincideEstado = estado.equals("Todos") || (estado.equals("Activos") && i.isEstaActivo()) || (estado.equals("Inactivos") && !i.isEstaActivo());
                    boolean coincideCategoria = categoria.equals("Todas") || categoriaStr.equalsIgnoreCase(categoria.toLowerCase());
                    boolean coincideFecha = true;
                    if (desde != null && hasta != null && i.getFechaCreacion() != null) {
                        java.time.LocalDate fecha = i.getFechaCreacion().toLocalDate();
                        coincideFecha = (fecha.compareTo(java.time.LocalDate.parse(desde)) >= 0 && fecha.compareTo(java.time.LocalDate.parse(hasta)) <= 0);
                    }
                    if (desde != null && i.getFechaCreacion() != null && hasta == null) {
                        java.time.LocalDate fecha = i.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(java.time.LocalDate.parse(desde)) >= 0;
                    }
                    if (hasta != null && i.getFechaCreacion() != null && desde == null) {
                        java.time.LocalDate fecha = i.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(java.time.LocalDate.parse(hasta)) <= 0;
                    }
                    return coincideTexto && coincideEstado && coincideCategoria && coincideFecha;
                })
                .toList();
        tablaItems.setItems(filtrados);
    }

    @Override
    public void crearItems(ItemEntity item) {
        controladorItems.crear(item);
        listaItems = obtenerItems();
        tablaItems.setItems(listaItems);
    }

    @Override
    public void actualizarItems(ItemEntity item) {
        controladorItems.actualizar(item.getItemId(), item);
        listaItems = obtenerItems();
        tablaItems.setItems(listaItems);
    }

    @Override
    public void eliminarItems(ItemEntity item) {
        controladorItems.eliminar(item.getItemId());
        listaItems = obtenerItems();
        tablaItems.setItems(listaItems);
    }

    @Override
    public void verItems(ItemEntity item) {
        RegistrarItem modal = new RegistrarItem((JFrame) SwingUtilities.getWindowAncestor(this), contenedorDI, this, item, true);
    }
}
