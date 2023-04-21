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
                int punt = minimax(casilla, conecta, false, 0, "");
                if (punt > puntMax) {
                    puntMax = punt;
                    mejorMov = col;
                }
            }
        }

        return mejorMov;
    }

    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad, String prefijo) {
        int ganador = tablero.checkWin();

        if (ganador == 1) {
            return -1000 + profundidad;
        } else if (ganador == 2) {
            return 1000 - profundidad;
        } else if (profundidad >= PROFUNDIDAD_MAX || tablero.getCount(0) == tablero.getFilas() * tablero.getColumnas()) {
            return 0;
        }

        String nuevoPrefijo = prefijo + (profundidad == 0 ? "" : "   |-");

        if (esMax) {
            int puntMax = Integer.MIN_VALUE;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid newGrid = new Grid(tablero);
                    newGrid.set(col, 2);
                    int score = minimax(newGrid, conecta, false, profundidad + 1, prefijo + " ");
                    // printTreeState(nuevoPrefijo, col, profundidad, score);
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
                    int score = minimax(newGrid, conecta, true, profundidad + 1, prefijo + " ");
                    // printTreeState(nuevoPrefijo, col, profundidad, score);
                    puntMin = Math.min(puntMin, score);
                }
            }

            return puntMin;
        }
    }

    private void printTreeState(String prefijo, int col, int profundidad, int score) {
        if (score != 0) {
            System.err.printf("%sColumna: %d, Profundidad: %d, Puntaje: %d%n", prefijo, col, profundidad + 1, score);
        }
    }
}
