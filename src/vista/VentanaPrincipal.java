package vista;

import estructuras.*;
import hilos.HiloGarita;
import hilos.HiloGaritaSalida;
import modelo.Vehiculo;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private ListaDobleResidentes listaResidentes;
    private ListaListasParqueo parqueo;
    private ColaVehiculos colaEntrada;
    private ColaSalida colaSalida;
    private PilaVehiculos pilaEventos;

    // Hilos de las garitas
    private HiloGarita garita1, garita2, garita3;
    private HiloGaritaSalida salida1, salida2;
    private boolean hilosActivos = false;

    private JTextArea areaLogEntrada;
    private JTextArea areaLogSalida;
    private JTextArea areaLogEventos;

    // Paneles para los grids dinámicos basados en la estructura real
    private JPanel gridAC;
    private JPanel gridEI;

    public VentanaPrincipal(ListaDobleResidentes listaResidentes) {
        this.listaResidentes = listaResidentes;
        
        this.parqueo = new ListaListasParqueo();
        this.colaEntrada = new ColaVehiculos();
        this.colaSalida = new ColaSalida();
        this.pilaEventos = new PilaVehiculos();

        setTitle("Sistema de Gestión de Parqueo - USAC");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        JTabbedPane panelTabs = new JTabbedPane();

        panelTabs.addTab("Panel de Parqueo", crearPanelParqueoVisualOrganizado());
        panelTabs.addTab("Panel de Residentes", new VentanaGestionResidentes(listaResidentes).getContentPane());
        panelTabs.addTab("Panel de Vehículos / Ingreso", crearPanelVehiculos());
        panelTabs.addTab("Panel de Entrada (3 Garitas)", crearPanelEntrada());
        panelTabs.addTab("Panel de Salida (2 Garitas)", crearPanelSalida());
        panelTabs.addTab("Panel de Eventos (Pila)", crearPanelEventos());

        panelTabs.addChangeListener(e -> {
            if (panelTabs.getSelectedIndex() == 0) {
                actualizarVistaParqueoVisual();
            } else if (panelTabs.getSelectedIndex() == 5) {
                actualizarVistaEventos();
            }
        });

        add(panelTabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelParqueoVisualOrganizado() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelNorte = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Distribución Oficial: Lista Doblemente Enlazada de Listas Simples (150 Espacios)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JLabel lblLibre = new JLabel("■ Libre");
        lblLibre.setForeground(new Color(0, 150, 0));
        lblLibre.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel lblOcupado = new JLabel("■ Ocupado");
        lblOcupado.setForeground(Color.RED);
        lblOcupado.setFont(new Font("Arial", Font.BOLD, 12));

        JButton btnRefrescar = new JButton("Actualizar Estado");
        panelLeyenda.add(lblLibre);
        panelLeyenda.add(lblOcupado);
        panelLeyenda.add(btnRefrescar);
        panelNorte.add(panelLeyenda, BorderLayout.SOUTH);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        JPanel panelSecciones = new JPanel(new GridLayout(2, 1, 10, 10));

        // Área A-C (Socios)
        JPanel panelAC = new JPanel(new BorderLayout());
        panelAC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), "Área A–C (Exclusiva para Socios)"));
        gridAC = new JPanel(new GridLayout(0, 25, 2, 2));

        // Área E-I (General)
        JPanel panelEI = new JPanel(new BorderLayout());
        panelEI.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Área E–I (Área General / Visitantes)"));
        gridEI = new JPanel(new GridLayout(0, 15, 2, 2));

        construirBotonesParqueo();

        panelAC.add(new JScrollPane(gridAC), BorderLayout.CENTER);
        panelEI.add(new JScrollPane(gridEI), BorderLayout.CENTER);

        panelSecciones.add(panelAC);
        panelSecciones.add(panelEI);
        panelPrincipal.add(panelSecciones, BorderLayout.CENTER);

        btnRefrescar.addActionListener(e -> actualizarVistaParqueoVisual());

        return panelPrincipal;
    }

    private void construirBotonesParqueo() {
        gridAC.removeAll();
        gridEI.removeAll();

        if (parqueo != null && parqueo.getInicioFilas() != null) {
            FilaParqueo filaActual = parqueo.getInicioFilas();
            while (filaActual != null) {
                EspacioParqueo espacioActual = filaActual.getPrimerEspacio();
                boolean esSocioFila = (filaActual.getLetraFila() == 'A' || filaActual.getLetraFila() == 'B' || filaActual.getLetraFila() == 'C');

                while (espacioActual != null) {
                    final String idEspacio = espacioActual.getIdEspacio();
                    JButton btnEspacio = new JButton(idEspacio);
                    
                    if (espacioActual.isOcupado()) {
                        btnEspacio.setBackground(Color.RED.brighter());
                        btnEspacio.setToolTipText("Ocupado por: " + (espacioActual.getVehiculoActual() != null ? espacioActual.getVehiculoActual().getPlaca() : "Desconocido"));
                    } else {
                        btnEspacio.setBackground(Color.GREEN.brighter());
                        btnEspacio.setToolTipText("Libre - " + idEspacio);
                    }
                    
                    btnEspacio.setFont(new Font("Arial", Font.BOLD, 9));
                    btnEspacio.addActionListener(e -> mostrarInfoNodoParqueo(idEspacio));

                    if (esSocioFila) {
                        gridAC.add(btnEspacio);
                    } else {
                        gridEI.add(btnEspacio);
                    }
                    espacioActual = espacioActual.getSiguiente();
                }
                filaActual = filaActual.getSiguienteFila();
            }
        }
        gridAC.revalidate();
        gridAC.repaint();
        gridEI.revalidate();
        gridEI.repaint();
    }

    private void mostrarInfoNodoParqueo(String idEspacio) {
        FilaParqueo fila = parqueo.getInicioFilas();
        EspacioParqueo espacioEncontrado = null;

        while (fila != null) {
            EspacioParqueo esp = fila.getPrimerEspacio();
            while (esp != null) {
                if (esp.getIdEspacio().equalsIgnoreCase(idEspacio)) {
                    espacioEncontrado = esp;
                    break;
                }
                esp = esp.getSiguiente();
            }
            if (espacioEncontrado != null) break;
            fila = fila.getSiguienteFila();
        }

        String estado = (espacioEncontrado != null && espacioEncontrado.isOcupado()) ? "Ocupado" : "Libre";
        String infoVehiculo = (espacioEncontrado != null && espacioEncontrado.getVehiculoActual() != null) ? 
                "\nVehículo: " + espacioEncontrado.getVehiculoActual().getPlaca() : "";

        JOptionPane.showMessageDialog(this, "Nodo Físico: " + idEspacio + "\nEstado: " + estado + infoVehiculo +
                "\nEstructura: Lista doblemente enlazada de listas simples.", "Información del Espacio", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarVistaParqueoVisual() {
        construirBotonesParqueo();
    }

    private JPanel crearPanelVehiculos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Asignación Manual de Vehículo a Espacio de Parqueo"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        
        JTextField txtPlacaManual = new JTextField(12);
        JTextField txtEspacioManual = new JTextField(6);
        JButton btnAsignarEspacio = new JButton("Estacionar en el Espacio Elegido");

        panel.add(new JLabel("Placa del Vehículo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPlacaManual, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID de Espacio (Ej. A1, B10, E5):"), gbc);
        gbc.gridx = 1;
        panel.add(txtEspacioManual, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAsignarEspacio, gbc);

        btnAsignarEspacio.addActionListener(e -> {
            String placa = txtPlacaManual.getText().trim();
            String idEspacioBuscado = txtEspacioManual.getText().trim().toUpperCase();
            
            if (placa.isEmpty() || idEspacioBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la placa y el ID del espacio (Ej. A1, E5).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FilaParqueo fila = parqueo.getInicioFilas();
            EspacioParqueo espacioEncontrado = null;

            while (fila != null) {
                EspacioParqueo esp = fila.getPrimerEspacio();
                while (esp != null) {
                    if (esp.getIdEspacio().equalsIgnoreCase(idEspacioBuscado)) {
                        espacioEncontrado = esp;
                        break;
                    }
                    esp = esp.getSiguiente();
                }
                if (espacioEncontrado != null) break;
                fila = fila.getSiguienteFila();
            }

            if (espacioEncontrado == null) {
                JOptionPane.showMessageDialog(this, "No existe el espacio con identificador: " + idEspacioBuscado, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (espacioEncontrado.isOcupado()) {
                JOptionPane.showMessageDialog(this, "El espacio " + idEspacioBuscado + " ya se encuentra ocupado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // =========================================================================
            // VALIDACIÓN GENERAL Y DINÁMICA DE SOCIOS PARA EL ÁREA A-C
            // =========================================================================
            boolean esAreaSocios = idEspacioBuscado.startsWith("A") || idEspacioBuscado.startsWith("B") || idEspacioBuscado.startsWith("C");
            if (esAreaSocios) {
                if (listaResidentes != null) {
                    boolean esSocioAutorizado = listaResidentes.esVehiculoDeSocio(placa);
                    if (!esSocioAutorizado) {
                        JOptionPane.showMessageDialog(this, 
                            "Acceso Denegado:\nEl espacio " + idEspacioBuscado + " pertenece al Área A–C (Exclusiva para Socios).\nEl vehículo con placa [" + placa + "] no pertenece a un residente socio registrado.", 
                            "Restricción de Área", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            // =========================================================================

            Vehiculo v = new Vehiculo(placa, "Genérica", "Modelo", "Color", "Automóvil");
            espacioEncontrado.setVehiculoActual(v);
            
            pilaEventos.push("Vehículo " + placa + " asignado manualmente al espacio " + idEspacioBuscado);
            
            JOptionPane.showMessageDialog(this, "¡Vehículo " + placa + " estacionado con éxito en " + idEspacioBuscado + "!");
            txtPlacaManual.setText("");
            txtEspacioManual.setText("");
            actualizarVistaParqueoVisual();
        });

        return panel;
    }

    private JPanel crearPanelEntrada() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Control de Entrada - 3 Garitas Activas"));
        
        areaLogEntrada = new JTextArea("Estado de las 3 garitas de entrada...\n");
        areaLogEntrada.setEditable(false);
        panel.add(new JScrollPane(areaLogEntrada), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel();
        JButton btnIniciarEntradas = new JButton("Iniciar Entradas (3 Garitas)");
        JButton btnDetenerEntradas = new JButton("Detener Entradas");
        
        panelBotones.add(btnIniciarEntradas);
        panelBotones.add(btnDetenerEntradas);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnIniciarEntradas.addActionListener(e -> {
            if (!hilosActivos) {
                garita1 = new HiloGarita("Garita 1", colaEntrada, parqueo, listaResidentes, pilaEventos, 2000, areaLogEntrada);
                garita2 = new HiloGarita("Garita 2", colaEntrada, parqueo, listaResidentes, pilaEventos, 2500, areaLogEntrada);
                garita3 = new HiloGarita("Garita 3", colaEntrada, parqueo, listaResidentes, pilaEventos, 3000, areaLogEntrada);
                
                garita1.start();
                garita2.start();
                garita3.start();
                hilosActivos = true;
                areaLogEntrada.append("--- Hilos de garitas de entrada iniciados ---\n");
                pilaEventos.push("Se iniciaron los hilos de las garitas de entrada.");
            } else {
                JOptionPane.showMessageDialog(this, "Los hilos ya se encuentran activos.");
            }
        });

        btnDetenerEntradas.addActionListener(e -> {
            if (hilosActivos) {
                if (garita1 != null) garita1.detenerHilo();
                if (garita2 != null) garita2.detenerHilo();
                if (garita3 != null) garita3.detenerHilo();
                hilosActivos = false;
                areaLogEntrada.append("--- Hilos de garitas de entrada detenidos ---\n");
                pilaEventos.push("Se detuvieron los hilos de las garitas de entrada.");
            }
        });

        return panel;
    }

    private JPanel crearPanelSalida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Control de Salida - 2 Garitas Activas"));
        
        areaLogSalida = new JTextArea("Estado de las garitas de salida...\n");
        areaLogSalida.setEditable(false);
        panel.add(new JScrollPane(areaLogSalida), BorderLayout.CENTER);

        JPanel panelControles = new JPanel(new FlowLayout());
        JTextField txtPlacaSalida = new JTextField(10);
        JButton btnLiberarPorPlaca = new JButton("Liberar por Placa");
        JButton btnIniciarSalidas = new JButton("Iniciar Hilos de Salida (2)");

        panelControles.add(new JLabel("Placa:"));
        panelControles.add(txtPlacaSalida);
        panelControles.add(btnLiberarPorPlaca);
        panelControles.add(btnIniciarSalidas);
        panel.add(panelControles, BorderLayout.SOUTH);

        btnIniciarSalidas.addActionListener(e -> {
            salida1 = new HiloGaritaSalida("Garita Salida 1", colaSalida, parqueo, pilaEventos, 2000, areaLogSalida);
            salida2 = new HiloGaritaSalida("Garita Salida 2", colaSalida, parqueo, pilaEventos, 2500, areaLogSalida);
            salida1.start();
            salida2.start();
            areaLogSalida.append("--- Hilos de salida iniciados ---\n");
            pilaEventos.push("Se iniciaron los hilos de las garitas de salida.");
        });

        btnLiberarPorPlaca.addActionListener(e -> {
            String placa = txtPlacaSalida.getText().trim();
            if (!placa.isEmpty()) {
                boolean liberado = parqueo.liberarEspacioVehiculo(placa);
                if (liberado) {
                    areaLogSalida.append("Vehículo " + placa + " retirado y espacio liberado con éxito.\n");
                    pilaEventos.push("Vehículo " + placa + " retirado del parqueo.");
                    txtPlacaSalida.setText("");
                    actualizarVistaParqueoVisual();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún espacio ocupado con la placa: " + placa, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar la placa del vehículo a retirar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Historial de Eventos (Pila LIFO)"));
        
        areaLogEventos = new JTextArea("=== ULTIMOS EVENTOS REGISTRADOS (LIFO) ===\n");
        areaLogEventos.setEditable(false);
        panel.add(new JScrollPane(areaLogEventos), BorderLayout.CENTER);
        
        JButton btnActualizarPila = new JButton("Actualizar Vista de Eventos");
        panel.add(btnActualizarPila, BorderLayout.SOUTH);

        btnActualizarPila.addActionListener(e -> actualizarVistaEventos());

        return panel;
    }

    private void actualizarVistaEventos() {
        if (areaLogEventos != null) {
            areaLogEventos.setText("=== ULTIMOS EVENTOS REGISTRADOS (LIFO) ===\n");
            String historial = pilaEventos.obtenerHistorialTexto();
            if (historial != null && !historial.isEmpty()) {
                areaLogEventos.append(historial);
            } else {
                areaLogEventos.append("No hay eventos registrados todavía o la pila está vacía.\n");
            }
        }
    }
}