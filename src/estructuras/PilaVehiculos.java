package estructuras;

import modelo.Vehiculo;

public class PilaVehiculos {
    private NodoVehiculo cima;
    private int tamanio;

    public PilaVehiculos() {
        this.cima = null;
        this.tamanio = 0;
    }

    // Insertar un vehículo en la cima de la pila (Push)
    public void apilar(Vehiculo vehiculo) {
        NodoVehiculo nuevo = new NodoVehiculo(vehiculo);
        nuevo.setSiguiente(cima);
        cima = nuevo;
        tamanio++;
    }

    // Extraer el vehículo de la cima (Pop)
    public Vehiculo desapilar() {
        if (estaVacia()) {
            return null;
        }
        Vehiculo vehiculo = cima.getVehiculo();
        cima = cima.getSiguiente();
        tamanio--;
        return vehiculo;
    }

    // Ver el vehículo en la cima sin sacarlo (Peek)
    public Vehiculo verCima() {
        if (estaVacia()) {
            return null;
        }
        return cima.getVehiculo();
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}