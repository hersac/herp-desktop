package com.hersac.ui.views.authentication.login;

import com.hersac.core.modules.authentication.entities.RequestEntity;
import com.hersac.core.modules.authentication.entities.ResponseEntity;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.views.authentication.login.interfaces.LoginListener;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.core.globals.exceptions.UsuarioNoEncontradoException;
import com.hersac.core.globals.exceptions.NoAutenticadoException;
import com.hersac.core.globals.exceptions.UsuarioBloqueadoException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class LoginView extends JPanel {
    private final AuthenticationController controladorAutenticacion;
    private LoginListener escuchaLogin;
    private String token;

    private static final Color COLOR_FONDO = ColorsTheme.BACKGROUND.get();
    private static final Color COLOR_PRIMARIO = ColorsTheme.PRIMARY.get();
    private static final Color COLOR_PRIMARIO_CLARO = ColorsTheme.PRIMARY_LIGTH.get();
    private static final Color COLOR_TEXTO_PLACEHOLDER = ColorsTheme.TEXT_SECONDARY.get();
    private static final Color COLOR_SECUNDARIO = ColorsTheme.SECONDARY.get();
    private static final Font FUENTE_TITULO = new Font("Roboto", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Roboto", Font.BOLD, 14);
    private static final Font FUENTE_INPUT = new Font("Roboto", Font.PLAIN, 12);
    private static final Font FUENTE_PASSWORD = new Font("Roboto", Font.BOLD, 10);
    private static final Font FUENTE_BOTON = new Font("Roboto", Font.BOLD, 12);

    public LoginView(AuthenticationController controladorAutenticacion) {
        this.controladorAutenticacion = controladorAutenticacion;
        inicializarUI();
    }

    public void inicializarUI() {
        setLayout(new GridLayout(1, 2));
        JPanel panelIzquierdo = construirPanelIzquierdo();
        JPanel panelDerecho = construirPanelDerecho();
        add(panelIzquierdo);
        add(panelDerecho);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component componenteClic = getComponentAt(e.getPoint());
                if (!(componenteClic instanceof JTextField)) {
                    requestFocusInWindow();
                }
            }
        });
    }

    private JPanel construirPanelIzquierdo() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));
        panel.setBackground(COLOR_FONDO);
        JLabel etiquetaTitulo = construirEtiqueta("HERP", FUENTE_TITULO, COLOR_PRIMARIO, SwingConstants.CENTER);
        JLabel etiquetaSubtitulo = construirEtiqueta("El ERP que tu negocio necesita", FUENTE_SUBTITULO, COLOR_SECUNDARIO, SwingConstants.CENTER);
        JPanel contenidoTitulos = new JPanel();
        contenidoTitulos.setLayout(new BoxLayout(contenidoTitulos, BoxLayout.Y_AXIS));
        contenidoTitulos.setOpaque(false);
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoTitulos.add(etiquetaTitulo);
        contenidoTitulos.add(etiquetaSubtitulo);
        JPanel contenidoFormulario = construirContenidoFormulario();
        panel.add(Box.createVerticalStrut(10));
        panel.add(contenidoTitulos);
        panel.add(contenidoFormulario);
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private JPanel construirContenidoFormulario() {
        JTextField campoCorreo = construirCampoTexto();
        campoCorreo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isUpperCase(c)) {
                    e.setKeyChar(Character.toLowerCase(c));
                }
            }
        });
        seleccionarInputTexto(campoCorreo);
        JPasswordField campoContrasena = construirCampoPassword();
        seleccionarInputPassword(campoContrasena);
        JLabel botonAcceder = construirBotonAcceder(() -> {
            String correo = campoCorreo.getText();
            String contrasena = new String(campoContrasena.getPassword());
            RequestEntity solicitud = new RequestEntity(correo, contrasena);
            try {
                ResponseEntity respuesta = controladorAutenticacion.login(solicitud);
                if (respuesta != null) {
                    token = respuesta.getToken();
                    manejarLogin();
                }
            } catch (UsuarioNoEncontradoException ex) {
                mostrarMensajeError(ex.getMessage());
            } catch (UsuarioBloqueadoException ex) {
                mostrarMensajeError(ex.getMessage());
            } catch (NoAutenticadoException ex) {
                mostrarMensajeError(ex.getMessage());
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado al intentar iniciar sesión");
            }
        });
        JPanel contenidoFormulario = new JPanel(new GridLayout(4, 1));
        contenidoFormulario.setOpaque(false);
        contenidoFormulario.add(campoCorreo);
        contenidoFormulario.add(campoContrasena);
        contenidoFormulario.add(Box.createVerticalStrut(10));
        contenidoFormulario.add(botonAcceder);
        return contenidoFormulario;
    }

    private JPanel construirPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(obtenerImagenLogin(), BorderLayout.CENTER);
        return panel;
    }

    private JLabel construirEtiqueta(String texto, Font fuente, Color color, int alineacion) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Roboto", fuente.getStyle(), fuente.getSize()));
        etiqueta.setForeground(color);
        etiqueta.setHorizontalAlignment(alineacion);
        return etiqueta;
    }

    private JTextField construirCampoTexto() {
        JTextField campo = new JTextField("hello@example.com");
        campo.setOpaque(false);
        campo.setFont(FUENTE_INPUT);
        campo.setForeground(COLOR_TEXTO_PLACEHOLDER);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_PRIMARIO),
            new EmptyBorder(2, 10, 2, 10)
        ));
        campo.setFont(new Font("Roboto", Font.PLAIN, 12));
        return campo;
    }

    private JPasswordField construirCampoPassword() {
        JPasswordField campo = new JPasswordField("******");
        campo.setOpaque(false);
        campo.setFont(FUENTE_PASSWORD);
        campo.setForeground(COLOR_TEXTO_PLACEHOLDER);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_PRIMARIO),
            new EmptyBorder(2, 10, 2, 10)
        ));
        campo.setFont(new Font("Roboto", Font.BOLD, 10));
        return campo;
    }

    private JLabel construirBotonAcceder(Runnable alClic) {
        JLabel boton = new JLabel("Acceder");
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(Color.WHITE);
        boton.setBackground(COLOR_PRIMARIO);
        boton.setOpaque(true);
        boton.setFont(new Font("Roboto", Font.BOLD, 12));
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO_CLARO);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (alClic != null) {
                    alClic.run();
                }
            }
        });
        return boton;
    }

    private void seleccionarInputTexto(JTextField campoTexto) {
        final String placeholder = campoTexto.getText();
        campoTexto.setForeground(COLOR_TEXTO_PLACEHOLDER);
        campoTexto.setFont(new Font("Roboto", Font.PLAIN, 12));
        campoTexto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoTexto.getText().equals(placeholder)) {
                    campoTexto.setText("");
                    campoTexto.setForeground(Color.WHITE);
                    campoTexto.setCaretColor(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campoTexto.getText().trim().isEmpty()) {
                    campoTexto.setText(placeholder);
                    campoTexto.setForeground(COLOR_TEXTO_PLACEHOLDER);
                }
            }
        });
    }

    private void seleccionarInputPassword(JPasswordField campoPassword) {
        final String placeholder = new String(campoPassword.getPassword());
        campoPassword.setFont(new Font("Roboto", Font.BOLD, 10));
        campoPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(campoPassword.getPassword()).equals(placeholder)) {
                    campoPassword.setText("");
                    campoPassword.setForeground(Color.WHITE);
                    campoPassword.setCaretColor(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(campoPassword.getPassword()).trim().isEmpty()) {
                    campoPassword.setText(placeholder);
                    campoPassword.setForeground(COLOR_TEXTO_PLACEHOLDER);
                }
            }
        });
    }

    private JLabel obtenerImagenLogin() {
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
                    int anchoPanel = getWidth();
                    int altoPanel = getHeight();
                    int anchoImg = imagen.getWidth(null);
                    int altoImg = imagen.getHeight(null);
                    double escala = Math.max((double) anchoPanel / anchoImg, (double) altoPanel / altoImg);
                    int nuevoAncho = (int) (anchoImg * escala);
                    int nuevoAlto = (int) (altoImg * escala);
                    int x = (anchoPanel - nuevoAncho) / 2;
                    int y = (altoPanel - nuevoAlto) / 2;
                    g.drawImage(imagen, x, y, nuevoAncho, nuevoAlto, this);
                }
            }
        };
    }

    public void addLoginListener(LoginListener escuchaLogin) {
        this.escuchaLogin = escuchaLogin;
    }

    private void manejarLogin() {
        if (token != null && !token.isEmpty() && escuchaLogin != null) {
            escuchaLogin.onLoginSuccess(token);
        }
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de autenticación", JOptionPane.ERROR_MESSAGE);
    }
}
