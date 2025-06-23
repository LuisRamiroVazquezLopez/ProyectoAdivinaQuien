/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package BaseDeDatos;

import JuegoCodigo.FondoImagen;
import JuegoCodigo.Musica;
import JuegoGrafico.MenuPrincipal;
import static JuegoGrafico.MenuPrincipal.musica;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aorus
 */
public class ConsultaBD extends FondoImagen {

    
    DefaultTableModel model = new DefaultTableModel();
    
    /**
     * Creates new form ConsultaBD
     */
    public ConsultaBD() {
        super("/Imagenes/records.png");
        initComponents();
        
        
        model.addColumn("ID");
        model.addColumn("Jugador 1");
        model.addColumn("Jugador 2");
        model.addColumn("Ganador");
        model.addColumn("Personaje Ganador");
        model.addColumn("Fecha");
        model.addColumn("Duracion (s)");

        jTableBD.setModel(model);
        jTableBD.setRowHeight(25);
        jTableBD.setShowGrid(true);
        jTableBD.setGridColor(Color.LIGHT_GRAY);
        jTableBD.setIntercellSpacing(new Dimension(0, 1));
        
        cargarPartidasOrdenadas();

        // Asignar modelo
        jTableBD.setModel(model);

        // Personalizar tabla
        jTableBD.setRowHeight(25);
        jTableBD.setShowGrid(true);
        jTableBD.setGridColor(Color.LIGHT_GRAY);
        jTableBD.setIntercellSpacing(new Dimension(0, 1));
        
        
        
         //PARA EL BOTON DE PAUSAR
        ImageIcon iconoP = new ImageIcon(getClass().getResource("/Imagenes/pausa_boton.png"));
        Image imagenP = iconoP.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        //PARA REPRODUCIR
        ImageIcon iconoR = new ImageIcon(getClass().getResource("/Imagenes/Reanudar_boton.png"));
        Image imagenR = iconoR.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        //QUITAR BORDES DE BOTON
        ControlMusicaBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        ControlMusicaBoton.setBorderPainted(false);     // Elimina el borde
        ControlMusicaBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        if(musica.estaReproduciendo()){
            ControlMusicaBoton.setIcon(new ImageIcon(imagenP));
            ControlMusicaBoton.setSelected(false);
            Musica.flag=true;
        } else {
            ControlMusicaBoton.setIcon(new ImageIcon(imagenR));
            ControlMusicaBoton.setSelected(true);
            Musica.flag=false;
        }
        
        ImageIcon icono1 = new ImageIcon(getClass().getResource("/Imagenes/volver_boton.png"));
        Image imagen1 = icono1.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        VolverBotonPanel1.setIcon(new ImageIcon(imagen1));
        VolverBotonPanel1.setContentAreaFilled(false); // Elimina el fondo del botón
        VolverBotonPanel1.setBorderPainted(false);     // Elimina el borde
        VolverBotonPanel1.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        
        ImageIcon icono2 = new ImageIcon(getClass().getResource("/Imagenes/consulta_p_duracion_boton.png"));
        Image imagen2 = icono2.getImage().getScaledInstance(400, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        jButtonConsultar.setIcon(new ImageIcon(imagen2));
        jButtonConsultar.setContentAreaFilled(false); // Elimina el fondo del botón
        jButtonConsultar.setBorderPainted(false);     // Elimina el borde
        jButtonConsultar.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        
        ImageIcon icono3 = new ImageIcon(getClass().getResource("/Imagenes/consulta_p_nombre_boton.png"));
        Image imagen3 = icono3.getImage().getScaledInstance(400, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        jButtonNombre.setIcon(new ImageIcon(imagen3));
        jButtonNombre.setContentAreaFilled(false); // Elimina el fondo del botón
        jButtonNombre.setBorderPainted(false);     // Elimina el borde
        jButtonNombre.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
    }
    
    private void cargarDatosEnTabla(List<PartidaBD> partidas) {
        // Limpiar tabla antes de cargar nuevos datos
        model.setRowCount(0);
        
        if (partidas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron partidas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (PartidaBD partida : partidas) {
                Object[] fila = {
                    partida.getId(),
                    partida.getJugador1(),
                    partida.getJugador2(),
                    partida.getGanador(),
                    partida.getPersonajeGanador(), // Orden correcto
                    partida.getFecha(), 
                    partida.getDuracion()
                };
                model.addRow(fila);
            }
        }
    }
    
    private void cargarPartidasOrdenadas() {
        List<PartidaBD> partidas = ConexionBD.buscarOrdenado();
        cargarDatosEnTabla(partidas);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        VolverBotonPanel1 = new javax.swing.JButton();
        ControlMusicaBoton = new javax.swing.JToggleButton();
        jButtonConsultar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableBD = new javax.swing.JTable();
        jButtonNombre = new javax.swing.JButton();

        VolverBotonPanel1.setPreferredSize(new java.awt.Dimension(200, 80));
        VolverBotonPanel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VolverBotonPanel1ActionPerformed(evt);
            }
        });

        ControlMusicaBoton.setPreferredSize(new java.awt.Dimension(40, 40));
        ControlMusicaBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ControlMusicaBotonActionPerformed(evt);
            }
        });

        jButtonConsultar.setPreferredSize(new java.awt.Dimension(300, 80));
        jButtonConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultarActionPerformed(evt);
            }
        });

        jTableBD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableBD.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableBD.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTableBD);

        jButtonNombre.setPreferredSize(new java.awt.Dimension(300, 80));
        jButtonNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNombreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(VolverBotonPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ControlMusicaBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1616, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(144, 144, 144))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ControlMusicaBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButtonConsultar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(VolverBotonPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jButtonNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(211, 211, 211))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void VolverBotonPanel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VolverBotonPanel1ActionPerformed
        // Cierra la ventana actual completamente
        MenuPrincipal.musica.pausar();//para que no se duplique el sonido
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose(); // Cierra el JFrame actual

        // Crea un nuevo JFrame con el menú principal
        MenuPrincipal nuevoMenu = new MenuPrincipal();
        nuevoMenu.setVisible(true); // Muestra el nuevo menú con todo correctamente configurado
    }//GEN-LAST:event_VolverBotonPanel1ActionPerformed

    private void ControlMusicaBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ControlMusicaBotonActionPerformed
        ImageIcon iconoP = new ImageIcon(getClass().getResource("/Imagenes/pausa_boton.png"));
        Image imagenP = iconoP.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        //PARA REPRODUCIR
        ImageIcon iconoR = new ImageIcon(getClass().getResource("/Imagenes/Reanudar_boton.png"));
        Image imagenR = iconoR.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        if (ControlMusicaBoton.isSelected()) {
            musica.pausar();
            ControlMusicaBoton.setIcon(new ImageIcon(imagenR));
            Musica.flag=false;
        } else {
            musica.continuar();
            ControlMusicaBoton.setIcon(new ImageIcon(imagenP));
            Musica.flag=true;
        }
    }//GEN-LAST:event_ControlMusicaBotonActionPerformed

    private void jButtonConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultarActionPerformed
        // TODO add your handling code here:
        cargarPartidasOrdenadas();   
    }//GEN-LAST:event_jButtonConsultarActionPerformed

    private void jButtonNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNombreActionPerformed
        String nombreJugador = JOptionPane.showInputDialog(this, "Introduce el nombre a buscar:", "Buscar por Nombre", JOptionPane.PLAIN_MESSAGE);
        if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
            List<PartidaBD> partidas = ConexionBD.buscarPorNombre(nombreJugador);
            cargarDatosEnTabla(partidas);
        }
        
    }//GEN-LAST:event_jButtonNombreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton ControlMusicaBoton;
    private javax.swing.JButton VolverBotonPanel1;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonNombre;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableBD;
    // End of variables declaration//GEN-END:variables
}
