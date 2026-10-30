package modelo;

import estructuras.ListaSimpleVehiculos;

public class Residente {
    private String dpi;
    private String nombre;
    private String numeroCasa;
    private boolean esSocio; 
    private ListaSimpleVehiculos vehiculosAsociados;

    // Constructor de 4 parámetros (el que reclaman ControladorPersistencia y VentanaGestionResidentes)
    public Residente(String dpi, String nombre, String numeroCasa, boolean esSocio) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.numeroCasa = numeroCasa;
        this.esSocio = esSocio;
        this.vehiculosAsociados = new ListaSimpleVehiculos();
    }

    // Constructor alternativo por si se usa de 3
    public Residente(String dpi, String nombre, boolean esSocio) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.numeroCasa = "N/A";
        this.esSocio = esSocio;
        this.vehiculosAsociados = new ListaSimpleVehiculos();
    }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    
    // Método de compatibilidad requerido por VentanaGestionResidentes
    public String getId() { return dpi; }
    public void setId(String id) { this.dpi = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(String numeroCasa) { this.numeroCasa = numeroCasa; }

    public boolean isEsSocio() { return esSocio; }
    public void setEsSocio(boolean esSocio) { this.esSocio = esSocio; }

    public ListaSimpleVehiculos getVehiculosAsociados() { return vehiculosAsociados; }
    public void setVehiculosAsociados(ListaSimpleVehiculos vehiculosAsociados) { this.vehiculosAsociados = vehiculosAsociados; }
}