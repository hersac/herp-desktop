package com.hersac.ui.views.terceros;

import com.hersac.core.di.DIContainer;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.ui.views.terceros.forms.FiltrosTercerosForm;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;
import com.hersac.ui.views.terceros.tablas.TercerosTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionTerceros extends JPanel implements TercerosListeners {
    private final TercerosController tercerosController;
    private final PermissionService permissionService;
    private JFrame frame = new JFrame("Registrar Tercero");
    private final TercerosTable tablaTercerosTable;
    private List<TerceroEntity> listaCompletaTerceros;
    private FiltrosTercerosForm filtrosForm;
    private JTextField searchField;

    public GestionTerceros(DIContainer diContainer) {
        this.tercerosController = diContainer.getTercerosController();
        this.permissionService = diContainer.getPermissionService();
        this.tablaTercerosTable = new TercerosTable(permissionService);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Terceros");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosForm = new FiltrosTercerosForm();
        filtrosForm.setOnFiltrosCambiados(this::filtrarTerceros);
        JButton registrarBtn = new JButton("Registrar Tercero");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setEnabled(permissionService == null || permissionService.tienePermiso((long) Permiso.CREAR_TERCERO.getId()));
        registrarBtn.setVisible(permissionService == null || permissionService.tienePermiso((long) Permiso.CREAR_TERCERO.getId()));
        registrarBtn.addActionListener(e -> {
            new com.hersac.ui.views.terceros.modales.RegistrarTercero(frame, this, null, permissionService);
        });
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarTerceros();
            }
        });
        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.setOpaque(false);
        panelBtn.setPreferredSize(new Dimension(900, 40));
        panelBtn.setMaximumSize(new Dimension(900, 40));
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
        JScrollPane scrollPane = new JScrollPane(tablaTercerosTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());

        this.listaCompletaTerceros = obtenerTerceros();
        tablaTercerosTable.setTerceros(listaCompletaTerceros);
        tablaTercerosTable.setActionListener(this);
    }

    private void filtrarTerceros() {
        String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        String estado = filtrosForm.getEstadoSeleccionado();
        TipoPersonaEntity tipoSeleccionado = filtrosForm.getTipoSeleccionado();
        final Integer tipoIdSeleccionado = (tipoSeleccionado != null) ? tipoSeleccionado.getTipoPersonaId() : null;
        List<TerceroEntity> filtrados = listaCompletaTerceros.stream()
            .filter(t -> {
                String idStr = String.valueOf(t.getTerceroId());
                String nombre = t.getNombre() != null ? t.getNombre().toLowerCase() : "";
                String estadoTercero = t.getEstado() != null ? t.getEstado().toLowerCase() : "";
                Integer tipoId = t.getTipoPersona() != null ? t.getTipoPersona().getTipoPersonaId() : null;
                String tipoNombre = t.getTipoPersona() != null && t.getTipoPersona().getNombre() != null ? t.getTipoPersona().getNombre().toLowerCase() : "";
                boolean coincideTexto = texto.isEmpty() || idStr.contains(texto) || nombre.contains(texto) || estadoTercero.contains(texto) || tipoNombre.contains(texto);
                boolean coincideEstado = estado.equals("Todos") || estadoTercero.equals(estado.toLowerCase());
                boolean coincideTipo = tipoIdSeleccionado == null || (tipoId != null && tipoIdSeleccionado.equals(tipoId));
                return coincideTexto && coincideEstado && coincideTipo;
            })
            .toList();
        tablaTercerosTable.setTerceros(filtrados);
    }

    // Métodos de TercerosListeners
    @Override
    public void crearTercero(TerceroEntity tercero) {
        tercerosController.crear(tercero);
        this.listaCompletaTerceros = obtenerTerceros();
        tablaTercerosTable.setTerceros(listaCompletaTerceros);
        JOptionPane.showMessageDialog(this,
                "Tercero creado exitosamente:\n\nNombre: " + tercero.getNombre() + "\nID: " + tercero.getTerceroId(),
                "Tercero Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verTercero(TerceroEntity tercero) {
        new com.hersac.ui.views.terceros.modales.RegistrarTercero(frame, this, tercero, permissionService);
    }

    @Override
    public void actualizarTercero(TerceroEntity tercero) {
        tercerosController.actualizar(tercero.getTerceroId(), tercero);
        this.listaCompletaTerceros = obtenerTerceros();
        tablaTercerosTable.setTerceros(listaCompletaTerceros);
        JOptionPane.showMessageDialog(this,
                "Tercero actualizado exitosamente:\n\nNombre: " + tercero.getNombre() + "\nID: " + tercero.getTerceroId(),
                "Tercero Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarTercero(TerceroEntity tercero) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al tercero " + tercero.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tercerosController.eliminar(tercero.getTerceroId());
            this.listaCompletaTerceros = obtenerTerceros();
            tablaTercerosTable.setTerceros(listaCompletaTerceros);
            JOptionPane.showMessageDialog(this, "Tercero eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private List<TerceroEntity> obtenerTerceros() {
        return tercerosController.buscarTodos();
    }
}
