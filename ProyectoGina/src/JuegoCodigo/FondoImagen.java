package JuegoCodigo;

import javax.swing.*;
import java.awt.*;

//Clase para poner fondos de pantalla
public class FondoImagen extends JPanel {
    private Image imagen;

    public FondoImagen(String rutaImagen) {
        setImagen(rutaImagen);
    }

    public void setImagen(String rutaImagen) {
        ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
        this.imagen = icono.getImage();
        repaint(); // Redibuja cuando cambias imagen
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
