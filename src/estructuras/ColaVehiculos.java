package estructuras;

import modelo.Vehiculo;

public class ColaVehiculos {
    private NodoVehiculo frente;
    private NodoVehiculo fin;
    private int tamanio;

    public ColaVehiculos() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    // Insertar un vehículo al final de la cola (Enqueue)
    public void encolar(Vehiculo vehiculo) {
        NodoVehiculo nuevo = new NodoVehiculo(vehiculo);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }
        tamanio++;
    }

    // Extraer el vehículo que está al frente de la cola (Dequeue)
    public Vehiculo desencolar() {
        if (estaVacia()) {
            return null;
        }
        Vehiculo vehiculo = frente.getVehiculo();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null; // Si la cola queda vacía, limpiamos el fin también
        }
        tamanio--;
        return vehiculo;
    }

    // Ver el vehículo al frente sin sacarlo
    public Vehiculo verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getVehiculo();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}