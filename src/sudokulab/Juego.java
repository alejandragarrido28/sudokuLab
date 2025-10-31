package SudokuLab;

import java.util.*;

public class Juego {
    private int[][] matriz = new int[9][9];
    private boolean[][] bloqueados = new boolean[9][9];

    public int obtenerValor(int fila, int columna) {
        return matriz[fila][columna];
    }

    public boolean estaBloqueado(int fila, int columna) {
        return bloqueados[fila][columna];
    }

    public void colocarValor(int fila, int columna, int valor) {
        matriz[fila][columna] = valor;
    }

    public void crearNuevoJuego(int cantidadPistas) {
        inicializarMatrizCompleta();
        eliminarCeldas(81 - cantidadPistas);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matriz[i][j] != 0) {
                    bloqueados[i][j] = true;
                } else {
                    bloqueados[i][j] = false;
                }
            }
        }
    }

    public void reiniciarCeldasEditables() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (bloqueados[i][j] == false) {
                    matriz[i][j] = 0;
                }
            }
        }
    }

    public boolean validarMovimiento(int fila, int columna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (matriz[fila][i] == valor) {
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (matriz[i][columna] == valor) {
                return false;
            }
        }
        int inicioFila = (fila / 3) * 3;
        int inicioColumna = (columna / 3) * 3;
        for (int i = inicioFila; i < inicioFila + 3; i++) {
            for (int j = inicioColumna; j < inicioColumna + 3; j++) {
                if (matriz[i][j] == valor) {
                    return false;
                }
            }
        }
        return true;
    }

    private void inicializarMatrizCompleta() {
        matriz = new int[9][9];
        completarCasillas(0, 0);
    }

    private boolean completarCasillas(int fila, int columna) {
        if (fila == 9) {
            return true;
        }
        int siguienteFila = fila;
        int siguienteColumna = columna + 1;
        if (columna == 8) {
            siguienteFila = fila + 1;
            siguienteColumna = 0;
        }
        ArrayList<Integer> numeros = new ArrayList<>();
        for (int num = 1; num <= 9; num++) {
            numeros.add(num);
        }
        Collections.shuffle(numeros);
        for (int i = 0; i < numeros.size(); i++) {
            int valor = numeros.get(i);
            if (validarMovimiento(fila, columna, valor)) {
                matriz[fila][columna] = valor;
                if (completarCasillas(siguienteFila, siguienteColumna)) {
                    return true;
                }
            }
        }
        matriz[fila][columna] = 0;
        return false;
    }

    private void eliminarCeldas(int cantidad) {
        int removidos = 0;
        Random aleatorio = new Random();
        while (removidos < cantidad) {
            int fila = aleatorio.nextInt(9);
            int columna = aleatorio.nextInt(9);
            if (matriz[fila][columna] != 0) {
                int respaldo = matriz[fila][columna];
                matriz[fila][columna] = 0;
                int[][] duplicado = duplicarMatriz();
                int soluciones = calcularSoluciones(duplicado, 2);
                if (soluciones == 1) {
                    removidos = removidos + 1;
                } else {
                    matriz[fila][columna] = respaldo;
                }
            }
        }
    }

    private int calcularSoluciones(int[][] matrizTemp, int limite) {
        return resolverRecursivo(matrizTemp, 0, 0, limite);
    }

    private int resolverRecursivo(int[][] matrizTemp, int fila, int columna, int limite) {
        if (fila == 9) {
            return 1;
        }
        int proxFila = fila;
        int proxCol = columna + 1;
        if (columna == 8) {
            proxFila = fila + 1;
            proxCol = 0;
        }
        if (matrizTemp[fila][columna] != 0) {
            return resolverRecursivo(matrizTemp, proxFila, proxCol, limite);
        }
        int contador = 0;
        for (int valor = 1; valor <= 9; valor++) {
            if (esValidoEn(matrizTemp, fila, columna, valor)) {
                matrizTemp[fila][columna] = valor;
                contador = contador + resolverRecursivo(matrizTemp, proxFila, proxCol, limite);
                if (contador >= limite) {
                    break;
                }
            }
        }
        matrizTemp[fila][columna] = 0;
        return contador;
    }

    private boolean esValidoEn(int[][] matrizTemp, int fila, int columna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (matrizTemp[fila][i] == valor) {
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (matrizTemp[i][columna] == valor) {
                return false;
            }
        }
        int baseF = (fila / 3) * 3;
        int baseC = (columna / 3) * 3;
        for (int i = baseF; i < baseF + 3; i++) {
            for (int j = baseC; j < baseC + 3; j++) {
                if (matrizTemp[i][j] == valor) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] duplicarMatriz() {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }
}

