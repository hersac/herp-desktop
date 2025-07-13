package com.hersac.ui.views.comercial.inventario.parciales;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.comercial.inventario.contenidos.bodegas.GestionBodegas;
import com.hersac.ui.views.comercial.inventario.contenidos.movimientosinventarios.GestionMovimientosInventarios;
import com.hersac.ui.views.comercial.inventario.contenidos.proveedores.GestionProveedores;
import com.hersac.ui.views.comercial.inventario.listeners.InventarioListeners;
import com.hersac.ui.views.comercial.inventario.contenidos.items.GestionItems;
import com.hersac.ui.views.comercial.inventario.contenidos.productos.GestionProductos;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class PestanasComponent extends JPanel {
    private InventarioListeners listener;
    private JTabbedPane pestanas;

    public PestanasComponent(DIContainer diContainer) {
        setOpaque(false);
        setLayout(new BorderLayout());
        pestanas = new JTabbedPane();
        pestanas.setPreferredSize(new Dimension(800, 500));
        pestanas.addTab("Productos", new JScrollPane(new GestionProductos(diContainer)));
        pestanas.addTab("Items", new JScrollPane(new GestionItems(diContainer)));
        pestanas.addTab("Proveedores", new JScrollPane(new GestionProveedores(diContainer)));
        pestanas.addTab("Bodegas", new JScrollPane(new GestionBodegas(diContainer)));
        pestanas.addTab("Movimientos de inventario", new JScrollPane(new GestionMovimientosInventarios(diContainer)));
        pestanas.addTab("Órdenes de compra", new JScrollPane(new JPanel()));
        pestanas.addChangeListener(e -> {
            if (listener != null) {
                listener.onTabChanged(pestanas.getSelectedIndex());
            }
        });
        add(pestanas, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 500));
    }

    public void setInventarioListener(InventarioListeners listener) {
        this.listener = listener;
    }
}
