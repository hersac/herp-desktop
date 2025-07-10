package com.hersac.ui.views.comercial.clientes.tablas;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.ui.views.comercial.clientes.constantes.TiposClienteEnum;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ClientesTable extends JPanel {
    private final JTable tablaClientes;
    private final DefaultTableModel modeloTabla;
    private ClientesListeners clientesListeners;
    private PermissionService permissionService;

    public ClientesTable() {
        this(null);
    }

    public ClientesTable(PermissionService permissionService) {
        this.permissionService = permissionService;
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(40);
        tablaClientes.setShowGrid(false);
        // Aquí puedes agregar renderers y editores personalizados para la columna de acciones
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
    }

    public void setClientesListeners(ClientesListeners clientesListeners) {
        this.clientesListeners = clientesListeners;
    }

    public void setClientes(List<ClienteEntity> clientes) {
        modeloTabla.setRowCount(0);
        for (ClienteEntity cliente : clientes) {
            modeloTabla.addRow(new Object[]{
                cliente.getClienteId(),
                cliente.getTercero() != null ? cliente.getTercero().getNombre() : "",
                cliente.getTercero() != null ? cliente.getTercero().getTipoPersona() : "",
                cliente.isEsta_activo() ? "Activo" : "Inactivo",
                "Acciones"
            });
        }
    }
}

