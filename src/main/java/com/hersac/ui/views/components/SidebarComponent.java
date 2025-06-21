package com.hersac.ui.views.components;

import com.hersac.ui.views.globals.enums.ColorsTheme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class SidebarComponent extends JPanel {

    private final Color BACKGROUND_COLOR = ColorsTheme.BACKGROUND.get();
    private final Color TEXT_COLOR = ColorsTheme.TEXT_PRIMARY.get();

    public SidebarComponent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(150, 0));
        setBackground(BACKGROUND_COLOR);

        Font labelFont = new Font("Roboto", Font.BOLD, 14);

        add(crearSeccionSuperior());
        add(createOptionPanel("Módulos", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Compañía", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Terceros", labelFont));
        add(Box.createVerticalStrut(10));
        add(createOptionPanel("Reportes", labelFont));
    }

    private JPanel crearSeccionSuperior() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);
        panelSuperior.setMaximumSize(new Dimension(150, 130));

        URL avatarURL = getClass().getResource("/images/avatar.jpeg");
        ImageIcon avatarIcon = new ImageIcon(avatarURL);

        Icon iconConfiguraciones = FontIcon.of(FontAwesomeSolid.COG, 16, TEXT_COLOR);
        Icon iconNotificaciones = FontIcon.of(FontAwesomeSolid.BELL, 16, TEXT_COLOR);

        JPanel panelAvatar = new AvatarPanelCircular(avatarIcon);
        JPanel panelConfiguraciones = new IconoPanelCircular(iconConfiguraciones);
        JPanel panelNotificaciones = new IconoPanelCircular(iconNotificaciones);

        abrirOpcion(panelAvatar, () -> System.out.println("Avatar clicked"));
        abrirOpcion(panelConfiguraciones, () -> System.out.println("Configuraciones clicked"));
        abrirOpcion(panelNotificaciones, () -> System.out.println("Notificaciones clicked"));

        JPanel panelContenedorAvatar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelContenedorAvatar.setOpaque(false);
        panelContenedorAvatar.add(panelAvatar);

        JPanel panelContenedorBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelContenedorBotones.setOpaque(false);
        panelContenedorBotones.add(panelConfiguraciones);
        panelContenedorBotones.add(panelNotificaciones);

        panelSuperior.add(panelContenedorAvatar);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(panelContenedorBotones);

        return panelSuperior;
    }

    private JPanel createOptionPanel(String text, Font font) {
        JPanel optionPanel = new JPanel(new GridBagLayout());
        optionPanel.setPreferredSize(new Dimension(150, 50));
        optionPanel.setMaximumSize(new Dimension(150, 50));
        optionPanel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        optionPanel.add(label);
        abrirOpcion(optionPanel, () -> System.out.println(text + " clicked"));

        return optionPanel;
    }

    private void abrirOpcion(JPanel panel, Runnable action) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
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
