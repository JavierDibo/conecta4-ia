package plot4;

/**
 * @author Javier Francisco Dibo Gómez
 * @author Cristian Ojeda del Moral
 */
public class MiniMaxPlayer extends Player {
    /**
     * Numero de filas del tablero (definidas en Main).
     */
    static final int FILAS = 6;

    /**
     * Numero de columnas del tablero (definidas en Main).
     */
    static final int COLUMNAS = 7;

    /**
     * Profundidad maxima de busqueda del algoritmo MiniMax.
     * El valor se calcula como FILAS * COLUMNAS, lo que representa una profundidad infinita.
     */
    static final int PROFUNDIDAD_MAX = FILAS * COLUMNAS;

    /**
     * Indica si se mostrara o no la informacion del arbol de busqueda en la salida estandar.
     */
    static final boolean MOSTRAR = false;

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
                    mostrar(col, profundidad, punt);
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
            System.err.printf("%sNivel %d | Col: %d | Puntaje: %s%n", tabulacion, profundidad + 1, col, formateo(punt));
        }
    }

    /**
     * Metodo privado que formatea el valor del puntaje para su impresion en la consola.
     *
     * @param punt El valor del puntaje.
     * @return El valor del puntaje formateado.
     */
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