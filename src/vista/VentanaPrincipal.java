package vista;

import controlador.ControladorPersistencia;
import estructuras.ColaSalida;
import estructuras.ColaVehiculos;
import estructuras.ListaDobleResidentes;
import estructuras.ListaListasParqueo;
import hilos.HiloGarita;
import hilos.HiloGaritaSalida;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {
    private ColaVehiculos colaGarita;
    private ColaSalida colaSalida; // Cola única para las garitas de salida
    private ListaListasParqueo parqueo;
    private ListaDobleResidentes listaResidentes; // Estructura principal de residentes
    private HiloGarita[] hilosEntrada; // Las 3 garitas de entrada
    private HiloGaritaSalida[] hilosSalida; // Las 2 garitas de salida

    private JButton btnIniciarGaritas;
    private JButton btnDetenerGaritas;
    private JButton btnAgregarVehiculo;
    private JButton btnIniciarSalidas; // Botón para arrancar las garitas de salida
    private JButton btnSimularSalida; // Botón para simular que un vehículo se va
    private JButton btnGestionResidentes; // Botón para abrir la ventana de gestión de residentes
    private JTextArea areaLog;

    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Sistema de Gestión de Parqueo - USAC");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar estructuras de datos del proyecto
        colaGarita = new ColaVehiculos();
        colaSalida = new ColaSalida();
        parqueo = new ListaListasParqueo();
        listaResidentes = new ListaDobleResidentes();
        hilosEntrada = new HiloGarita[3]; // 3 garitas de entrada
        hilosSalida = new HiloGaritaSalida[2]; // 2 garitas de salida

        // CARGAR DATOS DESDE LOS ARCHIVOS AL ARRANCAR
        ControladorPersistencia.cargarDatos(listaResidentes);

        // GUARDAR DATOS AUTOMÁTICAMENTE AL CERRAR LA VENTANA
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ControladorPersistencia.guardarDatos(listaResidentes);
            }
        });

        // Inicializar componentes gráficos
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior para botones organizados
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        btnIniciarGaritas = new JButton("Iniciar Entradas (3)");
        btnDetenerGaritas = new JButton("Detener Entradas");
        btnAgregarVehiculo = new JButton("Simular Ingreso");
        btnIniciarSalidas = new JButton("Iniciar Salidas (2)");
        btnSimularSalida = new JButton("Simular Salida");
        btnGestionResidentes = new JButton("Gestión Residentes"); // Inicialización del botón

        panelBotones.add(btnIniciarGaritas);
        panelBotones.add(btnDetenerGaritas);
        panelBotones.add(btnAgregarVehiculo);
        panelBotones.add(btnIniciarSalidas);
        panelBotones.add(btnSimularSalida);
        panelBotones.add(btnGestionResidentes); // Agregado al panel superior

        add(panelBotones, BorderLayout.NORTH);

        // Área de texto central para logs y eventos
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaLog);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos de los botones
        btnIniciarGaritas.addActionListener(e -> iniciarGaritasEntrada());
        btnDetenerGaritas.addActionListener(e -> detenerGaritasEntrada());
        btnAgregarVehiculo.addActionListener(e -> simularIngreso());
        btnIniciarSalidas.addActionListener(e -> iniciarGaritasSalida());
        btnSimularSalida.addActionListener(e -> simularSalida());
        
        // Evento para abrir la ventana de gestión de residentes pasándole la lista actual
        btnGestionResidentes.addActionListener(e -> {
            VentanaGestionResidentes vGestion = new VentanaGestionResidentes(listaResidentes);
            vGestion.setVisible(true);
        });
    }

    private void iniciarGaritasEntrada() {
        boolean iniciado = false;
        for (int i = 0; i < hilosEntrada.length; i++) {
            if (hilosEntrada[i] == null || !hilosEntrada[i].isAlive()) {
                hilosEntrada[i] = new HiloGarita("Garita Entrada " + (i + 1), colaGarita, parqueo, 3000, areaLog);
                hilosEntrada[i].start();
                iniciado = true;
            }
        }
        
        if (iniciado) {
            areaLog.append(">>> Las 3 garitas de entrada han sido iniciadas en paralelo.\n");
        } else {
            areaLog.append(">>> Las garitas de entrada ya están en ejecución.\n");
        }
    }

    private void detenerGaritasEntrada() {
        for (int i = 0; i < hilosEntrada.length; i++) {
            if (hilosEntrada[i] != null && hilosEntrada[i].isAlive()) {
                hilosEntrada[i].detenerHilo();
            }
        }
        areaLog.append(">>> Todas las garitas de entrada han sido detenidas.\n");
    }

    private void iniciarGaritasSalida() {
        boolean iniciado = false;
        for (int i = 0; i < hilosSalida.length; i++) {
            if (hilosSalida[i] == null || !hilosSalida[i].isAlive()) {
                hilosSalida[i] = new HiloGaritaSalida("Garita Salida " + (i + 1), colaSalida, parqueo, 3000, areaLog);
                hilosSalida[i].start();
                iniciado = true;
            }
        }

        if (iniciado) {
            areaLog.append(">>> Las 2 garitas de salida han sido iniciadas en paralelo.\n");
        } else {
            areaLog.append(">>> Las garitas de salida ya están en ejecución.\n");
        }
    }

    private void simularIngreso() {
        modelo.Vehiculo vPrueba = new modelo.Vehiculo("P-" + (int)(Math.random() * 900 + 100), "Toyota", "Corolla", "Blanco", "Automóvil");
        
        synchronized (colaGarita) {
            colaGarita.encolar(vPrueba);
            colaGarita.notifyAll(); 
        }
        
        areaLog.append("Vehículo agregado a la cola de entrada: Placa " + vPrueba.getPlaca() + "\n");
    }

    private void simularSalida() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la placa del vehículo que desea retirar:");
        if (placa != null && !placa.trim().isEmpty()) {
            modelo.Vehiculo vSalida = new modelo.Vehiculo(placa.trim().toUpperCase(), "Genérico", "Genérico", "N/A", "Automóvil");
            
            synchronized (colaSalida) {
                colaSalida.encolar(vSalida);
                colaSalida.notifyAll(); 
            }
            
            areaLog.append("Vehículo con placa " + vSalida.getPlaca() + " agregado a la cola de salida.\n");
        }
    }
}