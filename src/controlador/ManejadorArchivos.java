package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManejadorArchivos {

    // Método para leer un archivo de texto y retornar su contenido línea por línea o procesarlo
    public static String leerArchivo(String rutaArchivo) {
        StringBuilder contenido = new StringBuilder();
        File archivo = new File(rutaArchivo);
        
        if (!archivo.exists()) {
            return "El archivo no existe en la ruta especificada: " + rutaArchivo;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return contenido.toString();
    }

    // Método para escribir o agregar texto a un archivo
    public static boolean escribirArchivo(String rutaArchivo, String contenido, boolean anexar) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, anexar))) {
            bw.write(contenido);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}