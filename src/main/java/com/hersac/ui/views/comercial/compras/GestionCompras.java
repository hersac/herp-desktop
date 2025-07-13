package com.hersac.ui.views.comercial.compras;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.ui.controllers.movimientosinventarios.MovimientosInventariosController;
import com.hersac.ui.controllers.proveedores.ProveedoresController;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.ui.controllers.compras.ComprasController;
import com.hersac.ui.controllers.comprasdetalles.ComprasDetallesController;
import com.hersac.core.modules.compras.entities.CompraEntity;
import com.hersac.core.modules.comprasdetalles.entities.CompraDetalleEntity;
import com.hersac.ui.views.comercial.compras.forms.SelectorProveedorForm;
import com.hersac.ui.views.comercial.compras.forms.SelectorItemsForm;
import com.hersac.ui.views.comercial.compras.tablas.ItemsComprasTable;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.movimientosinventarios.constants.TipoReferencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GestionCompras extends JPanel {
    private SelectorProveedorForm selectorProveedorForm;
    private SelectorItemsForm selectorItemsForm;
    private ItemsComprasTable tablaItemsCompras;
    private JLabel etiquetaTotal;
    private JButton botonGuardarCompra;
    private JTextField campoObservaciones;
    private ProveedorEntity proveedorSeleccionado;
    private ProveedoresController controladorProveedores;
    private ItemsController controladorItems;
    private ComprasController controladorCompras;
    private ComprasDetallesController controladorDetallesCompra;
    private MovimientosInventariosController controladorMovimientosInventario;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    public GestionCompras(DIContainer contenedor) {
        controladorProveedores = contenedor.getProveedoresController();
        controladorItems = contenedor.getItemsController();
        controladorCompras = contenedor.getComprasController();
        controladorDetallesCompra = contenedor.getComprasDetallesController();
        controladorMovimientosInventario = contenedor.getMovimientosInventariosController();

        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Compras", SwingConstants.CENTER);
        titulo.setFont(new Font("Roboto", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        add(panelPrincipal, BorderLayout.CENTER);

        selectorProveedorForm = new SelectorProveedorForm();
        aplicarFuenteRoboto(selectorProveedorForm);
        selectorProveedorForm.addBuscarListener(e -> buscarProveedor());
        panelPrincipal.add(selectorProveedorForm);

        JPanel panelObservaciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelObservaciones.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        JLabel etiquetaObservaciones = new JLabel("Observaciones:");
        etiquetaObservaciones.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelObservaciones.add(etiquetaObservaciones);
        campoObservaciones = new JTextField(40);
        campoObservaciones.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelObservaciones.add(campoObservaciones);
        panelPrincipal.add(panelObservaciones);

        selectorItemsForm = new SelectorItemsForm();
        aplicarFuenteRoboto(selectorItemsForm);
        selectorItemsForm.addBuscarListener(e -> buscarItem());
        selectorItemsForm.addAgregarListener(e -> agregarItemATabla());
        panelPrincipal.add(selectorItemsForm);

        tablaItemsCompras = new ItemsComprasTable();
        aplicarFuenteRoboto(tablaItemsCompras);
        panelPrincipal.add(tablaItemsCompras);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        etiquetaTotal = new JLabel("Total: $0.00");
        etiquetaTotal.setFont(new Font("Roboto", Font.BOLD, 16));
        panelTotal.add(etiquetaTotal);
        botonGuardarCompra = new JButton("Guardar Compra");
        botonGuardarCompra.setFont(new Font("Roboto", Font.BOLD, 14));
        panelTotal.add(botonGuardarCompra);
        panelPrincipal.add(panelTotal);

        botonGuardarCompra.addActionListener(e -> guardarCompra());
    }

    private void buscarProveedor() {
        String idProveedor = selectorProveedorForm.getProveedorId();
        if (idProveedor.isEmpty()) {
            mostrarMensaje("Ingrese la cédula del proveedor.");
            return;
        }
        proveedorSeleccionado = controladorProveedores.buscarPorTerceroId(idProveedor);
        if (proveedorSeleccionado != null && proveedorSeleccionado.getTercero() != null) {
            selectorProveedorForm.setProveedorNombre(proveedorSeleccionado.getTercero().getNombre());
            return;
        }
        selectorProveedorForm.setProveedorNombre("");
        mostrarMensaje("Proveedor no encontrado.");
    }

    private void buscarItem() {
        String codigo = selectorItemsForm.getItemId();
        if (codigo.isEmpty()) {
            mostrarMensaje("Ingrese el código del producto.");
            return;
        }
        ItemEntity item = controladorItems.buscarPorCodigo(codigo);
        if (item != null) {
            selectorItemsForm.setItemNombre(item.getNombre());
            selectorItemsForm.setPrecioUnitario(String.valueOf(item.getPrecioUnitario()));
            return;
        }
        selectorItemsForm.setItemNombre("");
        selectorItemsForm.setPrecioUnitario("");
        mostrarMensaje("Producto no encontrado.");
    }

    private void agregarItemATabla() {
        String codigo = selectorItemsForm.getItemId();
        String nombre = selectorItemsForm.getItemNombre();
        String cantidadStr = selectorItemsForm.getCantidad();
        Double precioOriginal = selectorItemsForm.getPrecioUnitarioOriginal();
        if (codigo.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty() || precioOriginal == null) {
            mostrarMensaje("Completa todos los campos del producto.");
            return;
        }
        int cantidad = obtenerCantidadValida(cantidadStr);
        if (cantidad <= 0) {
            mostrarMensaje("La cantidad debe ser mayor a cero.");
            return;
        }
        BigDecimal precio = obtenerPrecioValido(precioOriginal);
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarMensaje("El precio debe ser mayor a cero.");
            return;
        }
        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
        tablaItemsCompras.getModeloTabla().addRow(new Object[]{codigo, nombre, cantidad, precio, subtotal});
        actualizarTotal();
        selectorItemsForm.setItemId("");
        selectorItemsForm.setItemNombre("");
        selectorItemsForm.setCantidad("");
        selectorItemsForm.setPrecioUnitario("");
    }

    private int obtenerCantidadValida(String cantidadStr) {
        try {
            return Integer.parseInt(cantidadStr);
        } catch (NumberFormatException ex) {
            mostrarMensaje("Cantidad inválida.");
            return 0;
        }
    }

    private BigDecimal obtenerPrecioValido(Double precioOriginal) {
        try {
            return BigDecimal.valueOf(precioOriginal);
        } catch (Exception ex) {
            mostrarMensaje("Precio inválido.");
            return BigDecimal.ZERO;
        }
    }

    private void guardarCompra() {
        if (proveedorSeleccionado == null) {
            mostrarMensaje("Debe seleccionar un proveedor.");
            return;
        }
        DefaultTableModel modeloTabla = tablaItemsCompras.getModeloTabla();
        if (modeloTabla.getRowCount() == 0) {
            mostrarMensaje("Debe agregar al menos un producto.");
            return;
        }
        BigDecimal totalCompra = BigDecimal.ZERO;
        List<CompraDetalleEntity> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String codigo = modeloTabla.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString().replace(",", ""));
            BigDecimal precio = new BigDecimal(modeloTabla.getValueAt(i, 3).toString().replace(",", ""));
            BigDecimal subtotal = new BigDecimal(modeloTabla.getValueAt(i, 4).toString().replace(",", ""));
            totalCompra = totalCompra.add(subtotal);
            ItemEntity item = controladorItems.buscarPorCodigo(codigo);
            CompraDetalleEntity detalle = CompraDetalleEntity.builder()
                    .cantidad(cantidad)
                    .precioUnitario(precio)
                    .subTotal(subtotal)
                    .item(item)
                    .build();
            detalles.add(detalle);
        }
        CompraEntity compra = CompraEntity.builder()
                .proveedor(proveedorSeleccionado)
                .totalCompra(totalCompra)
                .estado("PENDIENTE")
                .observaciones(campoObservaciones.getText())
                .build();
        try {
            compra = controladorCompras.crear(compra);
            for (CompraDetalleEntity detalle : detalles) {
                detalle.setCompra(compra);
                controladorDetallesCompra.crear(detalle);
                MovimientoInventarioEntity movimiento = MovimientoInventarioEntity.builder()
                        .tipoMovimiento("ENTRADA")
                        .referenciaId(compra.getCompraId())
                        .referenciaTipo((int)TipoReferencia.COMPRA.getValue())
                        .cantidad(detalle.getCantidad())
                        .item(detalle.getItem())
                        .bodega(detalle.getItem().getBodega())
                        .build();
                controladorMovimientosInventario.crear(movimiento);
            }
            mostrarMensaje("Compra guardada exitosamente.");
            tablaItemsCompras.limpiarTabla();
            actualizarTotal();
            selectorProveedorForm.setProveedorId("");
            selectorProveedorForm.setProveedorNombre("");
            proveedorSeleccionado = null;
            campoObservaciones.setText("");
        } catch (Exception ex) {
            mostrarMensaje("Error al guardar la compra: " + ex.getMessage());
        }
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        DefaultTableModel modeloTabla = tablaItemsCompras.getModeloTabla();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object subtotalObj = modeloTabla.getValueAt(i, 4);
            BigDecimal subtotal = subtotalObj instanceof BigDecimal ? (BigDecimal) subtotalObj : new BigDecimal(subtotalObj.toString().replace(",", ""));
            total = total.add(subtotal);
        }
        etiquetaTotal.setText("Total: $" + formatoMoneda.format(total));
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void aplicarFuenteRoboto(Component componente) {
        Font fuente = new Font("Roboto", Font.PLAIN, 14);
        if (componente instanceof JLabel) {
            ((JLabel) componente).setFont(fuente);
        } else if (componente instanceof JTextField) {
            ((JTextField) componente).setFont(fuente);
        } else if (componente instanceof JPanel) {
            for (Component hijo : ((JPanel) componente).getComponents()) {
                aplicarFuenteRoboto(hijo);
            }
        } else if (componente instanceof JButton) {
            ((JButton) componente).setFont(fuente);
        }
    }
}
