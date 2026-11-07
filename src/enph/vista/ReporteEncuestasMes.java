/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package enph.vista;

import enph.Main;
import enph.modelo.SesionUsuario;
import java.sql.SQLException;
import java.util.List;
import enph.modelo.ReporteRendimientoEncuestador;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ReporteEncuestasMes extends javax.swing.JFrame {
    
private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReporteEncuestasMes.class.getName());
    
public ReporteEncuestasMes() {

    initComponents();

    Lbl__Usuario1.setText(SesionUsuario.nombreParaMostrar());
    Lbl_Rol1.setText(SesionUsuario.rolParaMostrar());

    // Configuración visual de la tabla
    Tbl_encuestador.setRowHeight(32);
    Tbl_encuestador.getTableHeader().setFont(
            new java.awt.Font("Bahnschrift", 1, 18)
    );
    Tbl_encuestador.setFont(
            new java.awt.Font("Bahnschrift", 0, 18)
    );

    Tbl_encuestador.setAutoResizeMode(
            javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );

    Tbl_encuestador.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
    );

    // Mostrar el año actual inicialmente
    Txt_Año.setText(String.valueOf(LocalDate.now().getYear()));

    configurarTabla();

    // Actualizar al cambiar el mes
    Cmb_Mes.addActionListener(e -> cargarReporte());

    // Actualizar al presionar Enter en el año
    Txt_Año.addActionListener(e -> cargarReporte());

    cargarReporte();

    setLocationRelativeTo(null);
}
private void configurarTabla() {

    DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Encuestador", "Realizadas", "Pendientes"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    Tbl_encuestador.setModel(modelo);

    ajustarColumnas(Tbl_encuestador);
}
private void cargarReporte() {

    String mesSeleccionado = (String) Cmb_Mes.getSelectedItem();
    String anioTexto = Txt_Año.getText().trim();

    // Validar que exista un mes seleccionado
    if (mesSeleccionado == null) {
        return;
    }

    // Validar que el año no esté vacío
    if (anioTexto.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Ingrese el año que desea consultar.",
                "Dato requerido",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    try {

        int mes = Integer.parseInt(mesSeleccionado);
        int anio = Integer.parseInt(anioTexto);

        // Validar el año
        if (anio < 1900 || anio > 9999) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un año válido entre 1900 y 9999.",
                    "Año inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Consultar el reporte mediante Main
        List<ReporteRendimientoEncuestador> resultados =
                Main.generarReporteRendimiento(mes, anio);

        // Crear el modelo de la tabla
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Encuestador", "Realizadas", "Pendientes"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        int totalRealizadas = 0;
        int totalPendientes = 0;

        // Recorrer los resultados
        for (ReporteRendimientoEncuestador reporte : resultados) {

            modelo.addRow(new Object[]{
                reporte.getNombreEncuestador(),
                reporte.getRealizadas(),
                reporte.getPendientes()
            });

            totalRealizadas += reporte.getRealizadas();
            totalPendientes += reporte.getPendientes();
        }

        // Mostrar los resultados en la tabla
        Tbl_encuestador.setModel(modelo);

        ajustarColumnas(Tbl_encuestador);

        // Calcular promedios por encuestador
        if (!resultados.isEmpty()) {

            double promedioRealizadas =
                    (double) totalRealizadas / resultados.size();

            double promedioPendientes =
                    (double) totalPendientes / resultados.size();

            Lbl_PromedioRealizadas.setText(
                    String.format("%.2f", promedioRealizadas)
            );

            Lbl_PromedioPendientes.setText(
                    String.format("%.2f", promedioPendientes)
            );

        } else {

            Lbl_PromedioRealizadas.setText("0");
            Lbl_PromedioPendientes.setText("0");

            JOptionPane.showMessageDialog(
                    this,
                    "No hay registros para el mes y año seleccionados.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "El año debe contener únicamente números.",
                "Dato inválido",
                JOptionPane.WARNING_MESSAGE
        );

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al generar el reporte:\n" + e.getMessage(),
                "Error de consulta",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

// Ajusta el ancho de cada columna al contenido más largo: toma el mayor entre el título y todos los datos de la columna.
private void ajustarColumnas(javax.swing.JTable tabla) {
    for (int col = 0; col < tabla.getColumnCount(); col++) {
        javax.swing.table.TableColumn columna = tabla.getColumnModel().getColumn(col);

        // Ancho del título
        java.awt.Component comp = tabla.getTableHeader().getDefaultRenderer()
                .getTableCellRendererComponent(tabla, columna.getHeaderValue(), false, false, 0, col);
        int ancho = comp.getPreferredSize().width;

        // Ancho del dato más largo
        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            comp = tabla.prepareRenderer(tabla.getCellRenderer(fila, col), fila, col);
            ancho = Math.max(ancho, comp.getPreferredSize().width);
        }

        columna.setPreferredWidth(ancho + 20); // 20 px de margen
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pnl_Contenido = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Lbl_Rol = new javax.swing.JLabel();
        Lbl__Usuario = new javax.swing.JLabel();
        Pnl_Central = new javax.swing.JPanel();
        Pnl_Banner = new javax.swing.JPanel();
        Lbl_Titulo = new javax.swing.JLabel();
        Lbl_Rol1 = new javax.swing.JLabel();
        Lbl__Usuario1 = new javax.swing.JLabel();
        Lbl_TituloPantalla = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbl_encuestador = new javax.swing.JTable();
        Btn_Volver = new javax.swing.JButton();
        Lbl_Tiutlo = new javax.swing.JLabel();
        Cmb_Mes = new javax.swing.JComboBox<>();
        Lbl_TituloRealizadas = new javax.swing.JLabel();
        Lbl_tituloPendientes = new javax.swing.JLabel();
        Lbl_PromedioRealizadas = new javax.swing.JLabel();
        Lbl_PromedioPendientes = new javax.swing.JLabel();
        Lbl_Año = new javax.swing.JLabel();
        Txt_Año = new javax.swing.JTextField();

        jButton1.setBackground(new java.awt.Color(182, 215, 168));
        jButton1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 158, 15));
        jButton1.setText("Ingresar");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel4.setText("Usuario:");

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 40)); // NOI18N
        jLabel3.setText("Ingresar usuario y contraseña");

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel5.setText("Contraseña:");

        javax.swing.GroupLayout Pnl_ContenidoLayout = new javax.swing.GroupLayout(Pnl_Contenido);
        Pnl_Contenido.setLayout(Pnl_ContenidoLayout);
        Pnl_ContenidoLayout.setHorizontalGroup(
            Pnl_ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_ContenidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_ContenidoLayout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addGroup(Pnl_ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Pnl_ContenidoLayout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap())
        );
        Pnl_ContenidoLayout.setVerticalGroup(
            Pnl_ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_ContenidoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(37, 37, 37)
                .addGroup(Pnl_ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jButton1)
                .addContainerGap())
        );

        Lbl_Rol.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        Lbl_Rol.setText("Encuestador");

        Lbl__Usuario.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        Lbl__Usuario.setText("Carlos Pérez Bayona");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Pnl_Banner.setBackground(new java.awt.Color(159, 197, 248));
        Pnl_Banner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Lbl_Titulo.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        Lbl_Titulo.setText("Encuesta Nacional de Presupuestos de los Hogares (ENPH)");

        Lbl_Rol1.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        Lbl_Rol1.setText("Admin (Rol)");

        Lbl__Usuario1.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        Lbl__Usuario1.setText("Nombre del usuario");

        Lbl_TituloPantalla.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        Lbl_TituloPantalla.setText("Encuestas realizadas por mes");

        javax.swing.GroupLayout Pnl_BannerLayout = new javax.swing.GroupLayout(Pnl_Banner);
        Pnl_Banner.setLayout(Pnl_BannerLayout);
        Pnl_BannerLayout.setHorizontalGroup(
            Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_BannerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Lbl_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lbl_TituloPantalla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addGroup(Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Lbl__Usuario1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Lbl_Rol1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(25, 25, 25))
        );
        Pnl_BannerLayout.setVerticalGroup(
            Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_BannerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_BannerLayout.createSequentialGroup()
                        .addComponent(Lbl__Usuario1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lbl_Rol1))
                    .addGroup(Pnl_BannerLayout.createSequentialGroup()
                        .addComponent(Lbl_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lbl_TituloPantalla)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        Tbl_encuestador.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Tbl_encuestador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Encuestador", "Realizadas", "Pendientes"
            }
        ));
        jScrollPane1.setViewportView(Tbl_encuestador);

        Btn_Volver.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Btn_Volver.setText("Volver ");
        Btn_Volver.addActionListener(this::Btn_VolverActionPerformed);

        Lbl_Tiutlo.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Tiutlo.setText("Mes:");

        Cmb_Mes.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Cmb_Mes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        Lbl_TituloRealizadas.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_TituloRealizadas.setText("Promedio realizadas: ");

        Lbl_tituloPendientes.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_tituloPendientes.setText("Promedio pendientes:");

        Lbl_PromedioRealizadas.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_PromedioRealizadas.setText("0");

        Lbl_PromedioPendientes.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_PromedioPendientes.setText("0");

        Lbl_Año.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Año.setText("Año:");

        Txt_Año.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Txt_Año.addActionListener(this::Txt_AñoActionPerformed);

        javax.swing.GroupLayout Pnl_CentralLayout = new javax.swing.GroupLayout(Pnl_Central);
        Pnl_Central.setLayout(Pnl_CentralLayout);
        Pnl_CentralLayout.setHorizontalGroup(
            Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Banner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Pnl_CentralLayout.createSequentialGroup()
                .addGap(281, 281, 281)
                .addComponent(Lbl_Tiutlo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cmb_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(Lbl_Año, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Txt_Año, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_CentralLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_CentralLayout.createSequentialGroup()
                        .addGap(805, 805, 805)
                        .addComponent(Btn_Volver))
                    .addGroup(Pnl_CentralLayout.createSequentialGroup()
                        .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pnl_CentralLayout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(Lbl_TituloRealizadas)
                                .addGap(18, 18, 18)
                                .addComponent(Lbl_PromedioRealizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102)
                                .addComponent(Lbl_tituloPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Lbl_PromedioPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        Pnl_CentralLayout.setVerticalGroup(
            Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_CentralLayout.createSequentialGroup()
                .addComponent(Pnl_Banner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Lbl_Tiutlo)
                    .addComponent(Cmb_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lbl_Año)
                    .addComponent(Txt_Año, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Lbl_TituloRealizadas)
                    .addComponent(Lbl_PromedioRealizadas)
                    .addComponent(Lbl_tituloPendientes)
                    .addComponent(Lbl_PromedioPendientes))
                .addGap(18, 18, 18)
                .addComponent(Btn_Volver)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Central, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Central, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void Btn_VolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_VolverActionPerformed
        // TODO add your handling code here:
        new MenuAdminReportes().setVisible(true); // vuelve al menú de los reportes
        dispose(); // cierra esta ventana
    }//GEN-LAST:event_Btn_VolverActionPerformed

    private void Txt_AñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Txt_AñoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Txt_AñoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ReporteEncuestasMes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Volver;
    private javax.swing.JComboBox<String> Cmb_Mes;
    private javax.swing.JLabel Lbl_Año;
    private javax.swing.JLabel Lbl_PromedioPendientes;
    private javax.swing.JLabel Lbl_PromedioRealizadas;
    private javax.swing.JLabel Lbl_Rol;
    private javax.swing.JLabel Lbl_Rol1;
    private javax.swing.JLabel Lbl_Titulo;
    private javax.swing.JLabel Lbl_TituloPantalla;
    private javax.swing.JLabel Lbl_TituloRealizadas;
    private javax.swing.JLabel Lbl_Tiutlo;
    private javax.swing.JLabel Lbl__Usuario;
    private javax.swing.JLabel Lbl__Usuario1;
    private javax.swing.JLabel Lbl_tituloPendientes;
    private javax.swing.JPanel Pnl_Banner;
    private javax.swing.JPanel Pnl_Central;
    private javax.swing.JPanel Pnl_Contenido;
    private javax.swing.JTable Tbl_encuestador;
    private javax.swing.JTextField Txt_Año;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
