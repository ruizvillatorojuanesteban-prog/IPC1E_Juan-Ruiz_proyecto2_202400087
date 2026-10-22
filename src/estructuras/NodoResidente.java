package estructuras;

import modelo.Residente;

public class NodoResidente {
    private Residente residente;
    private NodoResidente siguiente;
    private NodoResidente anterior;

    public NodoResidente(Residente residente) {
        this.residente = residente;
        this.siguiente = null;
        this.anterior = null;
    }

    public Residente getResidente() { return residente; }
    public void setResidente(Residente residente) { this.residente = residente; }

    public NodoResidente getSiguiente() { return siguiente; }
    public void setSiguiente(NodoResidente siguiente) { this.siguiente = siguiente; }

    public NodoResidente getAnterior() { return anterior; }
    public void setAnterior(NodoResidente anterior) { this.anterior = anterior; }
}