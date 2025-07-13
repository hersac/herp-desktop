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
    private final ItemsListeners oyente;
    private final DIContainer contenedorDI;
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoPrecioUnitario;
    private JTextField campoStock;
    private JCheckBox cajaActiva;
    private ItemEntity itemRegistrado;
    private JTextField campoCodigoProducto;
    private JTextField campoNombreProducto;
    private ProductoEntity productoEncontrado;
    private ProductosController productosController;
    private JTextField campoCategoria;
    private ItemEntity itemOriginal;
    private JTextField campoBodegaId;
    private JTextField campoBodegaNombre;
    private com.hersac.core.modules.bodegas.entities.BodegaEntity bodegaEncontrada;

    public RegistrarItem(JFrame padre, DIContainer contenedorDI, ItemsListeners oyente) {
        this(padre, contenedorDI, oyente, null, false);
    }

    public RegistrarItem(JFrame padre, DIContainer contenedorDI, ItemsListeners oyente, ItemEntity item, boolean esActualizacion) {
        super(padre, esActualizacion ? "Actualizar Item" : "Registrar Item", true);
        this.oyente = oyente;
        this.contenedorDI = contenedorDI;
        this.productosController = contenedorDI.getProductosController();
        if (esActualizacion && item != null) {
            this.itemOriginal = item;
        }
        setSize(550, 680);
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelFormulario = crearCampos();
        if (item != null) {
            precargarDatos(item);
        }
        JPanel panelBotones = crearPanelBotones(esActualizacion);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel crearCampos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        int anchoEtiqueta = 120;
        int anchoCampo = 200;
        int altoCampo = 40;
        JLabel etiquetaCodigo = new JLabel("Código:");
        etiquetaCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaCodigo.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaCodigo, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoCodigo.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoCodigo.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoCodigo.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        aplicarFiltroMayusculas(campoCodigo);
        panel.add(campoCodigo, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaNombre.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoNombre.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoNombre.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        aplicarFiltroMayusculas(campoNombre);
        panel.add(campoNombre, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaDescripcion = new JLabel("Descripción:");
        etiquetaDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaDescripcion.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaDescripcion, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoDescripcion.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoDescripcion.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoDescripcion, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaPrecio = new JLabel("Precio Unitario:");
        etiquetaPrecio.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaPrecio.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaPrecio, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoPrecioUnitario.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoPrecioUnitario.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoPrecioUnitario.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoPrecioUnitario, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaStock = new JLabel("Stock:");
        etiquetaStock.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaStock.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaStock, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoStock.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoStock.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoStock.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoStock, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaCategoria = new JLabel("Categoría:");
        etiquetaCategoria.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaCategoria.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaCategoria, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoCategoria.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoCategoria.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoCategoria.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoCategoria, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaActivo = new JLabel("Activo:");
        etiquetaActivo.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaActivo.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaActivo, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        cajaActiva.setFont(new Font("Roboto", Font.PLAIN, 14));
        panel.add(cajaActiva, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaBodegaId = new JLabel("Bodega ID:");
        etiquetaBodegaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaBodegaId.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaBodegaId, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoBodegaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBodegaId.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoBodegaId.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoBodegaId, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaBodegaNombre = new JLabel("Nombre Bodega:");
        etiquetaBodegaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaBodegaNombre.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaBodegaNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoBodegaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBodegaNombre.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoBodegaNombre.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoBodegaNombre, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaCodigoProducto = new JLabel("Código Producto:");
        etiquetaCodigoProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaCodigoProducto.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaCodigoProducto, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoCodigoProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoCodigoProducto.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoCodigoProducto.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoCodigoProducto, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel etiquetaNombreProducto = new JLabel("Nombre Producto:");
        etiquetaNombreProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiquetaNombreProducto.setPreferredSize(new Dimension(anchoEtiqueta, altoCampo));
        panel.add(etiquetaNombreProducto, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campoNombreProducto.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoNombreProducto.setMinimumSize(new Dimension(anchoCampo, altoCampo));
        campoNombreProducto.setPreferredSize(new Dimension(anchoCampo, altoCampo));
        panel.add(campoNombreProducto, gbc);
        return panel;
    }

    private JPanel crearPanelBotones(boolean esActualizacion) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton(esActualizacion ? "Actualizar" : "Guardar");
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonGuardar.addActionListener(e -> guardarItem(esActualizacion));
        panel.add(botonGuardar);
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonCancelar);
        return panel;
    }

    private void guardarItem(boolean esActualizacion) {
        itemRegistrado = new ItemEntity();
        if (esActualizacion && itemOriginal != null) {
            itemRegistrado.setItemId(itemOriginal.getItemId());
        }
        itemRegistrado.setCodigo(campoCodigo.getText());
        itemRegistrado.setNombre(campoNombre.getText());
        itemRegistrado.setDescripcion(campoDescripcion.getText());
        itemRegistrado.setCategoria(campoCategoria.getText());
        try {
            itemRegistrado.setPrecioUnitario(Double.parseDouble(campoPrecioUnitario.getText()));
        } catch (NumberFormatException e) {
            itemRegistrado.setPrecioUnitario(0.0);
        }
        try {
            itemRegistrado.setStock(Integer.parseInt(campoStock.getText()));
        } catch (NumberFormatException e) {
            itemRegistrado.setStock(0);
        }
        itemRegistrado.setEstaActivo(cajaActiva.isSelected());
        if (productoEncontrado != null) {
            itemRegistrado.setProducto(productoEncontrado);
        } else if (esActualizacion && itemOriginal != null && itemOriginal.getProducto() != null) {
            itemRegistrado.setProducto(itemOriginal.getProducto());
        }
        if (bodegaEncontrada != null) {
            itemRegistrado.setBodega(bodegaEncontrada);
        }
        if (esActualizacion) {
            oyente.actualizarItems(itemRegistrado);
        } else {
            oyente.crearItems(itemRegistrado);
        }
        dispose();
    }

    private void precargarDatos(ItemEntity item) {
        campoCodigo.setText(item.getCodigo());
        campoNombre.setText(item.getNombre());
        campoDescripcion.setText(item.getDescripcion());
        campoCategoria.setText(item.getCategoria());
        campoPrecioUnitario.setText(String.valueOf(item.getPrecioUnitario()));
        campoStock.setText(String.valueOf(item.getStock()));
        cajaActiva.setSelected(item.isEstaActivo());
        if (item.getProducto() != null) {
            campoCodigoProducto.setText(item.getProducto().getCodigo());
            campoNombreProducto.setText(item.getProducto().getNombre());
        }
        if (item.getBodega() != null) {
            campoBodegaId.setText(String.valueOf(item.getBodega().getBodegaId()));
            campoBodegaNombre.setText(item.getBodega().getNombre());
            bodegaEncontrada = item.getBodega();
        }
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

    public ItemEntity getItemRegistrado() {
        return itemRegistrado;
    }

    {
        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoDescripcion = new JTextField();
        campoPrecioUnitario = new JTextField();
        campoStock = new JTextField();
        campoCategoria = new JTextField();
        cajaActiva = new JCheckBox("Activo", true);
        campoBodegaId = new JTextField();
        campoBodegaNombre = new JTextField();
        campoBodegaNombre.setEditable(false);
        campoBodegaNombre.setBackground(new Color(240,240,240));
        campoBodegaId.addActionListener(e -> buscarBodega());
        campoCodigoProducto = new JTextField();
        campoNombreProducto = new JTextField();
        campoNombreProducto.setEditable(false);
        campoNombreProducto.setBackground(new Color(240,240,240));
        campoCodigoProducto.addActionListener(e -> buscarProductoPorCodigo());
    }

    private void buscarProductoPorCodigo() {
        String codigo = campoCodigoProducto.getText().trim();
        if (!codigo.isEmpty()) {
            productoEncontrado = productosController.buscarPorCodigo(codigo);
            if (productoEncontrado != null) {
                campoNombreProducto.setText(productoEncontrado.getNombre());
            } else {
                campoNombreProducto.setText("No encontrado");
            }
        } else {
            campoNombreProducto.setText("");
        }
    }

    private void buscarBodega() {
        String idText = campoBodegaId.getText();
        bodegaEncontrada = null;
        campoBodegaNombre.setText("");
        try {
            Long id = Long.parseLong(idText);
            var bodegasController = contenedorDI.getBodegasController();
            bodegaEncontrada = bodegasController.buscarPorId(id);
            if (bodegaEncontrada != null) {
                campoBodegaNombre.setText(bodegaEncontrada.getNombre());
            } else {
                campoBodegaNombre.setText("No encontrada");
            }
        } catch (Exception ex) {
            campoBodegaNombre.setText("ID inválido");
        }
    }
}
