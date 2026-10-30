package estructuras;

import modelo.Residente;
import modelo.Vehiculo;

public class ListaDobleResidentes {
    private NodoResidente cabeza;
    private NodoResidente cola;

    public static class NodoResidente {
        private Residente residente;
        private NodoResidente siguiente;
        private NodoResidente anterior;

        public NodoResidente(Residente residente) {
            this.residente = residente;
            this.siguiente = null;
            this.anterior = null;
        }

        public Residente getResidente() { return residente; }
        public NodoResidente getSiguiente() { return siguiente; }
        public NodoResidente getAnterior() { return anterior; }
    }

    public ListaDobleResidentes() {
        this.cabeza = null;
        this.cola = null;
    }

    // Método requerido por ControladorPersistencia y VentanaGestionResidentes
    public void insertar(Residente residente) {
        NodoResidente nuevoNodo = new NodoResidente(residente);
        if (cabeza == null) {
            cabeza = cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }

    public void registrarResidente(String dpi, String nombre, boolean esSocio) {
        insertar(new Residente(dpi, nombre, esSocio));
    }

    public void registrarResidente(String dpi, String nombre, String numeroCasa, boolean esSocio) {
        insertar(new Residente(dpi, nombre, numeroCasa, esSocio));
    }

    // Búsqueda por ID o DPI (Alias requerido por las ventanas)
    public Residente buscar(String id) {
        return buscarResidente(id);
    }

    public Residente buscarResidente(String dpi) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.residente.getDpi().equalsIgnoreCase(dpi) || 
                actual.residente.getId().equalsIgnoreCase(dpi)) {
                return actual.residente;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean modificarResidente(String dpi, String nuevoNombre, boolean nuevoEsSocio) {
        Residente r = buscarResidente(dpi);
        if (r != null) {
            r.setNombre(nuevoNombre);
            r.setEsSocio(nuevoEsSocio);
            return true;
        }
        return false;
    }

    public boolean eliminarResidente(String dpi) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.residente.getDpi().equalsIgnoreCase(dpi) || 
                actual.residente.getId().equalsIgnoreCase(dpi)) {
                if (actual == cabeza) {
                    cabeza = cabeza.siguiente;
                    if (cabeza != null) cabeza.anterior = null;
                    else cola = null;
                } else if (actual == cola) {
                    cola = cola.anterior;
                    cola.siguiente = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                }
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean registrarVehiculoAResidente(String dpiResidente, Vehiculo vehiculo) {
        Residente r = buscarResidente(dpiResidente);
        if (r != null) {
            r.getVehiculosAsociados().agregarVehiculo(vehiculo);
            return true;
        }
        return false;
    }

    // Método clave para que las garitas identifiquen el acceso al área A-C de socios
    public boolean esVehiculoDeSocio(String placa) {
        NodoResidente actual = cabeza;
        while (actual != null) {
            if (actual.residente.isEsSocio()) {
                if (actual.residente.getVehiculosAsociados().contieneVehiculo(placa)) {
                    return true;
                }
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public NodoResidente getCabeza() {
        return cabeza;
    }
}