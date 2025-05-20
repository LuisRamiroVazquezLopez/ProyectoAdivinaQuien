/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JuegoCodigo;

import javax.sound.sampled.*;

/**
 *
 * @author manue
 */
public class Musica {
    private Clip clip;
    private long posicionPausada = 0;

    public void reproducir(String ruta) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(getClass().getResource(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.setMicrosecondPosition(posicionPausada);//se le pone donde se empieza a reproducir y no se reinicia el audio
            clip.start();//Inicia la repodruccion del sonido
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
