package estructuras;

import modelo.Vehiculo;

public class EspacioParqueo {
    private String idEspacio;
    private boolean ocupado;
    private Vehiculo vehiculoActual;
    private EspacioParqueo siguiente;

    public EspacioParqueo(String idEspacio) {
        this.idEspacio = idEspacio;
        this.ocupado = false;
        this.vehiculoActual = null;
        this.siguiente = null;
    }

    public String getIdEspacio() { return idEspacio; }
    public boolean isOcupado() { return ocupado; }
    public Vehiculo getVehiculoActual() { return vehiculoActual; }
    
    public void estacionarVehiculo(Vehiculo v) {
        this.vehiculoActual = v;
        this.ocupado = true;
    }

    // Método añadido para total compatibilidad con la asignación manual
    public void setVehiculoActual(Vehiculo v) {
        this.vehiculoActual = v;
        this.ocupado = (v != null);
    }

    public void liberarEspacio() {
        this.vehiculoActual = null;
        this.ocupado = false;
    }

    public EspacioParqueo getSiguiente() { return siguiente; }
    public void setSiguiente(EspacioParqueo siguiente) { this.siguiente = siguiente; }
}