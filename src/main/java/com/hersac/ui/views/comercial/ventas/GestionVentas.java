package com.hersac.ui.views.comercial.ventas;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.controllers.movimientosinventarios.MovimientosInventariosController;
import com.hersac.ui.controllers.clientes.ClientesController;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.ui.controllers.ventas.VentasController;
import com.hersac.ui.controllers.ventasdetalles.VentasDetallesController;
import com.hersac.core.modules.ventas.entities.VentaEntity;
import com.hersac.core.modules.ventasdetalles.entities.VentaDetalleEntity;
import com.hersac.ui.views.comercial.ventas.forms.SelectorClienteForm;
import com.hersac.ui.views.comercial.ventas.forms.SelectorItemsForm;
import com.hersac.ui.views.comercial.ventas.tablas.itemsVentaTable;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.movimientosinventarios.constants.TipoReferencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GestionVentas extends JPanel {
    private SelectorClienteForm formularioCliente;
    private SelectorItemsForm formularioProducto;
    private itemsVentaTable tablaProductos;
    private JLabel etiquetaTotal;
    private JButton botonGuardar;
    private JTextField campoObservaciones;
    private ClienteEntity clienteSeleccionado;
    private ClientesController controladorClientes;
    private ItemsController controladorProductos;
    private VentasController controladorVentas;
    private VentasDetallesController controladorDetalles;
    private MovimientosInventariosController controladorInventario;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    public GestionVentas(DIContainer contenedor) {
        controladorClientes = contenedor.getClientesController();
        controladorProductos = contenedor.getItemsController();
        controladorVentas = contenedor.getVentasController();
        controladorDetalles = contenedor.getVentasDetallesController();
        controladorInventario = contenedor.getMovimientosInventariosController();
        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Ventas", SwingConstants.CENTER);
        titulo.setFont(new Font("Roboto", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        add(panelPrincipal, BorderLayout.CENTER);
        formularioCliente = new SelectorClienteForm();
        formularioCliente.setFuenteRoboto();
        formularioCliente.addBuscarListener(e -> buscarCliente());
        panelPrincipal.add(formularioCliente);
        JPanel panelObservaciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelObservaciones.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        JLabel etiquetaObservaciones = new JLabel("Observaciones:");
        etiquetaObservaciones.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelObservaciones.add(etiquetaObservaciones);
        campoObservaciones = new JTextField(40);
        campoObservaciones.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelObservaciones.add(campoObservaciones);
        panelPrincipal.add(panelObservaciones);
        formularioProducto = new SelectorItemsForm();
        formularioProducto.setFuenteRoboto();
        formularioProducto.addBuscarListener(e -> buscarProducto());
        formularioProducto.addAgregarListener(e -> agregarProductoATabla());
        panelPrincipal.add(formularioProducto);
        tablaProductos = new itemsVentaTable();
        tablaProductos.setFuenteRoboto();
        panelPrincipal.add(tablaProductos);
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        etiquetaTotal = new JLabel("Total: $0.00");
        etiquetaTotal.setFont(new Font("Roboto", Font.BOLD, 16));
        panelTotal.add(etiquetaTotal);
        botonGuardar = new JButton("Guardar Venta");
        botonGuardar.setFont(new Font("Roboto", Font.BOLD, 14));
        panelTotal.add(botonGuardar);
        panelPrincipal.add(panelTotal);
        botonGuardar.addActionListener(e -> guardarVenta());
    }

    private void buscarCliente() {
        String cedula = formularioCliente.getClienteId();
        if (cedula.isEmpty()) {
            mostrarMensaje("Ingrese la cédula del cliente.");
            return;
        }
        try {
            clienteSeleccionado = controladorClientes.buscarPorTerceroId(cedula);
            if (clienteSeleccionado != null && clienteSeleccionado.getTercero() != null) {
                formularioCliente.setClienteNombre(clienteSeleccionado.getTercero().getNombre());
                return;
            }
            formularioCliente.setClienteNombre("");
            mostrarMensaje("Cliente no encontrado.");
        } catch (Exception ex) {
            formularioCliente.setClienteNombre("");
            mostrarMensaje("Error al buscar cliente: " + ex.getMessage());
        }
    }

    private void buscarProducto() {
        String codigo = formularioProducto.getItemId();
        if (codigo.isEmpty()) {
            mostrarMensaje("Ingrese el código del producto.");
            return;
        }
        try {
            ItemEntity producto = controladorProductos.buscarPorCodigo(codigo);
            if (producto != null) {
                formularioProducto.setItemNombre(producto.getNombre());
                formularioProducto.setPrecioUnitario(String.valueOf(producto.getPrecioUnitario()));
                return;
            }
            formularioProducto.setItemNombre("");
            formularioProducto.setPrecioUnitario("");
            mostrarMensaje("Producto no encontrado.");
        } catch (Exception ex) {
            formularioProducto.setItemNombre("");
            formularioProducto.setPrecioUnitario("");
            mostrarMensaje("Error al buscar producto: " + ex.getMessage());
        }
    }

    private void agregarProductoATabla() {
        String codigo = formularioProducto.getItemId();
        String nombre = formularioProducto.getItemNombre();
        String cantidadStr = formularioProducto.getCantidad();
        Double precioOriginal = formularioProducto.getPrecioUnitarioOriginal();
        if (codigo.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty() || precioOriginal == null) {
            mostrarMensaje("Completa todos los campos del producto.");
            return;
        }
        int cantidad = obtenerCantidadValida(cantidadStr);
        if (cantidad <= 0) return;
        BigDecimal precio = obtenerPrecioValido(precioOriginal);
        if (precio == null) return;
        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
        tablaProductos.getModeloTabla().addRow(new Object[]{codigo, nombre, cantidad, precio, subtotal});
        actualizarTotal();
        formularioProducto.setItemId("");
        formularioProducto.setItemNombre("");
        formularioProducto.setCantidad("");
        formularioProducto.setPrecioUnitario("");
    }

    private int obtenerCantidadValida(String cantidadStr) {
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad > 0) return cantidad;
            mostrarMensaje("La cantidad debe ser mayor a cero.");
        } catch (NumberFormatException ex) {
            mostrarMensaje("Cantidad inválida.");
        }
        return -1;
    }

    private BigDecimal obtenerPrecioValido(Double precioOriginal) {
        try {
            BigDecimal precio = BigDecimal.valueOf(precioOriginal);
            if (precio.compareTo(BigDecimal.ZERO) > 0) return precio;
            mostrarMensaje("El precio debe ser mayor a cero.");
        } catch (Exception ex) {
            mostrarMensaje("Precio inválido.");
        }
        return null;
    }

    private void guardarVenta() {
        if (clienteSeleccionado == null) {
            mostrarMensaje("Debe seleccionar un cliente.");
            return;
        }
        DefaultTableModel modelo = tablaProductos.getModeloTabla();
        if (modelo.getRowCount() == 0) {
            mostrarMensaje("Debe agregar al menos un producto.");
            return;
        }
        BigDecimal totalVenta = BigDecimal.ZERO;
        List<VentaDetalleEntity> detalles = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String codigo = modelo.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 2).toString().replace(",", ""));
            BigDecimal precio = new BigDecimal(modelo.getValueAt(i, 3).toString().replace(",", ""));
            BigDecimal subtotal = new BigDecimal(modelo.getValueAt(i, 4).toString().replace(",", ""));
            totalVenta = totalVenta.add(subtotal);
            ItemEntity producto = controladorProductos.buscarPorCodigo(codigo);
            VentaDetalleEntity detalle = VentaDetalleEntity.builder()
                    .cantidad(cantidad)
                    .precioUnitario(precio)
                    .subTotal(subtotal)
                    .item(producto)
                    .build();
            detalles.add(detalle);
        }
        VentaEntity venta = VentaEntity.builder()
                .cliente(clienteSeleccionado)
                .totalVenta(totalVenta)
                .estado("PENDIENTE")
                .observaciones(campoObservaciones.getText())
                .build();
        try {
            venta = controladorVentas.crear(venta);
            for (VentaDetalleEntity detalle : detalles) {
                detalle.setVenta(venta);
                controladorDetalles.crear(detalle);
                MovimientoInventarioEntity movimiento = MovimientoInventarioEntity.builder()
                        .tipoMovimiento("SALIDA")
                        .referenciaId(venta.getVentaId())
                        .referenciaTipo((int)TipoReferencia.COMPRA.getValue())
                        .cantidad(detalle.getCantidad())
                        .item(detalle.getItem())
                        .bodega(detalle.getItem().getBodega())
                        .build();
                controladorInventario.crear(movimiento);
            }
            mostrarMensaje("Venta guardada exitosamente.");
            tablaProductos.limpiarTabla();
            actualizarTotal();
            formularioCliente.setClienteId("");
            formularioCliente.setClienteNombre("");
            clienteSeleccionado = null;
            campoObservaciones.setText("");
        } catch (Exception ex) {
            mostrarMensaje("Error al guardar la venta: " + ex.getMessage());
        }
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        DefaultTableModel modelo = tablaProductos.getModeloTabla();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object subtotalObj = modelo.getValueAt(i, 4);
            BigDecimal subtotal;
            if (subtotalObj instanceof BigDecimal) {
                subtotal = (BigDecimal) subtotalObj;
            } else {
                subtotal = new BigDecimal(subtotalObj.toString().replace(",", ""));
            }
            total = total.add(subtotal);
        }
        etiquetaTotal.setText("Total: $" + formatoMoneda.format(total));
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
