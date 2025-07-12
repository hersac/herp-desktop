package com.hersac.ui.views.comercial.inventario.contenidos.items;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltrosItemsForm;
import com.hersac.ui.views.comercial.inventario.listeners.ItemsListeners;
import com.hersac.ui.views.comercial.inventario.tablas.ItemsTable;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarItem;

public class GestionItems extends JPanel implements ItemsListeners {
    private JFrame frame = new JFrame("Registrar Item");
    private final ItemsController itemsController;
    private final ItemsTable tablaItemsTable = new ItemsTable();
    private List<ItemEntity> listaCompletaItems;
    private JTextField searchField;
    private final DIContainer diContainer;
    private FiltrosItemsForm filtrosItemsForm;

    public GestionItems(DIContainer diContainer) {
        this.diContainer = diContainer;
        this.itemsController = diContainer.getItemsController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Items");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosItemsForm = new FiltrosItemsForm();
        filtrosItemsForm.setOnFiltrosCambiados(this::filtrarItems);
        JButton registrarBtn = new JButton("Registrar Item");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        FontIcon iconVer = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(iconVer);
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarItems();
            }
        });
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
        JScrollPane scrollPane = new JScrollPane(tablaItemsTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosItemsForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());
        List<ItemEntity> listaItems = obtenerItems();
        this.listaCompletaItems = listaItems;
        tablaItemsTable.setItems(listaItems);
        registrarBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new RegistrarItem(parentFrame, diContainer, this);
        });
        tablaItemsTable.setActionListener(this);
        actualizarCategoriasFiltro();
    }

    private void ejecutarAccion(JButton panel, Runnable accion) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.setBackground(ColorsTheme.PRIMARY_LIGTH.get());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(ColorsTheme.PRIMARY.get());
            }
        });
    }

    private void filtrarItems() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String estado = filtrosItemsForm.getEstadoSeleccionado();
        final String categoria = filtrosItemsForm.getCategoriaSeleccionada();
        final String desde = filtrosItemsForm.getFechaDesde();
        final String hasta = filtrosItemsForm.getFechaHasta();
        List<ItemEntity> filtrados = listaCompletaItems.stream()
                .filter(i -> {
                    String itemIdStr = String.valueOf(i.getItemId());
                    String codigo = i.getCodigo() != null ? i.getCodigo().toLowerCase() : "";
                    String nombre = i.getNombre() != null ? i.getNombre().toLowerCase() : "";
                    String descripcion = i.getDescripcion() != null ? i.getDescripcion().toLowerCase() : "";
                    String estadoItem = i.isEstaActivo() ? "Activos" : "Inactivos";
                    boolean coincideTexto = texto.isEmpty()
                        || itemIdStr.contains(texto)
                        || codigo.contains(texto)
                        || nombre.contains(texto)
                        || descripcion.contains(texto)
                        || estadoItem.toLowerCase().contains(texto);
                    boolean coincideEstado = estado.equals("Todos") || estadoItem.equals(estado);
                    boolean coincideCategoria = categoria == null || categoria.equals("Todas") || (i.getCategoria() != null && i.getCategoria().equalsIgnoreCase(categoria));
                    boolean coincideFecha = true;
                    if (desde != null && hasta != null && i.getFechaCreacion() != null) {
                        String fecha = i.getFechaCreacion().toString();
                        coincideFecha = (fecha.compareTo(desde) >= 0 && fecha.compareTo(hasta) <= 0);
                    }
                    return coincideTexto && coincideEstado && coincideCategoria && coincideFecha;
                })
                .collect(Collectors.toList());
        tablaItemsTable.setItems(filtrados);
    }

    private List<ItemEntity> obtenerItems() {
        return itemsController.buscarTodos();
    }

    @Override
    public void crearItems(ItemEntity item) {
        itemsController.crear(item);
        JOptionPane.showMessageDialog(this, "Item creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.listaCompletaItems = obtenerItems();
        actualizarCategoriasFiltro();
        tablaItemsTable.setItems(listaCompletaItems);
    }

    @Override
    public void eliminarItems(ItemEntity item) {
        itemsController.eliminar(item.getItemId());
        JOptionPane.showMessageDialog(this, "Item eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.listaCompletaItems = obtenerItems();
        actualizarCategoriasFiltro();
        tablaItemsTable.setItems(listaCompletaItems);
    }

    @Override
    public void actualizarItems(ItemEntity item) {
        // Buscar el item original por su código (o id si lo tienes)
        ItemEntity original = itemsController.buscarPorCodigo(item.getCodigo());
        if (original != null) {
            itemsController.actualizar(original.getItemId(), item);
            JOptionPane.showMessageDialog(this, "Item actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el item para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.listaCompletaItems = obtenerItems();
        actualizarCategoriasFiltro();
        tablaItemsTable.setItems(listaCompletaItems);
    }

    private void actualizarCategoriasFiltro() {
        List<String> categorias = listaCompletaItems.stream()
            .map(ItemEntity::getCategoria)
            .filter(c -> c != null && !c.isEmpty())
            .distinct()
            .sorted()
            .toList();
        filtrosItemsForm.setCategorias(categorias);
    }

    @Override
    public void verItems(ItemEntity item) {
        // Eliminado: ahora el modal se abre desde ItemsTable
    }

    public DIContainer getDIContainer() {
        return diContainer;
    }
}
