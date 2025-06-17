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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Servidor {
    private static final int PUERTO = 8080;
    private static final List<PrintWriter> clientes = Collections.synchronizedList(new ArrayList<>());
    private static List<String> tableroCompartido;
    private static boolean turnoJugador1 = true;
    private static final String[] personajesOcultos = new String[2]; // indice 0 del JUGADOR1 y 1 para el 2

    public static void main(String[] args) {
        tableroCompartido = generarTableroAleatorio();
        System.out.println("Tablero generado: " + tableroCompartido);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando jugadores...");

            while (clientes.size() < 2) {
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientes) {
                        clientes.add(out);
                    }

                out.println("TABLERO:" + String.join(",", tableroCompartido));
                out.println("TURNO:" + (clientes.size() == 1 ? "JUGADOR1" : "JUGADOR2"));

                new Thread(new ManejadorCliente(socket, out, clientes.size())).start();
            }
        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        }
    }

    private static List<String> generarTableroAleatorio() {
        List<String> personajes = Arrays.asList(
            "Andres Iniesta", "Cristiano Ronaldo", "Messi", "NeymarJR",
            "Mbappe", "Hugo Sanchez", "Ronaldinho", "Zidane",
            "Maradona", "Modric", "Xavi Hernandez", "Pele",
            "Ronaldo Nazario", "Thierry Henry", "Puyol", "Roberto Carlos",
            "Cannavaro", "Gullit P", "Jorge Campos", "Giovani Dos Santos",
            "Carlos Vela", "Johan Cruyff", "Hector Herrera", "Sergio Ramos",
            "Rafa Marquez", "Harry Kane", "Roberto Baggio", "Zidane",
            "Lamine Yamal", "Marcelo Vieira", "Kevin De Bruyne", "Drogba",
            "Gerson Oliveira", "Javier Hernandez"
        );
        Collections.shuffle(personajes);
        return new ArrayList<>(personajes.subList(0, 24));
    }

    private static class ManejadorCliente implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final int idJugador; // 1 o 2
        private BufferedReader in;

        public ManejadorCliente(Socket socket, PrintWriter out, int idJugador) {
            this.socket = socket;
            this.out = out;
            this.idJugador = idJugador;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.equals("DESCONEXION")) {
                        break;
                    }
                    System.out.println("Mensaje del Jugador " + idJugador + ": " + mensaje);
                    procesarMensaje(mensaje);
                }
            } catch (IOException e) {
                System.err.println("Jugador " + idJugador + " desconectado abruptamente.");
            } finally {
                cerrarConexion();
            }
        }

        private void procesarMensaje(String mensaje) {
            if (mensaje.startsWith("PERSONAJE_OCULTO:")) {
                String personaje = mensaje.substring(17);
                personajesOcultos[idJugador - 1] = personaje;
                System.out.println("Jugador " + idJugador + " eligió personaje oculto: " + personaje);
            } else if (mensaje.startsWith("PREGUNTA:")) {
                broadcast("PREGUNTA:" + mensaje.substring(9), out);
                cambiarTurno();
            } else if (mensaje.startsWith("ADIVINANZA:")) {
                String intento = mensaje.substring(11);
                int oponente = (idJugador == 1) ? 2 : 1;
                String personajeReal = personajesOcultos[oponente - 1];

                String resultado = intento.equalsIgnoreCase(personajeReal) ? "CORRECTA" : "INCORRECTA";

                // se envia el resultado al jugador que hizo la adivinanza
                out.println("RESULTADO_ADIVINANZA:" + resultado);

                // notifica al otro
                PrintWriter oponenteWriter = clientes.get(oponente - 1);
                if (oponenteWriter != null) {
                    oponenteWriter.println("ADIVINANZA:" + intento);
                    oponenteWriter.println("RESULTADO_ADIVINANZA:" + resultado);
                }
            } else if (mensaje.startsWith("RESPUESTA:")) {
                broadcast("RESPUESTA:" + mensaje.substring(9), out);
            }
        }

        private void cambiarTurno() {
            turnoJugador1 = !turnoJugador1;
            broadcast("TURNO:" + (turnoJugador1 ? "JUGADOR1" : "JUGADOR2"), null);
        }

        private void cerrarConexion() {
            try {
                if (in != null) in.close();
                if (out != null) {
                    clientes.remove(out);
                    out.close();
                }
                if (socket != null) socket.close();
                broadcast("OPONENTE_DESCONECTADO", null);
            } catch (IOException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    private static void broadcast(String mensaje, PrintWriter sender) {
        synchronized (clientes) {
            for (PrintWriter writer : clientes) {
                if (writer != sender) {
                    writer.println(mensaje);
                }
            }
        }
    }
}
