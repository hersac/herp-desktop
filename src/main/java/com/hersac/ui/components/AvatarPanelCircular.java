package com.hersac.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class AvatarPanelCircular extends JPanel {

    private final Image imagen;

    public AvatarPanelCircular(ImageIcon iconoImagen) {
        this.imagen = iconoImagen.getImage();
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60));
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        if (imagen == null) return;

        int diametro = Math.min(getWidth(), getHeight());
        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int anchoImagen = imagen.getWidth(null);
        int altoImagen = imagen.getHeight(null);
        if (anchoImagen <= 0 || altoImagen <= 0) return;

        double escala = Math.max((double) diametro / anchoImagen, (double) diametro / altoImagen);
        int nuevoAncho = (int) (anchoImagen * escala);
        int nuevoAlto = (int) (altoImagen * escala);

        int posicionX = (anchoPanel - nuevoAncho) / 2;
        int posicionY = (altoPanel - nuevoAlto) / 2;

        Graphics2D graficos2D = (Graphics2D) graficos.create();
        graficos2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graficos2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graficos2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Float recorte = new Ellipse2D.Float(
            (anchoPanel - diametro) / 2f,
            (altoPanel - diametro) / 2f,
            diametro,
            diametro
        );
        graficos2D.setClip(recorte);
        graficos2D.drawImage(imagen, posicionX, posicionY, nuevoAncho, nuevoAlto, this);
        graficos2D.setClip(null);
        graficos2D.setColor(new Color(0, 0, 0, 40));
        graficos2D.setStroke(new BasicStroke(1.2f));
        graficos2D.draw(recorte);
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
