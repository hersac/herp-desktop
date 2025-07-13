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
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoUnidadMedida;
    private JTextField campoPrecioBase;
    private JCheckBox cajaActiva;
    private ProductoEntity productoRegistrado;
    private final ProductosListeners oyente;
    private final boolean esEdicion;

    public RegistrarProducto(JFrame padre, ProductosListeners oyente, ProductoEntity productoParaEditar) {
        super(padre, productoParaEditar != null ? "Actualizar Producto" : "Registrar Producto", true);
        this.oyente = oyente;
        this.productoRegistrado = productoParaEditar;
        this.esEdicion = productoParaEditar != null;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarVentana();
        JPanel panelFormulario = crearCampos();
        if (esEdicion && productoRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel panelBotones = crearPanelBotones();
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        campoCodigo = new JTextField();
        campoCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        aplicarFiltroMayusculas(campoCodigo);
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        aplicarFiltroMayusculas(campoNombre);
        campoDescripcion = new JTextField();
        campoDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoUnidadMedida = new JTextField();
        campoUnidadMedida.setFont(new Font("Roboto", Font.PLAIN, 14));
        aplicarFiltroMayusculas(campoUnidadMedida);
        campoPrecioBase = new JTextField();
        campoPrecioBase.setFont(new Font("Roboto", Font.PLAIN, 14));
        cajaActiva = new JCheckBox("Activo", true);
        cajaActiva.setFont(new Font("Roboto", Font.PLAIN, 14));
        panel.add(crearEtiqueta("Código:"));
        panel.add(campoCodigo);
        panel.add(crearEtiqueta("Nombre:"));
        panel.add(campoNombre);
        panel.add(crearEtiqueta("Descripción:"));
        panel.add(campoDescripcion);
        panel.add(crearEtiqueta("Unidad de Medida:"));
        panel.add(campoUnidadMedida);
        panel.add(crearEtiqueta("Precio Base:"));
        panel.add(campoPrecioBase);
        panel.add(crearEtiqueta("¿Está activo?:"));
        panel.add(cajaActiva);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        return etiqueta;
    }

    private void aplicarFiltroMayusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String cadena, AttributeSet attr) throws BadLocationException {
                if (cadena != null) {
                    super.insertString(fb, offset, cadena.toUpperCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String texto, AttributeSet attrs) throws BadLocationException {
                if (texto != null) {
                    super.replace(fb, offset, length, texto.toUpperCase(), attrs);
                }
            }
        });
    }

    private void cargarDatosEdicion() {
        campoCodigo.setText(productoRegistrado.getCodigo());
        campoNombre.setText(productoRegistrado.getNombre());
        campoDescripcion.setText(productoRegistrado.getDescripcion());
        campoUnidadMedida.setText(productoRegistrado.getUnidadMedida());
        campoPrecioBase.setText(String.valueOf(productoRegistrado.getPrecioBase()));
        cajaActiva.setSelected(productoRegistrado.isEstadoActivo());
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton(esEdicion ? "Actualizar" : "Guardar");
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonGuardar.addActionListener(e -> guardarProducto());
        panel.add(botonGuardar);
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonCancelar);
        return panel;
    }

    private void guardarProducto() {
        if (productoRegistrado == null) {
            productoRegistrado = new ProductoEntity();
        }
        productoRegistrado.setCodigo(campoCodigo.getText());
        productoRegistrado.setNombre(campoNombre.getText());
        productoRegistrado.setDescripcion(campoDescripcion.getText());
        productoRegistrado.setUnidadMedida(campoUnidadMedida.getText());
        try {
            productoRegistrado.setPrecioBase(Double.parseDouble(campoPrecioBase.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio base inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        productoRegistrado.setEstadoActivo(cajaActiva.isSelected());
        productoRegistrado.setUsuarioActualizacion(UsuarioEntity.builder().usuarioId(1L).build());
        if (!esEdicion) {
            productoRegistrado.setUsuarioCreacion(UsuarioEntity.builder().usuarioId(1L).build());
            if (oyente != null) oyente.crearProducto(productoRegistrado);
        } else {
            if (oyente != null) oyente.actualizarProducto(productoRegistrado);
        }
        dispose();
    }

    public ProductoEntity getProductoRegistrado() {
        return productoRegistrado;
    }
}
