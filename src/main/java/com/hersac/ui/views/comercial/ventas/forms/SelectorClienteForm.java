package com.hersac.ui.views.comercial.ventas.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectorClienteForm extends JPanel {
    private JTextField campoIdCliente;
    private JTextField campoNombreCliente;
    private JButton botonBuscarCliente;

    public SelectorClienteForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Seleccionar Cliente"));
        JLabel etiquetaId = new JLabel("Cédula del cliente:");
        etiquetaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaId);
        campoIdCliente = new JTextField(10);
        campoIdCliente.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoIdCliente);
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaNombre);
        campoNombreCliente = new JTextField(30);
        campoNombreCliente.setEditable(false);
        campoNombreCliente.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoNombreCliente);
        botonBuscarCliente = new JButton("Buscar");
        botonBuscarCliente.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonBuscarCliente);
    }

    public void setFuenteRoboto() {
        setFont(new Font("Roboto", Font.PLAIN, 14));
    }
    public void setClienteNombre(String nombre) {
        campoNombreCliente.setText(nombre);
    }
    public String getClienteId() {
        return campoIdCliente.getText();
    }
    public void setClienteId(String id) {
        campoIdCliente.setText(id);
    }
    public void addBuscarListener(ActionListener listener) {
        botonBuscarCliente.addActionListener(listener);
    }
}
