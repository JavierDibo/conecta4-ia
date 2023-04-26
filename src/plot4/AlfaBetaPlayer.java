package plot4;

public class AlfaBetaPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 9;
    static final boolean MOSTRAR_ARBOL = false;
    static final int JUGADOR_NULO = 0;
    static final int JUGADOR_UNO = 1;
    static final int JUGADOR_DOS = 2;
    static final int PROFUNDIDAD_BASE = 0;

    /**
     * Metodo que implementa el algoritmo Minimax con poda Alfa-Beta para tomar la decision de juego. Recibe el tablero
     * actual y el valor de la ficha del jugador que esta usand esta estrategia de juego, y devuelve el numero de
     * columna donde el jugador deberiaponer su ficha.
     *
     * @param tablero El tablero de juego actual.
     * @param conecta El numero de fichas consecutivas necesarias para ganar.
     * @return El numero de columna donde el jugador debe poner su ficha.
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
                casilla.set(columna, JUGADOR_DOS);
                int puntuacion = minimax(casilla, conecta, false, PROFUNDIDAD_BASE, alfa, beta);
                if (puntuacion > puntuacionMax) {
                    puntuacionMax = puntuacion;
                    mejorMovimiento = columna;
                }
            }
        }

        return mejorMovimiento;
    }

    /**
     * Metodo recursivo que implementa el algoritmo Minimax con poda Alfa-Beta. Recibe el tablero actual, el valor de
     * la ficha del jugador que esta usando esta estrategia de juego, un booleano que indica si se esta en un nivel
     * "maximo" o "minimo" del arbol de juego, la profundidad actual en el arbol, y los valores alfa y beta para la
     * poda. Devuelve la puntuacion para la jugada actual.
     *
     * @param tablero     El tablero de juego actual.
     * @param conecta     El numero de fichas consecutivas necesarias para ganar.
     * @param esMax       Indica si se esta en un nivel "maximo" o "minimo" del arbol de juego.
     * @param profundidad La profundidad actual en el arbol de juego.
     * @param alfa        Valor para la poda Alfa-Beta.
     * @param beta        Valor para la poda Alfa-Beta.
     * @return La puntuacion para la jugada actual.
     */
    private int minimax(Grid tablero, int conecta, boolean esMax, int profundidad, int alfa, int beta) {
        int ganador = tablero.checkWin();

        if (ganador == JUGADOR_UNO) {
            return -1000 + profundidad;
        } else if (ganador == JUGADOR_DOS) {
            return 1000 - profundidad;
        } else if (profundidad >= PROFUNDIDAD_MAX || tablero.getCount(JUGADOR_NULO) == tablero.getFilas() * tablero.getColumnas()) {
            return heuristica(tablero, esMax, conecta);
        }

        if (esMax) {
            int puntuacionMax = Integer.MIN_VALUE;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid casilla = new Grid(tablero);
                    casilla.set(columna, JUGADOR_DOS);
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
                    nuevaCasilla.set(columna, JUGADOR_UNO);
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
     * Metodo que calcula la heuristica para una posicion en el tablero. Asigna un valor mayor a un tablero donde haya
     * piezas consecutivas, en cualquier direccion, mientras mas haya mas valiosa es. Devuelve la puntuacion para la
     * posicion actual.
     *
     * @param tablero El tablero de juego actual.
     * @param esMax   Indica si se esta en un nivel "maximo" o "minimo" del arbol de juego.
     * @param conecta El numero de fichas consecutivas necesarias para ganar.
     * @return La puntuacion para la posicion actual.
     */
    private int heuristica(Grid tablero, boolean esMax, int conecta) {
        // Cantidad maxima de piezas consecutivas del jugador.
        int piezasConsecutivasMax = 0;
        int jugador = esMax ? JUGADOR_DOS : JUGADOR_UNO;

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                // Si la posicion actual del tablero pertenece al jugador, se analizan las posibles direcciones de piezas consecutivas.
                if (tablero.get(fila, columna) == jugador) {
                    // Arrays con las direcciones posibles a analizar en el tablero (arriba-izquierda, arriba, arriba-derecha, derecha).
                    int[] dirX = {-1, 0, 1, 1};
                    int[] dirY = {1, 1, 1, 0};
                    // Itera a traves de las direcciones posibles.
                    for (int direccion = 0; direccion < 4; direccion++) {
                        // Calcula las nuevas coordenadas en funcion de la direccion actual.
                        int nx = fila + dirX[direccion];
                        int ny = columna + dirY[direccion];
                        int cuenta = 1;
                        boolean espacioLibre = false;

                        // Mientras la posicion nx, ny sea igual al jugador o haya un espacio libre, sigue analizando en la direccion actual.
                        while (tablero.get(nx, ny) == jugador || (tablero.get(nx, ny) == 0 && !espacioLibre)) {
                            // Si la posicion nx, ny esta vacia, marca espacioLibre como verdadero.
                            if (tablero.get(nx, ny) == 0) {
                                espacioLibre = true;
                            } else {
                                // Si no esta vacia, incrementa la cuenta de piezas consecutivas.
                                cuenta++;
                            }
                            // Avanza a la siguiente posicion en la direccion actual.
                            nx += dirX[direccion];
                            ny += dirY[direccion];
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
     * Metodo que muestra informacion sobre el arbol de juego en la consola. Recibe el numero de columna, la
     * profundidad actual en el arbol y la puntuacion para la jugada actual.
     *
     * @param columna     El numero de columna de la jugada actual.
     * @param profundidad La profundidad actual en el arbol de juego.
     * @param puntuacion  La puntuacion para la jugada actual.
     */
    private void mostrar(int columna, int profundidad, int puntuacion) {
        if (MOSTRAR_ARBOL) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntuacion: %s%n", tabulacion, profundidad + 1, columna, puntuacion);
        }
    }
}