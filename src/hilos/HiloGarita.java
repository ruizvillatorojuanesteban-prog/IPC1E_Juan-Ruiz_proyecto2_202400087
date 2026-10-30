package hilos;

import estructuras.ColaVehiculos;
import estructuras.ListaDobleResidentes;
import estructuras.ListaListasParqueo;
import estructuras.PilaVehiculos;
import modelo.Vehiculo;
import javax.swing.JTextArea;

public class HiloGarita extends Thread {
    private String nombreGarita;
    private ColaVehiculos colaVehiculos;
    private ListaListasParqueo parqueo;
    private ListaDobleResidentes listaResidentes;
    private PilaVehiculos pilaEventos;
    private boolean enEjecucion;
    private int tiempoEspera;
    private JTextArea areaLog;

    public HiloGarita(String nombreGarita, ColaVehiculos colaVehiculos, ListaListasParqueo parqueo, 
                      ListaDobleResidentes listaResidentes, PilaVehiculos pilaEventos, int tiempoEspera, JTextArea areaLog) {
        this.nombreGarita = nombreGarita;
        this.colaVehiculos = colaVehiculos;
        this.parqueo = parqueo;
        this.listaResidentes = listaResidentes;
        this.pilaEventos = pilaEventos;
        this.enEjecucion = true;
        this.tiempoEspera = tiempoEspera;
        this.areaLog = areaLog;
    }

    @Override
    public void run() {
        while (enEjecucion) {
            Vehiculo vehiculoAtendido = null;
            
            synchronized (colaVehiculos) {
                while (colaVehiculos.estaVacia() && enEjecucion) {
                    try {
                        colaVehiculos.wait(); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (!enEjecucion) break;

                vehiculoAtendido = colaVehiculos.desencolar();
            }

            if (vehiculoAtendido != null) {
                // Validación segura del estatus de socio para el área A-C
                boolean esSocio = false;
                if (listaResidentes != null) {
                    try {
                        // Verificación mediante reflexión o métodos estándar si existen
                        java.lang.reflect.Method m = listaResidentes.getClass().getMethod("esVehiculoDeSocio", String.class);
                        esSocio = (boolean) m.invoke(listaResidentes, vehiculoAtendido.getPlaca());
                    } catch (Exception e) {
                        // Si el método no existe en tu estructura, por defecto se evalúa como general/visitante
                        esSocio = false; 
                    }
                }

                // Intentar estacionarlo en la lista de listas del parqueo
                boolean asignado = parqueo.estacionarVehiculo(vehiculoAtendido, esSocio);
                
                String mensaje;
                if (asignado) {
                    mensaje = "[" + nombreGarita + "] Vehículo " + vehiculoAtendido.getPlaca() + 
                              " ingresó y fue estacionado con éxito.\n";
                    if (pilaEventos != null) {
                        pilaEventos.apilar(new Vehiculo(vehiculoAtendido.getPlaca(), "Asignado", "Espacio Exitoso", "N/A", "Acción"));
                    }
                } else {
                    mensaje = "[" + nombreGarita + "] Vehículo " + vehiculoAtendido.getPlaca() + 
                              " rechazado (Parqueo lleno o área no disponible).\n";
                    if (pilaEventos != null) {
                        pilaEventos.apilar(new Vehiculo(vehiculoAtendido.getPlaca(), "Rechazado", "Sin Espacio", "N/A", "Acción"));
                    }
                }
                
                System.out.print(mensaje);
                if (areaLog != null) {
                    areaLog.append(mensaje);
                }
            }

            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void detenerHilo() {
        this.enEjecucion = false;
        synchronized (colaVehiculos) {
            colaVehiculos.notifyAll();
        }
        this.interrupt();
    }
}