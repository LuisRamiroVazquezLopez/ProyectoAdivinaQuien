/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author tuntu
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ManejadorJugador implements Runnable {
    private final Socket socket;
    private Partida partida;
    private PrintWriter out;
    private BufferedReader in;
    private String nombreJugador;

    public ManejadorJugador(Socket socket) {
        this.socket = socket;
    }
    
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // El primer mensaje debe ser el nombre del jugador
            String primerMensaje = in.readLine();
            if (primerMensaje != null && primerMensaje.startsWith("REGISTRAR_NOMBRE:")) {
                this.nombreJugador = primerMensaje.substring("REGISTRAR_NOMBRE:".length());
                System.out.println("Nombre registrado: " + this.nombreJugador);
            }

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (partida != null) {
                    partida.procesarMensaje(mensaje, this);
                }
            }
        } catch (IOException e) {
            System.out.println("Jugador desconectado: " + e.getMessage());
        } finally {
            if (partida != null) {
                partida.jugadorDesconectado(this);
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
}