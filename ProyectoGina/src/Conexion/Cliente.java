/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author tuntu
 */
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> tablero;
    private boolean conectado = false;
    private String miTurno = "";
    private String miJugador = ""; // "JUGADOR1" o "JUGADOR2"
    private JuegoGrafico.Juego juegoRef; // Referencia para actualizar la UI

    public Cliente(String direccionServidor, int puerto, JuegoGrafico.Juego juegoRef) {
        this.juegoRef = juegoRef;
        conectarAServidor(direccionServidor, puerto);
    }

    private void conectarAServidor(String direccionServidor, int puerto) {
        int intentos = 0;
        while (intentos < 3 && !conectado) {
            try {
                socket = new Socket(direccionServidor, puerto);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                conectado = true;

                // Recibir configuración inicial del servidor
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.startsWith("TABLERO:")) {
                        tablero = Arrays.asList(mensaje.substring(8).split(","));
                        System.out.println("Tablero recibido: " + tablero);
                    } else if (mensaje.startsWith("TURNO:")) {
                        miJugador = mensaje.substring(6);
                        actualizarInterfazTurno();
                    }

                    if (tablero != null && !miJugador.isEmpty()) {
                        break; // Configuración inicial completa
                    }
                }

                new Thread(this::escucharServidor).start();

            } catch (IOException e) {
                intentos++;
                System.err.println("Intento " + intentos + " fallido. Error: " + e.getMessage());
                if (intentos == 3) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, 
                            "No se pudo conectar al servidor.\nError: " + e.getMessage(),
                            "Error de conexión",
                            JOptionPane.ERROR_MESSAGE);
                    });
                }
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
    }

    private void escucharServidor() {
        try {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Mensaje del servidor: " + mensaje);

                if (mensaje.startsWith("TURNO:")) {
                    miTurno = mensaje.substring(6);
                    actualizarInterfazTurno();
                } 
                else if (mensaje.equals("OPONENTE_DESCONECTADO")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                            "El oponente se ha desconectado.\nPartida terminada.",
                            "Desconexión",
                            JOptionPane.WARNING_MESSAGE);
                        volverAlMenuPrincipal();
                    });
                }
                else if (mensaje.startsWith("PREGUNTA:")) {
                    String pregunta = mensaje.substring(9);
                    mostrarPreguntaOponente(pregunta);
                }
                else if (mensaje.startsWith("RESPUESTA:")) {
                    String respuesta = mensaje.substring(9);
                    JOptionPane.showMessageDialog(null, 
                        "El oponente respondió: " + respuesta,
                        "Respuesta recibida", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                else if (mensaje.startsWith("RESULTADO_ADIVINANZA:")) {
                    String resultado = mensaje.substring(22);
                    if (resultado.equalsIgnoreCase("CORRECTA")) {
                        JOptionPane.showMessageDialog(null,
                            "¡Adivinaste el personaje del oponente!\nGanaste la partida.",
                            "¡Ganador!", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "Fallaste al adivinar el personaje.",
                            "Sigue jugando", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    private void actualizarInterfazTurno() {
        SwingUtilities.invokeLater(() -> {
            if (juegoRef != null) {
                juegoRef.actualizarEstadoTurno(miTurno.equals(miJugador));
            }
        });
    }

    private void mostrarPreguntaOponente(String pregunta) {
        SwingUtilities.invokeLater(() -> {
            int respuesta = JOptionPane.showConfirmDialog(null,
                "El oponente pregunta: " + pregunta,
                "Responde la pregunta",
                JOptionPane.YES_NO_OPTION);

            enviarMensaje("RESPUESTA:" + (respuesta == JOptionPane.YES_OPTION ? "SI" : "NO"));
        });
    }

    private void volverAlMenuPrincipal() {
        // Aquí podrías cambiar el contenido del JFrame o reiniciar el juego
    }

    public void enviarPregunta(String pregunta) {
        if (miTurno.equals(miJugador)) {
            enviarMensaje("PREGUNTA:" + pregunta);
        }
    }

    public void enviarAdivinanza(String personaje) {
        enviarMensaje("ADIVINANZA:" + personaje);
    }

    public void enviarMensaje(String mensaje) {
        if (conectado) {
            out.println(mensaje);
        }
    }

    public void cerrarConexion() {
        try {
            if (conectado) {
                enviarMensaje("DESCONEXION");
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
                conectado = false;
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    public List<String> getTablero() {
        return tablero;
    }

    public boolean esMiTurno() {
        return miTurno.equals(miJugador);
    }
}
