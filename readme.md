# Actividad 1. Implementación del algoritmo

## MINIMAX

La implementación de un algoritmo MINIMAX puede llevarse a cabo de formas
diferentes, pero lo importante es tener clara la idea de su funcionamiento.

El algoritmo debe generar un árbol de soluciones completo a partir de un nodo
dado, y este árbol se debería desplegar hasta los nodos finales con todas las
posibles combinaciones a lo largo de su desarrollo. Como se puede imaginar, el
coste es muy alto y necesitamos mecanismos que optimicen esta técnica.

* NOTA: Para implementar el algoritmo MINIMAX completo será conveniente
  reducir el tamaño del tablero (`filas`, `columnas`) o el número de fichas a
  conectar (`conecta`), dentro de la clase `Main`. Indicar qué valores se han
  usado.

El objetivo a evaluar será la construcción del árbol MINIMAX sobre cada una de
las jugadas que se vayan realizando por parte del jugador, es decir, cuando el
Jugador 1 (siempre humano) pone una ficha se debe generar un árbol MINIMAX con todas las
posibles jugadas hasta los nodos terminales. El algoritmo debe elegir el
movimiento que, tras propagar la función de utilidad por el árbol, la
maximice. Para facilitar la corrección, se puede desarrollar un método de
visualización, por ejemplo, mediante consola/fichero que presente de una
forma intuitiva la estructura del árbol MINIMAX con las posibles jugadas de la
máquina.

# MAIN

Este código define una clase llamada `Main` que extiende de la clase `JFrame` y también implementa la
interfaz `ActionListener`. La clase contiene varios campos y métodos que se utilizan para crear un juego de Connect 4 (
Conecta-4).

Los campos incluyen:

- `movimiento`: un contador para realizar el seguimiento del número de movimientos realizados.
- `turnoJ1`: un booleano que indica de quién es el turno (true para el jugador 1, false para el jugador 2).
- `jugadorcpu`: un booleano que indica si el jugador 2 es controlado por la CPU o no.
- `iaplayer`: un entero que indica qué tipo de CPU se está utilizando para el jugador 2 (si `jugadorcpu` es verdadero).
- `pulsado`: un booleano que indica si el botón del tablero se ha presionado o no.
- `PLAYER1`, `PLAYER2` y `VACIO`: constantes enteras que representan los diferentes estados de las celdas del tablero.
- `MINIMAX`, `MINIMAXRES` y `ALFABETA`: constantes enteras que representan los diferentes algoritmos de la CPU
  utilizados.
- `FILAS`, `COLUMNAS` y `CONECTA`: constantes enteras que representan las dimensiones del tablero y el número de
  conexiones necesarias para ganar el juego.
- `tableroGUI`: una matriz de botones que representa la interfaz gráfica del tablero del juego.
- `juego`: una instancia de la clase `Grid` que representa el tablero del juego.
- `player2`: una instancia de la clase `Player` que representa al jugador 2 (si `jugadorcpu` es verdadero).
- `ficha1` y `ficha2`: instancias de `ImageIcon` que representan las imágenes de las fichas del jugador 1 y del jugador
    2.
- `barra`, `archivo`, `opciones`, `salir`, `p1h`, `p2h`, `p2c`, `p2c2`, `p2c3`, `p2c4`, `nombre` y `title`: componentes
  de la interfaz gráfica utilizados para mostrar el menú y la información del juego.

Los métodos incluyen:

- `actionPerformed`: un método que se llama cuando se hace clic en uno de los botones de la interfaz gráfica. Este
  método realiza diferentes acciones en función del botón que se haya presionado.
- `updateGrid`: un método que actualiza la interfaz gráfica del tablero en función del estado actual del juego.
- `finJuego`: un método que se llama cuando el juego ha terminado. Este método muestra un mensaje de felicitación o
  empate en función del resultado del juego y reinicia el juego.
- `reset`: un método que reinicia el juego.
- `initialize`: un método auxiliar que inicializa los botones del tablero.
- `run`: un método que se utiliza para crear la interfaz gráfica del juego y ejecutar el juego.
- `main`: el método principal que crea una instancia de `Main` y ejecuta el juego.

Se puede crear una instancia de la clase `Main` y llamar al método `run` en esa instancia para ejecutar el juego.

# GRID

Este código define una clase llamada `Grid` que representa un tablero de juego. La clase tiene los siguientes campos:

* `tablero`: una matriz bidimensional de enteros que representa el contenido del tablero.
* `filas`: un entero que indica la cantidad de filas del tablero.
* `columnas`: un entero que indica la cantidad de columnas del tablero.
* `conecta`: un entero que indica cuántas fichas deben estar conectadas para ganar el juego.

La clase tiene dos constructores:

* `Grid(int f, int c, int s)`: un constructor que inicializa un tablero vacío con la cantidad de filas, columnas y
  conecta especificados.
* `Grid(Grid original)`: un constructor que crea una copia de otro objeto `Grid`.

La clase también tiene los siguientes métodos públicos:

* `getFilas()`: devuelve la cantidad de filas del tablero.
* `getColumnas()`: devuelve la cantidad de columnas del tablero.
* `getGrid()`: devuelve una referencia a la matriz `tablero`.
* `copyGrid()`: devuelve una copia de la matriz `tablero`.
* `checkWin()`: comprueba si hay un ganador en el tablero y devuelve el número del jugador ganador (1 o 2), o 0 si no
  hay ganador.
* `fullColumn(int col)`: comprueba si la columna especificada está llena.
* `topColumn(int col)`: devuelve el número de jugador que tiene la ficha en la parte superior de la columna
  especificada, o -2 si la columna está vacía.
* `set(int col, int jugador)`: coloca una ficha del jugador especificado en la columna especificada y devuelve el índice
  de fila donde se colocó la ficha, o -1 si la columna está llena.
* `get(int x, int y)`: devuelve el número de jugador que tiene la ficha en la posición especificada, o -2 si la posición
  está fuera de los límites del tablero.
* `print()`: imprime el contenido del tablero en la consola.
* `getCount(int player)`: devuelve la cantidad de fichas del jugador especificado en el tablero.

Por ejemplo, para crear un objeto `Grid` con 6 filas, 7 columnas y conecta 4, se puede usar el siguiente código:

`Grid tablero = new Grid(6, 7, 4);`

Luego, se pueden llamar a los métodos públicos de la clase para interactuar con el tablero. Por ejemplo, para obtener la
cantidad de filas del tablero, se puede llamar al método `getFilas()`:

`int filas = tablero.getFilas();`

Para colocar una ficha del jugador 1 en la columna 3, se puede llamar al método `set()`:

`int fila = tablero.set(3, 1);`

Si la columna está llena, el método `set()` devolverá -1 y no colocará la ficha.