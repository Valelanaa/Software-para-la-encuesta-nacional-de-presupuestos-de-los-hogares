package enph;

import enph.vista.InicioSesion;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Swing no es seguro para hilos: toda la interfaz gráfica debe
        // crearse y modificarse en el hilo de eventos (Event Dispatch Thread).
        // invokeLater programa el código para que se ejecute en ese hilo.
        SwingUtilities.invokeLater(() -> {
            // Crea la ventana de inicio de sesión, que es la primera pantalla
            InicioSesion ventana = new InicioSesion();
            ventana.setVisible(true);
        });
    }
}
