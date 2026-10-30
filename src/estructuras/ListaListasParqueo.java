package estructuras;

import modelo.Vehiculo;

public class ListaListasParqueo {
    private FilaParqueo inicioFilas;

    public ListaListasParqueo() {
        // Área de socios (A, B, C con 25 espacios cada una)
        agregarFila('A', 25);
        agregarFila('B', 25);
        agregarFila('C', 25);
        
        // Área general/visitantes (E, F, G, H, I con 15 espacios cada una)
        agregarFila('E', 15);
        agregarFila('F', 15);
        agregarFila('G', 15);
        agregarFila('H', 15);
        agregarFila('I', 15);
    }

    private void agregarFila(char letra, int capacidad) {
        FilaParqueo nuevaFila = new FilaParqueo(letra, capacidad);
        if (inicioFilas == null) {
            inicioFilas = nuevaFila;
        } else {
            FilaParqueo actual = inicioFilas;
            while (actual.getSiguienteFila() != null) {
                actual = actual.getSiguienteFila();
            }
            actual.setSiguienteFila(nuevaFila);
        }
    }

    // Nuevo método getter para la representación gráfica en Swing
    public FilaParqueo getInicioFilas() {
        return inicioFilas;
    }

    public boolean estacionarVehiculo(Vehiculo vehiculo, boolean esSocio) {
        FilaParqueo actual = inicioFilas;
        
        while (actual != null) {
            boolean esFilaSocio = (actual.getLetraFila() == 'A' || actual.getLetraFila() == 'B' || actual.getLetraFila() == 'C');
            
            if (esSocio && esFilaSocio) {
                if (actual.asignarVehiculoEnEspacioLibre(vehiculo)) return true;
            } else if (!esSocio && !esFilaSocio) {
                if (actual.asignarVehiculoEnEspacioLibre(vehiculo)) return true;
            }
            actual = actual.getSiguienteFila();
        }
        
        // Desborde para socios si su área está llena
        if (esSocio) {
            actual = inicioFilas;
            while (actual != null) {
                boolean esFilaGeneral = (actual.getLetraFila() >= 'E' && actual.getLetraFila() <= 'I');
                if (esFilaGeneral) {
                    if (actual.asignarVehiculoEnEspacioLibre(vehiculo)) return true;
                }
                actual = actual.getSiguienteFila();
            }
        }
        
        return false;
    }

    public boolean liberarEspacioVehiculo(String placa) {
        FilaParqueo actualFila = inicioFilas;
        while (actualFila != null) {
            EspacioParqueo actualEspacio = actualFila.getPrimerEspacio();
            while (actualEspacio != null) {
                if (actualEspacio.isOcupado() && actualEspacio.getVehiculoActual() != null && 
                    actualEspacio.getVehiculoActual().getPlaca().equalsIgnoreCase(placa)) {
                    actualEspacio.liberarEspacio();
                    return true;
                }
                actualEspacio = actualEspacio.getSiguiente();
            }
            actualFila = actualFila.getSiguienteFila();
        }
        return false;
    }
}