package hilos;

import estructuras.ColaSalida;
import estructuras.ListaListasParqueo;
import estructuras.PilaVehiculos;
import modelo.Vehiculo;
import javax.swing.JTextArea;

public class HiloGaritaSalida extends Thread {
    private String nombreGarita;
    private ColaSalida colaSalida;
    private ListaListasParqueo parqueo;
    private PilaVehiculos pilaEventos; // Pila manual de eventos (LIFO)
    private int tiempoEspera; // Tiempo que tarda en procesar la salida (ms)
    private JTextArea areaLog;
    private boolean activo;

    public HiloGaritaSalida(String nombreGarita, ColaSalida colaSalida, ListaListasParqueo parqueo, 
                            PilaVehiculos pilaEventos, int tiempoEspera, JTextArea areaLog) {
        this.nombreGarita = nombreGarita;
        this.colaSalida = colaSalida;
        this.parqueo = parqueo;
        this.pilaEventos = pilaEventos;
        this.tiempoEspera = tiempoEspera;
        this.areaLog = areaLog;
        this.activo = true;
    }

    @Override
    public void run() {
        while (activo) {
            Vehiculo vehiculoSalida = null;

            // Sincronizar el acceso a la cola única de salida
            synchronized (colaSalida) {
                while (colaSalida.estaVacia() && activo) {
                    try {
                        colaSalida.wait(); // Espera a que llegue un vehículo a la cola de salida
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (!activo) break;

                vehiculoSalida = colaSalida.desencolar();
            }

            if (vehiculoSalida != null) {
                try {
                    // Simulamos el tiempo de atención de la garita de salida
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                // Liberar el espacio en el parqueo utilizando la placa
                boolean liberado = parqueo.liberarEspacioVehiculo(vehiculoSalida.getPlaca());

                // Registrar las acciones obligatorias en la Pila LIFO exigida por la rúbrica
                if (pilaEventos != null) {
                    pilaEventos.apilar(new Vehiculo(vehiculoSalida.getPlaca(), "Vehículo salió", "Procesado", "N/A", "Acción"));
                    if (liberado) {
                        pilaEventos.apilar(new Vehiculo(vehiculoSalida.getPlaca(), "Espacio liberado", "Éxito", "N/A", "Acción"));
                    }
                }

                // Actualizar la interfaz (Log) de manera segura
                final Vehiculo v = vehiculoSalida;
                final boolean exito = liberado;
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (exito) {
                        areaLog.append("[" + nombreGarita + "] Vehículo con placa " + v.getPlaca() + " salió -> ¡Espacio liberado con éxito!\n");
                    } else {
                        areaLog.append("[" + nombreGarita + "] Vehículo con placa " + v.getPlaca() + " salió, pero no se encontró en el parqueo.\n");
                    }
                });
            }
        }
    }

    public void detenerHilo() {
        this.activo = false;
        synchronized (colaSalida) {
            colaSalida.notifyAll(); // Despierta al hilo si está esperando
        }
    }
}