package com.hersac;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.AppViews;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AppViews();
        });
    }
}
