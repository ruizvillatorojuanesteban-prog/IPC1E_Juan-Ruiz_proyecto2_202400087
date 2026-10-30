package vista;

import estructuras.ListaDobleResidentes;
import estructuras.ListaDobleResidentes.NodoResidente;
import modelo.Residente;
import modelo.Vehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaGestionResidentes extends JFrame {
    private ListaDobleResidentes listaResidentes;
    private JTextField txtId, txtNombre, txtCasa, txtPlaca, txtMarca, txtModelo, txtColor, txtTipo;
    private JCheckBox chkSocio;
    private JTable tablaResidentes;
    private DefaultTableModel modeloTabla;

    public VentanaGestionResidentes(ListaDobleResidentes listaResidentes) {
        this.listaResidentes = listaResidentes;

        setTitle("Gestión de Residentes y Vehículos - USAC");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        actualizarTablaResidentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Panel izquierdo: Formulario de datos del residente y vehículo
        JPanel panelFormulario = new JPanel(new GridLayout(10, 2, 5, 5));
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

        // Panel central: Tabla para recorrer y visualizar todos los residentes almacenados
        modeloTabla = new DefaultTableModel(new String[]{"ID / DPI", "Nombre", "No. Casa", "Socio", "Vehículos (Placas)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResidentes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaResidentes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Residentes Registrados (Recorrido de Lista Doble)"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior: Botones de control para Insertar, Buscar, Actualizar, Eliminar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnInsertar = new JButton("Insertar / Registrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar Campos");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnInsertar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        // Asociar acciones a los botones
        btnInsertar.addActionListener(e -> insertarResidenteVehiculo());
        btnBuscar.addActionListener(e -> buscarResidente());
        btnActualizar.addActionListener(e -> actualizarResidente());
        btnEliminar.addActionListener(e -> eliminarResidente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCerrar.addActionListener(e -> dispose());

        // Al hacer clic en una fila de la tabla, se cargan los datos al formulario automáticamente
        tablaResidentes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaResidentes.getSelectedRow() != -1) {
                int fila = tablaResidentes.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtCasa.setText(modeloTabla.getValueAt(fila, 2).toString());
                chkSocio.setSelected(Boolean.parseBoolean(modeloTabla.getValueAt(fila, 3).toString()));
            }
        });
    }

    private void insertarResidenteVehiculo() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String casa = txtCasa.getText().trim();
            boolean esSocio = chkSocio.isSelected();

            if (id.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar al menos el ID y el Nombre del residente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Residente residente = listaResidentes.buscarResidente(id);
            if (residente == null) {
                residente = new Residente(id, nombre, casa.isEmpty() ? "N/A" : casa, esSocio);
                listaResidentes.insertar(residente);
            } else {
                residente.setNombre(nombre);
                if (!casa.isEmpty()) residente.setNumeroCasa(casa);
                residente.setEsSocio(esSocio);
            }

            String placa = txtPlaca.getText().trim();
            if (!placa.isEmpty()) {
                String marca = txtMarca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String color = txtColor.getText().trim();
                String tipo = txtTipo.getText().trim();

                Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, color, tipo.isEmpty() ? "Automóvil" : tipo);
                boolean agregado = residente.getVehiculosAsociados().agregarVehiculo(vehiculo);
                if (!agregado) {
                    JOptionPane.showMessageDialog(this, "Residente guardado, pero ya alcanzó el límite de 3 vehículos permitidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "¡Residente insertado y vehículo asociado con éxito!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "¡Residente insertado / actualizado con éxito!");
            }

            limpiarCampos();
            actualizarTablaResidentes();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarResidente() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID / DPI del residente a buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Residente r = listaResidentes.buscarResidente(id);
        if (r != null) {
            txtNombre.setText(r.getNombre());
            txtCasa.setText(r.getNumeroCasa());
            chkSocio.setSelected(r.isEsSocio());
            JOptionPane.showMessageDialog(this, "¡Residente encontrado!\nNombre: " + r.getNombre() + "\nNo. Casa: " + r.getNumeroCasa() + "\nSocio: " + (r.isEsSocio() ? "Sí" : "No"));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún residente con el ID: " + id, "No encontrado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarResidente() {
        String id = txtId.getText().trim();
        String nuevoNombre = txtNombre.getText().trim();
        String nuevaCasa = txtCasa.getText().trim();
        boolean nuevoEsSocio = chkSocio.isSelected();

        if (id.isEmpty() || nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el ID y el nuevo nombre para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Residente r = listaResidentes.buscarResidente(id);
        if (r != null) {
            r.setNombre(nuevoNombre);
            if (!nuevaCasa.isEmpty()) r.setNumeroCasa(nuevaCasa);
            r.setEsSocio(nuevoEsSocio);
            JOptionPane.showMessageDialog(this, "¡Residente actualizado correctamente!");
            actualizarTablaResidentes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el residente con ID " + id + " para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarResidente() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese o seleccione el ID del residente a eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al residente con ID " + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = listaResidentes.eliminarResidente(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "¡Residente eliminado con éxito de la lista doble!");
                limpiarCampos();
                actualizarTablaResidentes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCasa.setText("");
        chkSocio.setSelected(false);
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtColor.setText("");
        txtTipo.setText("");
        tablaResidentes.clearSelection();
    }

    private void actualizarTablaResidentes() {
        modeloTabla.setRowCount(0);
        NodoResidente actual = listaResidentes.getCabeza();
        // Recorrido oficial de la lista doblemente enlazada
        while (actual != null) {
            Residente r = actual.getResidente();
            StringBuilder placas = new StringBuilder();
            var nodoV = r.getVehiculosAsociados().getCabeza();
            while (nodoV != null) {
                placas.append(nodoV.getVehiculo().getPlaca()).append(" ");
                nodoV = nodoV.getSiguiente();
            }
            modeloTabla.addRow(new Object[]{
                r.getId(),
                r.getNombre(),
                r.getNumeroCasa(),
                r.isEsSocio(),
                placas.toString().trim()
            });
            actual = actual.getSiguiente();
        }
    }
}