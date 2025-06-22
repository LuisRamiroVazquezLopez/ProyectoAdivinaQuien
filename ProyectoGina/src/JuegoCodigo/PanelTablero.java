package JuegoCodigo;

import JuegoGrafico.Juego;
import java.awt.*;
import javax.swing.*;

public class PanelTablero extends JPanel {

    private JButton[] botones = new JButton[24]; // Los 24 botones
    public static JButton aleatorio = new JButton("Personaje aleatorio"); // botón aleatorio
    public static JButton avanzar = new JButton("Avanzar");//boton para pasar al juego ya pa jugar

    public void PTSelec(String[] rutasImagenes) {//Funcion para imprimir el tablero de la seleccion de personaje conforme a lo que se necesita en este
        setLayout(new BorderLayout()); // Layout general del panel principal

        // Panel para el botón aleatorio (centrado arriba)
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);

        avanzar.setPreferredSize(new Dimension(200, 80));
        aleatorio.setPreferredSize(new Dimension(200, 80));

        panelSur.add(avanzar, BorderLayout.WEST);
        panelSur.add(aleatorio, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        // Panel para los 24 botones de personajes
        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 120, 8));
        panelBotones.setOpaque(false); // ← Hacer transparente
        for (int i = 0; i < 24; i++) {
            JButton boton = new JButton();

            //se le quita el fondo a los botones
            boton.setContentAreaFilled(false); // Elimina el fondo del botón
            boton.setBorderPainted(false);     // Elimina el borde
            boton.setFocusPainted(false);      // Elimina el borde de enfoque al hacer clic

            java.net.URL url = getClass().getResource(rutasImagenes[i]);
            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            } else {
                boton.setText("Imagen no encontrada");
            }

            String nombre = rutasImagenes[i].substring(rutasImagenes[i].lastIndexOf("/") + 1);
            nombre = nombre.replace(".png", "");
            boton.setText(nombre);

            boton.setHorizontalTextPosition(JButton.CENTER);
            boton.setVerticalTextPosition(JButton.BOTTOM);

            final String nombreJugador = nombre; // variable final para usar dentro del lambda
            boton.addActionListener(e -> {
                Juego.jugadorAle = nombreJugador;
                Juego.MJ.mostrarJugador("Seleccionaste a: ");
                //System.out.println(Juego.jugadorAle);
            });

            botones[i] = boton;
            panelBotones.add(boton);
        }

        // Añadir el panel de botones al centro del panel principal
        add(panelBotones, BorderLayout.CENTER);
    }

    public void PTJuego(String[] rutasImagenes) {//Funcion para imprimir el tablero del juego y lo que se necesita
        setLayout(new BorderLayout()); // Layout general del panel principal

        // Panel para los 24 botones de personajes
        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 120, 8));
        for (int i = 0; i < 24; i++) {
            JButton boton = new JButton();

            java.net.URL url = getClass().getResource(rutasImagenes[i]);
            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            } else {
                boton.setText("Imagen no encontrada");
            }

            String nombre = rutasImagenes[i].substring(rutasImagenes[i].lastIndexOf("/") + 1);
            nombre = nombre.replace(".png", "");
            boton.setText(nombre);

            boton.setHorizontalTextPosition(JButton.CENTER);
            boton.setVerticalTextPosition(JButton.BOTTOM);

            boton.addActionListener(e -> {

                //Aqui va la implementacion de que se desahabiliten los jugaroes cuando se les de un clic
                boton.setVisible(false);//es un ejemplo de lo que se puede hacer

            });

            botones[i] = boton;
            panelBotones.add(boton);
        }

        // Añadir el panel de botones al centro del panel principal
        add(panelBotones, BorderLayout.CENTER);
    }

    public JButton[] getBotones() {
        return botones;
    }

    public JButton getBotonAleatorio() {
        return aleatorio;
    }
}
