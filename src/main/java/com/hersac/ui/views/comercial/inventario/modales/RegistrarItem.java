package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.ui.controllers.productos.ProductosController;
import com.hersac.ui.views.comercial.inventario.listeners.ItemsListeners;
import com.hersac.core.di.DIContainer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class RegistrarItem extends JDialog {
    private final ItemsListeners listener;
    private final DIContainer diContainer;
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField descripcionField;
    private JTextField precioUnitarioField;
    private JTextField stockField;
    private JCheckBox activoCheckBox;
    private ItemEntity itemRegistrado;
    private JTextField codigoProductoField;
    private JTextField nombreProductoField;
    private ProductoEntity productoEncontrado;
    private ProductosController productosController;
    private JTextField categoriaField;
    private boolean soloLectura = false;
    private ItemEntity itemOriginal; // Para actualización

    public RegistrarItem(JFrame parent, DIContainer diContainer, ItemsListeners listener) {
        this(parent, diContainer, listener, null, false);
    }

    public RegistrarItem(JFrame parent, DIContainer diContainer, ItemsListeners listener, ItemEntity item, boolean esActualizacion) {
        super(parent, esActualizacion ? "Actualizar Item" : "Registrar Item", true);
        this.listener = listener;
        this.diContainer = diContainer;
        this.productosController = diContainer.getProductosController();
        if (esActualizacion && item != null) {
            this.itemOriginal = item;
        }
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = crearCampos();
        if (item != null) {
            precargarDatos(item);
        }
        JPanel buttonPanel = crearPanelBotones(esActualizacion);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel crearCampos() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Campo para código de producto
        codigoProductoField = new JTextField();
        aplicarFiltroMayusculas(codigoProductoField);
        formPanel.add(new JLabel("Código Producto:"));
        formPanel.add(codigoProductoField);
        // Campo informativo para nombre de producto
        nombreProductoField = new JTextField();
        nombreProductoField.setEditable(false);
        formPanel.add(new JLabel("Nombre Producto:"));
        formPanel.add(nombreProductoField);
        // Listener para buscar producto al presionar Enter
        codigoProductoField.addActionListener(e -> buscarProductoPorCodigo());
        // Campo para categoría
        categoriaField = new JTextField();
        aplicarFiltroMayusculas(categoriaField);
        formPanel.add(new JLabel("Categoría:"));
        formPanel.add(categoriaField);
        // Campos existentes
        codigoField = new JTextField();
        aplicarFiltroMayusculas(codigoField);
        nombreField = new JTextField();
        aplicarFiltroMayusculas(nombreField);
        descripcionField = new JTextField();
        precioUnitarioField = new JTextField();
        stockField = new JTextField();
        activoCheckBox = new JCheckBox("Activo", true);
        formPanel.add(new JLabel("Código:"));
        formPanel.add(codigoField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(descripcionField);
        formPanel.add(new JLabel("Precio Unitario:"));
        formPanel.add(precioUnitarioField);
        formPanel.add(new JLabel("Stock:"));
        formPanel.add(stockField);
        formPanel.add(new JLabel("¿Está activo?:"));
        formPanel.add(activoCheckBox);
        return formPanel;
    }

    private void buscarProductoPorCodigo() {
        String codigo = codigoProductoField.getText().trim();
        if (!codigo.isEmpty()) {
            productoEncontrado = productosController.buscarPorCodigo(codigo);
            if (productoEncontrado != null) {
                nombreProductoField.setText(productoEncontrado.getNombre());
            } else {
                nombreProductoField.setText("No encontrado");
            }
        } else {
            nombreProductoField.setText("");
        }
    }

    private JPanel crearPanelBotones(boolean esActualizacion) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esActualizacion ? "Actualizar" : "Guardar");
        guardarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        guardarBtn.setFocusPainted(false);
        guardarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> guardarItem(esActualizacion));
        buttonPanel.add(guardarBtn);
        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        cancelarBtn.setFocusPainted(false);
        cancelarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarItem(boolean esActualizacion) {
        itemRegistrado = new ItemEntity();
        if (esActualizacion && itemOriginal != null) {
            itemRegistrado.setItemId(itemOriginal.getItemId());
        }
        itemRegistrado.setCodigo(codigoField.getText());
        itemRegistrado.setNombre(nombreField.getText());
        itemRegistrado.setDescripcion(descripcionField.getText());
        itemRegistrado.setCategoria(categoriaField.getText());
        try {
            itemRegistrado.setPrecioUnitario(Double.parseDouble(precioUnitarioField.getText()));
        } catch (NumberFormatException e) {
            itemRegistrado.setPrecioUnitario(0.0);
        }
        try {
            itemRegistrado.setStock(Integer.parseInt(stockField.getText()));
        } catch (NumberFormatException e) {
            itemRegistrado.setStock(0);
        }
        itemRegistrado.setEstaActivo(activoCheckBox.isSelected());
        // Asignar producto correctamente
        if (productoEncontrado != null) {
            itemRegistrado.setProducto(productoEncontrado);
        } else if (esActualizacion && itemOriginal != null && itemOriginal.getProducto() != null) {
            itemRegistrado.setProducto(itemOriginal.getProducto());
        }
        if (esActualizacion) {
            listener.actualizarItems(itemRegistrado);
        } else {
            listener.crearItems(itemRegistrado);
        }
        dispose();
    }

    private void precargarDatos(ItemEntity item) {
        codigoField.setText(item.getCodigo());
        nombreField.setText(item.getNombre());
        descripcionField.setText(item.getDescripcion());
        categoriaField.setText(item.getCategoria());
        precioUnitarioField.setText(String.valueOf(item.getPrecioUnitario()));
        stockField.setText(String.valueOf(item.getStock()));
        activoCheckBox.setSelected(item.isEstaActivo());
        if (item.getProducto() != null) {
            codigoProductoField.setText(item.getProducto().getCodigo());
            nombreProductoField.setText(item.getProducto().getNombre());
        }
        // Ya no se deja en solo lectura, todos los campos quedan editables
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

    public ItemEntity getItemRegistrado() {
        return itemRegistrado;
    }
}
