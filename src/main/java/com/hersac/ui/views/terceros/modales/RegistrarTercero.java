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
    private JTextField idField;
    private JTextField nombreField;
    private JComboBox<TiposTerceroEnum> tipoComboBox;
    private JTextField direccionField;
    private JTextField telefonoField;
    private JTextField emailField;
    private JCheckBox activoCheckBox;
    private TerceroEntity terceroRegistrado;
    private final TercerosListeners listener;
    private final boolean esEdicion;

    public RegistrarTercero(JFrame parent, TercerosListeners listener, TerceroEntity terceroParaEditar) {
        super(parent, terceroParaEditar != null ? "Actualizar Tercero" : "Registrar Tercero", true);
        this.listener = listener;
        this.terceroRegistrado = terceroParaEditar;
        this.esEdicion = terceroParaEditar != null;
        initComponents();
    }

    private void initComponents() {
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        idField = new JTextField();
        nombreField = new JTextField();
        aplicarFiltroMayusculas(nombreField);
        tipoComboBox = new JComboBox<>();
        for (TiposTerceroEnum tipo : TiposTerceroEnum.values()) {
            tipoComboBox.addItem(tipo);
        }
        tipoComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof TiposTerceroEnum) {
                    value = ((TiposTerceroEnum) value).getNombre();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        direccionField = new JTextField();
        telefonoField = new JTextField();
        emailField = new JTextField();
        aplicarFiltroMinusculas(emailField);
        activoCheckBox = new JCheckBox("Activo", true);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Tipo de Persona:"));
        formPanel.add(tipoComboBox);
        formPanel.add(new JLabel("Dirección:"));
        formPanel.add(direccionField);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(telefonoField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("¿Está activo?:"));
        formPanel.add(activoCheckBox);
        if (esEdicion && terceroRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.addActionListener(e -> guardarTercero());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        boolean puedeEditar = true;
        idField.setEnabled(!esEdicion && puedeEditar);
        nombreField.setEnabled(puedeEditar);
        tipoComboBox.setEnabled(puedeEditar);
        direccionField.setEnabled(puedeEditar);
        telefonoField.setEnabled(puedeEditar);
        emailField.setEnabled(puedeEditar);
        activoCheckBox.setEnabled(puedeEditar);
        guardarBtn.setEnabled(puedeEditar);
        setVisible(true);
    }

    private void cargarDatosEdicion() {
        idField.setText(terceroRegistrado.getTerceroId());
        idField.setEnabled(false);
        nombreField.setText(terceroRegistrado.getNombre());
        tipoComboBox.setSelectedItem(TiposTerceroEnum.fromId(terceroRegistrado.getTipoPersona().getTipoPersonaId()));
        direccionField.setText(terceroRegistrado.getDireccion());
        telefonoField.setText(terceroRegistrado.getTelefono());
        emailField.setText(terceroRegistrado.getEmail());
        activoCheckBox.setSelected("Activo".equalsIgnoreCase(terceroRegistrado.getEstado()));
    }

    private void guardarTercero() {
        if (terceroRegistrado == null) {
            terceroRegistrado = new TerceroEntity();
        }
        if (!esEdicion) {
            terceroRegistrado.setTerceroId(idField.getText());
        }
        terceroRegistrado.setNombre(nombreField.getText());
        TiposTerceroEnum tipoSeleccionado = (TiposTerceroEnum) tipoComboBox.getSelectedItem();
        TipoPersonaEntity tipoPersona = new TipoPersonaEntity();
        tipoPersona.setTipoPersonaId(tipoSeleccionado != null ? tipoSeleccionado.getId() : null);
        terceroRegistrado.setTipoPersona(tipoPersona);
        terceroRegistrado.setDireccion(direccionField.getText());
        terceroRegistrado.setTelefono(telefonoField.getText());
        terceroRegistrado.setEmail(emailField.getText());
        terceroRegistrado.setEstado(activoCheckBox.isSelected() ? "Activo" : "Inactivo");
        if (!esEdicion) {
            listener.crearTercero(terceroRegistrado);
        } else {
            listener.actualizarTercero(terceroRegistrado);
        }
        dispose();
    }

    private void aplicarFiltroMayusculas(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
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

    private void aplicarFiltroMinusculas(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
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

    public TerceroEntity getTerceroRegistrado() {
        return terceroRegistrado;
    }
}
