package controlador;

import estructuras.ListaDobleResidentes;
import modelo.Residente;
import modelo.Vehiculo;

public class ControladorPersistencia {
    private static final String RUTA_RESIDENTES = "residentes.txt";
    private static final String RUTA_VEHICULOS = "vehiculos.txt";

    public static void cargarDatos(ListaDobleResidentes listaResidentes) {
        // 1. Cargar Residentes
        String contenidoResidentes = ManejadorArchivos.leerTexto(RUTA_RESIDENTES);
        if (!contenidoResidentes.isEmpty()) {
            String[] lineas = contenidoResidentes.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length >= 4) {
                    String id = partes[0];
                    String nombre = partes[1];
                    String casa = partes[2];
                    boolean esSocio = Boolean.parseBoolean(partes[3]);
                    
                    Residente res = new Residente(id, nombre, casa, esSocio);
                    listaResidentes.insertar(res);
                }
            }
        }

        // 2. Cargar Vehículos usando los 5 parámetros de tu constructor
        String contenidoVehiculos = ManejadorArchivos.leerTexto(RUTA_VEHICULOS);
        if (!contenidoVehiculos.isEmpty()) {
            String[] lineas = contenidoVehiculos.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length >= 5) {
                    String placa = partes[0];
                    String marca = partes[1];
                    String modelo = partes[2];
                    String color = partes[3];
                    String idResidente = partes[4];

                    Residente res = listaResidentes.buscar(idResidente);
                    if (res != null) {
                        // Pasamos los 5 parámetros requeridos por tu clase Vehiculo
                        Vehiculo veh = new Vehiculo(placa, marca, modelo, color, "Automovil");
                        res.getVehiculosAsociados().agregarVehiculo(veh);
                    }
                }
            }
        }
    }

    public static void guardarDatos(ListaDobleResidentes listaResidentes) {
        StringBuilder sbResidentes = new StringBuilder();
        StringBuilder sbVehiculos = new StringBuilder();

        ManejadorArchivos.guardarTexto(RUTA_RESIDENTES, sbResidentes.toString());
        ManejadorArchivos.guardarTexto(RUTA_VEHICULOS, sbVehiculos.toString());
    }
}