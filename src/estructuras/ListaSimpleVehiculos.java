package estructuras;

import modelo.Vehiculo;

public class ListaSimpleVehiculos {
    private NodoVehiculo cabeza;
    private int tamanio;

    public ListaSimpleVehiculos() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public boolean agregarVehiculo(Vehiculo vehiculo) {
        if (tamanio >= 3) {
            return false; // Límite máximo de 3 vehículos por residente
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

    public boolean eliminarVehiculo(String placa) {
        if (cabeza == null) return false;

        if (cabeza.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }

        NodoVehiculo actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public NodoVehiculo getCabeza() { return cabeza; }
    public int getTamanio() { return tamanio; }
}