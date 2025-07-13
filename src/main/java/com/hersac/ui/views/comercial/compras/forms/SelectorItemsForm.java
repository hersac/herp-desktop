package com.hersac.ui.views.comercial.compras.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class SelectorItemsForm extends JPanel {
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoCantidad;
    private JTextField campoPrecioUnitario;
    private JButton botonBuscar;
    private JButton botonAgregar;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
    private Double precioUnitarioOriginal;

    public SelectorItemsForm() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Agregar Producto"));
        JLabel etiquetaCodigo = new JLabel("Código:");
        etiquetaCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaCodigo);
        campoCodigo = new JTextField(10);
        campoCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoCodigo);
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(etiquetaNombre);
        campoNombre = new JTextField(15);
        campoNombre.setEditable(false);
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        add(campoNombre);
        botonBuscar = new JButton("Buscar");
        botonBuscar.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonBuscar);
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
        botonAgregar = new JButton("Agregar");
        botonAgregar.setFont(new Font("Roboto", Font.BOLD, 14));
        add(botonAgregar);
    }

    public String getItemId() { return campoCodigo.getText(); }
    public void setItemId(String id) { campoCodigo.setText(id); }
    public String getItemNombre() { return campoNombre.getText(); }
    public void setItemNombre(String nombre) { campoNombre.setText(nombre); }
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
    public Double getPrecioUnitarioOriginal() { return precioUnitarioOriginal; }
    public void addBuscarListener(ActionListener listener) { botonBuscar.addActionListener(listener); }
    public void addAgregarListener(ActionListener listener) { botonAgregar.addActionListener(listener); }
}
