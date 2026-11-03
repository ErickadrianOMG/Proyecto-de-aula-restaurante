package restaurante;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import java.awt.EventQueue;
import restaurante.presentacion.VentanaPrincipal;

/** Punto de entrada del programa. */
public class Main {
    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("restaurante.themes");
        FlatDarkLaf.setup();
        EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}

