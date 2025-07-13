package com.hersac.ui.views.terceros;

import com.hersac.core.di.DIContainer;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.terceros.forms.FiltrosTercerosForm;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;
import com.hersac.ui.views.terceros.modales.RegistrarTercero;
import com.hersac.ui.views.terceros.tablas.TercerosTable;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GestionTerceros extends JPanel implements TercerosListeners {
    private final TercerosController controladorTerceros;
    private final PermissionService servicioPermisos;
    private JFrame ventana = new JFrame("Registrar Tercero");
    private final TercerosTable tablaTerceros;
    private List<TerceroEntity> listaTerceros;
    private FiltrosTercerosForm formularioFiltros;
    private JTextField campoBusqueda;

    public GestionTerceros(DIContainer contenedorDI) {
        this.controladorTerceros = contenedorDI.getTercerosController();
        this.servicioPermisos = contenedorDI.getPermissionService();
        this.tablaTerceros = new TercerosTable(servicioPermisos);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Terceros");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosTercerosForm();
        formularioFiltros.setOnFiltrosCambiados(this::filtrarTerceros);
        JButton botonRegistrar = new JButton("Registrar Tercero");
        FontIcon iconoAgregar = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(iconoAgregar);
        botonRegistrar.addActionListener(e -> new RegistrarTercero(ventana, this, null));
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTerceros();
            }
        });
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(900, 40));
        panelBotones.setMaximumSize(new Dimension(900, 40));
        panelBotones.add(campoBusqueda);
        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(botonRegistrar);
        JPanel panelBotonesExpandible = new JPanel();
        panelBotonesExpandible.setLayout(new BoxLayout(panelBotonesExpandible, BoxLayout.X_AXIS));
        panelBotonesExpandible.setOpaque(false);
        panelBotonesExpandible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpandible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpandible.add(Box.createHorizontalGlue());
        panelBotonesExpandible.add(panelBotones);
        panelBotonesExpandible.add(Box.createHorizontalGlue());
        JScrollPane panelScroll = new JScrollPane(tablaTerceros);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll.setAlignmentX(CENTER_ALIGNMENT);
        panelScroll.setPreferredSize(new Dimension(900, 400));
        panelScroll.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formularioFiltros);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBotonesExpandible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelScroll);
        add(Box.createVerticalGlue());
        this.listaTerceros = obtenerTerceros();
        tablaTerceros.setTerceros(listaTerceros);
        tablaTerceros.setEscuchadorTerceros(this);
        aplicarFuenteRoboto();
    }

    private void aplicarFuenteRoboto() {
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);
        for (Component componente : getComponents()) {
            if (componente instanceof JLabel label) {
                label.setFont(fuenteRoboto);
            }
            if (componente instanceof JTextField textField) {
                textField.setFont(fuenteRoboto);
            }
        }
    }

    private void filtrarTerceros() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.getEstadoSeleccionado();
        TipoPersonaEntity tipoSeleccionado = formularioFiltros.getTipoSeleccionado();
        Integer tipoIdSeleccionado = tipoSeleccionado != null ? tipoSeleccionado.getTipoPersonaId() : null;
        List<TerceroEntity> filtrados = listaTerceros.stream()
            .filter(tercero -> {
                String idStr = String.valueOf(tercero.getTerceroId());
                String nombre = tercero.getNombre() != null ? tercero.getNombre().toLowerCase() : "";
                String estadoTercero = tercero.getEstado() != null ? tercero.getEstado().toLowerCase() : "";
                Integer tipoId = tercero.getTipoPersona() != null ? tercero.getTipoPersona().getTipoPersonaId() : null;
                String tipoNombre = tercero.getTipoPersona() != null && tercero.getTipoPersona().getNombre() != null ? tercero.getTipoPersona().getNombre().toLowerCase() : "";
                boolean coincideTexto = texto.isEmpty() || idStr.contains(texto) || nombre.contains(texto) || estadoTercero.contains(texto) || tipoNombre.contains(texto);
                boolean coincideEstado = "Todos".equals(estado) || estadoTercero.equals(estado.toLowerCase());
                boolean coincideTipo = tipoIdSeleccionado == null || (tipoId != null && tipoIdSeleccionado.equals(tipoId));
                return coincideTexto && coincideEstado && coincideTipo;
            })
            .toList();
        tablaTerceros.setTerceros(filtrados);
    }

    @Override
    public void crearTercero(TerceroEntity tercero) {
        controladorTerceros.crear(tercero);
        this.listaTerceros = obtenerTerceros();
        tablaTerceros.setTerceros(listaTerceros);
        JOptionPane.showMessageDialog(this,
                "Tercero creado exitosamente:\n\nNombre: " + tercero.getNombre() + "\nID: " + tercero.getTerceroId(),
                "Tercero Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verTercero(TerceroEntity tercero) {
        new RegistrarTercero(ventana, this, tercero);
    }

    @Override
    public void actualizarTercero(TerceroEntity tercero) {
        controladorTerceros.actualizar(tercero.getTerceroId(), tercero);
        this.listaTerceros = obtenerTerceros();
        tablaTerceros.setTerceros(listaTerceros);
        JOptionPane.showMessageDialog(this,
                "Tercero actualizado exitosamente:\n\nNombre: " + tercero.getNombre() + "\nID: " + tercero.getTerceroId(),
                "Tercero Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarTercero(TerceroEntity tercero) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al tercero " + tercero.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorTerceros.eliminar(tercero.getTerceroId());
            this.listaTerceros = obtenerTerceros();
            tablaTerceros.setTerceros(listaTerceros);
            JOptionPane.showMessageDialog(this, "Tercero eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private List<TerceroEntity> obtenerTerceros() {
        return controladorTerceros.buscarTodos();
    }
}
