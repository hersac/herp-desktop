package com.hersac.ui.views.comercial.ventas.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class SelectorItemsForm extends JPanel {
    private JTextField campoIdProducto;
    private JTextField campoNombreProducto;
    private JTextField campoCantidad;
    private JTextField campoPrecioUnitario;
    private JButton botonBuscarProducto;
    private JButton botonAgregarProducto;

    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
    private Double precioUnitarioOriginal;

    public SelectorItemsForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Agregar Producto"));
        JLabel etiquetaCodigo = new JLabel("Código:");
        etiquetaCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaCodigo);
        campoIdProducto = new JTextField(10);
        campoIdProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoIdProducto);
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaNombre);
        campoNombreProducto = new JTextField(15);
        campoNombreProducto.setEditable(false);
        campoNombreProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoNombreProducto);
        botonBuscarProducto = new JButton("Buscar");
        botonBuscarProducto.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonBuscarProducto);
        JLabel etiquetaCantidad = new JLabel("Cantidad:");
        etiquetaCantidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaCantidad);
        campoCantidad = new JTextField(5);
        campoCantidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoCantidad);
        JLabel etiquetaPrecio = new JLabel("Precio Unitario:");
        etiquetaPrecio.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaPrecio);
        campoPrecioUnitario = new JTextField(8);
        campoPrecioUnitario.setEditable(false);
        campoPrecioUnitario.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoPrecioUnitario);
        botonAgregarProducto = new JButton("Agregar");
        botonAgregarProducto.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonAgregarProducto);
    }

    public void setFuenteRoboto() {
        setFont(new Font("Roboto", Font.PLAIN, 14));
    }
    public String getItemId() { return campoIdProducto.getText(); }
    public void setItemId(String id) { campoIdProducto.setText(id); }
    public String getItemNombre() { return campoNombreProducto.getText(); }
    public void setItemNombre(String nombre) { campoNombreProducto.setText(nombre); }
    public String getCantidad() { return campoCantidad.getText(); }
    public void setCantidad(String cantidad) { campoCantidad.setText(cantidad); }
    public String getPrecioUnitario() { return campoPrecioUnitario.getText(); }
    public void setPrecioUnitario(String precio) {
        try {
            precioUnitarioOriginal = Double.parseDouble(precio);
            campoPrecioUnitario.setText(formatoMoneda.format(precioUnitarioOriginal));
        } catch (Exception e) {
            precioUnitarioOriginal = null;
            campoPrecioUnitario.setText(precio);
        }
    }
    public Double getPrecioUnitarioOriginal() {
        return precioUnitarioOriginal;
    }
    public void addBuscarListener(ActionListener listener) { botonBuscarProducto.addActionListener(listener); }
    public void addAgregarListener(ActionListener listener) { botonAgregarProducto.addActionListener(listener); }
}
