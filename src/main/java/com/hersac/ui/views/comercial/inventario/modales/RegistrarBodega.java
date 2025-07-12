package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.comercial.inventario.listeners.BodegasListeners;
import com.hersac.core.di.DIContainer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.time.LocalDateTime;

public class RegistrarBodega extends JDialog {
    private final BodegasListeners listener;
    private final DIContainer diContainer;
    private JTextField nombreField;
    private JCheckBox activaCheckBox;
    private BodegaEntity bodegaRegistrada;
    private boolean soloLectura = false;
    private BodegaEntity bodegaOriginal;

    public RegistrarBodega(JFrame parent, DIContainer diContainer, BodegasListeners listener) {
        this(parent, diContainer, listener, null, false);
    }

    public RegistrarBodega(JFrame parent, DIContainer diContainer, BodegasListeners listener, BodegaEntity bodega, boolean esActualizacion) {
        super(parent, esActualizacion ? "Actualizar Bodega" : "Registrar Bodega", true);
        this.listener = listener;
        this.diContainer = diContainer;
        if (esActualizacion && bodega != null) {
            this.bodegaOriginal = bodega;
        }
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = crearCampos();
        if (bodega != null) {
            precargarDatos(bodega);
        }
        JPanel buttonPanel = crearPanelBotones(esActualizacion);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel crearCampos() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        nombreField = new JTextField();
        aplicarFiltroMayusculas(nombreField);
        formPanel.add(new JLabel("Nombre Bodega:"));
        formPanel.add(nombreField);
        activaCheckBox = new JCheckBox("Activa", true);
        formPanel.add(new JLabel("¿Está activa?:"));
        formPanel.add(activaCheckBox);
        return formPanel;
    }

    private JPanel crearPanelBotones(boolean esActualizacion) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esActualizacion ? "Actualizar" : "Guardar");
        guardarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        guardarBtn.setFocusPainted(false);
        guardarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> guardarBodega(esActualizacion));
        buttonPanel.add(guardarBtn);
        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        cancelarBtn.setFocusPainted(false);
        cancelarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarBodega(boolean esActualizacion) {
        bodegaRegistrada = new BodegaEntity();
        if (esActualizacion && bodegaOriginal != null) {
            bodegaRegistrada.setBodegaId(bodegaOriginal.getBodegaId());
            bodegaRegistrada.setFechaCreacion(bodegaOriginal.getFechaCreacion());
            bodegaRegistrada.setCreadaPor(bodegaOriginal.getCreadaPor());
        } else {
            bodegaRegistrada.setFechaCreacion(LocalDateTime.now());
            bodegaRegistrada.setCreadaPor(null);
        }
        bodegaRegistrada.setNombre(nombreField.getText());
        bodegaRegistrada.setEstaActiva(activaCheckBox.isSelected());
        if (esActualizacion) {
            listener.actualizarBodega(bodegaRegistrada);
        } else {
            listener.crearBodega(bodegaRegistrada);
        }
        dispose();
    }

    private void precargarDatos(BodegaEntity bodega) {
        nombreField.setText(bodega.getNombre());
        activaCheckBox.setSelected(Boolean.TRUE.equals(bodega.getEstaActiva()));
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

    public BodegaEntity getBodegaRegistrada() {
        return bodegaRegistrada;
    }
}

