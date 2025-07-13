package com.hersac.ui.views.comercial.inventario.modales;

import java.awt.*;
import javax.swing.*;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.bodegas.entities.BodegaEntity;

public class RegistrarMovimiento extends JDialog {
    private JComboBox<String> tipoMovimientoCombo;
    private JTextField cantidadField;
    private JTextField referenciaIdField;
    private JComboBox<String> referenciaTipoCombo;
    private JTextField itemIdField;
    private JTextField itemNombreField;
    private JTextField bodegaIdField;
    private JTextField bodegaNombreField;
    private MovimientoInventarioEntity movimientoRegistrado;
    private final boolean esEdicion;
    private DIContainer diContainer;

    public RegistrarMovimiento(JFrame parent, MovimientoInventarioEntity movimientoParaEditar, DIContainer diContainer) {
        super(parent, movimientoParaEditar != null ? "Actualizar Movimiento" : "Registrar Movimiento", true);
        this.movimientoRegistrado = movimientoParaEditar;
        this.esEdicion = movimientoParaEditar != null;
        this.diContainer = diContainer;
        initComponents();
    }

    private void initComponents() {
        configurarVentana();
        JPanel formPanel = crearCampos();
        if (esEdicion && movimientoRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel buttonPanel = crearPanelBotones();
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos() {
        tipoMovimientoCombo = new JComboBox<>(new String[]{"ENTRADA", "SALIDA"});
        cantidadField = new JTextField();
        referenciaIdField = new JTextField();
        referenciaTipoCombo = new JComboBox<>(new String[]{"Compra", "Venta"});
        itemIdField = new JTextField();
        itemNombreField = new JTextField();
        itemNombreField.setEditable(false);
        itemNombreField.setBackground(new Color(240,240,240));
        bodegaIdField = new JTextField();
        bodegaNombreField = new JTextField();
        bodegaNombreField.setEditable(false);
        bodegaNombreField.setBackground(new Color(240,240,240));

        tipoMovimientoCombo.addActionListener(e -> {
            String tipo = (String) tipoMovimientoCombo.getSelectedItem();
            if ("ENTRADA".equals(tipo)) {
                referenciaTipoCombo.setSelectedItem("Compra");
                referenciaTipoCombo.setEnabled(false);
            } else if ("SALIDA".equals(tipo)) {
                referenciaTipoCombo.setSelectedItem("Venta");
                referenciaTipoCombo.setEnabled(false);
            }
        });

        tipoMovimientoCombo.setSelectedIndex(0);
        referenciaTipoCombo.setSelectedItem("Compra");
        referenciaTipoCombo.setEnabled(false);

        itemIdField.addActionListener(e -> buscarItemPorCodigo());
        bodegaIdField.addActionListener(e -> buscarBodegaPorId());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        int labelWidth = 220;
        int inputMinWidth = 200;
        int inputHeight = 36;

        JLabel lblTipoMovimiento = new JLabel("Tipo Movimiento:");
        lblTipoMovimiento.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblTipoMovimiento, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        tipoMovimientoCombo.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        tipoMovimientoCombo.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(tipoMovimientoCombo, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblCantidad, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        cantidadField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        cantidadField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(cantidadField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblReferenciaId = new JLabel("Referencia ID:");
        lblReferenciaId.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblReferenciaId, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        referenciaIdField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        referenciaIdField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(referenciaIdField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblReferenciaTipo = new JLabel("Referencia Tipo:");
        lblReferenciaTipo.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblReferenciaTipo, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        referenciaTipoCombo.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        referenciaTipoCombo.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(referenciaTipoCombo, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblItemId = new JLabel("Código Item:");
        lblItemId.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblItemId, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        itemIdField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        itemIdField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(itemIdField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblItemNombre = new JLabel("Item Nombre:");
        lblItemNombre.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblItemNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        itemNombreField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        itemNombreField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(itemNombreField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblBodegaId = new JLabel("Bodega ID:");
        lblBodegaId.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblBodegaId, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        bodegaIdField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        bodegaIdField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(bodegaIdField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblBodegaNombre = new JLabel("Bodega Nombre:");
        lblBodegaNombre.setPreferredSize(new Dimension(labelWidth, 30));
        formPanel.add(lblBodegaNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        bodegaNombreField.setMinimumSize(new Dimension(inputMinWidth, inputHeight));
        bodegaNombreField.setPreferredSize(new Dimension(inputMinWidth, inputHeight));
        formPanel.add(bodegaNombreField, gbc);
        return formPanel;
    }

    private void buscarItemPorCodigo() {
        String codigo = itemIdField.getText().trim();
        itemNombreField.setText("");
        if (!codigo.isEmpty()) {
            com.hersac.core.modules.items.entities.ItemEntity itemEncontrado = null;
            try {
                var itemsController = diContainer.getItemsController();
                itemEncontrado = itemsController.buscarPorCodigo(codigo);
            } catch (Exception ex) {
                itemNombreField.setText("Error búsqueda");
            }
            if (itemEncontrado != null) {
                itemNombreField.setText(itemEncontrado.getNombre());
            } else {
                itemNombreField.setText("No encontrado");
            }
        }
    }

    private void buscarBodegaPorId() {
        String idText = bodegaIdField.getText().trim();
        bodegaNombreField.setText("");
        if (!idText.isEmpty()) {
            try {
                Long id = Long.parseLong(idText);
                var bodegasController = diContainer.getBodegasController();
                var bodegaEncontrada = bodegasController.buscarPorId(id);
                if (bodegaEncontrada != null) {
                    bodegaNombreField.setText(bodegaEncontrada.getNombre());
                } else {
                    bodegaNombreField.setText("No encontrada");
                }
            } catch (Exception ex) {
                bodegaNombreField.setText("ID inválido");
            }
        }
    }

    private void cargarDatosEdicion() {
        if (movimientoRegistrado.getTipoMovimiento() != null) {
            tipoMovimientoCombo.setSelectedItem(movimientoRegistrado.getTipoMovimiento().toUpperCase());
        }
        cantidadField.setText(String.valueOf(movimientoRegistrado.getCantidad()));
        referenciaIdField.setText(String.valueOf(movimientoRegistrado.getReferenciaId()));
        if (movimientoRegistrado.getReferenciaTipo() == 1) {
            referenciaTipoCombo.setSelectedItem("Compra");
        } else if (movimientoRegistrado.getReferenciaTipo() == 2) {
            referenciaTipoCombo.setSelectedItem("Venta");
        }
        referenciaTipoCombo.setEnabled(false);
        if (movimientoRegistrado.getItem() != null) {
            itemIdField.setText(movimientoRegistrado.getItem().getCodigo());
            itemNombreField.setText(movimientoRegistrado.getItem().getNombre());
        }
        if (movimientoRegistrado.getBodega() != null) {
            bodegaIdField.setText(String.valueOf(movimientoRegistrado.getBodega().getBodegaId()));
            bodegaNombreField.setText(movimientoRegistrado.getBodega().getNombre());
        }
    }

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.addActionListener(e -> guardarMovimiento());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarMovimiento() {
        if (movimientoRegistrado == null) {
            movimientoRegistrado = new MovimientoInventarioEntity();
        }
        movimientoRegistrado.setTipoMovimiento(((String) tipoMovimientoCombo.getSelectedItem()).toUpperCase());
        try {
            movimientoRegistrado.setCantidad(Integer.parseInt(cantidadField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            movimientoRegistrado.setReferenciaId(Long.parseLong(referenciaIdField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Referencia ID inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String tipoSeleccionado = (String) referenciaTipoCombo.getSelectedItem();
        if ("Compra".equals(tipoSeleccionado)) {
            movimientoRegistrado.setReferenciaTipo(1);
        } else if ("Venta".equals(tipoSeleccionado)) {
            movimientoRegistrado.setReferenciaTipo(2);
        }
        // Buscar el item persistido
        String itemCodigo = itemIdField.getText();
        ItemEntity itemPersistido = null;
        try {
            var itemsController = diContainer.getItemsController();
            itemPersistido = itemsController.buscarPorCodigo(itemCodigo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el Item", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (itemPersistido == null || itemPersistido.getItemId() == null) {
            JOptionPane.showMessageDialog(this, "El código de Item no existe en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        movimientoRegistrado.setItem(itemPersistido);
        Long bodegaId = null;
        BodegaEntity bodegaPersistida = null;
        try {
            bodegaId = Long.parseLong(bodegaIdField.getText());
            var bodegasController = diContainer.getBodegasController();
            bodegaPersistida = bodegasController.buscarPorId(bodegaId);
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
