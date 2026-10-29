package modelo;

import estructuras.ListaSimpleVehiculos;

public class Residente {
    private String id; // DPI o Código
    private String nombre;
    private String numeroCasa;
    private boolean esSocio;
    private ListaSimpleVehiculos vehiculosAsociados;

    public Residente(String id, String nombre, String numeroCasa, boolean esSocio) {
        this.id = id;
        this.nombre = nombre;
        this.numeroCasa = numeroCasa;
        this.esSocio = esSocio;
        this.vehiculosAsociados = new ListaSimpleVehiculos();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(String numeroCasa) { this.numeroCasa = numeroCasa; }

    public boolean isEsSocio() { return esSocio; }
    public void setEsSocio(boolean esSocio) { this.esSocio = esSocio; }

    public ListaSimpleVehiculos getVehiculosAsociados() { return vehiculosAsociados; }
}