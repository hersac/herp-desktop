package com.hersac.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class AvatarPanelCircular extends JPanel {

    private final Image image;

    public AvatarPanelCircular(ImageIcon imageIcon) {
        this.image = imageIcon.getImage();
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        int diameter = Math.min(getWidth(), getHeight());
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        if (imgWidth <= 0 || imgHeight <= 0) return;

        double scale = Math.max((double) diameter / imgWidth, (double) diameter / imgHeight);
        int newWidth = (int) (imgWidth * scale);
        int newHeight = (int) (imgHeight * scale);

        int x = (panelWidth - newWidth) / 2;
        int y = (panelHeight - newHeight) / 2;

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Float clip = new Ellipse2D.Float(
            (panelWidth - diameter) / 2f,
            (panelHeight - diameter) / 2f,
            diameter,
            diameter
        );
        g2.setClip(clip);

        g2.drawImage(image, x, y, newWidth, newHeight, this);
        g2.setClip(null);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(clip);

        g2.dispose();
    }
}
