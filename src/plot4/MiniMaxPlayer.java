package plot4;

public class MiniMaxPlayer extends Player {
    static final int FILAS = 6;
    static final int COLUMNAS = 7;
    static final int PROFUNDIDAD_MAX = FILAS * COLUMNAS; // Infinito
    static final boolean MOSTRAR = false;

    @Override
    public int turno(Grid tablero, int conecta) {
        int mejorMov = -1;
        int puntMax = Integer.MIN_VALUE;

        for (int col = 0; col < tablero.getColumnas(); col++) {
            if (!tablero.fullColumn(col)) {
                Grid casilla = new Grid(tablero);
                casilla.set(col, 2);
                int punt = minimax(casilla, conecta, false, 0);
                if (punt > puntMax) {
                    puntMax = punt;
                    mejorMov = col;
                }
            }
        }

        return mejorMov;
    }

    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad) {

        int ganador = tablero.checkWin();

        if (ganador == 1) {
            return -1000 + profundidad;
        } else if (ganador == 2) {
            return 1000 - profundidad;
        } else if (profundidad >= PROFUNDIDAD_MAX || tablero.getCount(0) == tablero.getFilas() * tablero.getColumnas()) {
            return 0;
        }

        if (esMax) {
            int puntMax = Integer.MIN_VALUE;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid casilla = new Grid(tablero);
                    casilla.set(col, 2);
                    int punt = minimax(casilla, conecta, false, profundidad + 1);
                    printTreeState(col, profundidad, punt);
                    puntMax = Math.max(puntMax, punt);
                }
            }

            return puntMax;
        } else {
            int puntMin = Integer.MAX_VALUE;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid newGrid = new Grid(tablero);
                    newGrid.set(col, 1);
                    int punt = minimax(newGrid, conecta, true, profundidad + 1);
                    printTreeState(col, profundidad, punt);
                    puntMin = Math.min(puntMin, punt);
                }
            }

            return puntMin;
        }
    }

    private void printTreeState(int col, int profundidad, int punt) {
        if (MOSTRAR) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntaje: %s%n", tabulacion, profundidad + 1, col, formateo(punt));
        }
    }

    private String formateo(int punt) {
        if (punt == Integer.MIN_VALUE) {
            return "-∞";
        } else if (punt == Integer.MAX_VALUE) {
            return "∞";
        } else {
            return String.valueOf(punt);
        }
    }
}
