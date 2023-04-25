package plot4;

public class AlfaBetaPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 9;
    static final boolean MOSTRAR_ARBOL = false;

    /**
     * Método que implementa el algoritmo Minimax con poda Alfa-Beta para tomar la decisión de juego. Recibe el tablero
     * actual y el valor de la ficha del jugador que está usand esta estrategia de juego, y devuelve el número de
     * columna donde el jugador deberíaponer su ficha.
     *
     * @param tablero El tablero de juego actual.
     * @param conecta El número de fichas consecutivas necesarias para ganar.
     * @return El número de columna donde el jugador debe poner su ficha.
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
     * Método recursivo que implementa el algoritmo Minimax con poda Alfa-Beta. Recibe el tablero actual, el valor de
     * la ficha del jugador que está usando esta estrategia de juego, un booleano que indica si se está en un nivel
     * "máximo" o "mínimo" del árbol de juego, la profundidad actual en el árbol, y los valores alfa y beta para la
     * poda. Devuelve la puntuación para la jugada actual.
     *
     * @param tablero     El tablero de juego actual.
     * @param conecta     El número de fichas consecutivas necesarias para ganar.
     * @param esMax       Indica si se está en un nivel "máximo" o "mínimo" del árbol de juego.
     * @param profundidad La profundidad actual en el árbol de juego.
     * @param alfa        Valor para la poda Alfa-Beta.
     * @param beta        Valor para la poda Alfa-Beta.
     * @return La puntuación para la jugada actual.
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
     * Método que calcula la heurística para una posición en el tablero. Recibe el tablero actual, un booleano que
     * indica si se está en un nivel "máximo" o "mínimo" del árbol de juego, y el número de fichas consecutivas
     * necesarias para ganar. Devuelve la puntuación para la posición actual.
     *
     * @param tablero El tablero de juego actual.
     * @param esMax   Indica si se está en un nivel "máximo" o "mínimo" del árbol de juego.
     * @param conecta El número de fichas consecutivas necesarias para ganar.
     * @return La puntuación para la posición actual.
     */
    private int heuristica(Grid tablero, boolean esMax, int conecta) {
        // Cantidad máxima de piezas consecutivas del jugador.
        int piezasConsecutivasMax = 0;
        int jugador = esMax ? 2 : 1;

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                // Si la posición actual del tablero pertenece al jugador, se analizan las posibles direcciones de piezas consecutivas.
                if (tablero.get(fila, columna) == jugador) {
                    // Arrays con las direcciones posibles a analizar en el tablero (arriba-izquierda, arriba, arriba-derecha, derecha).
                    int[] dirX = {-1, 0, 1, 1};
                    int[] dirY = {1, 1, 1, 0};
                    // Itera a través de las direcciones posibles.
                    for (int direccion = 0; direccion < 4; direccion++) {
                        // Calcula las nuevas coordenadas en función de la dirección actual.
                        int nx = fila + dirX[direccion];
                        int ny = columna + dirY[direccion];
                        int cuenta = 1;
                        boolean espacioLibre = false;

                        // Mientras la posición nx, ny sea igual al jugador o haya un espacio libre, sigue analizando en la dirección actual.
                        while (tablero.get(nx, ny) == jugador || (tablero.get(nx, ny) == 0 && !espacioLibre)) {
                            // Si la posición nx, ny está vacía, marca espacioLibre como verdadero.
                            if (tablero.get(nx, ny) == 0) {
                                espacioLibre = true;
                            } else {
                                // Si no está vacía, incrementa la cuenta de piezas consecutivas.
                                cuenta++;
                            }
                            // Avanza a la siguiente posición en la dirección actual.
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
     * Método que muestra información sobre el árbol de juego en la consola. Recibe el número de columna, la
     * profundidad actual en el árbol y la puntuación para la jugada actual.
     *
     * @param columna     El número de columna de la jugada actual.
     * @param profundidad La profundidad actual en el árbol de juego.
     * @param puntuacion  La puntuación para la jugada actual.
     */
    private void mostrar(int columna, int profundidad, int puntuacion) {
        if (MOSTRAR_ARBOL) {
            String tabulacion = "    ".repeat(profundidad + 1);
            System.err.printf("%sNivel %d | Col: %d | Puntuacion: %s%n", tabulacion, profundidad + 1, columna, puntuacion);
        }
    }
}