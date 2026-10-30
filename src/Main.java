import controlador.ControladorPersistencia;
import estructuras.ListaDobleResidentes;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        ListaDobleResidentes listaResidentes = new ListaDobleResidentes();

        ControladorPersistencia.cargarDatos(listaResidentes);

        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal(listaResidentes).setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ControladorPersistencia.guardarDatos(listaResidentes);
            System.out.println("Datos guardados correctamente al salir.");
        }));
    }
}