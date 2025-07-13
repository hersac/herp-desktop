package com.hersac.ui.components;

import com.hersac.ui.globals.enums.ColorsTheme;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class IconoPanelCircular extends JPanel {

    private final Icon icono;

    public IconoPanelCircular(Icon icono) {
        this.icono = icono;
        setOpaque(false);
        setPreferredSize(new Dimension(30, 30));
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);

        int diametro = Math.min(getWidth(), getHeight());
        int posicionX = (getWidth() - diametro) / 2;
        int posicionY = (getHeight() - diametro) / 2;

        Graphics2D graficos2D = (Graphics2D) graficos.create();
        graficos2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graficos2D.setColor(ColorsTheme.BACKGROUND.get());
        graficos2D.fillOval(posicionX, posicionY, diametro, diametro);

        if (icono instanceof FontIcon fontIcono) {
            fontIcono.setIconSize(diametro - 12);
            int fx = (getWidth() - fontIcono.getIconWidth()) / 2;
            int fy = (getHeight() - fontIcono.getIconHeight()) / 2;
            fontIcono.paintIcon(this, graficos2D, fx, fy);
        }
        if (!(icono instanceof FontIcon)) {
            int ix = (getWidth() - icono.getIconWidth()) / 2;
            int iy = (getHeight() - icono.getIconHeight()) / 2;
            icono.paintIcon(this, graficos2D, ix, iy);
        }

        graficos2D.dispose();
    }

    public static void aplicarFuenteRoboto(Component componente) {
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 12);
        if (componente instanceof JLabel etiqueta) {
            etiqueta.setFont(fuenteRoboto);
        }
        if (componente instanceof JTextField campoTexto) {
            campoTexto.setFont(fuenteRoboto);
        }
    }
}
