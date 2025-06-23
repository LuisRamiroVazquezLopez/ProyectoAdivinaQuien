/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package JuegoGrafico;
import JuegoCodigo.Code;
import JuegoCodigo.PanelTablero;
import Conexion.Cliente;
import JuegoCodigo.FondoImagen;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
/**
 *
 * @author manue
 */
    public class Juego extends FondoImagen {
        public static Cliente cliente;
        private java.util.Timer gameTimer; 
        private int segundosTranscurridos = 0;
        private JPanel panelDeJuego; 
        private PanelTablero panelTablero; 
        private boolean miTurno = false;
        private List<String> personajesDisponibles = new ArrayList<>();
        private List<String> personajesDescartados = new ArrayList<>();
        private String miPersonajeSeleccionado = null;
        private String rutaImagenPersonajeSeleccionado = null;
        private boolean listenersAgregados = false;


        public Juego(Cliente cliente, String nombreJugador) {
            super("/Imagenes/default.png");
            this.cliente = cliente;
            /*FondoImagen fondo = new FondoImagen("/Imagenes/default.png");
            fondo.setLayout(new javax.swing.GroupLayout(fondo)); // o cualquier layout que estés usando
            setContentPane(fondo);*/
            initComponents(); 
            configurarFecha();
            iniciarTimer(); 
            
            configurarUIPrincipal(nombreJugador);
            mostrarPanelDeEspera();
        }
        
        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
            if (this.cliente != null) {
                this.cliente.setJuegoRef(this);
            }
        }

        private void configurarUIPrincipal(String nombreJugador) {           
            jLabelNombreJugador.setText("Jugador: " + nombreJugador);
            
            panelDeJuego = new JPanel(new BorderLayout());
            panelDeJuego.setOpaque(false);
            
            // Usar un layout adecuado para el panel principal 'this'
            this.setLayout(new BorderLayout());
            
            JPanel panelSuperior = new JPanel(new GridBagLayout());
            panelSuperior.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.weightx = 1.0;

            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
            panelSuperior.add(jLabelNombreJugador, gbc);
            
            gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
            panelSuperior.add(jLabelNombreJuego, gbc);

            gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
            panelSuperior.add(jLabelFecha, gbc);

            gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_START;
            panelSuperior.add(jLabelPersonajePropio, gbc);

            gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
            panelSuperior.add(turnoLabel, gbc);
            
            gbc.gridx = 2; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
            panelSuperior.add(jLabelTimer, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
            panelSuperior.add(estadoLabel, gbc);

            this.add(panelSuperior, BorderLayout.NORTH);
            this.add(panelDeJuego, BorderLayout.CENTER);
        }

        public void iniciarJuegoConTablero(List<String> tableroServidor) {
            SwingUtilities.invokeLater(() -> {
                personajesDisponibles.clear();
                personajesDisponibles.addAll(tableroServidor);
                personajesDescartados.clear();
                
                Code.imagenestablero = new String[24];
                for (int i = 0; i < Math.min(24, tableroServidor.size()); i++) {
                    Code.imagenestablero[i] = "/JugadoresProyecto/" + tableroServidor.get(i) + ".png";
                }
                estadoLabel.setText("¡Partida lista! Selecciona tu personaje.");
                
                // Muestra el tablero de selección
                tablaorigin();
            });
        }
        
        public void mostrarMiPersonajeSeleccionado(String nombrePersonaje, String rutaImagen) {
            this.miPersonajeSeleccionado = nombrePersonaje;
            this.rutaImagenPersonajeSeleccionado = rutaImagen;
            
            SwingUtilities.invokeLater(() -> {
                jLabelPersonajePropio.setText("Tu personaje: " + nombrePersonaje);
            });

            if (panelTablero != null) {
                panelTablero.mostrarPersonajeSeleccionado(nombrePersonaje, rutaImagen);
            }
        }

        public void mostrarMiPersonajeSeleccionado(String nombrePersonaje) {
            String rutaImagen = "/JugadoresProyecto/" + nombrePersonaje + ".png";
            mostrarMiPersonajeSeleccionado(nombrePersonaje, rutaImagen);
        }
        
        public void descartarPersonaje(String nombrePersonaje) {
            if (!personajesDisponibles.contains(nombrePersonaje)) {
                return; // Ya esta descartado
            }
            
            if (nombrePersonaje.equals(miPersonajeSeleccionado)) {
                 JOptionPane.showMessageDialog(this, "No puedes descartar a tu propio personaje.", "Acción no válida", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            
            personajesDisponibles.remove(nombrePersonaje);
            personajesDescartados.add(nombrePersonaje);
            
            if (cliente != null) {
                cliente.descartarPersonaje(nombrePersonaje);
            }
            
            actualizarTableroVisual();
        }
        
        public void procesarDescarteServidor(String nombrePersonaje) {
            SwingUtilities.invokeLater(() -> {
                if (personajesDisponibles.contains(nombrePersonaje)) {
                    personajesDisponibles.remove(nombrePersonaje);
                    personajesDescartados.add(nombrePersonaje);
                    actualizarTableroVisual();
                }
            });
        }
        
        private void actualizarTableroVisual() {
            SwingUtilities.invokeLater(() -> {
                if (panelTablero != null) {
                    panelTablero.actualizarPersonajesDescartados(personajesDescartados);
                }
            });
        }

        private void iniciarTimer() {
            if (gameTimer != null) return;
            gameTimer = new java.util.Timer();
            gameTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    segundosTranscurridos++;
                    int minutos = segundosTranscurridos / 60;
                    int segundos = segundosTranscurridos % 60;
                    SwingUtilities.invokeLater(() -> 
                        jLabelTimer.setText(String.format("Tiempo: %02d:%02d", minutos, segundos))
                    );
                }
            }, 1000, 1000);
        }

        private void detenerTimer() {
            if (gameTimer != null) {
                gameTimer.cancel();
                gameTimer = null;
            }
        }

        private void mostrarPanelDeEspera() {
            panelDeJuego.removeAll();
            JLabel labelEspera = new JLabel("Buscando partida...", SwingConstants.CENTER);
            labelEspera.setFont(new Font("Arial", Font.BOLD, 24));
            panelDeJuego.add(labelEspera, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();
        }

        public void tablaorigin() {
            panelTablero = new PanelTablero(); 
            panelTablero.PTSelec(Code.imagenestablero);
            
            if (!listenersAgregados) {
                agregarListenersSeleccion();
                listenersAgregados = true;
            }
            
            panelDeJuego.removeAll();
            panelDeJuego.add(panelTablero, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();
        }

        public void tablarepite() {
            if (panelTablero == null) {
                panelTablero = new PanelTablero(); 
            }
            panelTablero.PTJuego(Code.imagenestablero);        

            panelDeJuego.removeAll();
            panelDeJuego.add(panelTablero, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();
        }

        public void cambiarATableroJuego() {
            if (miPersonajeSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un personaje primero.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tablarepite();
            if (rutaImagenPersonajeSeleccionado != null && panelTablero != null) {
                panelTablero.mostrarPersonajeSeleccionado(miPersonajeSeleccionado, rutaImagenPersonajeSeleccionado);
                panelTablero.setMiPersonajeSeleccionado(miPersonajeSeleccionado);
            }
        }

        public void actualizarEstadoJuego(boolean esMiTurno, String mensaje) {
            SwingUtilities.invokeLater(() -> {
                this.miTurno = esMiTurno; 
                estadoLabel.setText(mensaje);
                turnoLabel.setText(esMiTurno ? "¡ES TU TURNO!" : "Turno del oponente");
                turnoLabel.setForeground(esMiTurno ? new Color(0, 128, 0) : Color.RED);
                turnoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            });
        }
        
        private void agregarListenersSeleccion() {
            Random rand = new Random();

            PanelTablero.aleatorio.addActionListener(e -> {
                if (cliente.getTablero() != null && !cliente.getTablero().isEmpty()) {
                    List<String> tablero = cliente.getTablero();
                    int numale = rand.nextInt(tablero.size());
                    String personajeSeleccionado = tablero.get(numale);

                    cliente.seleccionarPersonaje(personajeSeleccionado);
                    
                    String rutaImagen = "/JugadoresProyecto/" + personajeSeleccionado + ".png";
                    mostrarMiPersonajeSeleccionado(personajeSeleccionado, rutaImagen);
                    
                    JOptionPane.showMessageDialog(this, "Has seleccionado aleatoriamente a: " + personajeSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(this, "El tablero aún no está listo.");
                }
            });

            PanelTablero.avanzar.addActionListener(e -> {
                if (getMiPersonajeSeleccionado() == null || getMiPersonajeSeleccionado().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes seleccionar un personaje antes de avanzar.", "Personaje requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                cambiarATableroJuego();
            });
        }

        public void mostrarPreguntaOponente(String pregunta) {
            SwingUtilities.invokeLater(() -> {
                int n = JOptionPane.showConfirmDialog(this,
                    pregunta, "Pregunta del Oponente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                String respuesta = (n == JOptionPane.YES_OPTION) ? "SI" : "NO";
                cliente.enviarRespuesta(respuesta);
            });
        }

        public void mostrarRespuestaOponente(String respuesta) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "El rival responde: " + respuesta, "Respuesta del oponente", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        
        public void manejarResultadoVictoria(boolean exito, String mensaje) {
            SwingUtilities.invokeLater(() -> {
                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡FELICIDADES! " + mensaje, "¡VICTORIA!", JOptionPane.INFORMATION_MESSAGE);
                    manejarFinJuego("VICTORIA");
                } else {
                    JOptionPane.showMessageDialog(this, "¡Incorrecto! " + mensaje, "Derrota", JOptionPane.ERROR_MESSAGE);
                    manejarFinJuego("DERROTA");
                }
            });
        }
        
        public void manejarFinJuego(String resultado) {
            detenerTimer();
            SwingUtilities.invokeLater(() -> {
                boolean victoria = false;
                String mensajeFinal = "";
                switch (resultado) {
                    case "VICTORIA":
                        victoria = true;
                        //mensajeFinal = "¡Has ganado la partida!";
                        break;
                    case "DERROTA":
                        victoria = false;
                        //mensajeFinal = "Has perdido. ¡Mejor suerte la próxima!";
                        break;
                    case "RIVAL_ABANDONA":
                        victoria = true;
                        mensajeFinal = "Tu rival ha abandonado la partida. ¡Has ganado!";
                        break;
                }
                mostrarPantallaFin(victoria, mensajeFinal);
            });
        }

        private void mostrarPantallaFin(boolean esVictoria, String mensaje) {
    // Obtener el JFrame padre
    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

    if (frame != null) {
        frame.getContentPane().removeAll(); // Eliminar todo el contenido de la ventana

        JPanel panelFinal = esVictoria ? new PantallaVictoria() : new PantallaDerrota();

        // Botón para volver al menú
        JButton menuButton = new JButton("Volver al Menú Principal");
        menuButton.addActionListener(e -> {
        MenuPrincipal.musica.pausar();//para que no se duplique el sonido
        frame.dispose(); // Cierra el JFrame actual

        // Crea un nuevo JFrame con el menú principal
        MenuPrincipal nuevoMenu = new MenuPrincipal();
        nuevoMenu.setVisible(true); // Muestra el nuevo menú con todo correctamente configurado
        });

        // Agrega el botón a la parte inferior del panel final
        panelFinal.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(menuButton);
        panelFinal.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panelFinal); // Agrega el panel final al frame
        frame.revalidate();
        frame.repaint();
    } else {
        System.err.println("No se encontró el JFrame padre.");
    }
}


        
        private void configurarFecha() {
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            jLabelFecha.setText(fechaActual.format(formatter));
        }
        
        public boolean esMiTurno() {
            return miTurno;
        }
        
        public void mostrarMensajeInfo(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
       
        public String getMiPersonajeSeleccionado() {
            return miPersonajeSeleccionado;
        }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        estadoLabel = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabelPersonajePropio = new javax.swing.JLabel();
        turnoLabel = new javax.swing.JLabel();
        jLabelNombreJuego = new javax.swing.JLabel();
        jLabelTimer = new javax.swing.JLabel();
        jLabelNombreJugador = new javax.swing.JLabel();

        estadoLabel.setText("Conectando al servidor...");

        jLabelFecha.setText("0/00/0000");

        jLabelPersonajePropio.setText("Aún no eliges");

        turnoLabel.setText("Esperando partida...");

        jLabelNombreJuego.setText("Adivina Quien Futchibol");

        jLabelTimer.setText("Tiempo: 00:00");

        jLabelNombreJugador.setText("Jugador: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(turnoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(estadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabelNombreJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(378, 378, 378)
                        .addComponent(jLabelNombreJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 303, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
            .addGroup(layout.createSequentialGroup()
                .addGap(232, 232, 232)
                .addComponent(jLabelPersonajePropio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabelNombreJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabelNombreJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(turnoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(estadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(62, 62, 62)
                .addComponent(jLabelPersonajePropio, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(420, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel estadoLabel;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelNombreJuego;
    private javax.swing.JLabel jLabelNombreJugador;
    private javax.swing.JLabel jLabelPersonajePropio;
    private javax.swing.JLabel jLabelTimer;
    private javax.swing.JLabel turnoLabel;
    // End of variables declaration//GEN-END:variables
}
