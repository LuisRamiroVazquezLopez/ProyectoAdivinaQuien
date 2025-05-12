/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JuegoCodigo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author manue
 */
//para crear el tablero
public class PanelTablero extends JPanel {

    public PanelTablero(String[] imagenes) {
        setLayout(new GridLayout(6, 4, 100, 10)); // 6 filas x 4 columnas y los otros dos son el espacio entre imagenes (x,y)

        for (String ruta : imagenes) {
            JPanel celda = new JPanel(new BorderLayout());

            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
    
                // Escalar la imagen a 100x100 (ajusta el tamaño a tu gusto)
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

                JLabel labelImagen = new JLabel(iconoEscalado);
                labelImagen.setHorizontalAlignment(JLabel.CENTER);
                celda.add(labelImagen, BorderLayout.CENTER);

                String nombre = ruta.substring(ruta.lastIndexOf("/") + 1);
                JLabel labelTexto = new JLabel(nombre, JLabel.CENTER);
                celda.add(labelTexto, BorderLayout.SOUTH);
            } else {
                celda.add(new JLabel("Imagen no encontrada", JLabel.CENTER));
            }

            add(celda);
        }
    }
}
