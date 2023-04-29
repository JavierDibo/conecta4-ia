package plot4;

/**
 * @author Javier Francisco Dibo Gomez
 * @author Cristian Ojeda del Moral
 */
public class MiniMaxRestrainedPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 6;
    static final boolean MOSTRAR_ARBOL = false;
    static final int VACIO = Main.VACIO;
    static final int JUGADOR_UNO = Main.PLAYER1;
    static final int JUGADOR_DOS = Main.PLAYER2;
    static final int PROFUNDIDAD_BASE = 0;

    /**
     * Devuelve el numero de columna del mejor movimiento que el jugador puede hacer en el tablero dado, utilizando el
     * algoritmo de busqueda de minimax con profundidad restringida.
     *
     * @param tablero el tablero actual del juego
     * @param conecta la cantidad de fichas consecutivas necesarias para ganar el juego
     * @return la columna seleccionada como el mejor movimiento
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        int mejorMovimiento = -1;
        int puntuacionMax = Integer.MIN_VALUE;

        for (int columna = 0; columna < tablero.getColumnas(); columna++) {
            if (!tablero.fullColumn(columna)) {
                Grid casilla = new Grid(tablero);
                casilla.set(columna, JUGADOR_DOS);
                int puntuacion = minimax(casilla, conecta, false, PROFUNDIDAD_BASE);
                if (puntuacion > puntuacionMax) {
                    puntuacionMax = puntuacion;
                    mejorMovimiento = columna;
                }
            }
        }

        return mejorMovimiento;
    }

    /**
     * Metodo que implementa el algoritmo MiniMax para realizar la mejor jugada a realizar.
     *
     * @param tablero     El tablero actual del juego.
     * @param conecta     El numero de fichas que deben conectarse para ganar.
     * @param esMax       Indica si el jugador actual es el jugador maximizante o no.
     * @param profundidad La profundidad actual del arbol de busqueda.
     * @return El valor de la mejor jugada encontrada.
     */
    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad) {
        int ganador = tablero.checkWin();

        if (ganador == JUGADOR_UNO) {
            return Integer.MIN_VALUE + profundidad;
        } else if (ganador == JUGADOR_DOS) {
            return Integer.MAX_VALUE - profundidad;
        } else if (profundidad >= PROFUNDIDAD_MAX || tablero.getCount(VACIO) == tablero.getFilas() * tablero.getColumnas()) {
            return heuristica(tablero, esMax, conecta);
        }

        if (esMax) {
            int puntuacionMax = Integer.MIN_VALUE + 1000;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid casilla = new Grid(tablero);
                    casilla.set(columna, JUGADOR_DOS);
                    int puntuacion = minimax(casilla, conecta, false, profundidad + 1);
                    if (MOSTRAR_ARBOL) {
                        mostrar(columna, profundidad, puntuacion);
                    }
                    puntuacionMax = Math.max(puntuacionMax, puntuacion);
                }
            }

            return puntuacionMax;
        } else {
            int puntuacionMin = Integer.MAX_VALUE - 1000;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid nuevaCasilla = new Grid(tablero);
                    nuevaCasilla.set(columna, JUGADOR_UNO);
                    int puntuacion = minimax(nuevaCasilla, conecta, true, profundidad + 1);
                    if (MOSTRAR_ARBOL) {
                        mostrar(columna, profundidad, puntuacion);
                    }
                    puntuacionMin = Math.min(puntuacionMin, puntuacion);
                }
            }

            return puntuacionMin;
        }
    }

    /**
     * Calcula la puntuacion heuristica para un tablero dado, basado en la cantidad de piezas consecutivas de un jugador.
     *
     * @param tablero      el tablero para el cual se calculara la puntuacion heuristica
     * @param esJugadorDos un valor booleano que indica si se esta calculando la puntuacion para el jugador dos o no
     * @param conecta      la cantidad de fichas que deben estar conectadas para ganar el juego
     * @return la bondad para el tablero dado
     */
    private int heuristica(Grid tablero, boolean esJugadorDos, int conecta) {
        int piezasConsecutivasMax = 0;
        int jugador = esJugadorDos ? JUGADOR_DOS : JUGADOR_UNO;

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.get(fila, columna) == jugador) {
                    int[] dirX = {-1, 0, 1, 1};
                    int[] dirY = {1, 1, 1, 0};

                    for (int direccion = 0; direccion < 4; direccion++) {
                        int filaActual = fila + dirX[direccion];
                        int columnaActual = columna + dirY[direccion];
                        int cuenta = 1;
                        boolean espacioLibre = false;

                        while (tablero.get(filaActual, columnaActual) == jugador || (tablero.get(filaActual, columnaActual) == VACIO && !espacioLibre)) {
                            if (tablero.get(filaActual, columnaActual) == 0) {
                                espacioLibre = true;
                            } else {
                                cuenta++;
                            }
                            filaActual += dirX[direccion];
                            columnaActual += dirY[direccion];
                        }

                        if (espacioLibre) {
                            piezasConsecutivasMax = Math.max(piezasConsecutivasMax, cuenta);
                        }
                    }
                }
            }
        }

        int puntuacionHeuristica = esJugadorDos ? piezasConsecutivasMax * piezasConsecutivasMax : -(piezasConsecutivasMax * piezasConsecutivasMax);
        return puntuacionHeuristica;
    }


    /**
     * Muestra informacion sobre el nodo actual en el arbol de busqueda, incluyendo la columna, la profundidad y la puntuacion.
     *
     * @param columna     la columna actual del nodo
     * @param profundidad la profundidad actual del nodo en el arbol de busqueda
     * @param puntuacion  la puntuacion actual del nodo
     */
    private void mostrar(int columna, int profundidad, int puntuacion) {
        if (MOSTRAR_ARBOL) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntuacion: %s%n", tabulacion, profundidad + 1, columna, puntuacion);
        }
    }
}
