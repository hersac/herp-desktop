package com.hersac.ui.components;

import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.listeners.NavigationListener;
import com.hersac.ui.listeners.LogoutListener;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class NavbarComponent extends JPanel {
    private NavigationListener escuchaNavegacion;
    private LogoutListener escuchaCerrarSesion;
    private static final Color COLOR_FONDO = ColorsTheme.BACKGROUND.get();
    private static final Color COLOR_TEXTO = ColorsTheme.TEXT_PRIMARY.get();
    private String moduloSeleccionado = "";
    private final JPanel panelMenuFijo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
    private final JPanel panelMenuDinamico = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
    private final List<String> MODULOS_PRINCIPALES = Arrays.asList("Comercial", "Financiero", "Usuarios");
    private final PermissionService servicioPermisos;

    public NavbarComponent(PermissionService servicioPermisos) {
        this.servicioPermisos = servicioPermisos;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 25));
        setBackground(COLOR_FONDO);
        panelMenuFijo.setBackground(COLOR_FONDO);
        panelMenuDinamico.setBackground(COLOR_FONDO);
        Font fuenteEtiqueta = new Font("Roboto", Font.PLAIN, 11);
        panelMenuFijo.add(crearEtiqueta("Módulos", fuenteEtiqueta, new String[]{"Comercial", "Financiero", "Usuarios"}, null));
        panelMenuFijo.add(crearEtiqueta("Compañía", fuenteEtiqueta, new String[]{"Perfil", "Configuración"}, null));
        panelMenuFijo.add(crearEtiqueta("Terceros", fuenteEtiqueta, null, "gestion-terceros"));
        add(panelMenuFijo, BorderLayout.WEST);
        add(panelMenuDinamico, BorderLayout.CENTER);
        JButton botonCerrarSesion = new JButton(FontIcon.of(FontAwesomeSolid.SIGN_OUT_ALT, 14, COLOR_TEXTO));
        botonCerrarSesion.setToolTipText("Cerrar sesión");
        botonCerrarSesion.setBorderPainted(false);
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setContentAreaFilled(false);
        botonCerrarSesion.setOpaque(false);
        botonCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCerrarSesion.addActionListener(e -> {
            if (escuchaCerrarSesion != null) {
                escuchaCerrarSesion.onLogout();
            }
        });
        JPanel panelCerrarSesion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        panelCerrarSesion.setBackground(COLOR_FONDO);
        panelCerrarSesion.add(botonCerrarSesion);
        add(panelCerrarSesion, BorderLayout.EAST);
        construirMenu(fuenteEtiqueta);
    }

    private JLabel crearEtiqueta(String texto, Font fuente, String[] subOpciones, String claveNavegacion) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("Roboto", Font.PLAIN, fuente.getSize()));
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(COLOR_FONDO);
        etiqueta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        agregarEfectoHover(etiqueta);
        if (claveNavegacion != null) {
            etiqueta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (escuchaNavegacion != null) {
                        escuchaNavegacion.onNavigate(claveNavegacion);
                    }
                }
            });
        }
        if (subOpciones != null && subOpciones.length > 0) {
            JPopupMenu menuEmergente = crearMenuEmergente(subOpciones, fuente);
            agregarEscuchadorMenu(etiqueta, menuEmergente);
        }
        if (MODULOS_PRINCIPALES.contains(texto)) {
            etiqueta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    moduloSeleccionado = texto;
                    construirMenu(fuente);
                }
            });
        }
        return etiqueta;
    }

    private void agregarEfectoHover(JLabel etiqueta) {
        etiqueta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                etiqueta.setBackground(ColorsTheme.BACKGROUND_LIGHT.get());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                etiqueta.setBackground(ColorsTheme.BACKGROUND.get());
            }
        });
    }

    private JPopupMenu crearMenuEmergente(String[] opciones, Font fuente) {
        JPopupMenu menu = new JPopupMenu();
        for (String opcion : opciones) {
            JMenuItem item = new JMenuItem(opcion);
            item.setBackground(COLOR_FONDO);
            item.setForeground(COLOR_TEXTO);
            item.setFont(new Font("Roboto", Font.PLAIN, fuente.getSize()));
            item.setBorder(BorderFactory.createLineBorder(COLOR_FONDO, 1));
            item.addActionListener(e -> {
                if (MODULOS_PRINCIPALES.contains(opcion)) {
                    moduloSeleccionado = opcion;
                    construirMenu(fuente);
                }
            });
            menu.add(item);
        }
        return menu;
    }

    private void agregarEscuchadorMenu(JLabel etiqueta, JPopupMenu menuEmergente) {
        etiqueta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuEmergente.show(etiqueta, 0, etiqueta.getHeight());
            }
        });
    }

    private void construirMenu(Font fuenteEtiqueta) {
        panelMenuDinamico.removeAll();
        if ("Comercial".equals(moduloSeleccionado)) {
            panelMenuDinamico.add(crearEtiqueta("Clientes", fuenteEtiqueta, null, "gestion-clientes"));
            panelMenuDinamico.add(crearEtiqueta("Ventas", fuenteEtiqueta, null, "gestion-ventas"));
            panelMenuDinamico.add(crearEtiqueta("Compras", fuenteEtiqueta, null, "gestion-compras"));
            panelMenuDinamico.add(crearEtiqueta("Inventario", fuenteEtiqueta, null, "gestion-inventario"));
            panelMenuDinamico.add(crearEtiqueta("Reportes", fuenteEtiqueta, new String[]{"Ventas", "Compras", "Inventario"}, null));
        }
        if ("Financiero".equals(moduloSeleccionado)) {
            panelMenuDinamico.add(crearEtiqueta("CxC", fuenteEtiqueta, null,null));
            panelMenuDinamico.add(crearEtiqueta("CxP", fuenteEtiqueta, null, null));
            panelMenuDinamico.add(crearEtiqueta("Movimientos", fuenteEtiqueta, null, null));
            panelMenuDinamico.add(crearEtiqueta("Bancos", fuenteEtiqueta, null, null));
            panelMenuDinamico.add(crearEtiqueta("Reportes", fuenteEtiqueta, new String[]{"Flujo de caja", "Estado de resultados", "Balance General"}, null));
        }
        if ("Usuarios".equals(moduloSeleccionado)) {
            if (servicioPermisos.tienePermiso((long) Permiso.VER_GESTION_USUARIO.getId())) {
                panelMenuDinamico.add(crearEtiqueta("Gestión usuarios", fuenteEtiqueta, null, "gestion-usuarios"));
            }
            if (servicioPermisos.tienePermiso((long) Permiso.VER_ROL_PERMISO.getId())) {
                panelMenuDinamico.add(crearEtiqueta("Roles y permisos", fuenteEtiqueta, null, "roles-permisos"));
            }
            panelMenuDinamico.add(crearEtiqueta("Auditoría", fuenteEtiqueta, null, "auditorias"));
        }
        panelMenuDinamico.revalidate();
        panelMenuDinamico.repaint();
    }

    public void setNavigationListener(NavigationListener listener) {
        this.escuchaNavegacion = listener;
    }

    public void setLogoutListener(LogoutListener listener) {
        this.escuchaCerrarSesion = listener;
    }

    public NavigationListener getNavigationListener() {
        return this.escuchaNavegacion;
    }
}
