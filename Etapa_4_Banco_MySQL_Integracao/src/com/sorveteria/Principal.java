package com.sorveteria;

import com.sorveteria.view.TelaLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de entrada principal da aplicação Sorveteria Tropicalzin Cremoso.
 */
public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Aplica tema nativo do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            TelaLogin login = new TelaLogin();
            login.setVisible(true);
        });
    }
}
