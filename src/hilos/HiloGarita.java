package hilos;

import estructuras.ColaVehiculos;
import estructuras.ListaListasParqueo;
import modelo.Vehiculo;
import javax.swing.JTextArea;

public class HiloGarita extends Thread {
    private String nombreGarita; // Ej: "Entrada 1", "Entrada 2", etc.
    private ColaVehiculos colaVehiculos;
    private ListaListasParqueo parqueo; // Para asignar espacio al entrar
    private boolean enEjecucion;
    private int tiempoEspera;
    private JTextArea areaLog;

    public HiloGarita(String nombreGarita, ColaVehiculos colaVehiculos, ListaListasParqueo parqueo, int tiempoEspera, JTextArea areaLog) {
        this.nombreGarita = nombreGarita;
        this.colaVehiculos = colaVehiculos;
        this.parqueo = parqueo;
        this.enEjecucion = true;
        this.tiempoEspera = tiempoEspera;
        this.areaLog = areaLog;
    }

    @Override
    public void run() {
        while (enEjecucion) {
            Vehiculo vehiculoAtendido = null;
            
            // Sincronización estricta sobre la cola compartida
            synchronized (colaVehiculos) {
                while (colaVehiculos.estaVacia() && enEjecucion) {
                    try {
                        colaVehiculos.wait(); // Espera activa usando wait() si la cola está vacía
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (!enEjecucion) break;

                vehiculoAtendido = colaVehiculos.desencolar();
            }

            if (vehiculoAtendido != null) {
                // Intentar estacionarlo en la lista de listas del parqueo
                boolean asignado = parqueo.estacionarVehiculo(vehiculoAtendido, true);
                
                String mensaje = "[" + nombreGarita + "] Atendió vehículo placa: " + vehiculoAtendido.getPlaca() + 
                                 (asignado ? " -> ¡Estacionado con éxito!\n" : " -> ¡Parqueo lleno!\n");
                
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
            colaVehiculos.notifyAll(); // Despierta al hilo si estaba en wait()
        }
        this.interrupt();
    }
}