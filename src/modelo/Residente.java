package modelo;

import estructuras.ListaSimpleVehiculos;

public class Residente {
    private String id;
    private String nombre;
    private String apartamento;
    private ListaSimpleVehiculos vehiculos;

    public Residente(String id, String nombre, String apartamento) {
        this.id = id;
        this.nombre = nombre;
        this.apartamento = apartamento;
        this.vehiculos = new ListaSimpleVehiculos();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApartamento() { return apartamento; }
    public void setApartamento(String apartamento) { this.apartamento = apartamento; }

    public ListaSimpleVehiculos getVehiculos() { return vehiculos; }
}