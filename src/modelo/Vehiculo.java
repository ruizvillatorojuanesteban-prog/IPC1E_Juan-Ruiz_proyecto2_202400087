package modelo;

public class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private String tipo; // Automóvil, Motocicleta, etc.
    private String estado; // Estacionado, En cola, Fuera

    public Vehiculo(String placa, String marca, String modelo, String color, String tipo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipo = tipo;
        this.estado = "Fuera";
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}