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

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.tablas.ItemsTable;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarItem;

public class GestionItems extends JPanel {
    private JFrame frame = new JFrame("Registrar Item");
    private final ItemsController itemsController;
    private final ItemsTable tablaItemsTable = new ItemsTable();
    private List<ItemEntity> listaCompletaItems;
    private JTextField searchField;

    public GestionItems(DIContainer diContainer) {
        this.itemsController = diContainer.getItemsController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Items");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        JButton registrarBtn = new JButton("Registrar Item");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
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
        panelBtn.setPreferredSize(new Dimension(900, 40));
        panelBtn.setMaximumSize(new Dimension(900, 40));
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
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
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
            new RegistrarItem(parentFrame);
        });
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
        List<ItemEntity> filtrados = listaCompletaItems.stream()
                .filter(i -> {
                    String itemIdStr = String.valueOf(i.getItemId());
                    String codigo = i.getCodigo() != null ? i.getCodigo().toLowerCase() : "";
                    String nombre = i.getNombre() != null ? i.getNombre().toLowerCase() : "";
                    String descripcion = i.getDescripcion() != null ? i.getDescripcion().toLowerCase() : "";
                    String estado = i.isEstaActivo() ? "activo" : "inactivo";
                    boolean coincideTexto = texto.isEmpty()
                        || itemIdStr.contains(texto)
                        || codigo.contains(texto)
                        || nombre.contains(texto)
                        || descripcion.contains(texto)
                        || estado.contains(texto);
                    return coincideTexto;
                })
                .collect(Collectors.toList());
        tablaItemsTable.setItems(filtrados);
    }

    private List<ItemEntity> obtenerItems() {
        return itemsController.buscarTodos();
    }
}
