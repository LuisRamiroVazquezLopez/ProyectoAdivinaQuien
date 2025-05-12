/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JuegoCodigo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author manue
 */
public class Code {
    public static List<String> imagenes=new ArrayList<>();//Lista para guardar las 34 imagenes
    public static String[] imagenestablero=new String[24];//Array para los 24 jugasores con los que se juega
    
    //Funcion para cargar las imagenes de manera manual
    public void cargarImagenes(){
        imagenes.add("/JugadoresProyecto/Andres Iniesta.png");
        imagenes.add("/JugadoresProyecto/Cannavaro.png");
        imagenes.add("/JugadoresProyecto/Carlos Vela.png");
        imagenes.add("/JugadoresProyecto/Cristiano Ronaldo.png");
        imagenes.add("/JugadoresProyecto/Drogba.png");
        imagenes.add("/JugadoresProyecto/Gerson Oliveira.png");
        imagenes.add("/JugadoresProyecto/Giovanni Dos Santos.png");
        imagenes.add("/JugadoresProyecto/Gullit P.png");
        imagenes.add("/JugadoresProyecto/Harry Kane.png");
        imagenes.add("/JugadoresProyecto/Hector Herrera.png");
        imagenes.add("/JugadoresProyecto/Hugo Sanchez.png");
        imagenes.add("/JugadoresProyecto/Javier Hernandez.png");
        imagenes.add("/JugadoresProyecto/Johan Cruyff.png");
        imagenes.add("/JugadoresProyecto/Jorge Campos.png");
        imagenes.add("/JugadoresProyecto/Kevin De Bruyne.png");
        imagenes.add("/JugadoresProyecto/Lamine Yamal.png");
        imagenes.add("/JugadoresProyecto/Maradona.png");
        imagenes.add("/JugadoresProyecto/Marcelo Vieira.png");
        imagenes.add("/JugadoresProyecto/Mbappe.png");
        imagenes.add("/JugadoresProyecto/Messi.png");
        imagenes.add("/JugadoresProyecto/Modric.png");
        imagenes.add("/JugadoresProyecto/NeymarJR.png");
        imagenes.add("/JugadoresProyecto/Pele.png");
        imagenes.add("/JugadoresProyecto/Puyol.png");
        imagenes.add("/JugadoresProyecto/Rafa Marquez.png");
        imagenes.add("/JugadoresProyecto/Roberto Baggio.png");
        imagenes.add("/JugadoresProyecto/Roberto Carlos.png");
        imagenes.add("/JugadoresProyecto/Ronaldinho.png");
        imagenes.add("/JugadoresProyecto/Ronaldo Nazario.png");
        imagenes.add("/JugadoresProyecto/Sergio Ramos.png");
        imagenes.add("/JugadoresProyecto/Thiago Silva.png");
        imagenes.add("/JugadoresProyecto/Thierry Henry.png");
        imagenes.add("/JugadoresProyecto/Xavi Hernandez.png");
        imagenes.add("/JugadoresProyecto/Zidane.png");
    }
    
    //Funcion para llenar el vector
    public void llenarVector(){
        Collections.shuffle(imagenes);//se hace un acomodo aleatorio de la lista
        for(int i=0;i<24;i++){
            imagenestablero[i]=imagenes.get(i);//Se guarda el jugador de la posicion i de la lista en la posicion i del vector
        }
    }
}


