package com.hersac.ui.views.terceros.modales;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;
import com.hersac.ui.views.terceros.constantes.TiposTerceroEnum;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class RegistrarTercero extends JDialog {
    private JTextField campoId;
    private JTextField campoNombre;
    private JComboBox<TiposTerceroEnum> comboTipo;
    private JTextField campoDireccion;
    private JTextField campoTelefono;
    private JTextField campoEmail;
    private JCheckBox checkActivo;
    private TerceroEntity tercero;
    private final TercerosListeners escuchador;
    private final boolean esEdicion;

    public RegistrarTercero(JFrame padre, TercerosListeners escuchador, TerceroEntity terceroEditar) {
        super(padre, terceroEditar != null ? "Actualizar Tercero" : "Registrar Tercero", true);
        this.escuchador = escuchador;
        this.tercero = terceroEditar;
        this.esEdicion = terceroEditar != null;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelBotones = crearPanelBotones();
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        configurarEdicion();
        setVisible(true);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Font fuente = new Font("Roboto", Font.PLAIN, 14);
        inicializarCampos(fuente);
        agregarCamposAlPanel(panel, fuente);
        if (esEdicion && tercero != null) {
            cargarDatosEdicion();
        }
        return panel;
    }

    private void inicializarCampos(Font fuente) {
        campoId = new JTextField();
        campoId.setFont(fuente);
        campoNombre = new JTextField();
        campoNombre.setFont(fuente);
        aplicarFiltroMayusculas(campoNombre);
        comboTipo = new JComboBox<>();
        for (TiposTerceroEnum tipo : TiposTerceroEnum.values()) {
            comboTipo.addItem(tipo);
        }
        comboTipo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                if (value instanceof TiposTerceroEnum) {
                    value = ((TiposTerceroEnum) value).getNombre();
                }
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
                label.setFont(fuente);
                return label;
            }
        });
        campoDireccion = new JTextField();
        campoDireccion.setFont(fuente);
        campoTelefono = new JTextField();
        campoTelefono.setFont(fuente);
        campoEmail = new JTextField();
        campoEmail.setFont(fuente);
        aplicarFiltroMinusculas(campoEmail);
        checkActivo = new JCheckBox("Activo", true);
        checkActivo.setFont(fuente);
    }

    private void agregarCamposAlPanel(JPanel panel, Font fuente) {
        panel.add(crearLabel("ID:", fuente));
        panel.add(campoId);
        panel.add(crearLabel("Nombre:", fuente));
        panel.add(campoNombre);
        panel.add(crearLabel("Tipo de Persona:", fuente));
        panel.add(comboTipo);
        panel.add(crearLabel("Dirección:", fuente));
        panel.add(campoDireccion);
        panel.add(crearLabel("Teléfono:", fuente));
        panel.add(campoTelefono);
        panel.add(crearLabel("Email:", fuente));
        panel.add(campoEmail);
        panel.add(crearLabel("¿Está activo?:", fuente));
        panel.add(checkActivo);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        return label;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(e -> guardarTercero());
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonGuardar);
        panel.add(botonCancelar);
        botonGuardar.setEnabled(true);
        return panel;
    }

    private void configurarEdicion() {
        campoId.setEnabled(!esEdicion);
        campoNombre.setEnabled(true);
        comboTipo.setEnabled(true);
        campoDireccion.setEnabled(true);
        campoTelefono.setEnabled(true);
        campoEmail.setEnabled(true);
        checkActivo.setEnabled(true);
    }

    private void cargarDatosEdicion() {
        campoId.setText(tercero.getTerceroId());
        campoId.setEnabled(false);
        campoNombre.setText(tercero.getNombre());
        comboTipo.setSelectedItem(TiposTerceroEnum.fromId(tercero.getTipoPersona().getTipoPersonaId()));
        campoDireccion.setText(tercero.getDireccion());
        campoTelefono.setText(tercero.getTelefono());
        campoEmail.setText(tercero.getEmail());
        checkActivo.setSelected("Activo".equalsIgnoreCase(tercero.getEstado()));
    }

    private void guardarTercero() {
        if (tercero == null) {
            tercero = new TerceroEntity();
        }
        if (!esEdicion) {
            tercero.setTerceroId(campoId.getText());
        }
        tercero.setNombre(campoNombre.getText());
        TiposTerceroEnum tipoSeleccionado = (TiposTerceroEnum) comboTipo.getSelectedItem();
        TipoPersonaEntity tipoPersona = new TipoPersonaEntity();
        tipoPersona.setTipoPersonaId(tipoSeleccionado != null ? tipoSeleccionado.getId() : null);
        tercero.setTipoPersona(tipoPersona);
        tercero.setDireccion(campoDireccion.getText());
        tercero.setTelefono(campoTelefono.getText());
        tercero.setEmail(campoEmail.getText());
        tercero.setEstado(checkActivo.isSelected() ? "Activo" : "Inactivo");
        if (!esEdicion) {
            escuchador.crearTercero(tercero);
        }
        if (esEdicion) {
            escuchador.actualizarTercero(tercero);
        }
        dispose();
    }

    private void aplicarFiltroMayusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
    }

    private void aplicarFiltroMinusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    super.insertString(fb, offset, string.toLowerCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toLowerCase(), attrs);
                }
            }
        });
    }

    public TerceroEntity getTercero() {
        return tercero;
    }
}
