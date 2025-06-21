package com.hersac.ui.views.authentication.login;

import com.hersac.core.modules.authentication.entities.RequestEntity;
import com.hersac.core.modules.authentication.entities.ResponseEntity;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.views.authentication.login.interfaces.LoginListener;
import com.hersac.ui.globals.enums.ColorsTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class LoginView extends JPanel {

    private final AuthenticationController authController;
    private LoginListener loginListener;
    private String token;

    private static final Color COLOR_FONDO = ColorsTheme.BACKGROUND.get();
    private static final Color COLOR_PRIMARY = ColorsTheme.PRIMARY.get();
    private static final Color COLOR_PRIMARY_LIGHT = ColorsTheme.PRIMARY_LIGTH.get();
    private static final Color COLOR_TEXT_PLACEHOLDER = ColorsTheme.TEXT_SECONDARY.get();
    private static final Color COLOR_SECUNDARY = ColorsTheme.SECONDARY.get();
    private static final Font FONT_TITLE = new Font("Roboto", Font.BOLD, 18);
    private static final Font FONT_SUBTITLE = new Font("Roboto", Font.BOLD, 14);
    private static final Font FONT_INPUT = new Font("Roboto", Font.PLAIN, 12);
    private static final Font FONT_PASSWORD = new Font("Roboto", Font.BOLD, 10);
    private static final Font FONT_BUTTON = new Font("Roboto", Font.BOLD, 12);

    public LoginView(AuthenticationController authController) {
        this.authController = authController;
        initUI();
    }

    public void initUI() {

        setLayout(new GridLayout(1, 2));

        JPanel panelIzquierdo = buildPanelIzquierdo();
        JPanel panelDerecho = buildPanelDerecho();

        add(panelIzquierdo);
        add(panelDerecho);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component clickedComponent = getComponentAt(e.getPoint());
                if (!(clickedComponent instanceof JTextField) && !(clickedComponent instanceof JPasswordField)) {
                    requestFocusInWindow();
                }
            }
        });
    }

    private JPanel buildPanelIzquierdo() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));
        panel.setBackground(COLOR_FONDO);

        JLabel tituloLogin = buildLabel("HERP", FONT_TITLE, COLOR_PRIMARY, SwingConstants.CENTER);
        JLabel subtituloLogin = buildLabel("El ERP que tu negocio necesita", FONT_SUBTITLE, COLOR_SECUNDARY, SwingConstants.CENTER);

        JPanel titulosContent = new JPanel();
        titulosContent.setLayout(new BoxLayout(titulosContent, BoxLayout.Y_AXIS));
        titulosContent.setOpaque(false);
        tituloLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtituloLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulosContent.add(tituloLogin);
        titulosContent.add(subtituloLogin);

        JTextField correoField = buildTextField("hello@example.com");
        seleccionarInputText(correoField);

        JPasswordField contrasenaField = buildPasswordField("******");
        seleccionarInputPass(contrasenaField);

        JLabel submitBtn = buildSubmitButton("Acceder", () -> {
            String correo = correoField.getText();
            String contrasena = new String(contrasenaField.getPassword());

            RequestEntity request = new RequestEntity(correo, contrasena);
            ResponseEntity response = authController.login(request);

            if (response == null) {
                return;
            }

            token = response.getToken();
            handleLogin();
        });

        JPanel loginFormContent = new JPanel(new GridLayout(4, 1));
        loginFormContent.setOpaque(false);
        loginFormContent.add(correoField);
        loginFormContent.add(contrasenaField);
        loginFormContent.add(Box.createVerticalStrut(10));
        loginFormContent.add(submitBtn);

        panel.add(Box.createVerticalStrut(10));
        panel.add(titulosContent);
        panel.add(loginFormContent);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JPanel buildPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getImagenLogin(), BorderLayout.CENTER);
        return panel;
    }

    private JLabel buildLabel(String text, Font font, Color color, int alignment) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(alignment);
        return label;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setOpaque(false);
        field.setFont(FONT_INPUT);
        field.setForeground(COLOR_TEXT_PLACEHOLDER);
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_PRIMARY),
            new EmptyBorder(2, 10, 2, 10)
        ));
        return field;
    }

    private JPasswordField buildPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setOpaque(false);
        field.setFont(FONT_PASSWORD);
        field.setForeground(COLOR_TEXT_PLACEHOLDER);
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_PRIMARY),
            new EmptyBorder(2, 10, 2, 10)
        ));
        return field;
    }

    private JLabel buildSubmitButton(String text, Runnable onClick) {
        JLabel btn = new JLabel(text);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY_LIGHT);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        return btn;
    }

    private void seleccionarInputText(JTextField textField) {
        final String placeholder = textField.getText();

        textField.setForeground(COLOR_TEXT_PLACEHOLDER);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.WHITE);
                    textField.setCaretColor(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().trim().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(COLOR_TEXT_PLACEHOLDER);
                }
            }
        });
    }

    private void seleccionarInputPass(JPasswordField passwordField) {
        final String placeholder = new String(passwordField.getPassword());

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.WHITE);
                    passwordField.setCaretColor(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).trim().isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(COLOR_TEXT_PLACEHOLDER);
                }
            }
        });
    }

    private JLabel getImagenLogin() {
        URL url = getClass().getResource("/images/imageLogin.jpg");
        if (url == null) {
            throw new RuntimeException("Error al cargar la imagen de login");
        }

        ImageIcon iconoOriginal = new ImageIcon(url);

        return new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image imagen = iconoOriginal.getImage();
                if (imagen != null) {
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    int imgWidth = imagen.getWidth(null);
                    int imgHeight = imagen.getHeight(null);

                    double scale = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int newWidth = (int) (imgWidth * scale);
                    int newHeight = (int) (imgHeight * scale);

                    int x = (panelWidth - newWidth) / 2;
                    int y = (panelHeight - newHeight) / 2;

                    g.drawImage(imagen, x, y, newWidth, newHeight, this);
                }
            }
        };
    }

    public void addLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    private void handleLogin() {
        if (token != null && !token.isEmpty()) {
            if (loginListener != null) {
                loginListener.onLoginSuccess(token);
            }
        }
    }
}
