/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package JuegoGrafico;
import BaseDeDatos.ConsultaBD;
import Conexion.Cliente;
import JuegoCodigo.Code;
import JuegoCodigo.FondoImagen;
import JuegoCodigo.Musica;
import JuegoCodigo.PanelTablero;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author manue
 */
public class MenuPrincipal extends javax.swing.JFrame {
    private JPanel panelCentral;
    public static Musica musica=new Musica();
    Cliente cliente;
    int cont;
    public static int c;
    private boolean listenersAgregados = false;// para evitar listeners duplicados
    private Cursor cursorNormal;//Para ponerle diseño al cursor
    private Cursor cursorClick;
    private String nombreJugador;

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    /**
     * Creates new form MenuPrincipal
     */
    public MenuPrincipal() {
        //PONEMOS FONDO DE PANTALLA (se ppne aqui para que los botones se puedan ver)
        FondoImagen fondo = new FondoImagen("/Imagenes/portada.png");
        fondo.setLayout(new javax.swing.GroupLayout(fondo)); // o cualquier layout que estés usando
        setContentPane(fondo);
        initComponents();
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
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//para que se abra en pantalla completa
        if(Musica.flag==true&&c==0){
            musica.reproducir(Musica.elegirCancion());
            ControlMusicaBoton.setIcon(new ImageIcon(imagenP));
            ControlMusicaBoton.setSelected(false);
            c++;
        }else if(Musica.flag==true){
            musica.continuar();
            ControlMusicaBoton.setIcon(new ImageIcon(imagenP));
            ControlMusicaBoton.setSelected(false);
        }else {
            ControlMusicaBoton.setIcon(new ImageIcon(imagenR));
            ControlMusicaBoton.setSelected(true);
        }
        
        //Codigo para diseño de cursor
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image imgNormal = toolkit.getImage(getClass().getResource("/Imagenes/mouse_abierto.png"));
        Image imgClick = toolkit.getImage(getClass().getResource("/Imagenes/mouse_cerrado.png"));
        cursorNormal = toolkit.createCustomCursor(imgNormal, new Point(0, 0), "cursorNormal");
        cursorClick = toolkit.createCustomCursor(imgClick, new Point(0, 0), "cursorClick");

        // Aplica el cursor al JFrame principal
        setCursor(cursorNormal);

        // Cambia el cursor al presionar y soltar
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setCursor(cursorClick);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setCursor(cursorNormal);
            }
        });
        
        //SE PONEN LAS IMAGENES DE LOS BOTONES
        //CREDITOS
        ImageIcon icono1 = new ImageIcon(getClass().getResource("/Imagenes/credito_boton.png"));
        Image imagen1 = icono1.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        CreditosBoton.setIcon(new ImageIcon(imagen1));
        CreditosBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        CreditosBoton.setBorderPainted(false);     // Elimina el borde
        CreditosBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        //SALIR
        ImageIcon icono2 = new ImageIcon(getClass().getResource("/Imagenes/salir_boton.png"));
        Image imagen2 = icono2.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        SalirBoton.setIcon(new ImageIcon(imagen2));
        SalirBoton.setIcon(new ImageIcon(imagen2));
        SalirBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        SalirBoton.setBorderPainted(false);     // Elimina el borde
        SalirBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        //INSTRUCCIONES
        ImageIcon icono3 = new ImageIcon(getClass().getResource("/Imagenes/reglas_boton.png"));
        Image imagen3 = icono3.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        InstruccionesBoton.setIcon(new ImageIcon(imagen3));
        InstruccionesBoton.setIcon(new ImageIcon(imagen3));
        InstruccionesBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        InstruccionesBoton.setBorderPainted(false);     // Elimina el borde
        InstruccionesBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        
        //RECORD
        ImageIcon icono5 = new ImageIcon(getClass().getResource("/Imagenes/record_boton.png"));
        Image imagen5 = icono5.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        BDBoton.setIcon(new ImageIcon(imagen5));
        BDBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        BDBoton.setBorderPainted(false);     // Elimina el borde
        BDBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        //JUGAR
        ImageIcon icono4 = new ImageIcon(getClass().getResource("/Imagenes/jugar_boton.png"));
        Image imagen4 = icono4.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);//ajustar la imagen de acuerdo al tamaño del boton
        JugarBoton.setIcon(new ImageIcon(imagen4));
        JugarBoton.setIcon(new ImageIcon(imagen4));
        JugarBoton.setContentAreaFilled(false); // Elimina el fondo del botón
        JugarBoton.setBorderPainted(false);     // Elimina el borde
        JugarBoton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SalirBoton = new javax.swing.JButton();
        CreditosBoton = new javax.swing.JButton();
        InstruccionesBoton = new javax.swing.JButton();
        JugarBoton = new javax.swing.JButton();
        ControlMusicaBoton = new javax.swing.JToggleButton();
        BDBoton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Adivina quien!");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setPreferredSize(new java.awt.Dimension(955, 581));

        SalirBoton.setPreferredSize(new java.awt.Dimension(200, 100));
        SalirBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirBotonActionPerformed(evt);
            }
        });

        CreditosBoton.setPreferredSize(new java.awt.Dimension(200, 100));
        CreditosBoton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CreditosBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreditosBotonActionPerformed(evt);
            }
        });

        InstruccionesBoton.setPreferredSize(new java.awt.Dimension(200, 100));
        InstruccionesBoton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        InstruccionesBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstruccionesBotonActionPerformed(evt);
            }
        });

        JugarBoton.setPreferredSize(new java.awt.Dimension(200, 100));
        JugarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JugarBotonActionPerformed(evt);
            }
        });

        ControlMusicaBoton.setPreferredSize(new java.awt.Dimension(40, 40));
        ControlMusicaBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ControlMusicaBotonActionPerformed(evt);
            }
        });

        BDBoton.setPreferredSize(new java.awt.Dimension(200, 100));
        BDBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDBotonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(SalirBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(InstruccionesBoton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(CreditosBoton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                        .addComponent(ControlMusicaBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BDBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JugarBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addComponent(JugarBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(BDBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(InstruccionesBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(CreditosBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ControlMusicaBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SalirBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))))
        );

        SalirBoton.getAccessibleContext().setAccessibleName("SalirBoton");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //BOTON DE SALIR
    private void SalirBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirBotonActionPerformed
        musica.pausar();
        System.exit(0); // Finaliza el programa completamente
    }//GEN-LAST:event_SalirBotonActionPerformed
    //BOTON DE CREDITOS
    private void CreditosBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreditosBotonActionPerformed
        Creditos creditos= new Creditos();//creamos un obejot para enseñar el panel de creditos
        this.getContentPane().removeAll();                       // Limpia lo anterior
        this.getContentPane().setLayout(new BorderLayout());     // Usa BorderLayout
        this.getContentPane().add(new Creditos(), BorderLayout.CENTER); // Agrega el panel
        this.revalidate();                                       // Actualiza el layout
        this.repaint();                                          // Redibuja la ventana
    }//GEN-LAST:event_CreditosBotonActionPerformed
    //BOTON DE INSTRUCCIONES
    private void InstruccionesBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstruccionesBotonActionPerformed
        Instrucciones instrucciones= new Instrucciones();//creamos un obejot para enseñar el panel de Instrucciones
        this.getContentPane().removeAll();                       // Limpia lo anterior
        this.getContentPane().setLayout(new BorderLayout());     // Usa BorderLayout
        this.getContentPane().add(new Instrucciones(), BorderLayout.CENTER); // Agrega el panel
        this.revalidate();                                       // Actualiza el layout
        this.repaint();                                          // Redibuja la ventana
    }//GEN-LAST:event_InstruccionesBotonActionPerformed

    private void JugarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JugarBotonActionPerformed
        nombreJugador = JOptionPane.showInputDialog(this, "Introduce tu nombre:", "Nombre del Jugador", JOptionPane.PLAIN_MESSAGE);
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            nombreJugador = "Jugador " + new Random().nextInt(1000); // Nombre por defecto si no se ingresa
        }

        // Crear el panel de juego y el cliente
        Juego juego = new Juego(cliente, nombreJugador);
        cliente = new Cliente("localhost", 5000, juego, nombreJugador);
        juego.setCliente(cliente);
        
        // Cambiar al panel de juego
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(juego, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_JugarBotonActionPerformed

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

    private void BDBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDBotonActionPerformed
        // TODO add your handling code here:
        ConsultaBD consulta= new ConsultaBD();//creamos un obejot para enseñar el panel de Instrucciones
        this.getContentPane().removeAll();                       // Limpia lo anterior
        this.getContentPane().setLayout(new BorderLayout());     // Usa BorderLayout
        this.getContentPane().add(new ConsultaBD(), BorderLayout.CENTER); // Agrega el panel
        this.revalidate();                                       // Actualiza el layout
        this.repaint();    
        
    }//GEN-LAST:event_BDBotonActionPerformed
    
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BDBoton;
    private javax.swing.JToggleButton ControlMusicaBoton;
    private javax.swing.JButton CreditosBoton;
    private javax.swing.JButton InstruccionesBoton;
    private javax.swing.JButton JugarBoton;
    private javax.swing.JButton SalirBoton;
    // End of variables declaration//GEN-END:variables
}
