package estructuras;

import modelo.Residente;

public class ListaDobleResidentes {
    private NodoResidente cabeza;
    private NodoResidente cola;

    public ListaDobleResidentes() {
        this.cabeza = null;
        this.cola = null;
    }

    public void insertar(Residente residente) {
        NodoResidente nuevo = new NodoResidente(residente);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }
    }

    public Residente buscar(String id) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.getResidente().getId().equalsIgnoreCase(id)) {
                return actual.getResidente();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean eliminar(String id) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.getResidente().getId().equalsIgnoreCase(id)) {
                if (actual == cabeza) {
                    cabeza = cabeza.getSiguiente();
                    if (cabeza != null) cabeza.setAnterior(null);
                    else cola = null;
                } else if (actual == cola) {
                    cola = cola.getAnterior();
                    if (cola != null) cola.setSiguiente(null);
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public NodoResidente getCabeza() { return cabeza; }
}