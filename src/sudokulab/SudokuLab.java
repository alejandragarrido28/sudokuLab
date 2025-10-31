/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package SudokuLab;

import javax.swing.*;
import java.awt.*;

public class SudokuLab {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Sudoku");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(750, 700);
            ventana.setLocationRelativeTo(null);
            ventana.setResizable(false);
            ventana.getContentPane().setBackground(new Color(250, 250, 250));

            Juego logica = new Juego();
            Tablero tablero = new Tablero(logica);
            Partidas gestor = new Partidas(logica, tablero);

            JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
            panelPrincipal.setBackground(new Color(250, 250, 250));
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JPanel panelSuperior = new JPanel(new BorderLayout());
            panelSuperior.setBackground(new Color(250, 250, 250));
            
            JLabel titulo = new JLabel("SUDOKU", SwingConstants.LEFT);
            titulo.setFont(new Font("Segoe UI", Font.PLAIN, 32));
            titulo.setForeground(new Color(45, 45, 45));
            panelSuperior.add(titulo, BorderLayout.WEST);
            panelSuperior.add(gestor.obtenerPanelInfo(), BorderLayout.EAST);
            
            panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
            
            JPanel centroConBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            centroConBotones.setBackground(new Color(250, 250, 250));
            centroConBotones.add(tablero);
            centroConBotones.add(gestor.obtenerPanelNumeros());
            
            panelPrincipal.add(centroConBotones, BorderLayout.CENTER);
            panelPrincipal.add(gestor.obtenerPanelAcciones(), BorderLayout.SOUTH);
            
            ventana.add(panelPrincipal);
            ventana.setVisible(true);
        });
    }
}

