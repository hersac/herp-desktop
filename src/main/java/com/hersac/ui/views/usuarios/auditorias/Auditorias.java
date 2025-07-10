package com.hersac.ui.views.usuarios.auditorias;

import javax.swing.*;
import java.awt.*;

public class Auditorias extends JPanel {
    public Auditorias() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        JLabel titleLabel = new JLabel("Auditorias");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
    }
}
