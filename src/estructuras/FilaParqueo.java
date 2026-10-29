package estructuras;

import modelo.Vehiculo;

public class FilaParqueo {
    private char letraFila;
    private int capacidad;
    private EspacioParqueo primerEspacio;
    private FilaParqueo siguienteFila;

    public FilaParqueo(char letraFila, int capacidad) {
        this.letraFila = letraFila;
        this.capacidad = capacidad;
        this.primerEspacio = null;
        this.siguienteFila = null;
        inicializarEspacios();
    }

    private void inicializarEspacios() {
        EspacioParqueo ultimo = null;
        for (int i = 1; i <= capacidad; i++) {
            String id = letraFila + String.valueOf(i);
            EspacioParqueo nuevo = new EspacioParqueo(id);
            if (primerEspacio == null) {
                primerEspacio = nuevo;
            } else {
                ultimo.setSiguiente(nuevo);
            }
            ultimo = nuevo;
        }
    }

    public boolean asignarVehiculoEnEspacioLibre(Vehiculo v) {
        EspacioParqueo actual = primerEspacio;
        while (actual != null) {
            if (!actual.isOcupado()) {
                actual.estacionarVehiculo(v);
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public char getLetraFila() { return letraFila; }
    public EspacioParqueo getPrimerEspacio() { return primerEspacio; }
    public FilaParqueo getSiguienteFila() { return siguienteFila; }
    public void setSiguienteFila(FilaParqueo siguienteFila) { this.siguienteFila = siguienteFila; }
}