package controlador;
import java.io.*;

public class ManejadorArchivos {

    public static void guardarTexto(String ruta, String contenido) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(ruta, false))) {
            escritor.print(contenido);
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
        }
    }

    public static String leerTexto(String ruta) {
        StringBuilder sb = new StringBuilder();
        File archivo = new File(ruta);
        if (!archivo.exists()) return "";

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        return sb.toString();
    }
}