package plot4;

public class MiniMaxRestrainedPlayer extends Player {
    static final int PROFUNDIDAD_MAX = 6;
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

        for (int columna = 0; columna < tablero.getColumnas(); columna++) {
            if (!tablero.fullColumn(columna)) {
                Grid casilla = new Grid(tablero);
                casilla.set(columna, 2);
                int puntuacion = minimax(casilla, conecta, false, 0);
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
                    int puntuacion = minimax(casilla, conecta, false, profundidad + 1);
                    if (MOSTRAR_ARBOL) {
                        mostrar(columna, profundidad, puntuacion);
                    }
                    puntuacionMax = Math.max(puntuacionMax, puntuacion);
                }
            }

            return puntuacionMax;
        } else {
            int puntuacionMin = Integer.MAX_VALUE;

            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (!tablero.fullColumn(columna)) {
                    Grid nuevaCasilla = new Grid(tablero);
                    nuevaCasilla.set(columna, 1);
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
     * Método que calcula la heurística para una posición en el tablero. Recibe el tablero actual, un booleano que
     * indica si se está en un nivel "máximo" o "mínimo" del árbol de juego, y el número de fichas consecutivas
     * necesarias para ganar. Devuelve la puntuación para la posición actual.
     *
     * @param tablero El tablero de  * juego actual.
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