package enph;

import enph.vista.InicioSesion;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            InicioSesion inicio = new InicioSesion();
            inicio.setLocationRelativeTo(null);
            inicio.setVisible(true);
        });
    }
}