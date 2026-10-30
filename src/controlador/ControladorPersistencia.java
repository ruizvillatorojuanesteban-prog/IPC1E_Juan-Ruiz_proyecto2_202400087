package controlador;

import estructuras.ListaDobleResidentes;
import modelo.Residente;
import modelo.Vehiculo;

import java.io.*;

public class ControladorPersistencia {
    private static final String ARCHIVO_RESIDENTES = "residentes.txt";
    private static final String ARCHIVO_VEHICULOS = "vehiculos.txt";

    // Guardar datos al salir del programa
    public static void guardarDatos(ListaDobleResidentes listaResidentes) {
        try (PrintWriter pwRes = new PrintWriter(new FileWriter(ARCHIVO_RESIDENTES));
             PrintWriter pwVeh = new PrintWriter(new FileWriter(ARCHIVO_VEHICULOS))) {

            var actual = listaResidentes.getCabeza();
            while (actual != null) {
                Residente r = actual.getResidente();
                
                // Formato exigido: ID|NOMBRE|CASA|SOCIO
                pwRes.println(r.getDpi() + "|" + r.getNombre() + "|" + r.getNumeroCasa() + "|" + r.isEsSocio());

                // Guardar los vehículos asociados a este residente
                var nodoVehiculo = r.getVehiculosAsociados().getCabeza();
                while (nodoVehiculo != null) {
                    Vehiculo v = nodoVehiculo.getVehiculo();
                    // Formato exigido: PLACA|MARCA|MODELO|COLOR|ID_RESIDENTE
                    pwVeh.println(v.getPlaca() + "|" + v.getMarca() + "|" + v.getModelo() + "|" + v.getColor() + "|" + r.getDpi());
                    nodoVehiculo = nodoVehiculo.getSiguiente();
                }

                actual = actual.getSiguiente();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los archivos de texto: " + e.getMessage());
        }
    }

    // Cargar datos al iniciar el programa
    public static void cargarDatos(ListaDobleResidentes listaResidentes) {
        File fRes = new File(ARCHIVO_RESIDENTES);
        if (fRes.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fRes))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split("\\|");
                    if (partes.length >= 4) {
                        String id = partes[0];
                        String nombre = partes[1];
                        String casa = partes[2];
                        boolean esSocio = Boolean.parseBoolean(partes[3]);
                        listaResidentes.registrarResidente(id, nombre, casa, esSocio);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer residentes.txt: " + e.getMessage());
            }
        }

        File fVeh = new File(ARCHIVO_VEHICULOS);
        if (fVeh.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fVeh))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split("\\|");
                    if (partes.length >= 5) {
                        String placa = partes[0];
                        String marca = partes[1];
                        String modelo = partes[2];
                        String color = partes[3];
                        String idResidente = partes[4];

                        Vehiculo v = new Vehiculo(placa, marca, modelo, color, "Automóvil");
                        listaResidentes.registrarVehiculoAResidente(idResidente, v);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer vehiculos.txt: " + e.getMessage());
            }
        }
    }
}