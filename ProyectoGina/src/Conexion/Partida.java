/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author tuntu
 */
import BaseDeDatos.ConexionBD;
import BaseDeDatos.PartidaBD;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


//logica de la partida
public class Partida implements Runnable {
    private final ManejadorJugador j1;
    private final ManejadorJugador j2;
    private String personajeOcultoJ1;
    private String personajeOcultoJ2;
    private ManejadorJugador jugadorEnTurno;
    private final List<String> tablero;
    private boolean juegoIniciado = false;
    private final long startTime;
    private LocalDateTime fechaInicio;
    private int duracion;

    public Partida(ManejadorJugador j1, ManejadorJugador j2) {
        this.j1 = j1;
        this.j2 = j2;
        this.tablero = generarTableroAleatorio();
        this.startTime = System.currentTimeMillis();
        this.fechaInicio = LocalDateTime.now(); //inicio de partida
    }

    @Override
    public void run() {
        try {
            // Esperar a que los nombres de los jugadores estén disponibles
            // Este bucle ahora funcionará, porque los hilos de ManejadorJugador ya están corriendo.
            while (j1.getNombreJugador() == null || j2.getNombreJugador() == null) {
                Thread.sleep(100); 
            }

            // asigna los numeros del jugador
            j1.enviarMensaje("NUMERO_JUGADOR:1");
            j2.enviarMensaje("NUMERO_JUGADOR:2");
            
            //notifica que la partida empezo
            String tableroStr = String.join(",", tablero);
            j1.enviarMensaje("PARTIDA_EMPEZADA:" + tableroStr);
            j2.enviarMensaje("PARTIDA_EMPEZADA:" + tableroStr);


        } catch (Exception e) {
            System.err.println("Error al iniciar la partida: " + e.getMessage());
        }
    }

    
    public synchronized void procesarMensaje(String mensaje, ManejadorJugador remitente) {
        String[] partes = mensaje.split(":", 2);
        String comando = partes[0];
        String data = partes.length > 1 ? partes[1] : "";

        if (comando.equals("SELECCION_CHAR")) {
            if (remitente == j1) {
                personajeOcultoJ1 = data;
                System.out.println("Jugador 1 selecciono: " + data);
                j1.enviarMensaje("SELECCION_CONFIRMADA:" + data);
            } else {
                personajeOcultoJ2 = data;
                System.out.println("Jugador 2 selecciono: " + data);
                j2.enviarMensaje("SELECCION_CONFIRMADA:" + data);
            }

            // Si los dos estan listos se asigna el turno y se notifica para empezar a jugar
            if (personajeOcultoJ1 != null && personajeOcultoJ2 != null && !juegoIniciado) {
                juegoIniciado = true;
                jugadorEnTurno = j1; // El jugador 1 siempre empieza
                actualizarEstadoJuego("Los dos jugadores listos! Va el Jugador 1.");
            }
        } else if (comando.equals("PREGUNTA")) {
            if (remitente == jugadorEnTurno) {
                ManejadorJugador oponente = (remitente == j1) ? j2 : j1;
                oponente.enviarMensaje("PREGUNTA_RIVAL:" + data);
            } else {
                remitente.enviarMensaje("INFO:No es tu turno para hacer preguntas.");
            }
        } else if (comando.equals("RESPUESTA")) {
            // la respuesta se reenvia al jugador que pregunto
            ManejadorJugador oponente = (remitente == j1) ? j2 : j1;
            oponente.enviarMensaje("RESPUESTA_RIVAL:" + data);
            
            // cambiar de turno
            cambiarTurno();
            String nombreTurno = (jugadorEnTurno == j1) ? "Jugador 1" : "Jugador 2";
            actualizarEstadoJuego("Respuesta recibida. Ahora es el turno del " + nombreTurno + ".");
        } else if (comando.equals("ADIVINANZA") || comando.equals("INTENTO_GANAR")) {
            // solo permitir adivinanzas en el turno del jugador
            if (remitente == jugadorEnTurno) {
                ManejadorJugador oponente = (remitente == j1) ? j2 : j1;
                String personajeCorrecto = (remitente == j1) ? personajeOcultoJ2 : personajeOcultoJ1;

                if(data.equalsIgnoreCase(personajeCorrecto)) {
                    remitente.enviarMensaje("PARTIDA_TERMINADA:VICTORIA");
                    oponente.enviarMensaje("PARTIDA_TERMINADA:DERROTA");
                    guardarBD(remitente.getNombreJugador(), personajeCorrecto);
                } else {
                    remitente.enviarMensaje("INFO:Adivinanza incorrecta.");
                    cambiarTurno();
                    String nombreTurno = (jugadorEnTurno == j1) ? "Jugador 1" : "Jugador 2";
                    actualizarEstadoJuego("Adivinanza fallida. Turno del " + nombreTurno + ".");
                }
            } else {
                remitente.enviarMensaje("INFO:Solo puedes intentar ganar en tu turno.");
            }
        } else if (comando.equals("DESCARTAR")) {
            // permite descartar en cualquier momento
            ManejadorJugador oponente = (remitente == j1) ? j2 : j1;
            
            // Confirmar al jugador que descarto
            remitente.enviarMensaje("INFO:Personaje " + data + " descartado.");
            
            System.out.println("Personaje descartado: " + data + " por " + 
                             (remitente == j1 ? "Jugador 1" : "Jugador 2"));
        }
    }
    
    private void cambiarTurno() {
        jugadorEnTurno = (jugadorEnTurno == j1) ? j2 : j1;
    }

    private void actualizarEstadoJuego(String info) {
        String turnoStrJ1 = (jugadorEnTurno == j1) ? "T" : "F";
        String turnoStrJ2 = (jugadorEnTurno == j2) ? "T" : "F";

        j1.enviarMensaje("ACTUALIZAR_ESTADO:" + turnoStrJ1 + ":" + info);
        j2.enviarMensaje("ACTUALIZAR_ESTADO:" + turnoStrJ2 + ":" + info);
    }
    
    public void jugadorDesconectado(ManejadorJugador desconectado) {
        ManejadorJugador restante = (desconectado == j1 && j2 != null) ? j2 : j1;
        if(restante != null && restante.getSocket() != null && !restante.getSocket().isClosed()) {
             restante.enviarMensaje("PARTIDA_TERMINADA:RIVAL_ABANDONA");
             this.guardarBD(restante.getNombreJugador(), "Abandono");
        }
        System.out.println("Un jugador se ha desconectado. Finalizando partida.");
    }

    private void guardarBD(String ganador, String personajeGanador) {
        long endTime = System.currentTimeMillis();
        int duracionSegundos = (int) ((endTime - startTime) / 1000);

        PartidaBD partidaBD = new PartidaBD();
        partidaBD.setJugador1(j1.getNombreJugador());
        partidaBD.setJugador2(j2.getNombreJugador());
        partidaBD.setGanador(ganador);
        partidaBD.setPersonajeGanador(personajeGanador);
        partidaBD.setDuracion(duracionSegundos);
        
        boolean exito = ConexionBD.insertarPartida(partidaBD);
        if (exito) {
            System.out.println("Partida guardada en la base de datos.");
        } else {
            System.err.println("Error al guardar la partida en la base de datos.");
        }
    }
    
    private List<String> generarTableroAleatorio() {
        List<String> personajes = Arrays.asList(
            "Andres Iniesta", "Cristiano Ronaldo", "Messi", "NeymarJR",
            "Mbappe", "Hugo Sanchez", "Ronaldinho", "Zidane",
            "Maradona", "Modric", "Xavi Hernandez", "Pele",
            "Ronaldo Nazario", "Thierry Henry", "Puyol", "Roberto Carlos",
            "Cannavaro", "Gullit P", "Jorge Campos", "Giovani Dos Santos",
            "Carlos Vela", "Johan Cruyff", "Hector Herrera", "Sergio Ramos"
        );
        
        List<String> personajesAleatorios = new ArrayList<>(personajes);
        Collections.shuffle(personajesAleatorios, new Random());
        
        return personajesAleatorios.subList(0, 24);
    }
}