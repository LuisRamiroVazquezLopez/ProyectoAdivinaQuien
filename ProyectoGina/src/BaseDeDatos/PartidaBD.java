/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDeDatos;

import Conexion.Cliente;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Aorus
 */
public class PartidaBD {

    public PartidaBD() {
        this.fecha = LocalDateTime.now();   //fecha actual
    }

    public PartidaBD(int id, String jugador1, String jugador2, String ganador, String personajeGanador, LocalDateTime fecha, int duracion) {
        this.id = id;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ganador = ganador;
        this.personajeGanador = personajeGanador;
        this.fecha = fecha;
        this.duracion = duracion;
    }
    
    
    private int id;
    private String jugador1;
    private String jugador2;
    private String ganador;
    private String personajeGanador;
    private LocalDateTime fecha;
    private int duracion;
    
    
    //para pasar la fecha a la base de datos
    public Timestamp getFechaSQL() {
        return Timestamp.valueOf(fecha);
    }

    public void setFechaSQL(Timestamp timestamp) {
        this.fecha = timestamp.toLocalDateTime();
    }
    
    //para obtener los nombres de los jugadores

    public String getJugador1() {
        return jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getPersonajeGanador() {
        return personajeGanador;
    }

    public void setPersonajeGanador(String personajeGanador) {
        this.personajeGanador = personajeGanador;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    
    
    
}
