/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package enph.vista;
import java.util.List;
import java.util.ArrayList;
import enph.controlador.ReporteEncuestadorController;
import enph.modelo.ReporteBarrio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ReporteEncuestador extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReporteEncuestador.class.getName());
    private final ReporteEncuestadorController controlador = new ReporteEncuestadorController();

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);


public ReporteEncuestador() {
    initComponents();

    Lbl__Usuario1.setText(enph.modelo.SesionUsuario.nombreParaMostrar());
    Lbl_Rol1.setText(enph.modelo.SesionUsuario.rolParaMostrar());
    Lbl_TituloPantalla.setText("Reporte de encuestas");

    txt_FechaDesde.setToolTipText("Formato: dd/MM/yyyy");
    txt_FechaHasta.setToolTipText("Formato: dd/MM/yyyy");

    Tbl_Frecuencias.setRowHeight(32);
    Tbl_Frecuencias.getTableHeader().setFont(
            new java.awt.Font("Bahnschrift", 1, 18));
    Tbl_Frecuencias.setAutoResizeMode(
            javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

    configurarTabla();
    setLocationRelativeTo(null);
}
private void configurarTabla() {
    DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Barrio", "Asignadas", "Finalizadas",
                "Pendientes", "Incompletas"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    Tbl_Frecuencias.setModel(modelo);
    ajustarColumnas(Tbl_Frecuencias);
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
private LocalDate obtenerFecha(String texto) {
    return LocalDate.parse(texto.trim(), formatoFecha);
}
private void generarReporte() {
    try {
        LocalDate desde = obtenerFecha(txt_FechaDesde.getText());
        LocalDate hasta = obtenerFecha(txt_FechaHasta.getText());

        if (desde.isAfter(hasta)) {
            JOptionPane.showMessageDialog(this,
                    "La fecha Desde no puede ser posterior a la fecha Hasta.",
                    "Fechas incorrectas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<ReporteBarrio> datos = controlador.generarReporte(desde, hasta);

        DefaultTableModel modelo =
                (DefaultTableModel) Tbl_Frecuencias.getModel();

        modelo.setRowCount(0);

        int totalAsignadas = 0;
        int totalFinalizadas = 0;
        int totalPendientes = 0;
        int totalIncompletas = 0;

        for (ReporteBarrio r : datos) {
            modelo.addRow(new Object[]{
                r.getBarrio(),
                r.getAsignadas(),
                r.getFinalizadas(),
                r.getPendientes(),
                r.getIncompletas()
            });

            totalAsignadas += r.getAsignadas();
            totalFinalizadas += r.getFinalizadas();
            totalPendientes += r.getPendientes();
            totalIncompletas += r.getIncompletas();
        }

        Lbl_Asignadas.setText(String.valueOf(totalAsignadas));
        Lbl_Finalizadas.setText(String.valueOf(totalFinalizadas));
        Lbl_Pendientes.setText(String.valueOf(totalPendientes));
        Lbl_Incompletas.setText(String.valueOf(totalIncompletas));

        ajustarColumnas(Tbl_Frecuencias);

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron asignaciones en el rango seleccionado.",
                    "Reporte", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this,
                "Ingresa ambas fechas correctamente con el formato dd/MM/yyyy.",
                "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Error al generar el reporte: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
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
        jFileChooser1 = new javax.swing.JFileChooser();
        Pnl_Central = new javax.swing.JPanel();
        Pnl_Banner = new javax.swing.JPanel();
        Lbl_Titulo = new javax.swing.JLabel();
        Lbl_Rol1 = new javax.swing.JLabel();
        Lbl__Usuario1 = new javax.swing.JLabel();
        Lbl_TituloPantalla = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbl_Frecuencias = new javax.swing.JTable();
        Btn_Volver = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_FechaDesde = new javax.swing.JTextField();
        txt_FechaHasta = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Btn_GenerarReporte = new javax.swing.JButton();
        Lbl_Asignadas = new javax.swing.JLabel();
        Lbl_Finalizadas = new javax.swing.JLabel();
        Lbl_Asignadas1 = new javax.swing.JLabel();
        Lbl_Finalizadas1 = new javax.swing.JLabel();
        Lbl_Pendientes = new javax.swing.JLabel();
        Lbl_Pendientes1 = new javax.swing.JLabel();
        Lbl_Incompletas = new javax.swing.JLabel();
        Lbl_Incompletas1 = new javax.swing.JLabel();

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
        Lbl_TituloPantalla.setText("Reporte de encuestas por barrio");

        javax.swing.GroupLayout Pnl_BannerLayout = new javax.swing.GroupLayout(Pnl_Banner);
        Pnl_Banner.setLayout(Pnl_BannerLayout);
        Pnl_BannerLayout.setHorizontalGroup(
            Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_BannerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_BannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Lbl_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lbl_TituloPantalla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        Tbl_Frecuencias.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Tbl_Frecuencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Informacion de las asignaciones"
            }
        ));
        jScrollPane1.setViewportView(Tbl_Frecuencias);

        Btn_Volver.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Btn_Volver.setText("Volver al menú");
        Btn_Volver.addActionListener(this::Btn_VolverActionPerformed);

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel1.setText("Desde: ");

        txt_FechaDesde.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        txt_FechaDesde.addActionListener(this::txt_FechaDesdeActionPerformed);

        txt_FechaHasta.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        txt_FechaHasta.addActionListener(this::txt_FechaHastaActionPerformed);

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel2.setText("Hasta: ");

        Btn_GenerarReporte.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Btn_GenerarReporte.setText("Generar Reporte");
        Btn_GenerarReporte.addActionListener(this::Btn_GenerarReporteActionPerformed);

        Lbl_Asignadas.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Asignadas.setText("0");

        Lbl_Finalizadas.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Finalizadas.setText("0");

        Lbl_Asignadas1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Asignadas1.setText("Asignadas:");

        Lbl_Finalizadas1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Finalizadas1.setText("Finalizadas:");

        Lbl_Pendientes.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Pendientes.setText("0");

        Lbl_Pendientes1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Pendientes1.setText("Pendientes:");

        Lbl_Incompletas.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Incompletas.setText("0");

        Lbl_Incompletas1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        Lbl_Incompletas1.setText("Incompletas:");

        javax.swing.GroupLayout Pnl_CentralLayout = new javax.swing.GroupLayout(Pnl_Central);
        Pnl_Central.setLayout(Pnl_CentralLayout);
        Pnl_CentralLayout.setHorizontalGroup(
            Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Banner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Pnl_CentralLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_CentralLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(Pnl_CentralLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_FechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(108, 108, 108)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_FechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_CentralLayout.createSequentialGroup()
                .addGap(0, 49, Short.MAX_VALUE)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_CentralLayout.createSequentialGroup()
                        .addComponent(Btn_GenerarReporte)
                        .addGap(249, 249, 249)
                        .addComponent(Btn_Volver)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_CentralLayout.createSequentialGroup()
                        .addComponent(Lbl_Asignadas1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lbl_Asignadas, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(Lbl_Finalizadas1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lbl_Finalizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(Lbl_Pendientes1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Lbl_Pendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(Lbl_Incompletas1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Lbl_Incompletas, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        Pnl_CentralLayout.setVerticalGroup(
            Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_CentralLayout.createSequentialGroup()
                .addComponent(Pnl_Banner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_FechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_FechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(34, 34, 34)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Lbl_Asignadas)
                    .addComponent(Lbl_Finalizadas)
                    .addComponent(Lbl_Asignadas1)
                    .addComponent(Lbl_Finalizadas1)
                    .addComponent(Lbl_Pendientes)
                    .addComponent(Lbl_Pendientes1)
                    .addComponent(Lbl_Incompletas)
                    .addComponent(Lbl_Incompletas1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Pnl_CentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_CentralLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(Btn_Volver))
                    .addGroup(Pnl_CentralLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Btn_GenerarReporte)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Central, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Central, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        new MenuEncuestador().setVisible(true); // vuelve al menú del encuestador
        dispose(); // cierra esta ventana
    }//GEN-LAST:event_Btn_VolverActionPerformed

    private void txt_FechaDesdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_FechaDesdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_FechaDesdeActionPerformed

    private void txt_FechaHastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_FechaHastaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_FechaHastaActionPerformed

    private void Btn_GenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_GenerarReporteActionPerformed
    // TODO add your handling code here:
        generarReporte();
    }//GEN-LAST:event_Btn_GenerarReporteActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ReporteEncuestador().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_GenerarReporte;
    private javax.swing.JButton Btn_Volver;
    private javax.swing.JLabel Lbl_Asignadas;
    private javax.swing.JLabel Lbl_Asignadas1;
    private javax.swing.JLabel Lbl_Finalizadas;
    private javax.swing.JLabel Lbl_Finalizadas1;
    private javax.swing.JLabel Lbl_Incompletas;
    private javax.swing.JLabel Lbl_Incompletas1;
    private javax.swing.JLabel Lbl_Pendientes;
    private javax.swing.JLabel Lbl_Pendientes1;
    private javax.swing.JLabel Lbl_Rol1;
    private javax.swing.JLabel Lbl_Titulo;
    private javax.swing.JLabel Lbl_TituloPantalla;
    private javax.swing.JLabel Lbl__Usuario1;
    private javax.swing.JPanel Pnl_Banner;
    private javax.swing.JPanel Pnl_Central;
    private javax.swing.JPanel Pnl_Contenido;
    private javax.swing.JTable Tbl_Frecuencias;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txt_FechaDesde;
    private javax.swing.JTextField txt_FechaHasta;
    // End of variables declaration//GEN-END:variables
}
