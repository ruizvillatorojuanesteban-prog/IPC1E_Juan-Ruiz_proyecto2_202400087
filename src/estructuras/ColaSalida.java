package estructuras;

import modelo.Vehiculo;

public class ColaSalida {
    private NodoVehiculo frente;
    private NodoVehiculo fin;
    private int tamanio;

    public ColaSalida() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public synchronized void encolar(Vehiculo vehiculo) {
        NodoVehiculo nuevo = new NodoVehiculo(vehiculo);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        tamanio++;
    }

    public synchronized Vehiculo desencolar() {
        if (estaVacia()) {
            return null;
        }
        Vehiculo vehiculo = frente.getVehiculo();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return vehiculo;
    }

    public synchronized boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}
