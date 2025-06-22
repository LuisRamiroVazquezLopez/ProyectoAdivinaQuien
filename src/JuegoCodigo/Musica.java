/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JuegoCodigo;

import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author manue
 */
public class Musica {
    private Clip clip;
    private long posicionPausada = 0;
    public static boolean flag=true; //bandera para ver si la musica esta en reproduccion o no

    public void reproducir(String ruta) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.setMicrosecondPosition(posicionPausada);//se le pone donde se empieza a reproducir y no se reinicia el audio
            clip.start();//Inicia la repodruccion del sonido
        } catch (Exception e) {
            e.printStackTrace();
        }
        flag=false;
    }

    public void pausar() {
        if (clip != null && clip.isRunning()) {
            posicionPausada = clip.getMicrosecondPosition();//Para dspues reanudar la cancion justo donde se quedó
            clip.stop();//para el sonido
        }
    }

    public void continuar() {
        if (clip != null) {
            clip.setMicrosecondPosition(posicionPausada);//se le pone donde se empieza a reproducir
            clip.start();//se reproduce
        }
    }
    
    public boolean estaReproduciendo() {
        return clip != null && clip.isRunning();
    }
    
    public static String elegirCancion(){
        //Bloque de seleccion de musica (APORTACION)
        JFrame parent = new JFrame();
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV", "wav");//el primero es los archivos que aparecen y el segundo los archivos que se pueden seleccionar
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        File selectedFile = chooser.getSelectedFile();
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath(); // <- ruta completa
        }else{
            return "/Sonido/ReySound.wav";//musica por default en caso de fallar
         }//Fin del bloque de seleccionar musica (APORTACION)
    }   

}
