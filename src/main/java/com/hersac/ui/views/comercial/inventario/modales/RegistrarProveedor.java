package com.hersac.ui.views.comercial.inventario.modales;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.ui.views.comercial.inventario.listeners.ProveedorListeners;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.controllers.terceros.TercerosController;

import javax.swing.*;
import java.awt.*;

public class RegistrarProveedor extends JDialog {
    private JTextField campoNombre;
    private JCheckBox cajaActiva;
    private ProveedorEntity proveedorRegistrado;
    private final ProveedorListeners oyente;
    private final boolean esEdicion;
    private JTextField campoTerceroId;
    private JTextField campoTerceroNombre;
    private TercerosController tercerosController;

    public RegistrarProveedor(JFrame padre, ProveedorListeners oyente, ProveedorEntity proveedorParaEditar, TercerosController tercerosController) {
        super(padre, proveedorParaEditar != null ? "Actualizar Proveedor" : "Registrar Proveedor", true);
        this.oyente = oyente;
        this.proveedorRegistrado = proveedorParaEditar;
        this.esEdicion = proveedorParaEditar != null;
        this.tercerosController = tercerosController;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(420, 340));
        setResizable(false);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setPreferredSize(new Dimension(360, 180));
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        campoTerceroId = new JTextField();
        campoTerceroId.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoTerceroNombre = new JTextField();
        campoTerceroNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoTerceroNombre.setEditable(false);
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        cajaActiva = new JCheckBox("Activo");
        cajaActiva.setFont(new Font("Roboto", Font.PLAIN, 14));
        if (esEdicion && proveedorRegistrado != null && proveedorRegistrado.getTercero() != null) {
            campoTerceroId.setText(proveedorRegistrado.getTercero().getTerceroId());
            campoTerceroNombre.setText(proveedorRegistrado.getTercero().getNombre());
            campoNombre.setText(proveedorRegistrado.getTercero().getNombre());
            cajaActiva.setSelected(proveedorRegistrado.isEsta_activo());
        }
        campoTerceroId.addActionListener(e -> buscarTerceroPorId());
        panelFormulario.add(crearEtiqueta("ID Tercero:"));
        panelFormulario.add(campoTerceroId);
        panelFormulario.add(crearEtiqueta("Nombre Tercero:"));
        panelFormulario.add(campoTerceroNombre);
        panelFormulario.add(crearEtiqueta("Nombre Proveedor:"));
        panelFormulario.add(campoNombre);
        panelFormulario.add(crearEtiqueta("Activo:"));
        panelFormulario.add(cajaActiva);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);
        JButton botonGuardar = new JButton(esEdicion ? "Actualizar" : "Registrar");
        botonGuardar.setPreferredSize(new Dimension(120, 36));
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonGuardar);
        add(panelBotones, BorderLayout.SOUTH);
        botonGuardar.addActionListener(e -> guardarProveedor());
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, 14));
        return etiqueta;
    }

    private void buscarTerceroPorId() {
        String id = campoTerceroId.getText();
        TerceroEntity tercero = tercerosController.buscarPorId(id);
        if (tercero != null) {
            campoNombre.setText(tercero.getNombre());
            campoTerceroNombre.setText(tercero.getNombre());
        } else {
            campoNombre.setText("");
            campoTerceroNombre.setText("");
            JOptionPane.showMessageDialog(this, "Tercero no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarProveedor() {
        ProveedorEntity nuevoProveedor = new ProveedorEntity();
        String id = campoTerceroId.getText();
        TerceroEntity tercero = tercerosController.buscarPorId(id);
        nuevoProveedor.setTercero(tercero);
        nuevoProveedor.setEsta_activo(cajaActiva.isSelected());
        if (oyente != null) {
            oyente.crearProveedor(nuevoProveedor);
        }
        dispose();
    }

    public ProveedorEntity getProveedorRegistrado() {
        return proveedorRegistrado;
    }
}
