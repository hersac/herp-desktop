package com.hersac.ui.views.comercial.inventario.modales;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.comercial.inventario.listeners.ProductosListeners;

public class RegistrarProducto extends JDialog {
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField descripcionField;
    private JTextField unidadMedidaField;
    private JTextField precioBaseField;
    private JCheckBox activoCheckBox;
    private ProductoEntity productoRegistrado;
    private final ProductosListeners listener;
    private final boolean esEdicion;

    public RegistrarProducto(JFrame parent, ProductosListeners listener, ProductoEntity productoParaEditar) {
        super(parent, productoParaEditar != null ? "Actualizar Producto" : "Registrar Producto", true);
        this.listener = listener;
        this.productoRegistrado = productoParaEditar;
        this.esEdicion = productoParaEditar != null;
        initComponents();
    }

    private void initComponents() {
        configurarVentana();
        JPanel formPanel = crearCampos();
        if (esEdicion && productoRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel buttonPanel = crearPanelBotones();
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        codigoField = new JTextField();
        aplicarFiltroMayusculas(codigoField);
        nombreField = new JTextField();
        aplicarFiltroMayusculas(nombreField);
        descripcionField = new JTextField();
        unidadMedidaField = new JTextField();
        aplicarFiltroMayusculas(unidadMedidaField);
        precioBaseField = new JTextField();
        activoCheckBox = new JCheckBox("Activo", true);

        formPanel.add(new JLabel("Código:"));
        formPanel.add(codigoField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(descripcionField);
        formPanel.add(new JLabel("Unidad de Medida:"));
        formPanel.add(unidadMedidaField);
        formPanel.add(new JLabel("Precio Base:"));
        formPanel.add(precioBaseField);
        formPanel.add(new JLabel("¿Está activo?:"));
        formPanel.add(activoCheckBox);
        return formPanel;
    }

    private void aplicarFiltroMayusculas(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
    }

    private void cargarDatosEdicion() {
        codigoField.setText(productoRegistrado.getCodigo());
        nombreField.setText(productoRegistrado.getNombre());
        descripcionField.setText(productoRegistrado.getDescripcion());
        unidadMedidaField.setText(productoRegistrado.getUnidadMedida());
        precioBaseField.setText(String.valueOf(productoRegistrado.getPrecioBase()));
        activoCheckBox.setSelected(productoRegistrado.isEstadoActivo());
    }

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.addActionListener(e -> guardarProducto());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarProducto() {
        if (productoRegistrado == null) {
            productoRegistrado = new ProductoEntity();
        }
        productoRegistrado.setCodigo(codigoField.getText());
        productoRegistrado.setNombre(nombreField.getText());
        productoRegistrado.setDescripcion(descripcionField.getText());
        productoRegistrado.setUnidadMedida(unidadMedidaField.getText());
        try {
            productoRegistrado.setPrecioBase(Double.parseDouble(precioBaseField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio base inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        productoRegistrado.setEstadoActivo(activoCheckBox.isSelected());
        productoRegistrado.setUsuarioActualizacion(UsuarioEntity.builder().usuarioId(1L).build());
        if (!esEdicion) {
            productoRegistrado.setUsuarioCreacion(UsuarioEntity.builder().usuarioId(1L).build());
            if (listener != null) listener.crearProducto(productoRegistrado);
        } else {
            if (listener != null) listener.actualizarProducto(productoRegistrado);
        }
        dispose();
    }

    public ProductoEntity getProductoRegistrado() {
        return productoRegistrado;
    }
}
