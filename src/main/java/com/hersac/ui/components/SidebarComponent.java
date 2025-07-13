package com.hersac.ui.components;

import com.hersac.ui.globals.enums.ColorsTheme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class SidebarComponent extends JPanel {

    private final Color COLOR_FONDO = ColorsTheme.BACKGROUND.get();
    private final Color COLOR_TEXTO = ColorsTheme.TEXT_PRIMARY.get();

    public SidebarComponent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(150, 0));
        setBackground(COLOR_FONDO);

        Font fuenteLabel = new Font("Roboto", Font.BOLD, 14);

        add(crearSeccionSuperior());
        add(crearPanelOpcion("Informes", fuenteLabel));
        add(Box.createVerticalStrut(10));
        add(crearPanelOpcion("Calendario", fuenteLabel));
        add(Box.createVerticalStrut(10));
        add(crearPanelOpcion("Integraciones", fuenteLabel));
        add(Box.createVerticalStrut(10));
        add(crearPanelOpcion("Socios", fuenteLabel));
    }

    private JPanel crearSeccionSuperior() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);
        panelSuperior.setMaximumSize(new Dimension(150, 130));

        URL urlAvatar = getClass().getResource("/images/avatar.jpeg");
        ImageIcon iconoAvatar = new ImageIcon(urlAvatar);

        Icon iconoConfiguraciones = FontIcon.of(FontAwesomeSolid.COG, 16, COLOR_TEXTO);
        Icon iconoNotificaciones = FontIcon.of(FontAwesomeSolid.BELL, 16, COLOR_TEXTO);

        JPanel panelAvatar = new AvatarPanelCircular(iconoAvatar);
        JPanel panelConfiguraciones = new IconoPanelCircular(iconoConfiguraciones);
        JPanel panelNotificaciones = new IconoPanelCircular(iconoNotificaciones);

        agregarAccion(panelAvatar, () -> System.out.println("Avatar clickeado"));
        agregarAccion(panelConfiguraciones, () -> System.out.println("Configuraciones clickeado"));
        agregarAccion(panelNotificaciones, () -> System.out.println("Notificaciones clickeado"));

        JPanel contenedorAvatar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorAvatar.setOpaque(false);
        contenedorAvatar.add(panelAvatar);

        JPanel contenedorBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        contenedorBotones.setOpaque(false);
        contenedorBotones.add(panelConfiguraciones);
        contenedorBotones.add(panelNotificaciones);

        panelSuperior.add(contenedorAvatar);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(contenedorBotones);

        return panelSuperior;
    }

    private JPanel crearPanelOpcion(String texto, Font fuente) {
        JPanel panelOpcion = new JPanel(new GridBagLayout());
        panelOpcion.setPreferredSize(new Dimension(150, 50));
        panelOpcion.setMaximumSize(new Dimension(150, 50));
        panelOpcion.setBackground(COLOR_FONDO);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setFont(new Font("Roboto", Font.BOLD, 14));

        panelOpcion.add(etiqueta);
        agregarAccion(panelOpcion, () -> System.out.println(texto + " clickeado"));

        return panelOpcion;
    }

    private void agregarAccion(JPanel panel, Runnable accion) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.setBackground(ColorsTheme.BACKGROUND_LIGHT.get());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(ColorsTheme.BACKGROUND.get());
            }
        });
    }
}
