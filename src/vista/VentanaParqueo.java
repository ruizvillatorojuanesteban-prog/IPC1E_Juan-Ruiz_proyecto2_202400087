package vista;

import estructuras.EspacioParqueo;
import estructuras.FilaParqueo;
import estructuras.ListaListasParqueo;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class VentanaParqueo extends JFrame {
    private ListaListasParqueo parqueo;
    private JPanel panelContenedor;

    public VentanaParqueo(ListaListasParqueo parqueo) {
        this.parqueo = parqueo;
        setTitle("Representación Gráfica del Parqueo - USAC");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelContenedor);

        actualizarGrafico();
        add(scroll);
    }

    public void actualizarGrafico() {
        panelContenedor.removeAll();

        FilaParqueo filaActual = parqueo.getInicioFilas(); 

        while (filaActual != null) {
            JPanel panelFila = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
            panelFila.setBorder(BorderFactory.createTitledBorder("Fila " + filaActual.getLetraFila()));

            EspacioParqueo espacioActual = filaActual.getPrimerEspacio();
            while (espacioActual != null) {
                // Obtenemos dinámicamente el identificador y el tipo para evitar errores de nombres
                String idEspacio = obtenerValorMetodo(espacioActual, "getId", "getIdentificador", "getNombre");
                String tipoEspacio = obtenerValorMetodo(espacioActual, "getTipo", "getTipoEspacio", "getTipoArea");

                JButton btnEspacio = new JButton(idEspacio);
                btnEspacio.setPreferredSize(new Dimension(55, 35));
                btnEspacio.setFont(new Font("Arial", Font.BOLD, 10));

                if (espacioActual.isOcupado()) {
                    btnEspacio.setBackground(new Color(220, 53, 69)); // Rojo (Ocupado)
                    btnEspacio.setForeground(Color.WHITE);
                    String placa = (espacioActual.getVehiculoActual() != null) ? espacioActual.getVehiculoActual().getPlaca() : "N/D";
                    btnEspacio.setToolTipText("Ocupado por: " + placa);
                } else {
                    btnEspacio.setBackground(new Color(40, 167, 69)); // Verde (Libre)
                    btnEspacio.setForeground(Color.WHITE);
                    btnEspacio.setToolTipText("Libre (" + tipoEspacio + ")");
                }

                panelFila.add(btnEspacio);
                espacioActual = espacioActual.getSiguiente();
            }

            panelContenedor.add(panelFila);
            filaActual = filaActual.getSiguienteFila();
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    // Método auxiliar para buscar automáticamente el getter correcto en EspacioParqueo
    private String obtenerValorMetodo(Object objeto, String... nombresMetodos) {
        for (String nombre : nombresMetodos) {
            try {
                Method m = objeto.getClass().getMethod(nombre);
                Object resultado = m.invoke(objeto);
                if (resultado != null) return resultado.toString();
            } catch (Exception ignored) {}
        }
        return "N/D";
    }
}