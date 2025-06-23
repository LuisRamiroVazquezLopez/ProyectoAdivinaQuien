package JuegoCodigo;


import JuegoGrafico.Juego;
import JuegoGrafico.MenuPrincipal;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PanelTablero extends JPanel {
    private JButton[] botones = new JButton[24]; // Los 24 botones
    public static JButton aleatorio = new JButton("Personaje aleatorio"); // botón aleatorio
    public static JButton avanzar = new JButton("Avanzar"); // botón para pasar al juego
    public static JButton hacerPregunta = new JButton("Hacer Pregunta"); // botón para hacer preguntas
    private List<String> personajesDescartados = new ArrayList<>();
    private List<JLabel> labelPersonajes = new ArrayList<>();
    private JPanel panelPersonajeSeleccionado;
    private JLabel labelPersonajeSeleccionado;
    private String miPersonajeSeleccionado = null;
    
    // lista de preguntas predefinidas
    private final String[] preguntasPredefinidas = {
        "¿Es hombre?",
        "¿Es mujer?",
        "¿Juega en Europa?",
        "¿Juega en América?",
        "¿Es delantero?",
        "¿Es mediocampista?",
        "¿Es defensor?",
        "¿Es portero?",
        "¿Ha ganado un Mundial?",
        "¿Ha ganado la Champions League?",
        "¿Es brasileño?",
        "¿Es argentino?",
        "¿Es español?",
        "¿Es frances?",
        "¿Es mexicano?",
        "¿Tiene mas de 30 años?",
        "¿Esta retirado?",
        "¿Sigue jugando actualmente?",
        "¿Ha jugado en el Real Madrid?",
        "¿Ha jugado en el Barcelona?",
        "¿Es zurdo?",
        "¿Es diestro?",
        "¿Mide mas de 1.80m?",
        "¿Ha sido Balon de Oro?",
        "¿Juega en la Liga MX?",
        "¿Ha jugado en la Premier League?",
        "¿Tiene barba?",
        "¿Usa el numero 10?",
        "¿Es capitan de su seleccion?",
        "¿Tiene mas de 100 goles?"
    };
    
    public void PTSelec(String[] rutasImagenes) {
        removeAll();
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        //boton aleatorio
        JPanel panelAleatorio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAleatorio.setOpaque(false);
        panelAleatorio.add(aleatorio);
        panelSuperior.add(panelAleatorio, BorderLayout.NORTH);
        
        //personaje seleccionado
        crearPanelPersonajeSeleccionado();
        panelSuperior.add(panelPersonajeSeleccionado, BorderLayout.CENTER);
        panelPersonajeSeleccionado.setOpaque(false);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // panel para el boton de avanzar
        JPanel panelInferior = new JPanel();
        panelInferior.setOpaque(false);
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(avanzar);
        add(panelInferior, BorderLayout.EAST);

        // panel de los 24 botones de personajes
        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 120, 8));
        panelBotones.setOpaque(false);
        for (int i = 0; i < 24; i++) {
            JButton boton = new JButton();

            if (rutasImagenes[i] == null || rutasImagenes[i].trim().isEmpty()) {
                boton.setText("Error: Imagen no valida");
                botones[i] = boton;
                panelBotones.add(boton);
                continue;
            }

            java.net.URL url = getClass().getResource(rutasImagenes[i]);
            String nombre;

            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                
                nombre = rutasImagenes[i].substring(rutasImagenes[i].lastIndexOf("/") + 1);
                nombre = nombre.replace(".png", "");
                boton.setText(nombre);
            } else {
                boton.setText("Imagen no encontrada: " + rutasImagenes[i]);
                nombre = "Desconocido";
            }
            
            boton.setHorizontalTextPosition(JButton.CENTER);
            boton.setVerticalTextPosition(JButton.BOTTOM);

            final String nombreJugadorFinal = nombre;
            final int indiceBoton = i; // Para poder acceder al botón específico

            boton.addActionListener(e -> {
                // Notificar al servidor sobre la selección
                if (Juego.cliente != null && Juego.cliente.isConectado()) {
                    Juego.cliente.seleccionarPersonaje(nombreJugadorFinal);
                    
                    // Actualizar la UI local para mostrar el personaje seleccionado
                    mostrarPersonajeSeleccionado(nombreJugadorFinal, rutasImagenes[indiceBoton]);
                    
                    JOptionPane.showMessageDialog(this, "Has seleccionado a: " + nombreJugadorFinal);
                } else {
                    JOptionPane.showMessageDialog(this, "No estás conectado al servidor.");
                }
            });

            botones[i] = boton;
            botones[i].setOpaque(false);
            botones[i].setContentAreaFilled(false);
            botones[i].setBorderPainted(false);
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.CENTER);
        revalidate(); 
        repaint(); 
    }
    
    /**
     * Crea el panel para mostrar el personaje seleccionado
     */
    private void crearPanelPersonajeSeleccionado() {
        panelPersonajeSeleccionado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelPersonajeSeleccionado.setOpaque(false);
        panelPersonajeSeleccionado.setBorder(BorderFactory.createTitledBorder("Tu Personaje Seleccionado"));
        panelPersonajeSeleccionado.setPreferredSize(new Dimension(200, 120));
        
        labelPersonajeSeleccionado = new JLabel("Ningún personaje seleccionado");
        labelPersonajeSeleccionado.setHorizontalAlignment(SwingConstants.CENTER);
        labelPersonajeSeleccionado.setVerticalAlignment(SwingConstants.CENTER);
        
        panelPersonajeSeleccionado.add(labelPersonajeSeleccionado);
    }
    
    /**
     * Muestra el personaje seleccionado en el panel
     */
    public void mostrarPersonajeSeleccionado(String nombrePersonaje, String rutaImagen) {
        miPersonajeSeleccionado = nombrePersonaje;
        
        java.net.URL url = getClass().getResource(rutaImagen);
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            labelPersonajeSeleccionado.setIcon(new ImageIcon(imagen));
            labelPersonajeSeleccionado.setText(nombrePersonaje);
            labelPersonajeSeleccionado.setHorizontalTextPosition(JLabel.CENTER);
            labelPersonajeSeleccionado.setVerticalTextPosition(JLabel.BOTTOM);
        } else {
            labelPersonajeSeleccionado.setIcon(null);
            labelPersonajeSeleccionado.setText(nombrePersonaje);
        }
        
        panelPersonajeSeleccionado.revalidate();
        panelPersonajeSeleccionado.repaint();
    }
    
    public void PTJuego(String[] rutasImagenes) {
        removeAll();
        setLayout(new BorderLayout());
        
        // Panel superior con botón de pregunta y personaje seleccionado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        // Botón hacer pregunta
        JPanel panelPregunta = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hacerPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        hacerPregunta.setPreferredSize(new Dimension(200, 50));
        hacerPregunta.setBackground(new Color(135, 206, 250)); 
        
        hacerPregunta.addActionListener(e -> {
            if (Juego.cliente != null && Juego.cliente.esMiTurno()) {
                mostrarOpcionesPreguntas();
            } else {
                JOptionPane.showMessageDialog(this, "No es tu turno o no estás conectado", "Espera", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panelPregunta.add(hacerPregunta);
        panelSuperior.add(panelPregunta, BorderLayout.NORTH);
        
        // Mostrar el personaje seleccionado si existe
        if (panelPersonajeSeleccionado != null) {
            panelSuperior.add(panelPersonajeSeleccionado, BorderLayout.CENTER);
        }
        
        add(panelSuperior, BorderLayout.NORTH);
        
        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 10, 8));
        panelBotones.setOpaque(false);
        
        for (int i = 0; i < 24; i++) {
            JButton boton = new JButton();

            if (rutasImagenes[i] == null || rutasImagenes[i].trim().isEmpty()) {
                boton.setText("Error: Imagen no válida");
                botones[i] = boton;
                botones[i].setOpaque(false);
                botones[i].setContentAreaFilled(false);
                botones[i].setBorderPainted(false);
                panelBotones.add(boton);
                continue;
            }

            java.net.URL url = getClass().getResource(rutasImagenes[i]);
            String nombre;

            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                
                nombre = rutasImagenes[i].substring(rutasImagenes[i].lastIndexOf("/") + 1);
                nombre = nombre.replace(".png", "");
                boton.setText(nombre);
            } else {
                boton.setText("Imagen no encontrada: " + rutasImagenes[i]);
                nombre = "Desconocido";
            }

            boton.setHorizontalTextPosition(JButton.CENTER);
            boton.setVerticalTextPosition(JButton.BOTTOM);

            final String nombrePersonaje = nombre;
            
            boton.addActionListener(e -> {
                // Verificar si el personaje es el mismo que seleccioné
                if (nombrePersonaje.equals(miPersonajeSeleccionado)) {
                    JOptionPane.showMessageDialog(this, "No puedes interactuar con tu propio personaje seleccionado", "Acción no válida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Para descartar: siempre permitido
                // Para adivinar: solo en mi turno
                String[] opciones;
                if (Juego.cliente != null && Juego.cliente.esMiTurno()) {
                    opciones = new String[]{"Adivinar personaje", "Descartar personaje"};
                } else {
                    opciones = new String[]{"Descartar personaje"};
                }
                
                int seleccion = JOptionPane.showOptionDialog(
                    this,
                    "¿Qué quieres hacer con " + nombrePersonaje + "?",
                    "Acción",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );
                
                if (opciones.length == 2) { // Ambas opciones disponibles
                    switch (seleccion) {
                        case 0: // adivinar el personaje
                            int confirmacion = JOptionPane.showConfirmDialog(
                                this,
                                "¿Estas seguro de que quieres adivinar que el personaje secreto es " + nombrePersonaje + "?",
                                "Confirmar adivinanza",
                                JOptionPane.YES_NO_OPTION
                            );
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                Juego.cliente.enviarAdivinanza(nombrePersonaje);
                            }
                            break;
                            
                        case 1: // Descartar personaje
                            descartarPersonajeVisualmente(boton);
                            break;
                    }
                } else { // Solo descartar disponible
                    if (seleccion == 0) { // Descarta al personaje
                        descartarPersonajeVisualmente(boton);
                    }
                }
            });

            botones[i] = boton;
            botones[i].setOpaque(false);
            botones[i].setContentAreaFilled(false);
            botones[i].setBorderPainted(false);
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.CENTER);
        revalidate(); 
        repaint(); 
    }
    
    //descarta el personaje
    private void descartarPersonajeVisualmente(JButton boton) {
        java.net.URL urlDescartado = getClass().getResource("/Imagenes/tarjeta_roja.png");
        if (urlDescartado != null) {
            ImageIcon iconoDescartado = new ImageIcon(urlDescartado);
            Image imagenDescartada = iconoDescartado.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            
            boton.setIcon(new ImageIcon(imagenDescartada));
            boton.setText("");
            boton.setEnabled(false);
        } else {
            // Si no se encuentra la imagen de descarte
            boton.setVisible(false);
            JOptionPane.showMessageDialog(this, "No se encontro 'descartado.png'"+urlDescartado+" Personaje ocultado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        
        this.revalidate();
        this.repaint();
    }
    
    
    private void mostrarOpcionesPreguntas() {
        String[] opcionesTipoPregunta = {"Seleccionar del banco de preguntas", "Escribir mi propia pregunta"};
        int tipoSeleccion = JOptionPane.showOptionDialog(
            this,
            "¿Como quieres hacer tu pregunta?",
            "Tipo de pregunta",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesTipoPregunta,
            opcionesTipoPregunta[0]
        );
        
        String preguntaSeleccionada = null;
        
        switch (tipoSeleccion) {
            case 0: //selecciona de la lista
                preguntaSeleccionada = mostrarListaPreguntas();
                break;
                
            case 1: //escribe su propia pregunta 
                preguntaSeleccionada = mostrarInputPreguntaPersonalizada();
                break;
                
            default:
                return; //el usuario cancelo
        }
        
        //envia la pregunta si se selecciono alguna
        if (preguntaSeleccionada != null && !preguntaSeleccionada.trim().isEmpty()) {
            Juego.cliente.enviarPregunta(preguntaSeleccionada);
            JOptionPane.showMessageDialog(this, "Pregunta enviada: " + preguntaSeleccionada, "Pregunta enviada", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

     //muestra la lista de preguntas predefinidas para seleccionar
    private String mostrarListaPreguntas() {
        JDialog dialogoPreguntas = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Banco de Preguntas", true);
        dialogoPreguntas.setLayout(new BorderLayout());
        dialogoPreguntas.setSize(600, 500);
        dialogoPreguntas.setLocationRelativeTo(this);
        
        JList<String> listaPreguntas = new JList<>(preguntasPredefinidas);
        listaPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPreguntas.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollLista = new JScrollPane(listaPreguntas);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Selecciona una pregunta"));
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnSeleccionar = new JButton("Usar esta pregunta");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSeleccionar.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionar.setBackground(new Color(144, 238, 144));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(255, 182, 193));
        
        final String[] preguntaSeleccionada = {null};
        
        btnSeleccionar.addActionListener(e -> {
            String seleccionada = listaPreguntas.getSelectedValue();
            if (seleccionada != null) {
                preguntaSeleccionada[0] = seleccionada;
                dialogoPreguntas.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogoPreguntas, "Por favor selecciona una pregunta de la lista", "Seleccion requerida", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialogoPreguntas.dispose());
        
        listaPreguntas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String seleccionada = listaPreguntas.getSelectedValue();
                    if (seleccionada != null) {
                        preguntaSeleccionada[0] = seleccionada;
                        dialogoPreguntas.dispose();
                    }
                }
            }
        });
        
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);
        
        JLabel instrucciones = new JLabel("<html><center><b>Banco de Preguntas</b><br>Selecciona una pregunta</center></html>");
        instrucciones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        instrucciones.setFont(new Font("Arial", Font.BOLD, 14));
        instrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        
        dialogoPreguntas.add(instrucciones, BorderLayout.NORTH);
        dialogoPreguntas.add(scrollLista, BorderLayout.CENTER);
        dialogoPreguntas.add(panelBotones, BorderLayout.SOUTH);
        
        dialogoPreguntas.setVisible(true);
        
        return preguntaSeleccionada[0];
    }
    
    /**
     * Muestra un input para escribir una pregunta personalizada
     */
    private String mostrarInputPreguntaPersonalizada() {
        JDialog dialogoInput = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pregunta Personalizada", true);
        dialogoInput.setLayout(new BorderLayout());
        dialogoInput.setSize(500, 250);
        dialogoInput.setLocationRelativeTo(this);
        
        JTextArea textAreaPregunta = new JTextArea(4, 35);
        textAreaPregunta.setLineWrap(true);
        textAreaPregunta.setWrapStyleWord(true);
        textAreaPregunta.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaPregunta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollText = new JScrollPane(textAreaPregunta);
        scrollText.setBorder(BorderFactory.createTitledBorder("Escribe tu pregunta"));
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnEnviar = new JButton("Enviar pregunta");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviar.setBackground(new Color(144, 238, 144));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(255, 182, 193));
        
        final String[] preguntaPersonalizada = {null};
        
        btnEnviar.addActionListener(e -> {
            String texto = textAreaPregunta.getText().trim();
            if (!texto.isEmpty()) {
                if (!texto.endsWith("?")) {
                    texto += "?";
                }
                preguntaPersonalizada[0] = texto;
                dialogoInput.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogoInput, "Por favor escribe una pregunta", "Pregunta requerida", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialogoInput.dispose());
        
        textAreaPregunta.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    btnEnviar.doClick();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);
        
        JLabel instrucciones = new JLabel("<html><center><b>Pregunta Personalizada</b><br>Recuerda: solo se puede responder SÍ o NO<br>(Ctrl+Enter para enviar)</center></html>");
        instrucciones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        instrucciones.setFont(new Font("Arial", Font.BOLD, 12));
        instrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        
        dialogoInput.add(instrucciones, BorderLayout.NORTH);
        dialogoInput.add(scrollText, BorderLayout.CENTER);
        dialogoInput.add(panelBotones, BorderLayout.SOUTH);
        
        dialogoInput.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                textAreaPregunta.requestFocusInWindow();
            }
        });
        
        dialogoInput.setVisible(true);
        
        return preguntaPersonalizada[0];
    }
    

    public String getMiPersonajeSeleccionado() {
        return miPersonajeSeleccionado;
    }
    
    public void setMiPersonajeSeleccionado(String personaje) {
        this.miPersonajeSeleccionado = personaje;
    }
    

    public void actualizarPersonajesDescartados(List<String> descartados) {
        this.personajesDescartados = new ArrayList<>(descartados);
        this.repaint();
    }
    
    private void marcarComoDescartado(JLabel label) {
        label.setEnabled(false);
        
        if (label.getIcon() != null) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image img = icon.getImage();
            
            BufferedImage grayImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2d = grayImage.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();
            
            label.setIcon(new ImageIcon(grayImage));
        }
        
        label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    private void restaurarAparienciaNormal(JLabel label) {
        label.setEnabled(true);
        label.setBorder(null);
        
        String nombrePersonaje = obtenerNombrePersonajeDeLabel(label);
        if (nombrePersonaje != null) {
            String rutaImagen = "/JugadoresProyecto/" + nombrePersonaje + ".png";
            java.net.URL url = getClass().getResource(rutaImagen);
            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(
                    label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imagen));
            }
        }
    }
    
    private String obtenerNombrePersonaje(int indice) {
        if (Code.imagenestablero != null && indice < Code.imagenestablero.length) {
            String ruta = Code.imagenestablero[indice];
            if (ruta != null && ruta.contains("/")) {
                String archivo = ruta.substring(ruta.lastIndexOf("/") + 1);
                if (archivo.contains(".")) {
                    return archivo.substring(0, archivo.lastIndexOf("."));
                }
            }
        }
        return null;
    }
    
    private String obtenerNombrePersonajeDeLabel(JLabel label) {
        return label.getName();
    }
    
    private void configurarClickListener(JLabel label, String nombrePersonaje) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Juego.cliente != null && Juego.cliente.getJuegoRef() != null) {
                    Juego juego = (Juego) Juego.cliente.getJuegoRef();
                    
                    if (juego.getMiPersonajeSeleccionado() == null) {
                        juego.mostrarMiPersonajeSeleccionado(nombrePersonaje);
                        if (Juego.cliente != null) {
                            Juego.cliente.seleccionarPersonaje(nombrePersonaje);
                        }
                    } else {
                        juego.descartarPersonaje(nombrePersonaje);
                    }
                }
            }
        });
        
        label.setName(nombrePersonaje);
    }
}