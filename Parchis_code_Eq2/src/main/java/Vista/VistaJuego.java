package Vista;

import Controlador.ControlInicioPartida;
import Controlador.ControlPartida;
import Dominio.Casilla;
import Dominio.CasillaSalida;
import Dominio.CasillaSegura;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.ColorJugador;
import ENUM.EstadoFicha;
import ENUM.EstadoPartida;
import Modelo.IObserver;
import Modelo.IPartida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class VistaJuego extends JFrame implements IObserver {

    private Partida modelo;
    private ControlPartida controlador;
    private final Map<Long, ControlInicioPartida> controladoresInicio = new HashMap<>();

    private JButton btnLanzarDado;
    private JButton btnMoverFicha;
    private JTextField txtIdFicha;
    private JLabel lblResultado;
    private JLabel lblTurno;
    private JLabel lblMensaje;
    private PanelTablero panelTablero;

    private final Map<Ficha, Point> posicionesFichasPantalla = new HashMap<>();
    private final Map<ColorJugador, Color> paletaVisual = new HashMap<>();
    private final Map<Long, Rectangle> boundsBotonesListo = new HashMap<>();
    private final List<Jugador> ordenVisualLlegada = new ArrayList<>();
    private Rectangle boundsDado;

    private boolean mostrarSeleccionFichas = false;
    private int segundosCuentaRegresiva = 0;
    private Timer timerCuentaRegresiva;

    public VistaJuego() {
        inicializarPaletaPorDefecto();
        configurarVentana();
    }

    public VistaJuego(IPartida modelo) {
        if (modelo instanceof Partida) {
            this.modelo = (Partida) modelo;
            sincronizarOrdenLlegada();
        }
        inicializarPaletaPorDefecto();
        configurarVentana();
    }

    private void sincronizarOrdenLlegada() {
        if (modelo == null || modelo.getOrdenJugadores() == null) return;
        for (Jugador j : modelo.getOrdenJugadores()) {
            if (!ordenVisualLlegada.contains(j)) {
                ordenVisualLlegada.add(j);
            }
        }
    }

    private void inicializarPaletaPorDefecto() {
        paletaVisual.put(ColorJugador.ROJO, new Color(192, 18, 18));
        paletaVisual.put(ColorJugador.AMARILLO, new Color(214, 206, 0));
        paletaVisual.put(ColorJugador.AZUL, new Color(46, 98, 179));
        paletaVisual.put(ColorJugador.VERDE, new Color(78, 138, 36));
    }

    public void cambiarColorVisualJugador(ColorJugador colorLogico, Color nuevoColorVisual) {
        if (colorLogico != null && nuevoColorVisual != null) {
            paletaVisual.put(colorLogico, nuevoColorVisual);
            if (panelTablero != null) {
                panelTablero.repaint();
            }
        }
    }

    public void setModelo(Partida modelo) {
        this.modelo = modelo;
        sincronizarOrdenLlegada();
        repaint();
    }

    public void setControlador(ControlPartida controlador) {
        this.controlador = controlador;
    }

    public void registrarControladorInicio(ControlInicioPartida ctrl) {
        if (ctrl != null && ctrl.getIdJugadorLocal() != null) {
            this.controladoresInicio.put(ctrl.getIdJugadorLocal(), ctrl);
        }
    }

    private void configurarVentana() {
        setTitle("Parchís - Equipo 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 780);
        setLocationRelativeTo(null);
        setResizable(false);

        btnLanzarDado = new JButton("Lanzar Dado");
        btnMoverFicha = new JButton("Mover Ficha");
        txtIdFicha = new JTextField();
        lblResultado = new JLabel("Dado: -");
        lblTurno = new JLabel("Turno: Esperando...");
        lblMensaje = new JLabel("Esperando a que los jugadores confirmen...");

        panelTablero = new PanelTablero();
        setContentPane(panelTablero);
    }

    public void addLanzarDadoListener(ActionListener escuchador) {
        btnLanzarDado.addActionListener(escuchador);
    }

    public void addMoverFichaListener(ActionListener escuchador) {
        btnMoverFicha.addActionListener(escuchador);
    }

    public String getIdFichaTexto() {
        return txtIdFicha.getText();
    }

    private void iniciarCuentaRegresiva() {
        if (timerCuentaRegresiva != null && timerCuentaRegresiva.isRunning()) {
            timerCuentaRegresiva.stop();
        }
        segundosCuentaRegresiva = 3;
        if (panelTablero != null) {
            panelTablero.repaint();
        }

        timerCuentaRegresiva = new Timer(1000, e -> {
            segundosCuentaRegresiva--;
            if (segundosCuentaRegresiva <= 0) {
                segundosCuentaRegresiva = 0;
                timerCuentaRegresiva.stop();
                if (modelo != null && modelo.getTurnoActual() != null && modelo.getTurnoActual().getJugadorActual() != null) {
                    lblMensaje.setText("¡Comienza el juego! Turno de " + modelo.getTurnoActual().getJugadorActual().getNombre());
                }
            }
            if (panelTablero != null) {
                panelTablero.repaint();
            }
        });
        timerCuentaRegresiva.start();
    }

    @Override
    public void actualizar(String evento) {
        sincronizarOrdenLlegada();

        if (modelo != null) {
            lblResultado.setText("Dado: " + modelo.getResultadoDado());
            if (modelo.getTurnoActual() != null && modelo.getTurnoActual().getJugadorActual() != null) {
                lblTurno.setText("Turno: " + modelo.getTurnoActual().getJugadorActual().getNombre());
            }
        }

        switch (evento) {
            case "JUGADOR_UNIDO":
            case "JUGADOR_LISTO":
                mostrarSeleccionFichas = false;
                if (modelo != null && modelo.getOrdenJugadores() != null) {
                    lblTurno.setText("Sala de Espera (" + modelo.getVotosInicio() + "/" + modelo.getOrdenJugadores().size() + ")");
                }
                lblMensaje.setText("Esperando a que todos los jugadores presionen Listo...");
                break;
            case "PARTIDA_INICIADA":
                mostrarSeleccionFichas = false;
                if (modelo != null && modelo.getTurnoActual() != null && modelo.getTurnoActual().getJugadorActual() != null) {
                    lblTurno.setText("Turno: " + modelo.getTurnoActual().getJugadorActual().getNombre());
                    lblMensaje.setText("Orden sorteado. Iniciando partida...");
                } else {
                    lblMensaje.setText("¡Partida iniciada!");
                }
                iniciarCuentaRegresiva();
                break;
            case "INICIO":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("Haz clic en el dado junto a tu avatar para comenzar.");
                break;
            case "DADO_LANZADO":
                mostrarSeleccionFichas = true;
                lblMensaje.setText("Sacaste un " + (modelo != null ? modelo.getResultadoDado() : "") + ". Haz clic en una ficha resaltada.");
                break;
            case "SIN_MOVIMIENTOS":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("Sacaste un " + (modelo != null ? modelo.getResultadoDado() : "") + ". Sin movimientos válidos.");
                break;
            case "FICHA_INCORPORADA":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("¡Ficha incorporada al tablero!");
                break;
            case "FICHA_MOVIDA":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("Ficha movida.");
                break;
            case "CAPTURA_REALIZADA":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("¡Captura! +20 casillas aplicadas automáticamente.");
                break;
            case "FICHA_EN_META":
                boolean tieneMovsMeta = modelo != null && modelo.getFichasDisponibles() != null
                        && !modelo.getFichasDisponibles().isEmpty();
                mostrarSeleccionFichas = tieneMovsMeta;
                lblMensaje.setText("¡Ficha llegó a la meta! +10 movimientos de bono.");
                break;
            case "BLOQUEO_BARRERA":
            case "SALIDA_BLOQUEADA":
                lblMensaje.setText("Movimiento bloqueado por una barrera.");
                break;
            case "PENALIZACION_TRES_SEISES":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("¡Tres 6 seguidos! Ficha regresada a base.");
                break;
            case "PARTIDA_FINALIZADA":
                mostrarSeleccionFichas = false;
                lblMensaje.setText("¡PARTIDA FINALIZADA!");
                JOptionPane.showMessageDialog(this, "¡Tenemos un ganador: "
                        + modelo.getTurnoActual().getJugadorActual().getNombre() + "!");
                break;
            default:
                mostrarSeleccionFichas = false;
                lblMensaje.setText("Evento: " + evento);
                break;
        }

        if (panelTablero != null) {
            panelTablero.iniciarAnimacionMovimiento();
        }
    }

    private class PanelTablero extends JPanel {

        private final Map<Integer, Rectangle2D> rectCasillas = new HashMap<>();
        private final Map<ColorJugador, List<Rectangle2D>> rectPasillos = new HashMap<>();
        private final Map<ColorJugador, List<Point>> puntosBase = new HashMap<>();
        private final Map<ColorJugador, Point> puntosMeta = new HashMap<>();

        private final Map<Ficha, Point2D.Double> posicionesVisuales = new HashMap<>();
        private final Map<Ficha, Integer> ultimaCasillaConocida = new HashMap<>();
        private final Map<Ficha, Queue<Point>> rutasPendientes = new HashMap<>();
        private Timer timerAnimacion;

        private final Color GRIS_SEGURO = new Color(202, 202, 202);

        public PanelTablero() {
            setLayout(null);
            construirGeometriaTablero();
            configurarTimerAnimacion();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    manejarClicPantalla(e.getPoint());
                }
            });
        }

        private void configurarTimerAnimacion() {
            timerAnimacion = new Timer(16, e -> {
                boolean algunaEnMovimiento = false;
                double velocidad = 0.35;

                for (Map.Entry<Ficha, Point> entry : posicionesFichasPantalla.entrySet()) {
                    Ficha f = entry.getKey();
                    Point destinoFinal = entry.getValue();
                    Point2D.Double actual = posicionesVisuales.get(f);

                    if (actual == null) {
                        posicionesVisuales.put(f, new Point2D.Double(destinoFinal.x, destinoFinal.y));
                        continue;
                    }

                    Queue<Point> ruta = rutasPendientes.get(f);
                    Point objetivoActual = (ruta != null && !ruta.isEmpty()) ? ruta.peek() : destinoFinal;

                    double dx = objetivoActual.x - actual.x;
                    double dy = objetivoActual.y - actual.y;
                    double distancia = Math.hypot(dx, dy);

                    if (distancia > 2.0) {
                        actual.x += dx * velocidad;
                        actual.y += dy * velocidad;
                        algunaEnMovimiento = true;
                    } else {
                        actual.x = objetivoActual.x;
                        actual.y = objetivoActual.y;
                        if (ruta != null && !ruta.isEmpty()) {
                            ruta.poll();
                            algunaEnMovimiento = true;
                        }
                    }
                }

                repaint();

                if (!algunaEnMovimiento) {
                    timerAnimacion.stop();
                }
            });
        }

        public void iniciarAnimacionMovimiento() {
            recalcularDestinosFichas();
            if (!timerAnimacion.isRunning()) {
                timerAnimacion.start();
            }
            repaint();
        }

        private void manejarClicPantalla(Point p) {
            if (modelo == null) return;

            if (modelo.getEstadoPartida() == EstadoPartida.EN_ESPERA) {
                for (Map.Entry<Long, Rectangle> entry : boundsBotonesListo.entrySet()) {
                    if (entry.getValue().contains(p)) {
                        Long idJugador = entry.getKey();
                        Jugador jugador = modelo.getOrdenJugadores().stream()
                                .filter(j -> j.getIdJugador() != null && j.getIdJugador().equals(idJugador))
                                .findFirst()
                                .orElse(null);

                        if (jugador != null) {
                            boolean nuevoEstado = !jugador.isListo();
                            ControlInicioPartida ctrl = controladoresInicio.get(idJugador);
                            if (ctrl != null) {
                                ctrl.confirmarInicio(nuevoEstado);
                            } else {
                                modelo.confirmarInicioJugador(idJugador, nuevoEstado);
                            }
                        }
                        return;
                    }
                }
                return;
            }

            if (segundosCuentaRegresiva > 0) return;

            if (boundsDado != null && boundsDado.contains(p)) {
                btnLanzarDado.doClick();
                return;
            }

            if (!mostrarSeleccionFichas) return;

            List<Ficha> disponibles = modelo.getFichasDisponibles();
            if (disponibles == null || disponibles.isEmpty()) return;

            for (Ficha f : disponibles) {
                Point centro = posicionesFichasPantalla.get(f);
                if (centro != null && centro.distance(p) <= 18) {
                    mostrarSeleccionFichas = false;
                    repaint();

                    if (f.getEstado() == EstadoFicha.EN_BASE) {
                        if (controlador != null) {
                            controlador.solicitarIncorporarFicha(f.getIdFicha());
                        }
                    } else {
                        txtIdFicha.setText(String.valueOf(f.getIdFicha()));
                        btnMoverFicha.doClick();
                    }
                    break;
                }
            }
        }

        private void construirGeometriaTablero() {
            int boardX = 280;
            int boardY = 30;
            int boardSize = 660;

            double baseSize = boardSize * 0.36;
            double armWidth = boardSize - 2 * baseSize;
            double colW = armWidth / 3.0;
            double step = baseSize / 8.0;

            double northX = boardX + baseSize;
            rectCasillas.put(34, new Rectangle2D.Double(northX + colW, boardY, colW, step));

            int[] northLeft = {35, 36, 37, 38, 39, 40, 41, 42};
            for (int r = 0; r < 8; r++) {
                rectCasillas.put(northLeft[r], new Rectangle2D.Double(northX, boardY + r * step, colW, step));
            }

            int[] northRight = {33, 32, 31, 30, 29, 28, 27, 26};
            for (int r = 0; r < 8; r++) {
                rectCasillas.put(northRight[r], new Rectangle2D.Double(northX + 2 * colW, boardY + r * step, colW, step));
            }

            List<Rectangle2D> pasilloAmarillo = new ArrayList<>();
            for (int r = 1; r <= 7; r++) {
                pasilloAmarillo.add(new Rectangle2D.Double(northX + colW, boardY + r * step, colW, step));
            }
            rectPasillos.put(ColorJugador.AMARILLO, pasilloAmarillo);

            double southY = boardY + baseSize + armWidth;
            rectCasillas.put(68, new Rectangle2D.Double(northX + colW, southY + 7 * step, colW, step));

            int[] southLeft = {60, 61, 62, 63, 64, 65, 66, 67};
            for (int r = 0; r < 8; r++) {
                rectCasillas.put(southLeft[r], new Rectangle2D.Double(northX, southY + r * step, colW, step));
            }

            int[] southRight = {8, 7, 6, 5, 4, 3, 2, 1};
            for (int r = 0; r < 8; r++) {
                rectCasillas.put(southRight[r], new Rectangle2D.Double(northX + 2 * colW, southY + r * step, colW, step));
            }

            List<Rectangle2D> pasilloRojo = new ArrayList<>();
            for (int r = 6; r >= 0; r--) {
                pasilloRojo.add(new Rectangle2D.Double(northX + colW, southY + r * step, colW, step));
            }
            rectPasillos.put(ColorJugador.ROJO, pasilloRojo);

            double westY = boardY + baseSize;
            rectCasillas.put(51, new Rectangle2D.Double(boardX, westY + colW, step, colW));

            int[] westTop = {50, 49, 48, 47, 46, 45, 44, 43};
            for (int c = 0; c < 8; c++) {
                rectCasillas.put(westTop[c], new Rectangle2D.Double(boardX + c * step, westY, step, colW));
            }

            int[] westBottom = {52, 53, 54, 55, 56, 57, 58, 59};
            for (int c = 0; c < 8; c++) {
                rectCasillas.put(westBottom[c], new Rectangle2D.Double(boardX + c * step, westY + 2 * colW, step, colW));
            }

            List<Rectangle2D> pasilloVerde = new ArrayList<>();
            for (int c = 1; c <= 7; c++) {
                pasilloVerde.add(new Rectangle2D.Double(boardX + c * step, westY + colW, step, colW));
            }
            rectPasillos.put(ColorJugador.VERDE, pasilloVerde);

            double eastX = boardX + baseSize + armWidth;
            rectCasillas.put(17, new Rectangle2D.Double(eastX + 7 * step, westY + colW, step, colW));

            int[] eastTop = {25, 24, 23, 22, 21, 20, 19, 18};
            for (int c = 0; c < 8; c++) {
                rectCasillas.put(eastTop[c], new Rectangle2D.Double(eastX + c * step, westY, step, colW));
            }

            int[] eastBottom = {9, 10, 11, 12, 13, 14, 15, 16};
            for (int c = 0; c < 8; c++) {
                rectCasillas.put(eastBottom[c], new Rectangle2D.Double(eastX + c * step, westY + 2 * colW, step, colW));
            }

            List<Rectangle2D> pasilloAzul = new ArrayList<>();
            for (int c = 6; c >= 0; c--) {
                pasilloAzul.add(new Rectangle2D.Double(eastX + c * step, westY + colW, step, colW));
            }
            rectPasillos.put(ColorJugador.AZUL, pasilloAzul);

            puntosBase.put(ColorJugador.AMARILLO, generarPuntosBase(boardX, boardY, (int) baseSize));
            puntosBase.put(ColorJugador.AZUL, generarPuntosBase((int) eastX, boardY, (int) baseSize));
            puntosBase.put(ColorJugador.ROJO, generarPuntosBase((int) eastX, (int) southY, (int) baseSize));
            puntosBase.put(ColorJugador.VERDE, generarPuntosBase(boardX, (int) southY, (int) baseSize));

            int cx = boardX + boardSize / 2;
            int cy = boardY + boardSize / 2;
            puntosMeta.put(ColorJugador.AMARILLO, new Point(cx, cy - 35));
            puntosMeta.put(ColorJugador.AZUL, new Point(cx + 35, cy));
            puntosMeta.put(ColorJugador.ROJO, new Point(cx, cy + 35));
            puntosMeta.put(ColorJugador.VERDE, new Point(cx - 35, cy));
        }

        private List<Point> generarPuntosBase(int x, int y, int size) {
            int cx = x + size / 2;
            int cy = y + size / 2;
            int off = 32;
            List<Point> pts = new ArrayList<>();
            pts.add(new Point(cx - off, cy - off));
            pts.add(new Point(cx + off, cy - off));
            pts.add(new Point(cx - off, cy + off));
            pts.add(new Point(cx + off, cy + off));
            return pts;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(0, 0, new Color(72, 229, 154), 0, h, new Color(11, 56, 122));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            int boardX = 280;
            int boardY = 30;
            int boardSize = 660;
            int baseSize = (int) (boardSize * 0.36);
            int armWidth = boardSize - 2 * baseSize;

            Color cAmarillo = obtenerColorAwt(ColorJugador.AMARILLO);
            Color cAzul = obtenerColorAwt(ColorJugador.AZUL);
            Color cRojo = obtenerColorAwt(ColorJugador.ROJO);
            Color cVerde = obtenerColorAwt(ColorJugador.VERDE);

            g2.setColor(Color.WHITE);
            g2.fillRect(boardX, boardY, boardSize, boardSize);

            dibujarBaseEsquina(g2, boardX, boardY, baseSize, cAmarillo);
            dibujarBaseEsquina(g2, boardX + baseSize + armWidth, boardY, baseSize, cAzul);
            dibujarBaseEsquina(g2, boardX + baseSize + armWidth, boardY + baseSize + armWidth, baseSize, cRojo);
            dibujarBaseEsquina(g2, boardX, boardY + baseSize + armWidth, baseSize, cVerde);

            pintarListaRectangulos(g2, rectPasillos.get(ColorJugador.AMARILLO), cAmarillo);
            pintarListaRectangulos(g2, rectPasillos.get(ColorJugador.AZUL), cAzul);
            pintarListaRectangulos(g2, rectPasillos.get(ColorJugador.ROJO), cRojo);
            pintarListaRectangulos(g2, rectPasillos.get(ColorJugador.VERDE), cVerde);

            List<Casilla> casillasModelo = (modelo != null && modelo.getTablero() != null)
                    ? modelo.getTablero().getCasillas() : null;

            for (int num = 1; num <= 68; num++) {
                Rectangle2D r = rectCasillas.get(num);
                if (r == null) continue;

                Color colorCasilla = Color.WHITE;

                if (casillasModelo != null && num <= casillasModelo.size()) {
                    Casilla c = casillasModelo.get(num - 1);
                    if (c instanceof CasillaSalida) {
                        colorCasilla = obtenerColorAwt(((CasillaSalida) c).getColorAsociado());
                    } else if (c instanceof CasillaSegura) {
                        colorCasilla = GRIS_SEGURO;
                    }
                } else {
                    if (num == 39) colorCasilla = cAmarillo;
                    else if (num == 22) colorCasilla = cAzul;
                    else if (num == 5) colorCasilla = cRojo;
                    else if (num == 56) colorCasilla = cVerde;
                }

                g2.setColor(colorCasilla);
                g2.fill(r);
                g2.setColor(Color.DARK_GRAY);
                g2.draw(r);

                boolean esFondoClaro = (colorCasilla == Color.WHITE || colorCasilla == GRIS_SEGURO);
                g2.setColor(esFondoClaro ? Color.DARK_GRAY : Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                String txt = String.valueOf(num);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt,
                        (int) (r.getCenterX() - fm.stringWidth(txt) / 2.0),
                        (int) (r.getCenterY() + fm.getAscent() / 2.0 - 2));
            }

            dibujarCentroMeta(g2, boardX + baseSize, boardY + baseSize, armWidth, cAmarillo, cAzul, cRojo, cVerde);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(boardX, boardY, boardSize, boardSize);

            dibujarJugadoresLaterales(g2, w, h);
            dibujarDadoYEstado(g2, w, h);
            dibujarFichas(g2);

            if (segundosCuentaRegresiva > 0) {
                dibujarOverlayCuentaRegresiva(g2, boardX, boardY, boardSize);
            }
        }

        private void dibujarOverlayCuentaRegresiva(Graphics2D g2, int boardX, int boardY, int boardSize) {
            int cx = boardX + boardSize / 2;
            int cy = boardY + boardSize / 2;

            g2.setColor(new Color(10, 22, 45, 175));
            g2.fillRoundRect(cx - 145, cy - 110, 290, 220, 32, 32);

            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(3.5f));
            g2.drawRoundRect(cx - 145, cy - 110, 290, 220, 32, 32);
            g2.setStroke(new BasicStroke(1f));

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            String titulo = "INICIANDO PARTIDA";
            FontMetrics fmTit = g2.getFontMetrics();
            g2.drawString(titulo, cx - fmTit.stringWidth(titulo) / 2, cy - 58);

            g2.setColor(new Color(72, 229, 154));
            g2.setFont(new Font("SansSerif", Font.BOLD, 88));
            String num = String.valueOf(segundosCuentaRegresiva);
            FontMetrics fmNum = g2.getFontMetrics();
            g2.drawString(num, cx - fmNum.stringWidth(num) / 2, cy + 32);

            g2.setColor(new Color(220, 230, 245));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            String sub = "¡Prepárate!";
            FontMetrics fmSub = g2.getFontMetrics();
            g2.drawString(sub, cx - fmSub.stringWidth(sub) / 2, cy + 78);
        }

        private void dibujarBaseEsquina(Graphics2D g2, int x, int y, int size, Color color) {
            g2.setColor(color);
            g2.fillRect(x, y, size, size);

            int pad = 22;
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(x + pad, y + pad, size - 2 * pad, size - 2 * pad);

            int innerPad = 52;
            g2.fillOval(x + innerPad, y + innerPad, size - 2 * innerPad, size - 2 * innerPad);
            g2.setStroke(new BasicStroke(1));
        }

        private void pintarListaRectangulos(Graphics2D g2, List<Rectangle2D> lista, Color color) {
            if (lista == null) return;
            for (Rectangle2D r : lista) {
                g2.setColor(color);
                g2.fill(r);
                g2.setColor(Color.DARK_GRAY);
                g2.draw(r);
            }
        }

        private void dibujarCentroMeta(Graphics2D g2, int x, int y, int size,
                                       Color cNorte, Color cEste, Color cSur, Color cOeste) {
            int cx = x + size / 2;
            int cy = y + size / 2;

            g2.setColor(cNorte);
            g2.fillPolygon(new int[]{x, x + size, cx}, new int[]{y, y, cy}, 3);

            g2.setColor(cEste);
            g2.fillPolygon(new int[]{x + size, x + size, cx}, new int[]{y, y + size, cy}, 3);

            g2.setColor(cSur);
            g2.fillPolygon(new int[]{x, x + size, cx}, new int[]{y + size, y + size, cy}, 3);

            g2.setColor(cOeste);
            g2.fillPolygon(new int[]{x, x, cx}, new int[]{y, y + size, cy}, 3);

            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, size, size);
            g2.drawLine(x, y, x + size, y + size);
            g2.drawLine(x + size, y, x, y + size);
        }

        private Point obtenerPosicionPorColor(ColorJugador color, int w, int h) {
            if (color == null) return new Point(135, 110);
            switch (color) {
                case AMARILLO: // Superior izquierda
                    return new Point(135, 110);
                case AZUL:     // Superior derecha
                    return new Point(w - 215, 110);
                case VERDE:    // Inferior izquierda
                    return new Point(135, h - 220);
                case ROJO:     // Inferior derecha
                default:
                    return new Point(w - 215, h - 220);
            }
        }

        private void dibujarJugadoresLaterales(Graphics2D g2, int w, int h) {
            boundsBotonesListo.clear();
            sincronizarOrdenLlegada();

            if (ordenVisualLlegada.isEmpty()) return;

            Jugador activo = (modelo != null && modelo.getTurnoActual() != null)
                    ? modelo.getTurnoActual().getJugadorActual() : null;
            boolean enEspera = (modelo != null && modelo.getEstadoPartida() == EstadoPartida.EN_ESPERA);

            for (int slot = 0; slot < ordenVisualLlegada.size() && slot < 4; slot++) {
                Jugador jugador = ordenVisualLlegada.get(slot);
                if (jugador == null) continue;

                Point pos = obtenerPosicionPorColor(jugador.getColor(), w, h);
                Color colorSprite = obtenerColorAwt(jugador.getColor());
                boolean enTurno = !enEspera && segundosCuentaRegresiva == 0 && activo != null && activo.equals(jugador);
                boolean esAnfitrion = (slot == 0);

                int ordenTurno = -1;
                if (!enEspera && modelo != null && modelo.getOrdenJugadores() != null) {
                    int idx = modelo.getOrdenJugadores().indexOf(jugador);
                    if (idx >= 0) {
                        ordenTurno = idx + 1;
                    }
                }

                dibujarAvatarJugador(g2, pos.x, pos.y, jugador, colorSprite, enTurno, enEspera, esAnfitrion, ordenTurno);
            }
        }

        private void dibujarAvatarJugador(Graphics2D g2, int x, int y, Jugador jugador,
                                          Color colorSprite, boolean enTurno, boolean enEspera,
                                          boolean esAnfitrion, int ordenTurno) {
            String nombre = jugador.getNombre();

            if (enTurno) {
                g2.setColor(new Color(255, 255, 255, 90));
                g2.fillRoundRect(x - 15, y - 15, 110, 145, 20, 20);
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(x - 15, y - 15, 110, 145, 20, 20);
                g2.setStroke(new BasicStroke(1));
            }

            g2.setColor(Color.BLACK);
            g2.fillRoundRect(x + 10, y + 10, 60, 55, 12, 12);
            g2.setColor(colorSprite);
            g2.fillRoundRect(x + 14, y + 14, 52, 47, 8, 8);
            g2.setColor(Color.BLACK);
            g2.fillRect(x + 25, y + 28, 8, 8);
            g2.fillRect(x + 47, y + 28, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(nombre, x + 40 - fm.stringWidth(nombre) / 2, y + 88);

            if (esAnfitrion && enEspera) {
                g2.setColor(new Color(255, 230, 100));
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                String tag = "ANFITRIÓN";
                FontMetrics fmTag = g2.getFontMetrics();
                g2.drawString(tag, x + 40 - fmTag.stringWidth(tag) / 2, y + 4);
            }

            if (enEspera && jugador.getIdJugador() != null) {
                boolean listo = jugador.isListo();
                Rectangle btnRect = new Rectangle(x - 10, y + 98, 100, 26);
                boundsBotonesListo.put(jugador.getIdJugador(), btnRect);

                g2.setColor(listo ? new Color(46, 160, 67) : new Color(230, 140, 0));
                g2.fillRoundRect(btnRect.x, btnRect.y, btnRect.width, btnRect.height, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(btnRect.x, btnRect.y, btnRect.width, btnRect.height, 12, 12);
                g2.setStroke(new BasicStroke(1f));

                String textoBtn = listo ? "LISTO ✓" : "MARCAR LISTO";
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fmBtn = g2.getFontMetrics();
                g2.drawString(textoBtn,
                        btnRect.x + (btnRect.width - fmBtn.stringWidth(textoBtn)) / 2,
                        btnRect.y + 17);
            } else if (!enEspera && ordenTurno > 0) {
                Rectangle badgeRect = new Rectangle(x - 2, y + 98, 84, 22);
                g2.setColor(new Color(15, 30, 60, 190));
                g2.fillRoundRect(badgeRect.x, badgeRect.y, badgeRect.width, badgeRect.height, 11, 11);
                g2.setColor(enTurno ? Color.YELLOW : new Color(200, 220, 255));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(badgeRect.x, badgeRect.y, badgeRect.width, badgeRect.height, 11, 11);
                g2.setStroke(new BasicStroke(1f));

                String textoOrden = "Turno #" + ordenTurno;
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fmOrd = g2.getFontMetrics();
                g2.drawString(textoOrden,
                        badgeRect.x + (badgeRect.width - fmOrd.stringWidth(textoOrden)) / 2,
                        badgeRect.y + 15);
            }
        }

        private void dibujarDadoYEstado(Graphics2D g2, int w, int h) {
            boolean enEspera = (modelo == null || modelo.getEstadoPartida() == EstadoPartida.EN_ESPERA);

            // Barra inferior de estado siempre visible
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRoundRect(280, 702, 660, 34, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString(lblTurno.getText() + "  |  " + lblMensaje.getText(), 295, 724);

            // El dado no aparece hasta que la partida inicie y termine el contador
            if (enEspera || segundosCuentaRegresiva > 0) {
                boundsDado = null;
                return;
            }

            Jugador jugadorTurno = (modelo.getTurnoActual() != null)
                    ? modelo.getTurnoActual().getJugadorActual() : null;
            boolean debeTirarDado = (modelo.getTurnoActual() != null)
                    && modelo.getTurnoActual().getLanzamientosPendientes() > 0;
            boolean esPrimerTiroTurno = (modelo.getTurnoActual() != null)
                    && modelo.getTurnoActual().getLanzamientosRealizados() == 0;

            ColorJugador colorTurno = (jugadorTurno != null && jugadorTurno.getColor() != null)
                    ? jugadorTurno.getColor() : ColorJugador.AMARILLO;

            int dadoX = 35;
            int dadoY = 105;
            switch (colorTurno) {
                case AMARILLO: // Superior izquierda
                    dadoX = 35;
                    dadoY = 105;
                    break;
                case AZUL:     // Superior derecha
                    dadoX = w - 110;
                    dadoY = 105;
                    break;
                case VERDE:    // Inferior izquierda
                    dadoX = 35;
                    dadoY = h - 225;
                    break;
                case ROJO:     // Inferior derecha
                default:
                    dadoX = w - 110;
                    dadoY = h - 225;
                    break;
            }

            int tam = 72;
            boundsDado = new Rectangle(dadoX, dadoY, tam, tam);

            if (debeTirarDado) {
                g2.setColor(new Color(255, 235, 59, 130));
                g2.fillRoundRect(dadoX - 7, dadoY - 7, tam + 14, tam + 14, 24, 24);
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(dadoX - 7, dadoY - 7, tam + 14, tam + 14, 24, 24);
                g2.setStroke(new BasicStroke(1f));
            }

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(dadoX + 4, dadoY + 6, tam, tam, 18, 18);

            g2.setColor(new Color(215, 215, 222));
            g2.fillRoundRect(dadoX, dadoY + 3, tam, tam, 18, 18);

            GradientPaint gpDado = new GradientPaint(
                    dadoX, dadoY, Color.WHITE,
                    dadoX + tam, dadoY + tam, new Color(238, 238, 242));
            g2.setPaint(gpDado);
            g2.fillRoundRect(dadoX, dadoY, tam, tam, 18, 18);

            g2.setColor(debeTirarDado ? new Color(230, 140, 0) : new Color(60, 60, 65));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(dadoX, dadoY, tam, tam, 18, 18);
            g2.setStroke(new BasicStroke(1f));

            if (debeTirarDado && esPrimerTiroTurno) {
                dibujarSimboloTirar(g2, dadoX, dadoY, tam);
            } else {
                int valor = modelo.getResultadoDado();
                dibujarPuntosDado(g2, dadoX, dadoY, tam, valor);
            }

            g2.setColor(debeTirarDado ? Color.YELLOW : Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            String textoDado = debeTirarDado ? (esPrimerTiroTurno ? "TIRAR" : "TIRO EXTRA") : "DADO";
            FontMetrics fmTxt = g2.getFontMetrics();
            g2.drawString(textoDado, dadoX + (tam - fmTxt.stringWidth(textoDado)) / 2, dadoY + tam + 17);
        }

        private void dibujarSimboloTirar(Graphics2D g2, int x, int y, int tam) {
            g2.setColor(new Color(235, 95, 75));
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(x + 18, y + 16, tam - 36, tam - 36, 40, 265);

            int[] px = {x + tam - 19, x + tam - 13, x + tam - 24};
            int[] py = {y + 24, y + 32, y + 32};
            g2.fillPolygon(px, py, 3);

            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            String signo = "?";
            g2.drawString(signo,
                    x + (tam - fm.stringWidth(signo)) / 2,
                    y + (tam + fm.getAscent()) / 2 - 3);
            g2.setStroke(new BasicStroke(1f));
        }

        private void dibujarPuntosDado(Graphics2D g2, int x, int y, int tam, int valor) {
            int r = 7;
            int c = tam / 2;
            int izq = (int) (tam * 0.26);
            int der = (int) (tam * 0.74);
            int arr = (int) (tam * 0.26);
            int abj = (int) (tam * 0.74);

            Color colorPunto = (valor == 1 || valor == 6) ? new Color(195, 25, 25) : new Color(30, 30, 35);

            List<Point> puntos = new ArrayList<>();
            switch (valor) {
                case 1:
                    puntos.add(new Point(x + c, y + c));
                    break;
                case 2:
                    puntos.add(new Point(x + izq, y + arr));
                    puntos.add(new Point(x + der, y + abj));
                    break;
                case 3:
                    puntos.add(new Point(x + izq, y + arr));
                    puntos.add(new Point(x + c, y + c));
                    puntos.add(new Point(x + der, y + abj));
                    break;
                case 4:
                    puntos.add(new Point(x + izq, y + arr));
                    puntos.add(new Point(x + der, y + arr));
                    puntos.add(new Point(x + izq, y + abj));
                    puntos.add(new Point(x + der, y + abj));
                    break;
                case 5:
                    puntos.add(new Point(x + izq, y + arr));
                    puntos.add(new Point(x + der, y + arr));
                    puntos.add(new Point(x + c, y + c));
                    puntos.add(new Point(x + izq, y + abj));
                    puntos.add(new Point(x + der, y + abj));
                    break;
                case 6:
                    puntos.add(new Point(x + izq, y + arr));
                    puntos.add(new Point(x + der, y + arr));
                    puntos.add(new Point(x + izq, y + c));
                    puntos.add(new Point(x + der, y + c));
                    puntos.add(new Point(x + izq, y + abj));
                    puntos.add(new Point(x + der, y + abj));
                    break;
                default:
                    puntos.add(new Point(x + c, y + c));
                    break;
            }

            for (Point pt : puntos) {
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillOval(pt.x - r, pt.y - r + 1, r * 2, r * 2);

                g2.setColor(colorPunto);
                g2.fillOval(pt.x - r, pt.y - r, r * 2, r * 2);
            }
        }

        private void recalcularDestinosFichas() {
            posicionesFichasPantalla.clear();
            if (modelo == null || modelo.getOrdenJugadores() == null) return;

            Map<Integer, Integer> totalPorCasilla = new HashMap<>();
            for (Jugador j : modelo.getOrdenJugadores()) {
                if (j.getFichas() == null) continue;
                for (Ficha f : j.getFichas()) {
                    if (f.getEstado() == EstadoFicha.EN_TABLERO && f.getCasillaActual() != null) {
                        int num = f.getCasillaActual().getNumeroCasilla();
                        totalPorCasilla.put(num, totalPorCasilla.getOrDefault(num, 0) + 1);
                    }
                }
            }

            Map<Integer, Integer> indiceActualPorCasilla = new HashMap<>();

            for (Jugador j : modelo.getOrdenJugadores()) {
                if (j.getFichas() == null) continue;
                List<Ficha> fichas = j.getFichas();

                for (int idx = 0; idx < fichas.size(); idx++) {
                    Ficha f = fichas.get(idx);
                    Point centro = calcularCentroFicha(f, j.getColor(), idx, totalPorCasilla, indiceActualPorCasilla);
                    if (centro != null) {
                        posicionesFichasPantalla.put(f, centro);
                        actualizarRutaCasillaPorCasilla(f, centro);
                    }
                }
            }
        }

        private void actualizarRutaCasillaPorCasilla(Ficha f, Point destinoFinal) {
            if (f.getEstado() == EstadoFicha.EN_TABLERO && f.getCasillaActual() != null) {
                int numDestino = f.getCasillaActual().getNumeroCasilla();
                Integer numPrevio = ultimaCasillaConocida.get(f);

                if (numPrevio != null && numPrevio != numDestino) {
                    Queue<Point> cola = new ArrayDeque<>();
                    int actual = numPrevio;
                    int seguridad = 0;
                    while (actual != numDestino && seguridad < 68) {
                        actual = (actual % 68) + 1;
                        if (actual == numDestino) {
                            cola.add(destinoFinal);
                        } else {
                            Rectangle2D r = rectCasillas.get(actual);
                            if (r != null) {
                                cola.add(new Point((int) r.getCenterX(), (int) r.getCenterY()));
                            }
                        }
                        seguridad++;
                    }
                    rutasPendientes.put(f, cola);
                }
                ultimaCasillaConocida.put(f, numDestino);
            } else {
                ultimaCasillaConocida.remove(f);
                rutasPendientes.remove(f);
            }
        }

        private void dibujarFichas(Graphics2D g2) {
            recalcularDestinosFichas();
            if (modelo == null || modelo.getOrdenJugadores() == null) return;

            List<Ficha> disponibles = modelo.getFichasDisponibles();

            for (Jugador j : modelo.getOrdenJugadores()) {
                if (j.getFichas() == null) continue;

                for (Ficha f : j.getFichas()) {
                    Point destino = posicionesFichasPantalla.get(f);
                    if (destino == null) continue;

                    Point2D.Double posAnimada = posicionesVisuales.get(f);
                    if (posAnimada == null) {
                        posAnimada = new Point2D.Double(destino.x, destino.y);
                        posicionesVisuales.put(f, posAnimada);
                    }

                    Point centroDibujo = new Point((int) Math.round(posAnimada.x), (int) Math.round(posAnimada.y));

                    boolean esMovible = mostrarSeleccionFichas
                            && (disponibles != null && disponibles.contains(f));

                    dibujarFichaIndividual(g2, centroDibujo, obtenerColorAwt(j.getColor()), f.getIdFicha(), esMovible);
                }
            }
        }

        private Point calcularCentroFicha(Ficha f, ColorJugador color, int indiceEnJugador,
                                          Map<Integer, Integer> totalPorCasilla,
                                          Map<Integer, Integer> indiceActualPorCasilla) {
            if (f.getEstado() == EstadoFicha.EN_BASE) {
                List<Point> pts = puntosBase.get(color);
                if (pts != null && indiceEnJugador < pts.size()) {
                    return pts.get(indiceEnJugador);
                }
            } else if (f.getEstado() == EstadoFicha.EN_TABLERO && f.getCasillaActual() != null) {
                int num = f.getCasillaActual().getNumeroCasilla();
                Rectangle2D r = rectCasillas.get(num);
                if (r != null) {
                    int total = totalPorCasilla.getOrDefault(num, 1);
                    int pos = indiceActualPorCasilla.getOrDefault(num, 0);
                    indiceActualPorCasilla.put(num, pos + 1);

                    if (total <= 1) {
                        return new Point((int) r.getCenterX(), (int) r.getCenterY());
                    }

                    double factor = (pos - (total - 1) / 2.0);
                    if (r.getWidth() >= r.getHeight()) {
                        int sepX = (int) Math.min(22, r.getWidth() * 0.36);
                        return new Point((int) Math.round(r.getCenterX() + factor * sepX), (int) r.getCenterY());
                    } else {
                        int sepY = (int) Math.min(22, r.getHeight() * 0.36);
                        return new Point((int) r.getCenterX(), (int) Math.round(r.getCenterY() + factor * sepY));
                    }
                }
            } else if (f.getEstado() == EstadoFicha.EN_PASILLO) {
                List<Rectangle2D> pasillo = rectPasillos.get(color);
                int paso = Math.max(0, Math.min(f.getPasosDados() - 1, 6));
                if (pasillo != null && paso < pasillo.size()) {
                    Rectangle2D r = pasillo.get(paso);
                    return new Point((int) r.getCenterX(), (int) r.getCenterY());
                }
            } else if (f.getEstado() == EstadoFicha.EN_META) {
                return puntosMeta.get(color);
            }
            return null;
        }

        private void dibujarFichaIndividual(Graphics2D g2, Point centro, Color color, Long id, boolean resaltada) {
            int radio = 11;

            if (resaltada) {
                g2.setColor(new Color(255, 255, 255, 210));
                g2.fillOval(centro.x - radio - 5, centro.y - radio - 5, (radio + 5) * 2, (radio + 5) * 2);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(centro.x - radio - 5, centro.y - radio - 5, (radio + 5) * 2, (radio + 5) * 2);
                g2.setStroke(new BasicStroke(1));
            }

            g2.setColor(color);
            g2.fillOval(centro.x - radio, centro.y - radio, radio * 2, radio * 2);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(centro.x - radio + 3, centro.y - radio + 3, (radio - 3) * 2, (radio - 3) * 2);
            g2.setStroke(new BasicStroke(1));

            g2.setColor(Color.BLACK);
            g2.drawOval(centro.x - radio, centro.y - radio, radio * 2, radio * 2);
        }

        private Color obtenerColorAwt(ColorJugador color) {
            if (color == null) return Color.GRAY;
            return paletaVisual.getOrDefault(color, Color.GRAY);
        }
    }
}