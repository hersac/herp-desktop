package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
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
    private final BodegasListeners oyente;
    private final DIContainer contenedorDI;
    private JTextField campoNombre;
    private JCheckBox cajaActiva;
    private BodegaEntity bodegaRegistrada;
    private BodegaEntity bodegaOriginal;

    public RegistrarBodega(JFrame padre, DIContainer contenedorDI, BodegasListeners oyente) {
        this(padre, contenedorDI, oyente, null, false);
    }

    public RegistrarBodega(JFrame padre, DIContainer contenedorDI, BodegasListeners oyente, BodegaEntity bodega, boolean esActualizacion) {
        super(padre, esActualizacion ? "Actualizar Bodega" : "Registrar Bodega", true);
        this.oyente = oyente;
        this.contenedorDI = contenedorDI;
        if (esActualizacion && bodega != null) {
            this.bodegaOriginal = bodega;
        }
        setSize(400, 250);
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelFormulario = crearCampos();
        if (bodega != null) {
            precargarDatos(bodega);
        }
        JPanel panelBotones = crearPanelBotones(esActualizacion);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel crearCampos() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        aplicarFiltroMayusculas(campoNombre);
        panel.add(crearEtiqueta("Nombre Bodega:"));
        panel.add(campoNombre);
        cajaActiva = new JCheckBox("Activa", true);
        cajaActiva.setFont(new Font("Roboto", Font.PLAIN, 14));
        panel.add(crearEtiqueta("¿Está activa?:"));
        panel.add(cajaActiva);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        return etiqueta;
    }

    private JPanel crearPanelBotones(boolean esActualizacion) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton(esActualizacion ? "Actualizar" : "Guardar");
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonGuardar.addActionListener(e -> guardarBodega(esActualizacion));
        panel.add(botonGuardar);
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonCancelar);
        return panel;
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
        bodegaRegistrada.setNombre(campoNombre.getText());
        bodegaRegistrada.setEstaActiva(cajaActiva.isSelected());
        if (esActualizacion) {
            oyente.actualizarBodega(bodegaRegistrada);
        } else {
            oyente.crearBodega(bodegaRegistrada);
        }
        dispose();
    }

    private void precargarDatos(BodegaEntity bodega) {
        campoNombre.setText(bodega.getNombre());
        cajaActiva.setSelected(Boolean.TRUE.equals(bodega.getEstaActiva()));
    }

    private void aplicarFiltroMayusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
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
