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
    private JTextField bodegaIdField;
    private JTextField bodegaNombreField;
    private com.hersac.core.modules.bodegas.entities.BodegaEntity bodegaEncontrada;

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
        setSize(550, 680);
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
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        int labelWidth = 120;
        int inputMinWidth = 200;
        int inputHeight = 40;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblCodigo, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        codigoField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        codigoField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(codigoField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        nombreField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        nombreField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(nombreField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblDescripcion, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        descripcionField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        descripcionField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(descripcionField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio Unitario:");
        lblPrecio.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblPrecio, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        precioUnitarioField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        precioUnitarioField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(precioUnitarioField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblStock = new JLabel("Stock:");
        lblStock.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblStock, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        stockField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        stockField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(stockField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblCategoria, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        categoriaField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        categoriaField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(categoriaField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblActivo = new JLabel("Activo:");
        lblActivo.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblActivo, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(activoCheckBox, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblBodegaId = new JLabel("Bodega ID:");
        lblBodegaId.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblBodegaId, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        bodegaIdField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        bodegaIdField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(bodegaIdField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblBodegaNombre = new JLabel("Nombre Bodega:");
        lblBodegaNombre.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblBodegaNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        bodegaNombreField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        bodegaNombreField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(bodegaNombreField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblCodigoProducto = new JLabel("Código Producto:");
        lblCodigoProducto.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblCodigoProducto, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        codigoProductoField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        codigoProductoField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(codigoProductoField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblNombreProducto = new JLabel("Nombre Producto:");
        lblNombreProducto.setPreferredSize(new Dimension(labelWidth, inputHeight));
        formPanel.add(lblNombreProducto, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        nombreProductoField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        nombreProductoField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(nombreProductoField, gbc);
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

    private void buscarBodega() {
        String idText = bodegaIdField.getText();
        bodegaEncontrada = null;
        bodegaNombreField.setText("");
        try {
            Long id = Long.parseLong(idText);
            var bodegasController = diContainer.getBodegasController();
            bodegaEncontrada = bodegasController.buscarPorId(id);
            if (bodegaEncontrada != null) {
                bodegaNombreField.setText(bodegaEncontrada.getNombre());
            } else {
                bodegaNombreField.setText("No encontrada");
            }
        } catch (Exception ex) {
            bodegaNombreField.setText("ID inválido");
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
        if (bodegaEncontrada != null) {
            itemRegistrado.setBodega(bodegaEncontrada);
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
        if (item.getBodega() != null) {
            bodegaIdField.setText(String.valueOf(item.getBodega().getBodegaId()));
            bodegaNombreField.setText(item.getBodega().getNombre());
            bodegaEncontrada = item.getBodega();
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

    {
        codigoField = new JTextField();
        nombreField = new JTextField();
        descripcionField = new JTextField();
        precioUnitarioField = new JTextField();
        stockField = new JTextField();
        categoriaField = new JTextField();
        activoCheckBox = new JCheckBox("Activo", true);
        bodegaIdField = new JTextField();
        bodegaNombreField = new JTextField();
        bodegaNombreField.setEditable(false);
        bodegaNombreField.setBackground(new Color(240,240,240));
        bodegaIdField.addActionListener(e -> buscarBodega());
        codigoProductoField = new JTextField();
        nombreProductoField = new JTextField();
        nombreProductoField.setEditable(false);
        nombreProductoField.setBackground(new Color(240,240,240));
        codigoProductoField.addActionListener(e -> buscarProductoPorCodigo());
    }
}
