/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package JuegoGrafico;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author manue
 */
public class Creditos extends javax.swing.JPanel {

    /**
     * Creates new form Creditos
     */
    public Creditos() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        VolverBotonPanel1 = new javax.swing.JButton();

        jLabel1.setText("Aqui pones los creditos we, en esta pantalla");

        VolverBotonPanel1.setText("Volver");
        VolverBotonPanel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VolverBotonPanel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(361, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(360, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(VolverBotonPanel1)
                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 328, Short.MAX_VALUE)
                .addComponent(VolverBotonPanel1)
                .addGap(41, 41, 41))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton VolverBotonPanel1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
