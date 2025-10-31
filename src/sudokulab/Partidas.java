package SudokuLab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Partidas implements Tablero.EscuchadorCelda {
    private Juego logica;
    private Tablero tablero;
    private JPanel panelControl;
    private JLabel etiquetaFallos;
    private JComboBox<String> selectorNivel;
    private int filaSelec = -1;
    private int colSelec = -1;
    private int cantidadFallos = 0;
    private int pistasActuales = 25;
    private static final int LIMITE_FALLOS = 3;

    public Partidas(Juego log, Tablero tab) {
        this.logica = log;
        this.tablero = tab;
        tablero.setEscuchador(this);
        logica.crearNuevoJuego(pistasActuales);
        construirPanelControl();
        actualizarTablero();
    }

    private void construirPanelControl() {
        panelControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelControl.setBackground(new Color(250, 250, 250));
        
        selectorNivel = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        selectorNivel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        selectorNivel.setBackground(Color.WHITE);
        selectorNivel.setForeground(new Color(45, 45, 45));
        selectorNivel.setSelectedIndex(1);
        selectorNivel.setFocusable(false);
        selectorNivel.addActionListener(e -> {
            String nivel = selectorNivel.getSelectedItem().toString();
            if (nivel.equals("Fácil")) {
                pistasActuales = 30;
            } else if (nivel.equals("Media")) {
                pistasActuales = 25;
            } else {
                pistasActuales = 20;
            }
            iniciarNuevaPartida();
        });
        panelControl.add(selectorNivel);
        
        etiquetaFallos = new JLabel("Errores: 0/" + LIMITE_FALLOS);
        etiquetaFallos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaFallos.setForeground(new Color(100, 150, 200));
        panelControl.add(etiquetaFallos);
    }

    public JPanel obtenerPanelInfo() { 
        return panelControl; 
    }
    
    public JPanel obtenerPanelNumeros() {
        JPanel panelNumeros = new JPanel(new GridLayout(9, 1, 0, 10));
        panelNumeros.setBackground(new Color(250, 250, 250));
        for (int numero = 1; numero <= 9; numero++) {
            JButton boton = new JButton(String.valueOf(numero));
            boton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            boton.setBackground(Color.WHITE);
            boton.setForeground(new Color(45, 45, 45));
            boton.setFocusPainted(false);
            boton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            boton.setPreferredSize(new Dimension(50, 45));
            final int valorBoton = numero;
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    boton.setBackground(new Color(100, 150, 200));
                    boton.setForeground(Color.WHITE);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    boton.setBackground(Color.WHITE);
                    boton.setForeground(new Color(45, 45, 45));
                }
            });
            boton.addActionListener(e -> insertarNumero(valorBoton));
            panelNumeros.add(boton);
        }
        return panelNumeros;
    }
    
    public JPanel obtenerPanelAcciones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelAcciones.setBackground(new Color(250, 250, 250));
        
        JButton botonNuevo = new JButton("Nuevo Juego");
        botonNuevo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        botonNuevo.setBackground(new Color(45, 45, 45));
        botonNuevo.setForeground(Color.WHITE);
        botonNuevo.setFocusPainted(false);
        botonNuevo.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        botonNuevo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonNuevo.setBackground(new Color(100, 150, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                botonNuevo.setBackground(new Color(45, 45, 45));
            }
        });
        botonNuevo.addActionListener(e -> iniciarNuevaPartida());
        
        JButton botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        botonReiniciar.setBackground(Color.WHITE);
        botonReiniciar.setForeground(new Color(45, 45, 45));
        botonReiniciar.setFocusPainted(false);
        botonReiniciar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(45, 45, 45), 2),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        botonReiniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonReiniciar.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                botonReiniciar.setBackground(Color.WHITE);
            }
        });
        botonReiniciar.addActionListener(e -> limpiarCasillasEditables());
        
        panelAcciones.add(botonNuevo);
        panelAcciones.add(botonReiniciar);
        return panelAcciones;
    }

    @Override
    public void celdaClickeada(int fila, int col) {
        filaSelec = fila; 
        colSelec = col;
        tablero.establecerSeleccion(fila, col);
        actualizarTablero();
    }

    private void insertarNumero(int valor) {
        if (filaSelec < 0) {
            return;
        }
        if (colSelec < 0) {
            return;
        }
        if (cantidadFallos >= LIMITE_FALLOS) {
            return;
        }
        boolean esValido = logica.validarMovimiento(filaSelec, colSelec, valor);
        if (esValido) {
            logica.colocarValor(filaSelec, colSelec, valor);
        } else {
            cantidadFallos = cantidadFallos + 1;
            etiquetaFallos.setText("Errores: " + cantidadFallos + "/" + LIMITE_FALLOS);
            if (cantidadFallos >= LIMITE_FALLOS) {
                JOptionPane.showMessageDialog(null, "Límite de errores alcanzado. Nuevo juego.");
                iniciarNuevaPartida();
                return;
            } else {
                int restantes = LIMITE_FALLOS - cantidadFallos;
                JOptionPane.showMessageDialog(null, "Error. Quedan " + restantes + " intentos.");
            }
        }
        actualizarTablero();
    }

    private void iniciarNuevaPartida() {
        cantidadFallos = 0;
        logica.crearNuevoJuego(pistasActuales);
        filaSelec = -1;
        colSelec = -1;
        actualizarTablero();
    }

    private void limpiarCasillasEditables() {
        cantidadFallos = 0;
        logica.reiniciarCeldasEditables();
        actualizarTablero();
    }

    private void actualizarTablero() {
        etiquetaFallos.setText("Errores: " + cantidadFallos + "/" + LIMITE_FALLOS);
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                int valor = logica.obtenerValor(fila, col);
                boolean bloqueado = logica.estaBloqueado(fila, col);
                tablero.refrescarCelda(fila, col, valor, bloqueado);
            }
        }
    }
}

