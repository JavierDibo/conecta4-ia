package plot4;

/**
 * @author Javier Francisco Dibo Gómez
 * @author Cristian Ojeda del Moral
 */
public class MiniMaxPlayer extends Player {
    static final int JUGADOR_UNO = Main.PLAYER1;
    static final int JUGADOR_DOS = Main.PLAYER2;
    static final int VACIO = Main.VACIO;
    static final boolean MOSTRAR = true;

    /**
     * Metodo que se encarga de decidir el movimiento que realizara el jugador en el tablero.
     * Implementa el algoritmo MiniMax.
     *
     * @param tablero El tablero actual del juego.
     * @param conecta El numero de fichas consecutivas que se necesitan para ganar.
     * @return El numero de columna donde se realizara el movimiento.
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        int mejorMov = -1;
        int puntMax = Integer.MIN_VALUE;

        for (int col = 0; col < tablero.getColumnas(); col++) {
            if (!tablero.fullColumn(col)) {
                Grid casilla = new Grid(tablero);
                casilla.set(col, JUGADOR_DOS);
                int punt = minimax(casilla, conecta, false, 0);
                if (punt > puntMax) {
                    puntMax = punt;
                    mejorMov = col;
                }
            }
        }

        return mejorMov;
    }


    /**
     * Metodo privado que implementa el algoritmo MiniMax.
     *
     * @param tablero     El tablero actual del juego.
     * @param conecta     El numero de fichas consecutivas que se necesitan para ganar (no utilizado directamente).
     * @param esMax       Indica si el jugador actual es el jugador MAX.
     * @param profundidad La profundidad actual en el arbol de busqueda.
     * @return El valor de la posicion evaluada.
     */
    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad) {
        int ganador = tablero.checkWin();

        if (ganador == 1) {
            return Integer.MIN_VALUE + profundidad;
        } else if (ganador == -1) {
            return Integer.MAX_VALUE - profundidad;
        } else if ( tablero.getCount(VACIO) == tablero.getFilas() * tablero.getColumnas()) {
            return 0;
        }

        if (esMax) {
            int puntMax = Integer.MIN_VALUE + 1000;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid casilla = new Grid(tablero);
                    casilla.set(col, JUGADOR_DOS);
                    int punt = minimax(casilla, conecta, false, profundidad + 1);
                    mostrar(col, profundidad, punt);
                    puntMax = Math.max(puntMax, punt);
                }
            }


            return puntMax;
        } else {
            int puntMin = Integer.MAX_VALUE - 1000;

            for (int col = 0; col < tablero.getColumnas(); col++) {
                if (!tablero.fullColumn(col)) {
                    Grid newGrid = new Grid(tablero);
                    newGrid.set(col, JUGADOR_UNO);
                    int punt = minimax(newGrid, conecta, true, profundidad + 1);
                    mostrar(col, profundidad, punt);
                    puntMin = Math.min(puntMin, punt);
                }
            }

            return puntMin;
        }
    }

    /**
     * Metodo privado que imprime el estado actual del arbol de busqueda en la consola.
     *
     * @param col         El numero de columna que se esta evaluando.
     * @param profundidad La profundidad actual en el arbol de busqueda.
     * @param punt        El valor de la posicion evaluada.
     */
    private void mostrar(int col, int profundidad, int punt) {
        if (MOSTRAR) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntaje: %s%n", tabulacion, profundidad + 1, col, punt);
        }
    }
}