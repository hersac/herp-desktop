package com.hersac.ui.views.terceros;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.views.terceros.forms.FiltrosTercerosForm;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;
import com.hersac.ui.views.terceros.tablas.TercerosTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionTerceros extends JPanel implements TercerosListeners {
    private final TercerosController tercerosController;
    private JFrame frame = new JFrame("Registrar Tercero");
    private final TercerosTable tablaTercerosTable = new TercerosTable();
    private List<TerceroEntity> listaCompletaTerceros;
    private FiltrosTercerosForm filtrosForm;
    private JTextField searchField;

    public GestionTerceros(DIContainer diContainer) {
        this.tercerosController = diContainer.getTercerosController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel titleLabel = new JLabel("Gestión de Terceros");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        filtrosForm = new FiltrosTercerosForm();
        JButton registrarBtn = new JButton("Registrar Tercero");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
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
        // TODO: cargar listaCompletaTerceros y setear en tablaTercerosTable
        // TODO: listeners de búsqueda, filtros y registrarBtn
    }

    // Métodos de TercerosListeners (vacíos por ahora)
    @Override
    public void crearTercero(TerceroEntity tercero) {}
    @Override
    public void verTercero(TerceroEntity tercero) {}
    @Override
    public void actualizarTercero(TerceroEntity tercero) {}
    @Override
    public void eliminarTercero(TerceroEntity tercero) {}

    private List<TerceroEntity> obtenerTerceros() {
        return tercerosController.buscarTodos();
    }
}
