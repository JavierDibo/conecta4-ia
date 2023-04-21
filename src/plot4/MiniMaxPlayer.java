package plot4;

public class MiniMaxPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 5;

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
        boolean mostrar = true;

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
                    int score = minimax(casilla, conecta, false, profundidad + 1);
                    printTreeState(col, profundidad, score, mostrar);
                    puntMax = Math.max(puntMax, score);
                }
            }

            return puntMax;
        } else {
            int puntMin = Integer.MAX_VALUE;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid newGrid = new Grid(tablero);
                    newGrid.set(col, 1);
                    int score = minimax(newGrid, conecta, true, profundidad + 1);
                    printTreeState(col, profundidad, score, mostrar);
                    puntMin = Math.min(puntMin, score);
                }
            }

            return puntMin;
        }
    }

    private void printTreeState(int col, int profundidad, int score, boolean mostrar) {
        if (mostrar) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntaje: %s%n", tabulacion, profundidad + 1, col, formateo(score));
        }
    }

    private String formateo(int score) {
        if (score == Integer.MIN_VALUE) {
            return "-∞";
        } else if (score == Integer.MAX_VALUE) {
            return "∞";
        } else {
            return String.valueOf(score);
        }
    }
}
