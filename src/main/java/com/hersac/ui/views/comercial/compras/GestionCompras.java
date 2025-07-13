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
import com.hersac.ui.views.comercial.compras.listeners.ComprasListerns;
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

public class GestionCompras extends JPanel implements ComprasListerns {
    private SelectorProveedorForm selectorProveedorForm;
    private SelectorItemsForm selectorItemsForm;
    private ItemsComprasTable itemsComprasTable;
    private JLabel totalLabel;
    private JButton guardarCompraBtn;
    private JTextField observacionesField;
    private ProveedorEntity proveedorSeleccionado;
    private ItemEntity itemSeleccionado;
    private ProveedoresController proveedorController;
    private ItemsController itemsController;
    private ComprasController comprasController;
    private ComprasDetallesController comprasDetallesController;
    private MovimientosInventariosController movimientosInventarioController;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    public GestionCompras(DIContainer container) {
        this.proveedorController = container.getProveedoresController();
        this.itemsController = container.getItemsController();
        this.comprasController = container.getComprasController();
        this.comprasDetallesController = container.getComprasDetallesController();
        this.movimientosInventarioController = container.getMovimientosInventariosController();

        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Compras", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        add(panelPrincipal, BorderLayout.CENTER);

        selectorProveedorForm = new SelectorProveedorForm();
        selectorProveedorForm.addBuscarListener(e -> {
            String terceroId = selectorProveedorForm.getProveedorId();
            if (terceroId.isEmpty()) {
                JOptionPane.showMessageDialog(GestionCompras.this, "Ingrese la cédula del proveedor.");
                return;
            }
            try {
                proveedorSeleccionado = proveedorController.buscarPorTerceroId(terceroId);
                if (proveedorSeleccionado != null && proveedorSeleccionado.getTercero() != null) {
                    selectorProveedorForm.setProveedorNombre(proveedorSeleccionado.getTercero().getNombre());
                } else {
                    selectorProveedorForm.setProveedorNombre("");
                    JOptionPane.showMessageDialog(GestionCompras.this, "Proveedor no encontrado.");
                }
            } catch (Exception ex) {
                selectorProveedorForm.setProveedorNombre("");
                JOptionPane.showMessageDialog(GestionCompras.this, "Error al buscar proveedor: " + ex.getMessage());
            }
        });
        panelPrincipal.add(selectorProveedorForm);

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
                JOptionPane.showMessageDialog(GestionCompras.this, "Ingrese el código del producto.");
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
                    JOptionPane.showMessageDialog(GestionCompras.this, "Producto no encontrado.");
                }
            } catch (Exception ex) {
                selectorItemsForm.setItemNombre("");
                selectorItemsForm.setPrecioUnitario("");
                JOptionPane.showMessageDialog(GestionCompras.this, "Error al buscar producto: " + ex.getMessage());
            }
        });
        selectorItemsForm.addAgregarListener(e -> {
            String codigo = selectorItemsForm.getItemId();
            String nombre = selectorItemsForm.getItemNombre();
            String cantidadStr = selectorItemsForm.getCantidad();
            Double precioOriginal = selectorItemsForm.getPrecioUnitarioOriginal();
            if (codigo.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty() || precioOriginal == null) {
                JOptionPane.showMessageDialog(GestionCompras.this, "Completa todos los campos del producto.");
                return;
            }
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(GestionCompras.this, "La cantidad debe ser mayor a cero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GestionCompras.this, "Cantidad inválida.");
                return;
            }
            BigDecimal precio;
            try {
                precio = BigDecimal.valueOf(precioOriginal);
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(GestionCompras.this, "El precio debe ser mayor a cero.");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GestionCompras.this, "Precio inválido.");
                return;
            }
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
            itemsComprasTable.getModeloTabla().addRow(new Object[]{codigo, nombre, cantidad, precio, subtotal});
            actualizarTotal();
            selectorItemsForm.setItemId("");
            selectorItemsForm.setItemNombre("");
            selectorItemsForm.setCantidad("");
            selectorItemsForm.setPrecioUnitario("");
        });
        panelPrincipal.add(selectorItemsForm);

        itemsComprasTable = new ItemsComprasTable();
        JTable tabla = itemsComprasTable.getTablaItems();
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
        panelPrincipal.add(itemsComprasTable);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        totalPanel.add(totalLabel);
        guardarCompraBtn = new JButton("Guardar Compra");
        totalPanel.add(guardarCompraBtn);
        panelPrincipal.add(totalPanel);

        guardarCompraBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (proveedorSeleccionado == null) {
                    JOptionPane.showMessageDialog(GestionCompras.this, "Debe seleccionar un proveedor.");
                    return;
                }
                DefaultTableModel modeloTabla = itemsComprasTable.getModeloTabla();
                if (modeloTabla.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(GestionCompras.this, "Debe agregar al menos un producto.");
                    return;
                }
                BigDecimal totalCompra = BigDecimal.ZERO;
                List<CompraDetalleEntity> detalles = new ArrayList<>();
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    String codigo = modeloTabla.getValueAt(i, 0).toString();
                    String nombre = modeloTabla.getValueAt(i, 1).toString();
                    int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString().replace(",", ""));
                    BigDecimal precio = new BigDecimal(modeloTabla.getValueAt(i, 3).toString().replace(",", ""));
                    BigDecimal subtotal = new BigDecimal(modeloTabla.getValueAt(i, 4).toString().replace(",", ""));
                    totalCompra = totalCompra.add(subtotal);
                    ItemEntity item = itemsController.buscarPorCodigo(codigo);
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
                    .observaciones(observacionesField.getText())
                    .build();
                try {
                    compra = comprasController.crear(compra);
                    for (CompraDetalleEntity detalle : detalles) {
                        detalle.setCompra(compra);
                        comprasDetallesController.crear(detalle);
                        MovimientoInventarioEntity movimiento = MovimientoInventarioEntity.builder()
                            .tipoMovimiento("ENTRADA")
                            .referenciaId(compra.getCompraId())
                            .referenciaTipo((int)TipoReferencia.COMPRA.getValue())
                            .cantidad(detalle.getCantidad())
                            .item(detalle.getItem())
                            .bodega(detalle.getItem().getBodega())
                            .build();
                        movimientosInventarioController.crear(movimiento);
                    }
                    JOptionPane.showMessageDialog(GestionCompras.this, "Compra guardada exitosamente.");
                    itemsComprasTable.limpiarTabla();
                    actualizarTotal();
                    selectorProveedorForm.setProveedorId("");
                    selectorProveedorForm.setProveedorNombre("");
                    proveedorSeleccionado = null;
                    observacionesField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GestionCompras.this, "Error al guardar la compra: " + ex.getMessage());
                }
            }
        });
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        DefaultTableModel modeloTabla = itemsComprasTable.getModeloTabla();
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

    @Override
    public void onProveedorSeleccionado(String proveedorId) {}
    @Override
    public void onItemSeleccionado(String itemCodigo) {}
    @Override
    public void onAgregarItem(String codigo, String nombre, int cantidad, String precioUnitario) {}
}
