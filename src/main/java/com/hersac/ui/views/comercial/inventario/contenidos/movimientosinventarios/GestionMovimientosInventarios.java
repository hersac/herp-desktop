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
    private final MovimientosInventariosController controladorMovimientos;
    private final MovimientosInventarioTable tablaMovimientos = new MovimientosInventarioTable();
    private List<MovimientoInventarioEntity> listaMovimientos;
    private JTextField campoBusqueda;
    private final DIContainer contenedorDI;
    private FiltrosMovimientosForm formularioFiltros;

    public GestionMovimientosInventarios(DIContainer diContainer) {
        this.contenedorDI = diContainer;
        this.controladorMovimientos = diContainer.getMovimientosInventariosController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Movimientos de Inventario");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosMovimientosForm();
        formularioFiltros.establecerAlCambiarFiltros(this::filtrarMovimientos);
        JButton botonRegistrar = new JButton("Registrar Movimiento");
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        org.kordamp.ikonli.swing.FontIcon iconoPlus = org.kordamp.ikonli.swing.FontIcon.of(org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(iconoPlus);
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarMovimientos();
            }
        });
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(1200, 40));
        panelBotones.setMaximumSize(new Dimension(1200, 40));
        panelBotones.add(campoBusqueda);
        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(botonRegistrar);
        JPanel panelBotonesExpansible = new JPanel();
        panelBotonesExpansible.setLayout(new BoxLayout(panelBotonesExpansible, BoxLayout.X_AXIS));
        panelBotonesExpansible.setOpaque(false);
        panelBotonesExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        panelBotonesExpansible.add(panelBotones);
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        JScrollPane panelScroll = new JScrollPane(tablaMovimientos);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll.setAlignmentX(CENTER_ALIGNMENT);
        panelScroll.setPreferredSize(new Dimension(1200, 400));
        panelScroll.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formularioFiltros);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBotonesExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelScroll);
        add(Box.createVerticalGlue());
        listaMovimientos = obtenerMovimientos();
        tablaMovimientos.establecerMovimientos(listaMovimientos);
        botonRegistrar.addActionListener(e -> {
            RegistrarMovimiento modal = new RegistrarMovimiento(
                (JFrame) SwingUtilities.getWindowAncestor(this), null, contenedorDI
            );
            MovimientoInventarioEntity nuevoMovimiento = modal.getMovimientoRegistrado();
            if (nuevoMovimiento != null) {
                controladorMovimientos.crear(nuevoMovimiento);
                listaMovimientos = obtenerMovimientos();
                tablaMovimientos.establecerMovimientos(listaMovimientos);
            }
        });
    }

    private List<MovimientoInventarioEntity> obtenerMovimientos() {
        return controladorMovimientos.buscarTodos();
    }

    private void filtrarMovimientos() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String tipoMovimiento = formularioFiltros.obtenerTipoMovimientoSeleccionado();
        String producto = formularioFiltros.obtenerProductoSeleccionado();
        String bodega = formularioFiltros.obtenerBodegaSeleccionada();
        String usuario = formularioFiltros.obtenerUsuarioSeleccionado();
        String tipoReferencia = formularioFiltros.obtenerTipoReferenciaSeleccionado();
        java.time.LocalDate desde = formularioFiltros.obtenerFechaDesde();
        java.time.LocalDate hasta = formularioFiltros.obtenerFechaHasta();
        List<MovimientoInventarioEntity> filtrados = listaMovimientos.stream()
                .filter(m -> {
                    String idStr = String.valueOf(m.getMovimientoInventarioId());
                    String tipo = m.getTipoMovimiento() != null ? m.getTipoMovimiento().toLowerCase() : "";
                    String item = m.getItem() != null ? m.getItem().getNombre().toLowerCase() : "";
                    String bodegaStr = m.getBodega() != null ? m.getBodega().getNombre().toLowerCase() : "";
                    String usuarioStr = m.getUsuarioCreacion() != null ? m.getUsuarioCreacion().getNombre().toLowerCase() : "";
                    String referenciaStr = m.getReferenciaTipo() == 1 ? "COMPRA" : m.getReferenciaTipo() == 2 ? "VENTA" : "";
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
                    }
                    if (desde != null && m.getFechaCreacion() != null && hasta == null) {
                        java.time.LocalDate fecha = m.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(desde) >= 0;
                    }
                    if (hasta != null && m.getFechaCreacion() != null && desde == null) {
                        java.time.LocalDate fecha = m.getFechaCreacion().toLocalDate();
                        coincideFecha = fecha.compareTo(hasta) <= 0;
                    }
                    return coincideTexto && coincideTipo && coincideProducto && coincideBodega && coincideUsuario && coincideReferencia && coincideFecha;
                })
                .collect(Collectors.toList());
        tablaMovimientos.establecerMovimientos(filtrados);
    }
}
