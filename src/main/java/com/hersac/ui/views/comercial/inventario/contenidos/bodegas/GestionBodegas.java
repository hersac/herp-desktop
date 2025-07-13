package com.hersac.ui.views.comercial.inventario.contenidos.bodegas;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.ui.controllers.bodegas.BodegasController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.comercial.inventario.forms.FiltrosBodegasForm;
import com.hersac.ui.views.comercial.inventario.listeners.BodegasListeners;
import com.hersac.ui.views.comercial.inventario.tablas.BodegasTable;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarBodega;

public class GestionBodegas extends JPanel implements BodegasListeners {
    private final BodegasController controladorBodegas;
    private final BodegasTable tablaBodegas = new BodegasTable();
    private List<BodegaEntity> listaBodegas;
    private JTextField campoBusqueda;
    private final DIContainer contenedorDI;
    private FiltrosBodegasForm formularioFiltros;

    public GestionBodegas(DIContainer diContainer) {
        this.contenedorDI = diContainer;
        this.controladorBodegas = diContainer.getBodegasController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Bodegas");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosBodegasForm();
        formularioFiltros.establecerAlCambiarFiltros(this::filtrarBodegas);
        JButton botonRegistrar = new JButton("Registrar Bodega");
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        FontIcon iconoPlus = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
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
                filtrarBodegas();
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
        JScrollPane panelScroll = new JScrollPane(tablaBodegas);
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
        listaBodegas = obtenerBodegas();
        tablaBodegas.setBodegas(listaBodegas);
        tablaBodegas.setActionListener(this);
        botonRegistrar.addActionListener(e -> {
            JFrame padre = (JFrame) SwingUtilities.getWindowAncestor(this);
            new RegistrarBodega(padre, contenedorDI, this);
        });
    }

    private List<BodegaEntity> obtenerBodegas() {
        return controladorBodegas.buscarTodos();
    }

    private void filtrarBodegas() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.obtenerEstadoSeleccionado();
        String desde = formularioFiltros.obtenerFechaDesde();
        String hasta = formularioFiltros.obtenerFechaHasta();
        List<BodegaEntity> filtrados = listaBodegas.stream()
                .filter(b -> {
                    String idStr = String.valueOf(b.getBodegaId());
                    String nombre = b.getNombre() != null ? b.getNombre().toLowerCase() : "";
                    String estadoBodega = Boolean.TRUE.equals(b.getEstaActiva()) ? "Activas" : "Inactivas";
                    boolean coincideTexto = texto.isEmpty()
                        || idStr.contains(texto)
                        || nombre.contains(texto)
                        || estadoBodega.toLowerCase().contains(texto);
                    boolean coincideEstado = estado.equals("Todos") || estadoBodega.equals(estado);
                    boolean coincideFecha = true;
                    if (desde != null && hasta != null && b.getFechaCreacion() != null) {
                        String fecha = b.getFechaCreacion().toString();
                        coincideFecha = (fecha.compareTo(desde) >= 0 && fecha.compareTo(hasta) <= 0);
                    }
                    return coincideTexto && coincideEstado && coincideFecha;
                })
                .collect(Collectors.toList());
        tablaBodegas.setBodegas(filtrados);
    }

    @Override
    public void crearBodega(BodegaEntity bodega) {
        controladorBodegas.crear(bodega);
        listaBodegas = obtenerBodegas();
        tablaBodegas.setBodegas(listaBodegas);
    }

    @Override
    public void eliminarBodega(BodegaEntity bodega) {
        controladorBodegas.eliminar(bodega.getBodegaId());
        listaBodegas = obtenerBodegas();
        tablaBodegas.setBodegas(listaBodegas);
    }

    @Override
    public void actualizarBodega(BodegaEntity bodega) {
        BodegaEntity original = controladorBodegas.buscarPorId(bodega.getBodegaId());
        if (original != null) {
            controladorBodegas.actualizar(original.getBodegaId(), bodega);
        }
        listaBodegas = obtenerBodegas();
        tablaBodegas.setBodegas(listaBodegas);
    }

    @Override
    public void verBodega(BodegaEntity bodega) {
        // ...sin implementación...
    }

    public DIContainer getDIContainer() {
        return contenedorDI;
    }
}
