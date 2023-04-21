package plot4;

public class Grid {
    protected final int[][] tablero;

    protected final int filas;

    protected final int columnas;

    protected final int conecta;

    public Grid(int f, int c, int s) {
        filas = f;
        columnas = c;
        tablero = new int[filas][columnas];
        conecta = s;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = 0;
            }
        }
    }


    public Grid(Grid original) {
        filas = original.filas;
        columnas = original.columnas;
        tablero = new int[filas][columnas];
        conecta = original.conecta;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = original.tablero[i][j];
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int[][] getGrid() {
        return tablero;
    }

    public int[][] copyGrid() {
        int[][] copia = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, columnas);
        }

        return copia;
    }

    public int checkWin() {
        int ganador = 0;
        int ganar1;
        int ganar2;
        boolean salir = false;
        for (int i = 0; (i < filas) && !salir; i++) {
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < columnas) && !salir; j++) {
                if (tablero[i][j] != Main.VACIO) {
                    if (tablero[i][j] == Main.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    if (ganar1 >= conecta) {
                        ganador = Main.PLAYER1;
                        salir = true;
                    }
                    if (!salir) {
                        if (tablero[i][j] == Main.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        if (ganar2 >= conecta) {
                            ganador = Main.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
        }
        for (int i = 0; (i < columnas) && !salir; i++) {
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < filas) && !salir; j++) {
                if (tablero[j][i] != Main.VACIO) {
                    if (tablero[j][i] == Main.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    if (ganar1 >= conecta) {
                        ganador = Main.PLAYER1;
                        salir = true;
                    }
                    if (!salir) {
                        if (tablero[j][i] == Main.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        if (ganar2 >= conecta) {
                            ganador = Main.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
        }
        for (int i = 0; i < filas && !salir; i++) {
            for (int j = 0; j < columnas && !salir; j++) {
                int a = i;
                int b = j;
                ganar1 = 0;
                ganar2 = 0;
                while (a < filas && b < columnas && !salir) {
                    if (tablero[a][b] != Main.VACIO) {
                        if (tablero[a][b] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        if (ganar1 >= conecta) {
                            ganador = Main.PLAYER1;
                            salir = true;
                        }
                        if (ganador != Main.PLAYER1) {
                            if (tablero[a][b] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            if (ganar2 >= conecta) {
                                ganador = Main.PLAYER2;
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                    a++;
                    b++;
                }
            }
        }
        for (int i = filas - 1; i >= 0 && !salir; i--) {
            for (int j = 0; j < columnas && !salir; j++) {
                int a = i;
                int b = j;
                ganar1 = 0;
                ganar2 = 0;
                while (a >= 0 && b < columnas && !salir) {
                    if (tablero[a][b] != Main.VACIO) {
                        if (tablero[a][b] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        if (ganar1 >= conecta) {
                            ganador = Main.PLAYER1;
                            salir = true;
                        }
                        if (ganador != Main.PLAYER1) {
                            if (tablero[a][b] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            if (ganar2 >= conecta) {
                                ganador = Main.PLAYER2;
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                    a--;
                    b++;
                }
            }
        }

        return ganador;
    }


    public boolean fullColumn(int col) {
        int x = filas - 1;
        while ((x >= 0) && (tablero[x][col] != Main.VACIO)) {
            x--;
        }

        return (x < 0);
    }


    public int topColumn(int col) {
        int x = filas - 1;
        while ((x >= 0) && (tablero[x][col] != Main.VACIO)) {
            x--;
        }

        if (x < 0) {
            return -2;
        } else {
            return tablero[x][col];
        }

    }


    public int set(int col, int jugador) {

        int x = filas - 1;
        while ((x >= 0) && (tablero[x][col] != 0)) {
            x--;
        }

        if (x >= 0) {
            tablero[x][col] = jugador;
        }

        return x;

    }


    public int get(int x, int y) {
        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
            return tablero[x][y];
        } else {
            return -2;
        }
    }


    public void print() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public int getCount(int player) {
        int count = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }
}