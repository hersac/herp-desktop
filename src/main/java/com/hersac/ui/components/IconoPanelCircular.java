package com.hersac.ui.components;

import com.hersac.ui.globals.enums.ColorsTheme;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class IconoPanelCircular extends JPanel {

    private final Icon icon;

    public IconoPanelCircular(Icon icon) {
        this.icon = icon;
        setOpaque(false);
        setPreferredSize(new Dimension(30, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int diameter = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ColorsTheme.BACKGROUND.get());
        g2.fillOval(x, y, diameter, diameter);

        if (icon instanceof FontIcon fontIcon) {
            fontIcon.setIconSize(diameter - 12);
            int fx = (getWidth() - fontIcon.getIconWidth()) / 2;
            int fy = (getHeight() - fontIcon.getIconHeight()) / 2;
            fontIcon.paintIcon(this, g2, fx, fy);
        } else {
            int ix = (getWidth() - icon.getIconWidth()) / 2;
            int iy = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, ix, iy);
        }

        g2.dispose();
    }
}
