package com.hersac.ui.views.comercial.inventario.modales;

import java.awt.*;
import javax.swing.*;
import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.bodegas.entities.BodegaEntity;

public class RegistrarMovimiento extends JDialog {
    private JComboBox<String> comboTipoMovimiento;
    private JTextField campoCantidad;
    private JTextField campoReferenciaId;
    private JComboBox<String> comboReferenciaTipo;
    private JTextField campoItemId;
    private JTextField campoItemNombre;
    private JTextField campoBodegaId;
    private JTextField campoBodegaNombre;
    private MovimientoInventarioEntity movimientoRegistrado;
    private final boolean esEdicion;
    private DIContainer contenedorDI;

    public RegistrarMovimiento(JFrame padre, MovimientoInventarioEntity movimientoParaEditar, DIContainer contenedorDI) {
        super(padre, movimientoParaEditar != null ? "Actualizar Movimiento" : "Registrar Movimiento", true);
        this.movimientoRegistrado = movimientoParaEditar;
        this.esEdicion = movimientoParaEditar != null;
        this.contenedorDI = contenedorDI;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarVentana();
        JPanel panelFormulario = crearCampos();
        if (esEdicion && movimientoRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel panelBotones = crearPanelBotones();
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos() {
        comboTipoMovimiento = new JComboBox<>(new String[]{"ENTRADA", "SALIDA"});
        comboTipoMovimiento.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoCantidad = new JTextField();
        campoCantidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoReferenciaId = new JTextField();
        campoReferenciaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboReferenciaTipo = new JComboBox<>(new String[]{"Compra", "Venta"});
        comboReferenciaTipo.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoItemId = new JTextField();
        campoItemId.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoItemNombre = new JTextField();
        campoItemNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoItemNombre.setEditable(false);
        campoItemNombre.setBackground(new Color(240,240,240));
        campoBodegaId = new JTextField();
        campoBodegaId.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBodegaNombre = new JTextField();
        campoBodegaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBodegaNombre.setEditable(false);
        campoBodegaNombre.setBackground(new Color(240,240,240));
        comboTipoMovimiento.addActionListener(e -> actualizarReferenciaTipo());
        comboTipoMovimiento.setSelectedIndex(0);
        comboReferenciaTipo.setSelectedItem("Compra");
        comboReferenciaTipo.setEnabled(false);
        campoItemId.addActionListener(e -> buscarItemPorCodigo());
        campoBodegaId.addActionListener(e -> buscarBodegaPorId());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        agregarCampo(panel, gbc, "Tipo Movimiento:", comboTipoMovimiento);
        agregarCampo(panel, gbc, "Cantidad:", campoCantidad);
        agregarCampo(panel, gbc, "Referencia ID:", campoReferenciaId);
        agregarCampo(panel, gbc, "Referencia Tipo:", comboReferenciaTipo);
        agregarCampo(panel, gbc, "Código Item:", campoItemId);
        agregarCampo(panel, gbc, "Item Nombre:", campoItemNombre);
        agregarCampo(panel, gbc, "Bodega ID:", campoBodegaId);
        agregarCampo(panel, gbc, "Bodega Nombre:", campoBodegaNombre);
        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String texto, JComponent campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        etiqueta.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 0;
        panel.add(etiqueta, gbc);
        gbc.gridx = 1;
        campo.setPreferredSize(new Dimension(200, 36));
        panel.add(campo, gbc);
        gbc.gridy++;
    }

    private void actualizarReferenciaTipo() {
        String tipo = (String) comboTipoMovimiento.getSelectedItem();
        if ("ENTRADA".equals(tipo)) {
            comboReferenciaTipo.setSelectedItem("Compra");
        }
        if ("SALIDA".equals(tipo)) {
            comboReferenciaTipo.setSelectedItem("Venta");
        }
        comboReferenciaTipo.setEnabled(false);
    }

    private void buscarItemPorCodigo() {
        String codigo = campoItemId.getText().trim();
        campoItemNombre.setText("");
        if (!codigo.isEmpty()) {
            ItemEntity itemEncontrado = null;
            try {
                var itemsController = contenedorDI.getItemsController();
                itemEncontrado = itemsController.buscarPorCodigo(codigo);
            } catch (Exception ex) {
                campoItemNombre.setText("Error búsqueda");
            }
            if (itemEncontrado != null) {
                campoItemNombre.setText(itemEncontrado.getNombre());
            } else {
                campoItemNombre.setText("No encontrado");
            }
        }
    }

    private void buscarBodegaPorId() {
        String idText = campoBodegaId.getText().trim();
        campoBodegaNombre.setText("");
        if (!idText.isEmpty()) {
            try {
                Long id = Long.parseLong(idText);
                var bodegasController = contenedorDI.getBodegasController();
                var bodegaEncontrada = bodegasController.buscarPorId(id);
                if (bodegaEncontrada != null) {
                    campoBodegaNombre.setText(bodegaEncontrada.getNombre());
                } else {
                    campoBodegaNombre.setText("No encontrada");
                }
            } catch (Exception ex) {
                campoBodegaNombre.setText("ID inválido");
            }
        }
    }

    private void cargarDatosEdicion() {
        if (movimientoRegistrado.getTipoMovimiento() != null) {
            comboTipoMovimiento.setSelectedItem(movimientoRegistrado.getTipoMovimiento().toUpperCase());
        }
        campoCantidad.setText(String.valueOf(movimientoRegistrado.getCantidad()));
        campoReferenciaId.setText(String.valueOf(movimientoRegistrado.getReferenciaId()));
        if (movimientoRegistrado.getReferenciaTipo() == 1) {
            comboReferenciaTipo.setSelectedItem("Compra");
        }
        if (movimientoRegistrado.getReferenciaTipo() == 2) {
            comboReferenciaTipo.setSelectedItem("Venta");
        }
        comboReferenciaTipo.setEnabled(false);
        if (movimientoRegistrado.getItem() != null) {
            campoItemId.setText(movimientoRegistrado.getItem().getCodigo());
            campoItemNombre.setText(movimientoRegistrado.getItem().getNombre());
        }
        if (movimientoRegistrado.getBodega() != null) {
            campoBodegaId.setText(String.valueOf(movimientoRegistrado.getBodega().getBodegaId()));
            campoBodegaNombre.setText(movimientoRegistrado.getBodega().getNombre());
        }
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton(esEdicion ? "Actualizar" : "Guardar");
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonGuardar.addActionListener(e -> guardarMovimiento());
        panel.add(botonGuardar);
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonCancelar);
        return panel;
    }

    private void guardarMovimiento() {
        if (movimientoRegistrado == null) {
            movimientoRegistrado = new MovimientoInventarioEntity();
        }
        movimientoRegistrado.setTipoMovimiento(((String) comboTipoMovimiento.getSelectedItem()).toUpperCase());
        try {
            movimientoRegistrado.setCantidad(Integer.parseInt(campoCantidad.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            movimientoRegistrado.setReferenciaId(Long.parseLong(campoReferenciaId.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Referencia ID inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String tipoSeleccionado = (String) comboReferenciaTipo.getSelectedItem();
        if ("Compra".equals(tipoSeleccionado)) {
            movimientoRegistrado.setReferenciaTipo(1);
        }
        if ("Venta".equals(tipoSeleccionado)) {
            movimientoRegistrado.setReferenciaTipo(2);
        }
        String codigoItem = campoItemId.getText();
        ItemEntity itemPersistido = null;
        try {
            var itemsController = contenedorDI.getItemsController();
            itemPersistido = itemsController.buscarPorCodigo(codigoItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el Item", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (itemPersistido == null || itemPersistido.getItemId() == null) {
            JOptionPane.showMessageDialog(this, "El código de Item no existe en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        movimientoRegistrado.setItem(itemPersistido);
        Long idBodega = null;
        BodegaEntity bodegaPersistida = null;
        try {
            idBodega = Long.parseLong(campoBodegaId.getText());
            var bodegasController = contenedorDI.getBodegasController();
            bodegaPersistida = bodegasController.buscarPorId(idBodega);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar la Bodega", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bodegaPersistida == null || bodegaPersistida.getBodegaId() == null) {
            JOptionPane.showMessageDialog(this, "El ID de Bodega no existe en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        movimientoRegistrado.setBodega(bodegaPersistida);
        dispose();
    }

    public MovimientoInventarioEntity getMovimientoRegistrado() {
        return movimientoRegistrado;
    }
}
