package com.hersac.ui.views.comercial.ventas.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectorClienteForm extends JPanel {
    private JTextField clienteIdField;
    private JTextField clienteNombreField;
    private JButton buscarClienteBtn;

    public SelectorClienteForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Seleccionar Cliente"));
        add(new JLabel("Cédula del cliente:"));
        clienteIdField = new JTextField(10);
        add(clienteIdField);
        add(new JLabel("Nombre:"));
        clienteNombreField = new JTextField(30);
        clienteNombreField.setEditable(false);
        add(clienteNombreField);
        buscarClienteBtn = new JButton("Buscar");
        add(buscarClienteBtn);
    }

    public void setClienteNombre(String nombre) {
        clienteNombreField.setText(nombre);
    }
    public String getClienteId() {
        return clienteIdField.getText();
    }
    public void setClienteId(String id) {
        clienteIdField.setText(id);
    }
    public void addBuscarListener(ActionListener listener) {
        buscarClienteBtn.addActionListener(listener);
    }
}
