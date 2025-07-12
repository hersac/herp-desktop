package com.hersac.ui.views.comercial.inventario.contenidos.bodegas;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
    private JFrame frame = new JFrame("Registrar Bodega");
    private final BodegasController bodegasController;
    private final BodegasTable tablaBodegasTable = new BodegasTable();
    private List<BodegaEntity> listaCompletaBodegas;
    private JTextField searchField;
    private final DIContainer diContainer;
    private FiltrosBodegasForm filtrosBodegasForm;

    public GestionBodegas(DIContainer diContainer) {
        this.diContainer = diContainer;
        this.bodegasController = diContainer.getBodegasController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Bodegas");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosBodegasForm = new FiltrosBodegasForm();
        filtrosBodegasForm.setOnFiltrosCambiados(this::filtrarBodegas);
        JButton registrarBtn = new JButton("Registrar Bodega");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        FontIcon iconVer = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(iconVer);
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarBodegas();
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
        JScrollPane scrollPane = new JScrollPane(tablaBodegasTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosBodegasForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());
        List<BodegaEntity> listaBodegas = obtenerBodegas();
        this.listaCompletaBodegas = listaBodegas;
        tablaBodegasTable.setBodegas(listaBodegas);
        registrarBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new RegistrarBodega(parentFrame, diContainer, this);
        });
        tablaBodegasTable.setActionListener(this);
    }

    private void filtrarBodegas() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String estado = filtrosBodegasForm.getEstadoSeleccionado();
        final String desde = filtrosBodegasForm.getFechaDesde();
        final String hasta = filtrosBodegasForm.getFechaHasta();
        List<BodegaEntity> filtrados = listaCompletaBodegas.stream()
                .filter(b -> {
                    String bodegaIdStr = String.valueOf(b.getBodegaId());
                    String nombre = b.getNombre() != null ? b.getNombre().toLowerCase() : "";
                    String estadoBodega = Boolean.TRUE.equals(b.getEstaActiva()) ? "Activas" : "Inactivas";
                    boolean coincideTexto = texto.isEmpty()
                        || bodegaIdStr.contains(texto)
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
        tablaBodegasTable.setBodegas(filtrados);
    }

    private List<BodegaEntity> obtenerBodegas() {
        return bodegasController.buscarTodos();
    }

    @Override
    public void crearBodega(BodegaEntity bodega) {
        bodegasController.crear(bodega);
        JOptionPane.showMessageDialog(this, "Bodega creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.listaCompletaBodegas = obtenerBodegas();
        tablaBodegasTable.setBodegas(listaCompletaBodegas);
    }

    @Override
    public void eliminarBodega(BodegaEntity bodega) {
        bodegasController.eliminar(bodega.getBodegaId());
        JOptionPane.showMessageDialog(this, "Bodega eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.listaCompletaBodegas = obtenerBodegas();
        tablaBodegasTable.setBodegas(listaCompletaBodegas);
    }

    @Override
    public void actualizarBodega(BodegaEntity bodega) {
        BodegaEntity original = bodegasController.buscarPorId(bodega.getBodegaId());
        if (original != null) {
            bodegasController.actualizar(original.getBodegaId(), bodega);
            JOptionPane.showMessageDialog(this, "Bodega actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la bodega para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.listaCompletaBodegas = obtenerBodegas();
        tablaBodegasTable.setBodegas(listaCompletaBodegas);
    }

    @Override
    public void verBodega(BodegaEntity bodega) {

    }

    public DIContainer getDIContainer() {
        return diContainer;
    }
}
