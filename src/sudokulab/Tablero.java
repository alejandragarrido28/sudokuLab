/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudokulab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

/**
 *
 * @author mavasquez
 */
public class Tablero extends JPanel {
    private JTextField[][] campos = new JTextField[9][9];
    private EscuchadorCelda escuchador;
    private int filaActual = -1;
    private int colActual = -1;

    public Tablero(Juego logica) {
        setLayout(new GridLayout(9, 9, 0, 0));
        setBackground(new Color(45, 45, 45));
        setPreferredSize(new Dimension(450, 450));
        setMaximumSize(new Dimension(450, 450));
        setMinimumSize(new Dimension(450, 450));
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                JTextField campo = new JTextField();
                campo.setHorizontalAlignment(SwingConstants.CENTER);
                campo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
                int bordeTop = 1;
                if (fila % 3 == 0) {
                    bordeTop = 2;
                }
                int bordeLeft = 1;
                if (col % 3 == 0) {
                    bordeLeft = 2;
                }
                int bordeBottom = 1;
                if (fila == 8) {
                    bordeBottom = 2;
                }
                int bordeRight = 1;
                if (col == 8) {
                    bordeRight = 2;
                }
                campo.setBorder(new MatteBorder(bordeTop, bordeLeft, bordeBottom, bordeRight, new Color(45, 45, 45)));
                campo.setEditable(false);
                campo.setBackground(Color.WHITE);
                final int f = fila;
                final int c = col;
                campo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (escuchador != null) {
                            if (logica.estaBloqueado(f, c) == false) {
                                escuchador.celdaClickeada(f, c);
                            }
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (logica.estaBloqueado(f, c) == false) {
                            campo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        campo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                });
                campos[fila][col] = campo;
                add(campo);
            }
        }
    }

    public void setEscuchador(EscuchadorCelda e) { 
        this.escuchador = e; 
    }
    
    public void establecerSeleccion(int fila, int col) { 
        filaActual = fila; 
        colActual = col; 
    }

    public void refrescarCelda(int fila, int col, int valor, boolean esFijo) {
        JTextField campo = campos[fila][col];
        if (valor == 0) {
            campo.setText("");
        } else {
            campo.setText(String.valueOf(valor));
        }
        if (esFijo) {
            campo.setForeground(new Color(45, 45, 45));
            campo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        } else {
            campo.setForeground(new Color(100, 150, 200));
            campo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        }
        boolean esFilaActual = (fila == filaActual);
        boolean esColActual = (col == colActual);
        boolean mismoCuadrante = (fila/3 == filaActual/3 && col/3 == colActual/3);
        if (esFilaActual || esColActual || mismoCuadrante) {
            campo.setBackground(new Color(235, 240, 245));
        } else {
            campo.setBackground(Color.WHITE);
        }
    }

    public interface EscuchadorCelda { 
        void celdaClickeada(int fila, int col); 
    }
}

