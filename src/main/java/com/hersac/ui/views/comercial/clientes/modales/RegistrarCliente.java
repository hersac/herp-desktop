package com.hersac.ui.views.comercial.clientes.modales;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.controllers.terceros.TercerosController;

import javax.swing.*;
import java.awt.*;

public class RegistrarCliente extends JDialog {
    private JTextField campoNombre;
    private JCheckBox campoActivo;
    private ClienteEntity clienteRegistrado;
    private final ClientesListeners escuchador;
    private final boolean esEdicion;
    private final PermissionService servicioPermiso;
    private JTextField campoIdTercero;
    private JTextField campoNombreTercero;
    private TercerosController controladorTerceros;

    public RegistrarCliente(JFrame parent, ClientesListeners listener, ClienteEntity clienteParaEditar, PermissionService permissionService, TercerosController tercerosController) {
        super(parent, clienteParaEditar != null ? "Actualizar Cliente" : "Registrar Cliente", true);
        this.escuchador = listener;
        this.clienteRegistrado = clienteParaEditar;
        this.esEdicion = clienteParaEditar != null;
        this.servicioPermiso = permissionService;
        this.controladorTerceros = tercerosController;
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
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);
        campoIdTercero = new JTextField();
        campoIdTercero.setFont(fuenteRoboto);
        campoNombreTercero = new JTextField();
        campoNombreTercero.setEditable(false);
        campoNombreTercero.setFont(fuenteRoboto);
        campoNombre = new JTextField();
        campoNombre.setFont(fuenteRoboto);
        campoActivo = new JCheckBox("Activo");
        campoActivo.setFont(fuenteRoboto);
        if (esEdicion && clienteRegistrado != null && clienteRegistrado.getTercero() != null) {
            campoIdTercero.setText(clienteRegistrado.getTercero().getTerceroId());
            campoNombreTercero.setText(clienteRegistrado.getTercero().getNombre());
            campoNombre.setText(clienteRegistrado.getTercero().getNombre());
            campoActivo.setSelected(clienteRegistrado.isEsta_activo());
        }
        campoIdTercero.addActionListener(e -> buscarTerceroPorId());
        JLabel etiquetaIdTercero = new JLabel("ID Tercero:");
        etiquetaIdTercero.setFont(fuenteRoboto);
        JLabel etiquetaNombreTercero = new JLabel("Nombre Tercero:");
        etiquetaNombreTercero.setFont(fuenteRoboto);
        JLabel etiquetaNombreCliente = new JLabel("Nombre Cliente:");
        etiquetaNombreCliente.setFont(fuenteRoboto);
        JLabel etiquetaActivo = new JLabel("Activo:");
        etiquetaActivo.setFont(fuenteRoboto);
        panelFormulario.add(etiquetaIdTercero);
        panelFormulario.add(campoIdTercero);
        panelFormulario.add(etiquetaNombreTercero);
        panelFormulario.add(campoNombreTercero);
        panelFormulario.add(etiquetaNombreCliente);
        panelFormulario.add(campoNombre);
        panelFormulario.add(etiquetaActivo);
        panelFormulario.add(campoActivo);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);
        JButton botonGuardar = new JButton(esEdicion ? "Actualizar" : "Registrar");
        botonGuardar.setPreferredSize(new Dimension(120, 36));
        botonGuardar.setFont(fuenteRoboto);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(botonGuardar);
        add(panelBoton, BorderLayout.SOUTH);
        botonGuardar.addActionListener(e -> guardarCliente());
    }

    private void buscarTerceroPorId() {
        String idTercero = campoIdTercero.getText();
        TerceroEntity tercero = controladorTerceros.buscarPorId(idTercero);
        if (tercero != null) {
            campoNombre.setText(tercero.getNombre());
            campoNombreTercero.setText(tercero.getNombre());
            return;
        }
        campoNombre.setText("");
        campoNombreTercero.setText("");
        JOptionPane.showMessageDialog(this, "Tercero no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void guardarCliente() {
        ClienteEntity nuevoCliente = new ClienteEntity();
        String idTercero = campoIdTercero.getText();
        TerceroEntity tercero = controladorTerceros.buscarPorId(idTercero);
        nuevoCliente.setTercero(tercero);
        nuevoCliente.setEsta_activo(campoActivo.isSelected());
        if (escuchador != null) {
            escuchador.crearCliente(nuevoCliente);
        }
        dispose();
    }
}
