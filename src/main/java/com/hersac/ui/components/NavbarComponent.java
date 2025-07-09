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

    private NavigationListener navigationListener;
    private LogoutListener logoutListener;

    private static final Color COLOR_FONDO = ColorsTheme.BACKGROUND.get();
    private static final Color COLOR_TEXTO = ColorsTheme.TEXT_PRIMARY.get();

    private String moduloSeleccionado = "";

    private final JPanel panelMenuFijo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
    private final JPanel panelMenuDinamico = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));

    private final List<String> MODULOS_PRINCIPALES = Arrays.asList("Comercial", "Financiero", "Usuarios");

    private final PermissionService permissionService;

    public NavbarComponent(PermissionService permissionService) {
        this.permissionService = permissionService;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 25));
        setBackground(COLOR_FONDO);

        panelMenuFijo.setBackground(COLOR_FONDO);
        panelMenuDinamico.setBackground(COLOR_FONDO);

        Font fuenteEtiqueta = new Font("Roboto", Font.PLAIN, 11);

        panelMenuFijo.add(crearEtiqueta("Módulos", fuenteEtiqueta, new String[]{"Comercial", "Financiero", "Usuarios"}, null));
        panelMenuFijo.add(crearEtiqueta("Compañía", fuenteEtiqueta, new String[]{"Perfil", "Configuración"}, null));
        panelMenuFijo.add(crearEtiqueta("Terceros", fuenteEtiqueta, null, null));

        add(panelMenuFijo, BorderLayout.WEST);
        add(panelMenuDinamico, BorderLayout.CENTER);

        JButton btnLogout = new JButton(FontIcon.of(FontAwesomeSolid.SIGN_OUT_ALT, 14, COLOR_TEXTO));
        btnLogout.setToolTipText("Cerrar sesión");
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            if (logoutListener != null) {
                logoutListener.onLogout();
            }
        });
        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        panelLogout.setBackground(COLOR_FONDO);
        panelLogout.add(btnLogout);
        add(panelLogout, BorderLayout.EAST);

        construirMenu(fuenteEtiqueta);
    }

    private JLabel crearEtiqueta(String texto, Font fuente, String[] subOpciones, String navigationKey) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(COLOR_FONDO);
        etiqueta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        agregarEfectoHover(etiqueta);

        if (navigationKey != null && navigationListener != null) {
            etiqueta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    navigationListener.onNavigate(navigationKey);
                }
            });
        }

        boolean tieneSubOpciones = subOpciones != null && subOpciones.length > 0;
        if (tieneSubOpciones) {
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
            item.setFont(fuente);
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

        switch (moduloSeleccionado) {
            case "Comercial" -> {
                panelMenuDinamico.add(crearEtiqueta("Clientes", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Ventas", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Compras", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Inventario", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Reportes", fuenteEtiqueta, new String[]{"Ventas", "Compras", "Inventario"}, null));
            }
            case "Financiero" -> {
                panelMenuDinamico.add(crearEtiqueta("CxC", fuenteEtiqueta, null,null));
                panelMenuDinamico.add(crearEtiqueta("CxP", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Movimientos", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Bancos", fuenteEtiqueta, null, null));
                panelMenuDinamico.add(crearEtiqueta("Reportes", fuenteEtiqueta, new String[]{"Flujo de caja", "Estado de resultados", "Balance General"}, null));
            }
            case "Usuarios" -> {
                if (permissionService.tienePermiso((long) Permiso.VER_GESTION_USUARIO.getId())) {
                    panelMenuDinamico.add(crearEtiqueta("Gestión usuarios", fuenteEtiqueta, null, "gestion-usuarios"));
                }
                if (permissionService.tienePermiso((long) Permiso.VER_ROL_PERMISO.getId())) {
                    panelMenuDinamico.add(crearEtiqueta("Roles y permisos", fuenteEtiqueta, null, "roles-permisos"));
                }
                panelMenuDinamico.add(crearEtiqueta("Auditoría", fuenteEtiqueta, null, "auditorias"));
            }
        }

        panelMenuDinamico.revalidate();
        panelMenuDinamico.repaint();
    }

    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }

    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }

    public NavigationListener getNavigationListener() {
        return this.navigationListener;
    }
}
