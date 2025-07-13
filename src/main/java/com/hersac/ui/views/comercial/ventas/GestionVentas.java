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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GestionVentas extends JPanel {
    private SelectorClienteForm selectorClienteForm;
    private SelectorItemsForm selectorItemsForm;
    private itemsVentaTable itemsVentaTable;
    private JLabel totalLabel;
    private JButton guardarVentaBtn;
    private JTextField observacionesField;
    private ClienteEntity clienteSeleccionado;
    private ItemEntity itemSeleccionado;
    private ClientesController clientesController;
    private ItemsController itemsController;
    private VentasController ventasController;
    private VentasDetallesController ventasDetallesController;
    private MovimientosInventariosController movimientosInventarioController;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    public GestionVentas(DIContainer container) {
        this.clientesController = container.getClientesController();
        this.itemsController = container.getItemsController();
        this.ventasController = container.getVentasController();
        this.ventasDetallesController = container.getVentasDetallesController();
        this.movimientosInventarioController = container.getMovimientosInventariosController();

        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Ventas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        add(panelPrincipal, BorderLayout.CENTER);

        selectorClienteForm = new SelectorClienteForm();
        selectorClienteForm.addBuscarListener(e -> {
            String clienteId = selectorClienteForm.getClienteId();
            if (clienteId.isEmpty()) {
                JOptionPane.showMessageDialog(GestionVentas.this, "Ingrese la cédula del cliente.");
                return;
            }
            try {
                clienteSeleccionado = clientesController.buscarPorTerceroId(clienteId);
                if (clienteSeleccionado != null && clienteSeleccionado.getTercero() != null) {
                    selectorClienteForm.setClienteNombre(clienteSeleccionado.getTercero().getNombre());
                } else {
                    selectorClienteForm.setClienteNombre("");
                    JOptionPane.showMessageDialog(GestionVentas.this, "Cliente no encontrado.");
                }
            } catch (Exception ex) {
                selectorClienteForm.setClienteNombre("");
                JOptionPane.showMessageDialog(GestionVentas.this, "Error al buscar cliente: " + ex.getMessage());
            }
        });
        panelPrincipal.add(selectorClienteForm);

        JPanel observacionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        observacionesPanel.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        observacionesPanel.add(new JLabel("Observaciones:"));
        observacionesField = new JTextField(40);
        observacionesPanel.add(observacionesField);
        panelPrincipal.add(observacionesPanel);

        selectorItemsForm = new SelectorItemsForm();
        selectorItemsForm.addBuscarListener(e -> {
            String codigo = selectorItemsForm.getItemId();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(GestionVentas.this, "Ingrese el código del producto.");
                return;
            }
            try {
                itemSeleccionado = itemsController.buscarPorCodigo(codigo);
                if (itemSeleccionado != null) {
                    selectorItemsForm.setItemNombre(itemSeleccionado.getNombre());
                    selectorItemsForm.setPrecioUnitario(String.valueOf(itemSeleccionado.getPrecioUnitario()));
                } else {
                    selectorItemsForm.setItemNombre("");
                    selectorItemsForm.setPrecioUnitario("");
                    JOptionPane.showMessageDialog(GestionVentas.this, "Producto no encontrado.");
                }
            } catch (Exception ex) {
                selectorItemsForm.setItemNombre("");
                selectorItemsForm.setPrecioUnitario("");
                JOptionPane.showMessageDialog(GestionVentas.this, "Error al buscar producto: " + ex.getMessage());
            }
        });
        selectorItemsForm.addAgregarListener(e -> {
            String codigo = selectorItemsForm.getItemId();
            String nombre = selectorItemsForm.getItemNombre();
            String cantidadStr = selectorItemsForm.getCantidad();
            Double precioOriginal = selectorItemsForm.getPrecioUnitarioOriginal();
            if (codigo.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty() || precioOriginal == null) {
                JOptionPane.showMessageDialog(GestionVentas.this, "Completa todos los campos del producto.");
                return;
            }
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(GestionVentas.this, "La cantidad debe ser mayor a cero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GestionVentas.this, "Cantidad inválida.");
                return;
            }
            BigDecimal precio;
            try {
                precio = BigDecimal.valueOf(precioOriginal);
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(GestionVentas.this, "El precio debe ser mayor a cero.");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GestionVentas.this, "Precio inválido.");
                return;
            }
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
            itemsVentaTable.getModeloTabla().addRow(new Object[]{codigo, nombre, cantidad, precio, subtotal});
            actualizarTotal();
            selectorItemsForm.setItemId("");
            selectorItemsForm.setItemNombre("");
            selectorItemsForm.setCantidad("");
            selectorItemsForm.setPrecioUnitario("");
        });
        panelPrincipal.add(selectorItemsForm);

        itemsVentaTable = new itemsVentaTable();
        JTable tabla = itemsVentaTable.getTablaItems();
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                int col = tabla.getSelectedColumn();
                if (col == 2 || col == 3 || col == 4) {
                    if (value instanceof Number) {
                        setText(formatoMoneda.format(((Number) value).doubleValue()));
                    } else {
                        try {
                            setText(formatoMoneda.format(Double.parseDouble(value.toString())));
                        } catch (Exception e) {
                            setText(value != null ? value.toString() : "");
                        }
                    }
                } else {
                    setText(value != null ? value.toString() : "");
                }
            }
        });
        panelPrincipal.add(itemsVentaTable);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        totalPanel.add(totalLabel);
        guardarVentaBtn = new JButton("Guardar Venta");
        totalPanel.add(guardarVentaBtn);
        panelPrincipal.add(totalPanel);

        guardarVentaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clienteSeleccionado == null) {
                    JOptionPane.showMessageDialog(GestionVentas.this, "Debe seleccionar un cliente.");
                    return;
                }
                DefaultTableModel modeloTabla = itemsVentaTable.getModeloTabla();
                if (modeloTabla.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(GestionVentas.this, "Debe agregar al menos un producto.");
                    return;
                }
                BigDecimal totalVenta = BigDecimal.ZERO;
                List<VentaDetalleEntity> detalles = new ArrayList<>();
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    String codigo = modeloTabla.getValueAt(i, 0).toString();
                    String nombre = modeloTabla.getValueAt(i, 1).toString();
                    int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString().replace(",", ""));
                    BigDecimal precio = new BigDecimal(modeloTabla.getValueAt(i, 3).toString().replace(",", ""));
                    BigDecimal subtotal = new BigDecimal(modeloTabla.getValueAt(i, 4).toString().replace(",", ""));
                    totalVenta = totalVenta.add(subtotal);
                    ItemEntity item = itemsController.buscarPorCodigo(codigo);
                    VentaDetalleEntity detalle = VentaDetalleEntity.builder()
                            .cantidad(cantidad)
                            .precioUnitario(precio)
                            .subTotal(subtotal)
                            .item(item)
                            .build();
                    detalles.add(detalle);
                }
                VentaEntity venta = VentaEntity.builder()
                    .cliente(clienteSeleccionado)
                    .totalVenta(totalVenta)
                    .estado("PENDIENTE")
                    .observaciones(observacionesField.getText())
                    .build();
                try {
                    venta = ventasController.crear(venta);
                    for (VentaDetalleEntity detalle : detalles) {
                        detalle.setVenta(venta);
                        ventasDetallesController.crear(detalle);
                        MovimientoInventarioEntity movimiento = MovimientoInventarioEntity.builder()
                            .tipoMovimiento("SALIDA")
                            .referenciaId(venta.getVentaId())
                            .referenciaTipo((int)TipoReferencia.COMPRA.getValue())
                            .cantidad(detalle.getCantidad())
                            .item(detalle.getItem())
                            .bodega(detalle.getItem().getBodega())
                            .build();
                        movimientosInventarioController.crear(movimiento);
                    }
                    JOptionPane.showMessageDialog(GestionVentas.this, "Venta guardada exitosamente.");
                    itemsVentaTable.limpiarTabla();
                    actualizarTotal();
                    selectorClienteForm.setClienteId("");
                    selectorClienteForm.setClienteNombre("");
                    clienteSeleccionado = null;
                    observacionesField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GestionVentas.this, "Error al guardar la venta: " + ex.getMessage());
                }
            }
        });
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        DefaultTableModel modeloTabla = itemsVentaTable.getModeloTabla();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object subtotalObj = modeloTabla.getValueAt(i, 4);
            BigDecimal subtotal;
            if (subtotalObj instanceof BigDecimal) {
                subtotal = (BigDecimal) subtotalObj;
            } else {
                subtotal = new BigDecimal(subtotalObj.toString().replace(",", ""));
            }
            total = total.add(subtotal);
        }
        totalLabel.setText("Total: $" + formatoMoneda.format(total));
    }
}
