package estructuras;

import modelo.Residente;

public class ListaDobleResidentes {
    private NodoResidente cabeza;
    private NodoResidente cola;
    private int tamanio;

    public ListaDobleResidentes() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void agregarResidente(Residente residente) {
        NodoResidente nuevo = new NodoResidente(residente);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }
        tamanio++;
    }

    public Residente buscarPorId(String id) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.getResidente().getId().equalsIgnoreCase(id)) {
                return actual.getResidente();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean eliminarPorId(String id) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.getResidente().getId().equalsIgnoreCase(id)) {
                if (actual == cabeza) {
                    cabeza = actual.getSiguiente();
                    if (cabeza != null) cabeza.setAnterior(null);
                    else cola = null;
                } else if (actual == cola) {
                    cola = actual.getAnterior();
                    if (cola != null) cola.setSiguiente(null);
                    else cabeza = null;
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public int getTamanio() { return tamanio; }
    public NodoResidente getCabeza() { return cabeza; }
}