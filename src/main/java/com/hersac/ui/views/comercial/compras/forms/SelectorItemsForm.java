package com.hersac.ui.views.comercial.compras.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class SelectorItemsForm extends JPanel {
    private JTextField itemIdField;
    private JTextField itemNombreField;
    private JTextField cantidadField;
    private JTextField precioUnitarioField;
    private JButton buscarItemBtn;
    private JButton agregarItemBtn;

    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
    private Double precioUnitarioOriginal;

    public SelectorItemsForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Agregar Producto"));
        add(new JLabel("Código:"));
        itemIdField = new JTextField(10);
        add(itemIdField);
        add(new JLabel("Nombre:"));
        itemNombreField = new JTextField(15);
        itemNombreField.setEditable(false);
        add(itemNombreField);
        buscarItemBtn = new JButton("Buscar");
        add(buscarItemBtn);
        add(new JLabel("Cantidad:"));
        cantidadField = new JTextField(5);
        add(cantidadField);
        add(new JLabel("Precio Unitario:"));
        precioUnitarioField = new JTextField(8);
        precioUnitarioField.setEditable(false);
        add(precioUnitarioField);
        agregarItemBtn = new JButton("Agregar");
        add(agregarItemBtn);
    }

    public String getItemId() { return itemIdField.getText(); }
    public void setItemId(String id) { itemIdField.setText(id); }
    public String getItemNombre() { return itemNombreField.getText(); }
    public void setItemNombre(String nombre) { itemNombreField.setText(nombre); }
    public String getCantidad() { return cantidadField.getText(); }
    public void setCantidad(String cantidad) { cantidadField.setText(cantidad); }
    public String getPrecioUnitario() { return precioUnitarioField.getText(); }
    public void setPrecioUnitario(String precio) {
        try {
            precioUnitarioOriginal = Double.parseDouble(precio);
            precioUnitarioField.setText(formatoMoneda.format(precioUnitarioOriginal));
        } catch (Exception e) {
            precioUnitarioOriginal = null;
            precioUnitarioField.setText(precio);
        }
    }
    public Double getPrecioUnitarioOriginal() {
        return precioUnitarioOriginal;
    }
    public void addBuscarListener(ActionListener listener) { buscarItemBtn.addActionListener(listener); }
    public void addAgregarListener(ActionListener listener) { agregarItemBtn.addActionListener(listener); }
}
