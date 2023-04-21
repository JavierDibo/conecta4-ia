package plot4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements ActionListener {

    private int movimiento = 0;
    private boolean turnoJ1 = true;
    private boolean jugadorcpu = true;
    private int iaplayer;
    private boolean pulsado;

    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = -1;
    public static final int VACIO = 0;

    public static final int MINIMAX = 1;
    public static final int MINIMAXRES = 2;
    public static final int ALFABETA = 3;

    private static final int FILAS = 6;
    private static final int COLUMNAS = 7;
    private static final int CONECTA = 4;

    private JButton[][] tableroGUI;
    private Grid juego;
    private Player player2;
    private ImageIcon ficha1;
    private ImageIcon ficha2;

    private final JMenuBar barra = new JMenuBar();
    private final JMenu archivo = new JMenu("Archivo");
    private final JMenu opciones = new JMenu("Opciones");
    private final JMenuItem salir = new JMenuItem("Salir");
    private final JRadioButton p1h = new JRadioButton("Humano", true);
    private final JRadioButton p2h = new JRadioButton("Humano", false);
    private final JRadioButton p2c = new JRadioButton("CPU (Greedy)", true);
    private final JRadioButton p2c2 = new JRadioButton("CPU (MiniMax)", false);
    private final JRadioButton p2c3 = new JRadioButton("CPU (MiniMax Restringido)", false);
    private final JRadioButton p2c4 = new JRadioButton("CPU (MiniMax AlfaBeta)", false);
    private String cabecera = "Pr\u00e1cticas de IA (Curso 2022-23)";
    private final JLabel nombre = new JLabel(cabecera, JLabel.CENTER);
    private final String title = "Plot4 - Pr\u00e1cticas de IA (Curso 2022-23)";

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == p2h) {
            jugadorcpu = false;
            reset();
        } else if (ae.getSource() == p2c) {
            jugadorcpu = true;
            iaplayer = 0;
            reset();
        } else if (ae.getSource() == p2c2) {
            jugadorcpu = true;
            iaplayer = MINIMAX;
            reset();
        } else if (ae.getSource() == p2c3) {
            jugadorcpu = true;
            iaplayer = MINIMAXRES;
            reset();
        } else if (ae.getSource() == p2c4) {
            jugadorcpu = true;
            iaplayer = ALFABETA;
            reset();
        } else if (ae.getSource() == salir) {
            dispose();
            System.exit(0);
        } else {
            int x;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if (ae.getSource() == tableroGUI[i][j]) {
                        x = turnoJ1 ? juego.set(j, PLAYER1) : juego.set(j, PLAYER2);
                        if (!(x < 0)) {
                            if (jugadorcpu) {
                                pulsado = true;
                            }
                            turnoJ1 = !turnoJ1;
                            movimiento++;
                            finJuego(juego.checkWin());
                        }
                    }
                }
            }

            if (pulsado) {
                if (jugadorcpu) {
                    pulsado = false;
                    turnoJ1 = !turnoJ1;
                    movimiento++;
                    int pos = player2.turno(juego, CONECTA);
                    juego.set(pos, PLAYER2);
                    finJuego(juego.checkWin());
                }
            }

            String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
            cabecera2 += turnoJ1 ? "Jugador 1" : "Jugador2 ";
            nombre.setText(cabecera2);
        }

    }

    private void updateGrid() {
        System.out.println("Paso: " + movimiento);
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(juego.get(i, j) + " ");
                switch (juego.get(i, j)) {
                    case PLAYER1:
                        tableroGUI[i][j].setIcon(ficha1);
                        break;
                    case PLAYER2:
                        tableroGUI[i][j].setIcon(ficha2);
                        break;
                    default:
                        tableroGUI[i][j].setIcon(null);
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    public void finJuego(int ganador) {
        /**
         * Terminar el juego en caso de finalizar la partida
         * @param ganador El jugador que ha ganado (o 0 si hay empate)
         */
        updateGrid();
        switch (ganador) {
            case PLAYER1:
                System.out.println("Ganador: Jugador 1, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 1\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, ficha1);
                reset();
                break;
            case PLAYER2:
                System.out.println("Ganador: Jugador 2, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 2\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, ficha2);
                reset();
                break;
            default:
                if (movimiento >= FILAS * COLUMNAS) {
                    JOptionPane.showMessageDialog(this, "¡Empate!", "Conecta4", JOptionPane.INFORMATION_MESSAGE);
                    reset();
                }
                break;
        }
    }

    private void reset() {
        /**
         * Reinicia una partida
         */
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tableroGUI[i][j].setIcon(null);
                juego.getGrid()[i][j] = VACIO;
            }
        }
        turnoJ1 = true;
        movimiento = 0;
        pulsado = false;

        System.out.println();
        System.out.print("Nueva partida. Jugador 1: Humano vs Jugador 2: ");
        if (jugadorcpu) {
            switch (iaplayer) {
                case MINIMAX:
                    player2 = new MiniMaxPlayer();
                    System.out.println("CPU (MiniMax)");
                    cabecera = "Juego: Humano vs CPU MiniMax - ";
                    break;
                case MINIMAXRES:
                    player2 = new MiniMaxRestrainedPlayer();
                    System.out.println("CPU (MiniMax Restringido)");
                    cabecera = "Juego: Humano vs CPU MiniMax Restringido - ";
                    break;
                case ALFABETA:
                    player2 = new AlfaBetaPlayer();
                    System.out.println("CPU (MiniMax AlfaBeta)");
                    cabecera = "Juego: Humano vs CPU MiniMax AlfaBeta - ";
                    break;
                default:
                    player2 = new GreedyPlayer();
                    System.out.println("CPU (Greedy)");
                    cabecera = "Juego: Humano vs CPU Greedy - ";
            }
        } else {
            player2 = null;
            System.out.println("Humano");
            cabecera = "Juego: Humano vs Humano - ";
        }
        updateGrid();
        String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
        cabecera2 += turnoJ1 ? "Jugador 1" : "Jugador2 ";
        nombre.setText(cabecera2);
    }

    private void initialize(int i, int j, JButton[][] tablero, Color col) {
        tablero[i][j] = new JButton();
        tablero[i][j].addActionListener(this);
        tablero[i][j].setBackground(col);
    }

    private void run() {
        juego = new Grid(FILAS, COLUMNAS, CONECTA);
        tableroGUI = new JButton[FILAS][COLUMNAS];
        ficha1 = new ImageIcon("assets/player1.png");
        ficha2 = new ImageIcon("assets/player2.png");
        int altoVentana = (FILAS + 1) * ficha1.getIconWidth();
        int anchoVentana = COLUMNAS * ficha2.getIconWidth();

        switch (iaplayer) {
            case MINIMAX:
                player2 = new MiniMaxPlayer();
                break;
            case MINIMAXRES:
                player2 = new MiniMaxRestrainedPlayer();
                break;
            case ALFABETA:
                player2 = new AlfaBetaPlayer();
                break;
            default:
                player2 = new GreedyPlayer();
        }

        salir.addActionListener(this);
        archivo.add(salir);
        ButtonGroup m1Jugador = new ButtonGroup();
        m1Jugador.add(p1h);
        opciones.add(new JLabel("Jugador 1:"));
        opciones.add(p1h);
        opciones.add(new JLabel("Jugador 2:"));
        p2h.addActionListener(this);
        p2c.addActionListener(this);
        p2c2.addActionListener(this);
        p2c3.addActionListener(this);
        p2c4.addActionListener(this);
        ButtonGroup m2Jugador = new ButtonGroup();
        m2Jugador.add(p2h);
        m2Jugador.add(p2c);
        m2Jugador.add(p2c2);
        m2Jugador.add(p2c3);
        m2Jugador.add(p2c4);
        opciones.add(p2h);
        opciones.add(p2c);
        opciones.add(p2c2);
        opciones.add(p2c3);
        opciones.add(p2c4);

        barra.add(archivo);
        barra.add(opciones);
        setJMenuBar(barra);

        JPanel principal = new JPanel();
        principal.setLayout(new GridLayout(FILAS, COLUMNAS));

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                initialize(i, j, tableroGUI, Color.BLACK);
                principal.add(tableroGUI[i][j]);
            }
        }
        nombre.setForeground(Color.BLUE);
        add(nombre, "North");
        add(principal, "Center");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });

        setLocation(170, 25);
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        setTitle(title);
        setVisible(true);
        reset();
    }

    public static void main(String[] args) {
        Main juego = new Main();
        juego.run();
    }
}
