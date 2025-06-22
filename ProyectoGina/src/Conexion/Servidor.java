/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author tuntu
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Acepta conexiones de clientes y los agrupa en partidas de dos
public class Servidor {
    private static final int PUERTO = 5000;
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private static final List<ManejadorJugador> jugadoresEnEspera = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Servidor iniciado en el puerto " + PUERTO + "...");
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                ManejadorJugador jugador = new ManejadorJugador(socketCliente);

                synchronized (jugadoresEnEspera) {
                    jugadoresEnEspera.add(jugador);
                    System.out.println("Jugador conectado. Jugadores en espera: " + jugadoresEnEspera.size());

                    if (jugadoresEnEspera.size() == 2) {
                        ManejadorJugador j1 = jugadoresEnEspera.remove(0);
                        ManejadorJugador j2 = jugadoresEnEspera.remove(0);
                        Partida partida = new Partida(j1, j2);
                        j1.setPartida(partida);
                        j2.setPartida(partida);
                        pool.execute(partida);
                        System.out.println("Dos jugadores emparejados. Iniciando partida.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}