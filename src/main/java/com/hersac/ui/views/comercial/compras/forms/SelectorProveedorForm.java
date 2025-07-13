package com.hersac.ui.views.comercial.compras.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectorProveedorForm extends JPanel {
    private JTextField campoIdProveedor;
    private JTextField campoNombreProveedor;
    private JButton botonBuscarProveedor;

    public SelectorProveedorForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Seleccionar Proveedor"));
        JLabel etiquetaId = new JLabel("Cédula del proveedor:");
        etiquetaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaId);
        campoIdProveedor = new JTextField(10);
        campoIdProveedor.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoIdProveedor);
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaNombre);
        campoNombreProveedor = new JTextField(30);
        campoNombreProveedor.setEditable(false);
        campoNombreProveedor.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoNombreProveedor);
        botonBuscarProveedor = new JButton("Buscar");
        botonBuscarProveedor.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonBuscarProveedor);
    }

    public void setNombreProveedor(String nombre) {
        campoNombreProveedor.setText(nombre);
    }
    public void setProveedorNombre(String nombre) {
        setNombreProveedor(nombre);
    }
    public String getIdProveedor() {
        return campoIdProveedor.getText();
    }
    public void setIdProveedor(String id) {
        campoIdProveedor.setText(id);
    }
    public void addBuscarListener(ActionListener listener) {
        botonBuscarProveedor.addActionListener(listener);
    }
    public String getProveedorId() {
        return getIdProveedor();
    }
    public void setProveedorId(String id) {
        setIdProveedor(id);
    }
}
