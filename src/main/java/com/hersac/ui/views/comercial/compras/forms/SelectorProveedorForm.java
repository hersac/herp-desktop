package com.hersac.ui.views.comercial.compras.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectorProveedorForm extends JPanel {
    private JTextField proveedorIdField;
    private JTextField proveedorNombreField;
    private JButton buscarProveedorBtn;

    public SelectorProveedorForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Seleccionar Proveedor"));
        add(new JLabel("Cédula del proveedor:"));
        proveedorIdField = new JTextField(10);
        add(proveedorIdField);
        add(new JLabel("Nombre:"));
        proveedorNombreField = new JTextField(30);
        proveedorNombreField.setEditable(false);
        add(proveedorNombreField);
        buscarProveedorBtn = new JButton("Buscar");
        add(buscarProveedorBtn);
    }

    public void setProveedorNombre(String nombre) {
        proveedorNombreField.setText(nombre);
    }
    public String getProveedorId() {
        return proveedorIdField.getText();
    }
    public void setProveedorId(String id) {
        proveedorIdField.setText(id);
    }
    public void addBuscarListener(ActionListener listener) {
        buscarProveedorBtn.addActionListener(listener);
    }
}
