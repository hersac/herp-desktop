package com.hersac.ui.views;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.authentication.login.LoginView;
import com.hersac.ui.components.NavbarComponent;
import com.hersac.ui.components.SidebarComponent;
import com.hersac.ui.views.comercial.clientes.GestionClientes;
import com.hersac.ui.views.comercial.compras.GestionCompras;
import com.hersac.ui.views.comercial.inventario.GestionInventario;
import com.hersac.ui.views.comercial.ventas.GestionVentas;
import com.hersac.ui.views.terceros.GestionTerceros;
import com.hersac.ui.views.usuarios.auditorias.Auditorias;
import com.hersac.ui.views.usuarios.gestion.GestionUsuarios;
import com.hersac.ui.views.usuarios.rolesPermisos.RolesPermisos;

import javax.swing.*;
import java.awt.*;

public class AppViews extends JFrame {
    private final JPanel fondo;
    private JPanel panelContenido;
    private final DIContainer contenedor;
    private final Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);

    public AppViews(DIContainer contenedor) {
        this.contenedor = contenedor;
        setTitle("HERP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 700));
        setLocationRelativeTo(null);
        fondo = new JPanel(new BorderLayout());
        setContentPane(fondo);
        mostrarLogin();
        setVisible(true);
    }

    private void mostrarLogin() {
        fondo.removeAll();
        fondo.revalidate();
        fondo.repaint();
        LoginView vistaLogin = contenedor.getLoginView();
        fondo.add(vistaLogin, BorderLayout.CENTER);
        vistaLogin.addLoginListener(token -> {
            if (token != null) {
                mostrarVistaPrincipal();
            }
        });
    }

    private void mostrarVistaPrincipal() {
        fondo.removeAll();
        NavbarComponent barraNavegacion = new NavbarComponent(contenedor.getPermissionService());
        SidebarComponent barraLateral = new SidebarComponent();
        panelContenido = crearPanelContenido();
        barraNavegacion.setNavigationListener(destino -> mostrarVistaDestino(destino));
        barraNavegacion.setLogoutListener(this::mostrarLogin);
        fondo.add(barraNavegacion, BorderLayout.NORTH);
        fondo.add(barraLateral, BorderLayout.WEST);
        fondo.add(panelContenido, BorderLayout.CENTER);
        fondo.revalidate();
        fondo.repaint();
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel etiquetaBienvenida = new JLabel("Bienvenido al sistema");
        etiquetaBienvenida.setFont(new Font("Roboto", Font.PLAIN, 24));
        etiquetaBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(etiquetaBienvenida, BorderLayout.NORTH);
        return panel;
    }

    private void mostrarVistaDestino(String destino) {
        panelContenido.removeAll();
        JComponent vista = obtenerVista(destino);
        if (vista != null) {
            panelContenido.add(vista, BorderLayout.CENTER);
        }
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JComponent obtenerVista(String destino) {
        if ("gestion-clientes".equals(destino)) {
            return aplicarFuenteRoboto(new GestionClientes(contenedor));
        }
        if ("gestion-compras".equals(destino)) {
            return aplicarFuenteRoboto(new GestionCompras(contenedor));
        }
        if ("gestion-ventas".equals(destino)) {
            return aplicarFuenteRoboto(new GestionVentas(contenedor));
        }
        if ("gestion-inventario".equals(destino)) {
            return aplicarFuenteRoboto(new GestionInventario(contenedor));
        }
        if ("gestion-terceros".equals(destino)) {
            return aplicarFuenteRoboto(new GestionTerceros(contenedor));
        }
        if ("gestion-usuarios".equals(destino)) {
            return aplicarFuenteRoboto(new GestionUsuarios(contenedor));
        }
        if ("roles-permisos".equals(destino)) {
            return aplicarFuenteRoboto(new RolesPermisos(contenedor));
        }
        if ("auditorias".equals(destino)) {
            return aplicarFuenteRoboto(new Auditorias());
        }
        return null;
    }

    private JComponent aplicarFuenteRoboto(JComponent componente) {
        for (Component c : componente.getComponents()) {
            if (c instanceof JLabel label) {
                label.setFont(fuenteRoboto);
            }
            if (c instanceof JTextField campo) {
                campo.setFont(fuenteRoboto);
            }
            if (c instanceof JPasswordField campoPass) {
                campoPass.setFont(fuenteRoboto);
            }
        }
        return componente;
    }
}
