package estructuras;

import modelo.Vehiculo;

public class ListaSimpleVehiculos {
    private NodoVehiculo cabeza;
    private int tamanio = 0;

    public static class NodoVehiculo {
        private Vehiculo vehiculo;
        private NodoVehiculo siguiente;

        public NodoVehiculo(Vehiculo vehiculo) {
            this.vehiculo = vehiculo;
            this.siguiente = null;
        }

        public Vehiculo getVehiculo() { return vehiculo; }
        public NodoVehiculo getSiguiente() { return siguiente; }
        public void setSiguiente(NodoVehiculo siguiente) { this.siguiente = siguiente; }
    }

    public ListaSimpleVehiculos() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Agrega un vehículo validando el límite de 3 por residente (requerido por la ventana)
    public boolean agregarVehiculo(Vehiculo vehiculo) {
        if (tamanio >= 3) {
            return false; // Límite alcanzado
        }
        
        NodoVehiculo nuevo = new NodoVehiculo(vehiculo);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoVehiculo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
        return true;
    }

    // Valida si una placa específica está registrada en esta lista (clave para los socios)
    public boolean contieneVehiculo(String placa) {
        NodoVehiculo actual = cabeza;
        while (actual != null) {
            if (actual.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // Devuelve el tamaño actual (requerido por VentanaGestionResidentes para mostrar "X/3")
    public int getTamanio() {
        return tamanio;
    }

    public NodoVehiculo getCabeza() {
        return cabeza;
    }
}