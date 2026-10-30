package estructuras;

import modelo.Vehiculo;

public class PilaVehiculos {
    private NodoPila cima;
    private int tamanio;

    public static class NodoPila {
        private String evento;
        private NodoPila siguiente;

        public NodoPila(String evento) {
            this.evento = evento;
            this.siguiente = null;
        }

        public String getevento() { return evento; }
        public NodoPila getSiguiente() { return siguiente; }
    }

    public PilaVehiculos() {
        this.cima = null;
        this.tamanio = 0;
    }

    // Método para apilar cadenas de texto directas
    public void push(String evento) {
        NodoPila nuevo = new NodoPila(evento);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    public void apilar(String evento) {
        push(evento);
    }

    // Sobrecarga para aceptar objetos Vehiculo desde los hilos
    public void push(Vehiculo vehiculo) {
        String descripcion = "Vehículo procesado - Placa: " + vehiculo.getPlaca() + 
                             " | Marca: " + vehiculo.getMarca() + " | Color: " + vehiculo.getColor();
        push(descripcion);
    }

    public void apilar(Vehiculo vehiculo) {
        push(vehiculo);
    }

    public String pop() {
        if (cima == null) return null;
        String evento = cima.evento;
        cima = cima.siguiente;
        tamanio--;
        return evento;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    // Método para obtener todo el historial en texto ordenado LIFO
    public String obtenerHistorialTexto() {
        StringBuilder sb = new StringBuilder();
        NodoPila actual = cima;
        int contador = 1;
        while (actual != null) {
            sb.append(contador++).append(". ").append(actual.evento).append("\n");
            actual = actual.siguiente;
        }
        return sb.toString();
    }

    public int getTamanio() {
        return tamanio;
    }
}