package estructuras; 

import modelo.Vehiculo; 

public class ListaSimpleVehiculos {
    private NodoVehiculo cabeza;
    private int tamanio;

    public ListaSimpleVehiculos() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Agregar vehículo controlando la restricción de máximo 3 por residente
    public boolean agregarVehiculo(Vehiculo vehiculo) {
        if (tamanio >= 3) {
            System.out.println("Error: El residente ya tiene el límite de 3 vehículos.");
            return false;
        }

        NodoVehiculo nuevo = new NodoVehiculo(vehiculo);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoVehiculo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamanio++;
        return true;
    }

    public int getTamanio() { return tamanio; }
    public NodoVehiculo getCabeza() { return cabeza; }
}