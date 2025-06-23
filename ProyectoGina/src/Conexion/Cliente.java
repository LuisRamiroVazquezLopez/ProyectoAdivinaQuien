/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author tuntu
 */
import JuegoGrafico.Juego;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;


public class Cliente implements Runnable {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Juego juegoRef;
    private List<String> tablero;
    private boolean miTurno = false;
    private int miJugador; // 1 o 2
    private boolean conectado = false;
    private String miPersonajeSeleccionado = null;
    private final String nombreJugador;

    public Cliente(String host, int port, Juego juego, String nombreJugador) {
        this.host = host;
        this.port = port;
        this.juegoRef = juego;
        this.nombreJugador = nombreJugador;
        //inicia la conexion
        new Thread(this).start();
    }
    
    public void setJuegoRef(Juego juego) {
        this.juegoRef = juego;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            conectado = true;
            
            // Enviar el nombre del jugador al servidor
            enviarMensaje("REGISTRAR_NOMBRE:" + this.nombreJugador);
            
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                procesarMensajeServidor(serverMessage);
            }
        } catch (IOException e) {
            //para notificar una desconexion
            conectado = false;
            if (juegoRef != null) {
                SwingUtilities.invokeLater(() -> {
                    juegoRef.mostrarMensajeInfo("Se ha perdido la conexión con el servidor.");
                    System.err.println("Conexion perdida: " + e.getMessage());
                });
            }
        } finally {
            close();
        }
    }
    
    private void procesarMensajeServidor(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            String[] partes = mensaje.split(":", 2);
            String comando = partes[0];
            String data = partes.length > 1 ? partes[1] : "";
            
            switch (comando) {
                case "NUMERO_JUGADOR":
                    // Recibir el numero de jugador asignado
                    miJugador = Integer.parseInt(data);
                    System.out.println("Soy el jugador " + miJugador);
                    break;
                    
                case "PARTIDA_EMPEZADA":
                    // El servidor envia el tablero
                    tablero = Arrays.asList(data.split(","));
                    if (juegoRef != null) {
                        juegoRef.iniciarJuegoConTablero(tablero);
                    }
                    break;
                    
                case "ACTUALIZAR_ESTADO":
                    // para ver 
                    String[] estadoPartes = data.split(":", 2);
                    miTurno = "T".equals(estadoPartes[0]);
                    String info = estadoPartes.length > 1 ? estadoPartes[1] : "";
                    
                    if (juegoRef != null) {
                        juegoRef.actualizarEstadoJuego(miTurno, info);
                    }
                    break;
                    
                case "PREGUNTA_RIVAL":
                    if (juegoRef != null) {
                        juegoRef.mostrarPreguntaOponente(data);
                    }
                    break;
                    
                case "RESPUESTA_RIVAL":
                    if (juegoRef != null) {
                        juegoRef.mostrarRespuestaOponente(data);
                    }
                    break;
                    
                case "PARTIDA_TERMINADA":
                    if (juegoRef != null) {
                        juegoRef.manejarFinJuego(data);
                    }
                    break;
                    
                case "SELECCION_CONFIRMADA":
                    // Confirmar que el servidor recibio lo que se elegio
                    miPersonajeSeleccionado = data;
                    if (juegoRef != null) {
                        System.out.println("Seleccion confirmada por el servidor: " + data);
                        // Mostrar la imagen del personaje seleccionado
                        juegoRef.mostrarMiPersonajeSeleccionado(data);
                    }
                    break;
                    
                case "RESULTADO_VICTORIA":
                    //guarda la victoria
                    String[] resultadoPartes = data.split(":", 2);
                    boolean exito = "true".equals(resultadoPartes[0]);
                    String mensajeResultado = resultadoPartes.length > 1 ? resultadoPartes[1] : "";
                    
                    if (juegoRef != null) {
                        juegoRef.manejarResultadoVictoria(exito, mensajeResultado);
                    }
                    break;
                    
                case "PERSONAJE_DESCARTADO":
                    // El servidor notifica que un personaje fue descartado
                    if (juegoRef != null) {
                        juegoRef.procesarDescarteServidor(data);
                    }
                    break;
                    
                case "INFO":
                    // Mensajes informativos del servidor
                    if (juegoRef != null) {
                        juegoRef.mostrarMensajeInfo(data);
                    }
                    break;
            }
        });
    }

    public void enviarMensaje(String message) {
        if (out != null && conectado) {
            out.println(message);
        }
    }
    
    public void seleccionarPersonaje(String personaje) {
        if (conectado) {
            miPersonajeSeleccionado = personaje;
            enviarMensaje("SELECCION_CHAR:" + personaje);
        }
    }
    
    public void enviarPregunta(String pregunta) {
        if (conectado && miTurno) {
            enviarMensaje("PREGUNTA:" + pregunta);
        }
    }
    
    public void enviarRespuesta(String respuesta) {
        if (conectado) {
            enviarMensaje("RESPUESTA:" + respuesta);
        }
    }
    
    public void enviarAdivinanza(String personaje) {
        if (conectado && miTurno) {
            enviarMensaje("ADIVINANZA:" + personaje);
        }
    }
    
    public void intentarGanar(String personaje) {
        if (conectado && miTurno) {
            enviarMensaje("INTENTO_GANAR:" + personaje);
        }
    }
    
    // Permitir descartar en cualquier momento (no solo en tu turno)
    public void descartarPersonaje(String personaje) {
        if (conectado) {
            enviarMensaje("DESCARTAR:" + personaje);
        }
    }

    public List<String> getTablero() {
        return tablero;
    }
    
    public boolean esMiTurno() {
        return miTurno;
    }
    
    public int getMiJugador() {
        return miJugador;
    }
    
    public boolean isConectado() {
        return conectado;
    }

    public String getMiPersonajeSeleccionado() {
        return miPersonajeSeleccionado;
    }

    public void close() {
        conectado = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getJuegoRef() {
        return juegoRef;
    }
}