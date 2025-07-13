package com.hersac.ui.views.comercial.inventario.contenidos.movimientosinventarios;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.ui.controllers.movimientosinventarios.MovimientosInventariosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltrosMovimientosForm;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarMovimiento;
import com.hersac.ui.views.comercial.inventario.tablas.MovimientosInventarioTable;

public class GestionMovimientosInventarios extends JPanel {
    private final MovimientosInventariosController movimientosController;
    private final MovimientosInventarioTable tablaMovimientosTable = new MovimientosInventarioTable();
    private List<MovimientoInventarioEntity> listaCompletaMovimientos;
    private JTextField searchField;
    private final DIContainer diContainer;
    private FiltrosMovimientosForm filtrosMovimientosForm;

    public GestionMovimientosInventarios(DIContainer diContainer) {
        this.diContainer = diContainer;
        this.movimientosController = diContainer.getMovimientosInventariosController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Movimientos de Inventario");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosMovimientosForm = new FiltrosMovimientosForm();
        filtrosMovimientosForm.setOnFiltrosCambiados(this::filtrarMovimientos);
        JButton registrarBtn = new JButton("Registrar Movimiento");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        org.kordamp.ikonli.swing.FontIcon iconoPlus = org.kordamp.ikonli.swing.FontIcon.of(org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(iconoPlus);
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarMovimientos();
            }
        });
        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.setOpaque(false);
        panelBtn.setPreferredSize(new Dimension(1200, 40));
        panelBtn.setMaximumSize(new Dimension(1200, 40));
        panelBtn.add(searchField);
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(registrarBtn);
        JPanel panelBtnExpansible = new JPanel();
        panelBtnExpansible.setLayout(new BoxLayout(panelBtnExpansible, BoxLayout.X_AXIS));
        panelBtnExpansible.setOpaque(false);
        panelBtnExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.add(Box.createHorizontalGlue());
        panelBtnExpansible.add(panelBtn);
        panelBtnExpansible.add(Box.createHorizontalGlue());
        JScrollPane scrollPane = new JScrollPane(tablaMovimientosTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosMovimientosForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());
        List<MovimientoInventarioEntity> listaMovimientos = obtenerMovimientos();
        this.listaCompletaMovimientos = listaMovimientos;
        tablaMovimientosTable.setMovimientos(listaMovimientos);
        registrarBtn.addActionListener(e -> {
            RegistrarMovimiento modal = new RegistrarMovimiento(
                (JFrame) SwingUtilities.getWindowAncestor(this), null, diContainer
            );
            MovimientoInventarioEntity nuevoMovimiento = modal.getMovimientoRegistrado();
            if (nuevoMovimiento != null) {
                movimientosController.crear(nuevoMovimiento);
                listaCompletaMovimientos = obtenerMovimientos();
                tablaMovimientosTable.setMovimientos(listaCompletaMovimientos);
            }
        });

        add(filtrosMovimientosForm);
    }

    private List<MovimientoInventarioEntity> obtenerMovimientos() {
        return movimientosController.buscarTodos();
    }

    public DIContainer getDIContainer() {
        return diContainer;
    }

    private void filtrarMovimientos() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String tipoMovimiento = filtrosMovimientosForm.getTipoMovimientoSeleccionado();
        final String producto = filtrosMovimientosForm.getProductoSeleccionado();
        final String bodega = filtrosMovimientosForm.getBodegaSeleccionada();
        final String usuario = filtrosMovimientosForm.getUsuarioSeleccionado();
        final String tipoReferencia = filtrosMovimientosForm.getTipoReferenciaSeleccionado();
        final java.time.LocalDate desde = filtrosMovimientosForm.getFechaDesde();
        final java.time.LocalDate hasta = filtrosMovimientosForm.getFechaHasta();
        List<MovimientoInventarioEntity> filtrados = listaCompletaMovimientos.stream()
                .filter(m -> {
                    String idStr = String.valueOf(m.getMovimientoInventarioId());
                    String tipo = m.getTipoMovimiento() != null ? m.getTipoMovimiento().toLowerCase() : "";
                    String item = m.getItem() != null ? m.getItem().getNombre().toLowerCase() : "";
                    String bodegaStr = m.getBodega() != null ? m.getBodega().getNombre().toLowerCase() : "";
                    String usuarioStr = m.getUsuarioCreacion() != null ? m.getUsuarioCreacion().getNombre().toLowerCase() : "";
                    String referenciaStr = null;
                    switch(m.getReferenciaTipo()) {
                        case 1: referenciaStr = "COMPRA"; break;
                        case 2: referenciaStr = "VENTA"; break;
                        default: referenciaStr = "";
                    }
                    boolean coincideTexto = texto.isEmpty()
                        || idStr.contains(texto)
                        || tipo.contains(texto)
                        || item.contains(texto)
                        || bodegaStr.contains(texto)
                        || usuarioStr.contains(texto)
                        || referenciaStr.toLowerCase().contains(texto);
                    boolean coincideTipo = tipoMovimiento.equals("Todos") || tipo.equalsIgnoreCase(tipoMovimiento.toLowerCase());
                    boolean coincideProducto = producto.equals("Todos") || item.equalsIgnoreCase(producto.toLowerCase());
                    boolean coincideBodega = bodega.equals("Todas") || bodegaStr.equalsIgnoreCase(bodega.toLowerCase());
                    boolean coincideUsuario = usuario.equals("Todos") || usuarioStr.equalsIgnoreCase(usuario.toLowerCase());
                    boolean coincideReferencia = tipoReferencia.equals("Todos") || referenciaStr.equalsIgnoreCase(tipoReferencia);
                    boolean coincideFecha = true;
                    if (desde != null && hasta != null && m.getFechaCreacion() != null) {
                        java.time.LocalDate fecha = m.getFechaCreacion().toLocalDate();
                        coincideFecha = (fecha.compareTo(desde) >= 0 && fecha.compareTo(hasta) <= 0);
                    } else if (desde != null && m.getFechaCreacion() != null) {
                        java.time.LocalDate fecha = m.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(desde) >= 0;
                    } else if (hasta != null && m.getFechaCreacion() != null) {
                        java.time.LocalDate fecha = m.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(hasta) <= 0;
                    }
                    return coincideTexto && coincideTipo && coincideProducto && coincideBodega && coincideUsuario && coincideReferencia && coincideFecha;
                })
                .collect(Collectors.toList());
        tablaMovimientosTable.setMovimientos(filtrados);
    }
}
