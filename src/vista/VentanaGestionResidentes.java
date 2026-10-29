package vista;

import estructuras.ListaDobleResidentes;
import estructuras.NodoResidente;
import modelo.Residente;
import modelo.Vehiculo;
import javax.swing.*;
import java.awt.*;

public class VentanaGestionResidentes extends JFrame {
    private ListaDobleResidentes listaResidentes;
    private JTextField txtId, txtNombre, txtCasa, txtPlaca, txtMarca, txtModelo, txtColor, txtTipo;
    private JCheckBox chkSocio;
    private JTextArea areaInfo;

    public VentanaGestionResidentes(ListaDobleResidentes listaResidentes) {
        this.listaResidentes = listaResidentes;

        setTitle("Gestión de Residentes y Vehículos - USAC");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        actualizarVistaResidentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(9, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Residente y Vehículo"));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtCasa = new JTextField();
        chkSocio = new JCheckBox("¿Es Socio?");
        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtColor = new JTextField();
        txtTipo = new JTextField();

        panelFormulario.add(new JLabel("ID / DPI:"));
        panelFormulario.add(txtId);
        panelFormulario.add(new JLabel("Nombre Completo:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("No. de Casa:"));
        panelFormulario.add(txtCasa);
        panelFormulario.add(new JLabel("Tipo de Usuario:"));
        panelFormulario.add(chkSocio);
        panelFormulario.add(new JLabel("Placa Vehículo:"));
        panelFormulario.add(txtPlaca);
        panelFormulario.add(new JLabel("Marca:"));
        panelFormulario.add(txtMarca);
        panelFormulario.add(new JLabel("Modelo:"));
        panelFormulario.add(txtModelo);
        panelFormulario.add(new JLabel("Color:"));
        panelFormulario.add(txtColor);
        panelFormulario.add(new JLabel("Tipo (Automóvil/Moto):"));
        panelFormulario.add(txtTipo);

        add(panelFormulario, BorderLayout.WEST);

        areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaInfo);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Residentes Registrados"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnRegistrar = new JButton("Registrar / Agregar Vehículo");
        JButton btnCerrar = new JButton("Cerrar Ventana");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarORegistrarVehiculo());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void registrarORegistrarVehiculo() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String casa = txtCasa.getText().trim();
            boolean esSocio = chkSocio.isSelected();

            String placa = txtPlaca.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String color = txtColor.getText().trim();
            String tipo = txtTipo.getText().trim();

            if (id.isEmpty() || placa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar al menos el ID del residente y la placa del vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Buscamos si el residente ya existe en la lista doble
            Residente residente = listaResidentes.buscar(id);

            if (residente == null) {
                // Si no existe, lo creamos y lo agregamos a la lista principal
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El residente es nuevo, debe ingresar su Nombre Completo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                residente = new Residente(id, nombre, casa, esSocio);
                listaResidentes.insertar(residente);
            }

            // 2. Creamos el vehículo e intentamos agregarlo a su lista simple
            Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, color, tipo);
            boolean agregado = residente.getVehiculosAsociados().agregarVehiculo(vehiculo);

            if (!agregado) {
                JOptionPane.showMessageDialog(this, "¡Límite alcanzado! Este residente ya tiene registrados los 3 vehículos permitidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "¡Vehículo asociado con éxito al residente " + residente.getNombre() + "!");
            limpiarCampos();
            actualizarVistaResidentes();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        // Mantenemos el ID y nombre para facilitar registrar el 2do y 3er vehículo al mismo residente
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtColor.setText("");
        txtTipo.setText("");
    }

    private void actualizarVistaResidentes() {
        areaInfo.setText("");
        NodoResidente actual = listaResidentes.getCabeza(); 
        while (actual != null) {
            Residente r = actual.getResidente();
            // Contamos cuántos vehículos tiene actualmente
            int numVehiculos = r.getVehiculosAsociados().getTamanio();
            areaInfo.append("ID: " + r.getId() + " | " + r.getNombre() + " (Casa: " + r.getNumeroCasa() + ") - Vehículos: " + numVehiculos + "/3\n");
            actual = actual.getSiguiente();
        }
    }
}