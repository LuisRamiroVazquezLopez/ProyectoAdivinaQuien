/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package JuegoGrafico;
import JuegoCodigo.Code;
import JuegoCodigo.PanelTablero;
import Conexion.Cliente;
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
    public class Juego extends javax.swing.JPanel {
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

        public Juego(boolean esHost, Cliente cliente, String nombreJugador) {
            this.cliente = cliente;
            initComponents(); 
            configurarFecha();
            iniciarTimer(); 
            
            configurarUIPrincipal(nombreJugador);

            if (cliente != null) {
                cliente.setJuegoRef(this);
            }

            if (esHost) {
                tablaorigin();
            } else {
                mostrarPanelDeEspera();
            }
        }

        private void configurarUIPrincipal(String nombreJugador) {           
            jLabelNombreJugador.setText("Jugador: " + nombreJugador);
            
            panelDeJuego = new JPanel(new BorderLayout());
            
            GroupLayout layout = (GroupLayout) this.getLayout();
            
            this.setLayout(new BorderLayout());
            
            JPanel panelSuperior = new JPanel();
            panelSuperior.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
            panelSuperior.add(estadoLabel, gbc);
            
            gbc.gridx = 1;
            panelSuperior.add(turnoLabel, gbc);
            
            gbc.gridx = 2;
            panelSuperior.add(jLabelFecha, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
            panelSuperior.add(jLabelNombreJuego, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
            panelSuperior.add(jLabelNombreJugador, gbc);
            
            gbc.gridx = 1;
            panelSuperior.add(jLabelPersonajePropio, gbc);
            
            gbc.gridx = 2;
            panelSuperior.add(jLabelTimer, gbc);
            
            this.add(panelSuperior, BorderLayout.NORTH);
            this.add(panelDeJuego, BorderLayout.CENTER);
        }

        public void iniciarJuegoConTablero(List<String> tableroServidor) {
            SwingUtilities.invokeLater(() -> {
                // Copiar la lista del servidor a nuestras variables locales
                personajesDisponibles.clear();
                personajesDisponibles.addAll(tableroServidor);
                personajesDescartados.clear();
                
                Code.imagenestablero = new String[24];
                for (int i = 0; i < Math.min(24, tableroServidor.size()); i++) {
                    Code.imagenestablero[i] = "/JugadoresProyecto/" + tableroServidor.get(i) + ".png";
                }
                estadoLabel.setText("¡Partida lista! Selecciona tu personaje.");
            });
        }
        
        public void mostrarMiPersonajeSeleccionado(String nombrePersonaje, String rutaImagen) {
            this.miPersonajeSeleccionado = nombrePersonaje;
            this.rutaImagenPersonajeSeleccionado = rutaImagen;
            
            SwingUtilities.invokeLater(() -> {
                jLabelPersonajePropio.setText("Tu personaje: " + nombrePersonaje);
            });

            // Si ya estás en el panel de tablero, actualizar la visualización
            if (panelTablero != null) {
                panelTablero.mostrarPersonajeSeleccionado(nombrePersonaje, rutaImagen);
            }
        }

        public void mostrarMiPersonajeSeleccionado(String nombrePersonaje) {
            String rutaImagen = "/JugadoresProyecto/" + nombrePersonaje + ".png";
            mostrarMiPersonajeSeleccionado(nombrePersonaje, rutaImagen);
        }
        
        public void seleccionarPersonajeAleatorio() {
            if (personajesDisponibles == null || personajesDisponibles.isEmpty()) {
                // Si no hay personajes disponibles del servidor, usar tablero local
                if (cliente != null && cliente.getTablero() != null && !cliente.getTablero().isEmpty()) {
                    List<String> tablero = cliente.getTablero();
                    Random rand = new Random();
                    int indiceAleatorio = rand.nextInt(tablero.size());
                    String personajeSeleccionado = tablero.get(indiceAleatorio);
                    
                    // Enviar la selección al servidor y actualizar la UI local
                    cliente.seleccionarPersonaje(personajeSeleccionado);
                    mostrarMiPersonajeSeleccionado(personajeSeleccionado);
                    JOptionPane.showMessageDialog(this, "Has seleccionado aleatoriamente a: " + personajeSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay personajes disponibles para seleccionar.");
                }
                return;
            }
            
            Random rand = new Random();
            int indiceAleatorio = rand.nextInt(personajesDisponibles.size());
            String personajeSeleccionado = personajesDisponibles.get(indiceAleatorio);
            
            // Enviar la selección al servidor y actualizar la UI local
            if (cliente != null) {
                cliente.seleccionarPersonaje(personajeSeleccionado);
            }
            mostrarMiPersonajeSeleccionado(personajeSeleccionado);
            JOptionPane.showMessageDialog(this, "Has seleccionado aleatoriamente a: " + personajeSeleccionado);
        }
        
        public void descartarPersonaje(String nombrePersonaje) {
            if (!personajesDisponibles.contains(nombrePersonaje)) {
                return; // Ya esta descartado
            }
            
            // Verificar si es mi personaje seleccionado
            if (nombrePersonaje.equals(miPersonajeSeleccionado)) {
                // Solo permitir intento de victoria si es mi turno
                if (miTurno) {
                    int respuesta = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que el oponente eligió a " + nombrePersonaje + "?\n" +
                        "Si estás correcto, ¡GANAS! Si te equivocas, PIERDES.",
                        "¡Intento de Victoria!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (respuesta == JOptionPane.YES_OPTION) {
                        // Enviar intento de victoria al servidor
                        if (cliente != null) {
                            cliente.intentarGanar(nombrePersonaje);
                        }
                        return;
                    }
                } else {
                    // No es mi turno solo mostrar advertencia pero permitir descartar
                    int respuesta = JOptionPane.showConfirmDialog(this,
                        "Estás a punto de descartar tu propio personaje (" + nombrePersonaje + ").\n" +
                        "No es tu turno, así que no puedes intentar ganar ahora.\n" +
                        "¿Estás seguro de que quieres descartarlo?",
                        "Advertencia - Descartando tu personaje",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (respuesta != JOptionPane.YES_OPTION) {
                        return; // Cancelar el descarte
                    }
                }
            }
            
            personajesDisponibles.remove(nombrePersonaje);
            personajesDescartados.add(nombrePersonaje);
            
            // evia el descarte al servidor
            if (!nombrePersonaje.equals(miPersonajeSeleccionado) && cliente != null) {
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
                // recrea el tablero con los personajes actualizados
                if (panelTablero != null) {
                    panelTablero.actualizarPersonajesDescartados(personajesDescartados);
                }
            });
        }

        public void setMiTurno(boolean esMiTurno) {
            this.miTurno = esMiTurno;
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
            JLabel labelEspera = new JLabel("Esperando a otro jugador...", SwingConstants.CENTER);
            labelEspera.setFont(new Font("Arial", Font.BOLD, 18));
            panelDeJuego.add(labelEspera, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();
        }

        public void tablaorigin() {
            Code code = new Code();
            code.cargarImagenes();
            code.llenarVector();
            
            if (Code.imagenestablero == null || Code.imagenestablero.length == 0) {
                System.err.println("Error: Code.imagenestablero esta vacio o es null");
                // Crear un tablero de prueba si no existe
                Code.imagenestablero = new String[24];
                for (int i = 0; i < 24; i++) {
                    Code.imagenestablero[i] = "/JugadoresProyecto/default.png"; // Imagen por defecto
                }
            }

            panelTablero = new PanelTablero(); 
            panelTablero.PTSelec(Code.imagenestablero);
            panelDeJuego.removeAll();
            panelDeJuego.add(panelTablero, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();

            this.revalidate();
            this.repaint();
        }

        public void tablarepite() {
            if (Code.imagenestablero == null || Code.imagenestablero.length == 0) {
                System.err.println("Error: Code.imagenestablero no está disponible para el juego");
                return;
            }
            
            if (panelTablero == null) {
                panelTablero = new PanelTablero(); 
            }
            panelTablero.PTJuego(Code.imagenestablero);        

            panelDeJuego.removeAll();
            panelDeJuego.add(panelTablero, BorderLayout.CENTER);
            panelDeJuego.revalidate();
            panelDeJuego.repaint();
            this.revalidate();
            this.repaint();
        }

        public void cambiarATableroJuego() {
            if (miPersonajeSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un personaje primero.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // cambiar al panel de juego
            tablarepite();

            // mantiene el personaje seleccionado visible en el nuevo panel
            if (rutaImagenPersonajeSeleccionado != null && panelTablero != null) {
                panelTablero.mostrarPersonajeSeleccionado(miPersonajeSeleccionado, rutaImagenPersonajeSeleccionado);
                panelTablero.setMiPersonajeSeleccionado(miPersonajeSeleccionado);
            }
        }

        public void actualizarEstadoJuego(boolean miTurno, String mensaje) {
            SwingUtilities.invokeLater(() -> {
                this.miTurno = miTurno; 
                estadoLabel.setText(mensaje);
                turnoLabel.setText(miTurno ? "¡ES TU TURNO!" : "Turno del oponente");
                turnoLabel.setForeground(miTurno ? new Color(0, 128, 0) : Color.RED);
            });
        }

        public void mostrarPreguntaOponente(String pregunta) {
            SwingUtilities.invokeLater(() -> {
                int n = JOptionPane.showConfirmDialog(this,
                    pregunta, "Pregunta del Oponente", JOptionPane.YES_NO_OPTION);
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
                switch (resultado) {
                    case "VICTORIA":
                        victoria = true;
                        break;
                    case "DERROTA":
                        victoria = false;
                        break;
                    case "RIVAL_ABANDONA":
                        victoria = true;
                        break;
                }
                mostrarPantallaFin(victoria);
            });
        }

        private void mostrarPantallaFin(boolean esVictoria) {
            this.removeAll();
            this.setLayout(new BorderLayout());

            JInternalFrame pantallaFinal = esVictoria ? new PantallaVictoria() : new PantallaDerrota();
            this.add(pantallaFinal.getContentPane(), BorderLayout.CENTER);

            this.revalidate();
            this.repaint();
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

            System.out.println("INFO: " + mensaje);
        }//temporal lo tengo que arreglar pero ya me dio hueva o nose
       
        public String getMiPersonajeSeleccionado() {
            return miPersonajeSeleccionado;
        }
        
        public List<String> getPersonajesDisponibles() {
            return new ArrayList<>(personajesDisponibles);
        }
        
        public List<String> getPersonajesDescartados() {
            return new ArrayList<>(personajesDescartados);
        }

        public String getRutaImagenPersonajeSeleccionado() {
            return rutaImagenPersonajeSeleccionado;
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
