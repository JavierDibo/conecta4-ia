package plot4;

public class AlfaBetaPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 9;
    static final boolean MOSTRAR_ARBOL = false;

    /**
     * Metodo que determina el siguiente movimiento a realizar por el jugador.
     *
     * @param tablero El tablero actual del juego.
     * @param conecta El numero de fichas que deben conectarse para ganar.
     * @return La columna donde se debe colocar la ficha.
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        int mejorMovimiento = -1;
        int puntuacionMax = Integer.MIN_VALUE;
        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int columna = 0; columna < tablero.getColumnas(); columna++) {
            if (!tablero.fullColumn(columna)) {
                Grid casilla = new Grid(tablero);
                casilla.set(columna, 2);
                int puntuacion = minimax(casilla, conecta, false, 0, alfa, beta);
                if (puntuacion > puntuacionMax) {
                    puntuacionMax = puntuacion;
                    mejorMovimiento = columna;
                }
            }
        }

        return mejorMovimiento;
    }


    /**
     * Metodo que implementa el algoritmo MiniMax con poda alfa-beta para realizar la mejor jugada a realizar.
     *
     * @param tablero     El tablero actual del juego.
     * @param conecta     El numero de fichas que deben conectarse para ganar.
     * @param esMax       Indica si el jugador actual es el jugador maximizante o no.
     * @param profundidad La profundidad actual del arbol de busqueda.
     * @return El valor de la mejor jugada encontrada.
     */
    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad, int alfa, int beta) {
        int ganador = tablero.checkWin();

        if (ganador == 1) {
            return -1000 + profundidad;
        } else if (ganador == 2) {
            return 1000 - profundidad;
        } else if (profundidad >= PROFUNDIDAD_MAX || tablero.getCount(0) == tablero.getFilas() * tablero.getColumnas()) {
            return heuristica(tablero, esMax, conecta);
        }

        if (esMax) {
            int puntuacionMax = Integer.MIN_VALUE;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid casilla = new Grid(tablero);
                    casilla.set(columna, 2);
                    int puntuacion = minimax(casilla, conecta, false, profundidad + 1, alfa, beta);
                    if (MOSTRAR_ARBOL) {
                        mostrar(columna, profundidad, puntuacion);
                    }
                    puntuacionMax = Math.max(puntuacionMax, puntuacion);

                    alfa = Math.max(alfa, puntuacionMax);

                    if (alfa >= beta) {
                        break;
                    }
                }
            }

            return puntuacionMax;
        } else {
            int puntuacionMin = Integer.MAX_VALUE;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid nuevaCasilla = new Grid(tablero);
                    nuevaCasilla.set(columna, 1);
                    int puntuacion = minimax(nuevaCasilla, conecta, true, profundidad + 1, alfa, beta);
                    if (MOSTRAR_ARBOL) {
                        mostrar(columna, profundidad, puntuacion);
                    }
                    puntuacionMin = Math.min(puntuacionMin, puntuacion);

                    beta = Math.min(beta, puntuacionMin);

                    if (alfa >= beta) {
                        break;
                    }
                }
            }

            return puntuacionMin;
        }
    }


    /**
     * Metodo que calcula una heuristica para el tablero dado. Se basa en contar cuantas piezas consecutivas tiene el
     * jugador en cada una de las posibles direcciones (horizontal, vertical y diagonales), ademas le da mas valor a las
     * posiciones vacias adyacentes.
     *
     * @param tablero El tablero actual del juego.
     * @param esMax   Indica si el jugador actual es el jugador maximizante o no.
     * @param conecta El numero de fichas que deben conectarse para ganar.
     * @return El valor heuristico del tablero.
     */
    private int heuristica(Grid tablero, boolean esMax, int conecta) {
        int piezasConsecutivasMax = 0;
        int jugador = esMax ? 2 : 1;

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.get(fila, columna) == jugador) {
                    int[] dx = {-1, 0, 1, 1};
                    int[] dy = {1, 1, 1, 0};
                    for (int direccion = 0; direccion < 4; direccion++) {
                        int nx = fila + dx[direccion];
                        int ny = columna + dy[direccion];
                        int cuenta = 1;
                        boolean espacioLibre = false;

                        while (tablero.get(nx, ny) == jugador || (tablero.get(nx, ny) == 0 && !espacioLibre)) {
                            if (tablero.get(nx, ny) == 0) {
                                espacioLibre = true;
                            } else {
                                cuenta++;
                            }
                            nx += dx[direccion];
                            ny += dy[direccion];
                        }

                        if (espacioLibre) {
                            piezasConsecutivasMax = Math.max(piezasConsecutivasMax, cuenta);
                        }
                    }
                }
            }
        }

        if (esMax) {
            return piezasConsecutivasMax;
        } else {
            return -piezasConsecutivasMax;
        }
    }

    /**
     * Metodo que muestra informacion sobre el arbol de busqueda utilizado por el algoritmo MiniMax.
     *
     * @param columna     La columna que se esta explorando.
     * @param profundidad La profundidad actual del arbol de busqueda.
     * @param puntuacion  La puntuacion de la casilla actual.
     */
    private void mostrar(int columna, int profundidad, int puntuacion) {
        if (MOSTRAR_ARBOL) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntuacion: %s%n", tabulacion, profundidad + 1, columna, puntuacion);
        }
    }
}