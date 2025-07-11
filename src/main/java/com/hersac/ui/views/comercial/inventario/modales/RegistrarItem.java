package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.items.entities.ItemEntity;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class RegistrarItem extends JDialog {
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField descripcionField;
    private JTextField precioUnitarioField;
    private JTextField stockField;
    private JCheckBox activoCheckBox;
    private ItemEntity itemRegistrado;

    public RegistrarItem(JFrame parent) {
        super(parent, "Registrar Item", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = crearCampos();
        JPanel buttonPanel = crearPanelBotones();
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel crearCampos() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        cancelarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        guardarBtn.setFocusPainted(false);
        cancelarBtn.setFocusPainted(false);
        guardarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> guardarItem());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarItem() {
        itemRegistrado = new ItemEntity();
        itemRegistrado.setCodigo(codigoField.getText());
        itemRegistrado.setNombre(nombreField.getText());
        itemRegistrado.setDescripcion(descripcionField.getText());
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
        // Aquí puedes agregar lógica para guardar el item usando un listener o controlador
        dispose();
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
