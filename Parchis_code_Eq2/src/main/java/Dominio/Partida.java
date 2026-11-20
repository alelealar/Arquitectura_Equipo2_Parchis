package Dominio;

import ENUM.EstadoFicha;
import ENUM.EstadoPartida;
import Modelo.IObserver;
import Modelo.IPartida;

import java.util.*;

/**
 * Entidad principal del dominio que orquesta el flujo completo de una partida de Parchís.
 * Controla el ciclo de vida del juego, los turnos de los participantes, el lanzamiento
 * de dados, las reglas de negocio (capturas, penalizaciones por seises y bonificaciones)
 * y el cálculo de la puntuación en el marcador.
 *
 * Implementa el contrato definido por la interfaz {@link IPartida}.
 *
 * @author Equipo 2
 */
public class Partida implements IPartida {

    /** Identificador único de la partida. */
    private Long idPartida;

    /** Estado actual en el que se encuentra la partida (EN_ESPERA, EN_CURSO, FINALIZADA). */
    private EstadoPartida estadoPartida;

    /** Cantidad de votos registrados por los jugadores para iniciar la partida. */
    private int votosInicio;

    /** Índice del jugador activo dentro de la lista de orden de juego. */
    private int indiceJugadorActual;

    /** Lista ordenada con los jugadores que participan en la partida. */
    private List<Jugador> ordenJugadores;

    /** Mapa que almacena la puntuación acumulada de cada jugador. */
    private Map<Jugador, Integer> marcador;

    /** Objeto que gestiona las acciones del turno actual. */
    private Turno turnoActual;

    /** Tablero de juego asociado a la partida (Composición 1..1). */
    private Tablero tablero;

    /** Configuración personalizada de la partida (Composición 1..1). */
    private ConfiguracionPartida configuracionPartida;

    /** Indica si el jugador en turno ha obtenido un turno extra. */
    private boolean turnoExtra;

    /** Resultado del último lanzamiento de dado realizado. */
    private int resultadoDado;

    /** Referencia a la vista suscrita para recibir notificaciones (Patrón Observer). */
    private IObserver vistaSuscrita;

    private List<Ficha> fichasDisponibles;

    public Partida(Long idPartida, List<Jugador> jugadores) {
        this(idPartida, jugadores, new ConfiguracionPartida());
    }

    public Partida(Long idPartida, List<Jugador> jugadores, ConfiguracionPartida configuracion) {
        this.idPartida = idPartida;
        this.estadoPartida = EstadoPartida.EN_ESPERA;
        this.votosInicio = 0;
        this.indiceJugadorActual = 0;
        this.turnoExtra = false;
        this.resultadoDado = 1;
        this.configuracionPartida = (configuracion != null) ? configuracion : new ConfiguracionPartida();
        this.tablero = new Tablero(this.configuracionPartida);
        this.ordenJugadores = (jugadores != null) ? new ArrayList<>(jugadores) : new ArrayList<>();
        this.marcador = new HashMap<>();

        long contadorIdFicha = 1L;
        for (Jugador jugador : ordenJugadores) {
            marcador.put(jugador, 0);
            if (jugador.getFichas() == null || jugador.getFichas().isEmpty()) {
                List<Ficha> fichasJugador = new ArrayList<>();
                for (int i = 0; i < this.configuracionPartida.getFichasPorJugador(); i++) {
                    Ficha ficha = new Ficha(contadorIdFicha++, jugador);
                    fichasJugador.add(ficha);
                }
                jugador.setFichas(fichasJugador);
            }
        }

        if (!ordenJugadores.isEmpty()) {
            this.turnoActual = new Turno(ordenJugadores.get(0));
        }

        this.fichasDisponibles = new LinkedList<>();
    }

    public Partida(Long idPartida) {
        this.idPartida = idPartida;
    }

    @Override
    public void agregarObserver(IObserver observador) {
        this.vistaSuscrita = observador;
        System.out.println("MODELO: Vista conectada exitosamente al Observer.");
    }

    @Override
    public void notificarObservers(String evento) {
        if (vistaSuscrita != null) {
            vistaSuscrita.actualizar(evento);
        }
    }

    @Override
    public int lanzarDado() {
        if (turnoActual == null || turnoActual.getLanzamientosPendientes() < 1) {
            return 0;
        }

        resultadoDado = (int) (Math.random() * 6) + 1;

        turnoActual.registrarLanzamiento();

        if (resultadoDado == 6 && turnoActual.getLanzamientosRealizados() >= Turno.MAX_LANZAMIENTOS) {
            Ficha ultimaFicha = turnoActual.getUltimaFichaMovida();
            if (ultimaFicha != null && tablero != null) {
                tablero.regresarFichaBase(ultimaFicha);
            }
            turnoActual.cancelarTurno();
            gestionarSiguienteTurno();
            notificarObservers("PENALIZACION_TRES_SEISES");
            return resultadoDado;
        } else {
            turnoActual.agregarMovimiento(resultadoDado);

            if (resultadoDado == 6) {
                otorgarTurnoExtra();
            } else {
                this.turnoExtra = false;
            }

            calcularMovimientosDisponibles(resultadoDado);
        }

        if (fichasDisponibles != null && !fichasDisponibles.isEmpty()) {
            notificarObservers("DADO_LANZADO");
        } else {
            notificarObservers("SIN_MOVIMIENTOS");
        }

        return resultadoDado;
    }

    @Override
    public void gestionarSiguienteTurno() {
        if (turnoActual.getLanzamientosPendientes() < 1 && turnoActual.getMovimientosPendientes().isEmpty()) {
            indiceJugadorActual = (indiceJugadorActual + 1) % ordenJugadores.size();
            Jugador siguienteJugador = ordenJugadores.get(indiceJugadorActual);
            this.turnoActual.reiniciarTurno(siguienteJugador);
        }
    }

    @Override
    public void otorgarTurnoExtra() {
        this.turnoExtra = true;
        if (turnoActual != null) {
            turnoActual.bonificarLanzamiento();
        }
    }

    @Override
    public void calcularFichasBono(int distancia) {
        if (turnoActual != null && distancia > 0) {
            turnoActual.getMovimientosPendientes().add(distancia);
        }
    }

    @Override
    public void aplicarBono(int extra) {
        calcularFichasBono(extra);
    }

    @Override
    public void aplicarCaptura(Ficha fichaRival) {
        if (fichaRival == null) {
            return;
        }

        if (tablero != null) {
            tablero.regresarFichaBase(fichaRival);
        }

        Ficha fichaCapturadora = (turnoActual != null) ? turnoActual.getUltimaFichaMovida() : null;
        if (fichaCapturadora != null && tablero != null && tablero.verificarTrayectoriaLibre(fichaCapturadora, 20)) {
            if (fichaCapturadora.getEstado() == EstadoFicha.EN_PASILLO) {
                tablero.aplicarMovimientoConRebote(fichaCapturadora, 20);
            } else {
                tablero.procesarAvance(fichaCapturadora, 20);
            }
        }

        if (turnoActual != null && !turnoActual.tieneAccionesPendientes()) {
            gestionarSiguienteTurno();
        }

        notificarObservers("CAPTURA_REALIZADA");
    }

    @Override
    public void aplicarPenalizacion(Ficha ficha) {
        if (ficha == null) return;
        if (tablero != null) {
            tablero.regresarFichaBase(ficha);
        }
    }

    @Override
    public void calcularMovimientosDisponibles(int resultadoDado) {
        this.fichasDisponibles = new ArrayList<>();

        if (turnoActual == null || tablero == null) {
            return;
        }

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) {
            return;
        }

        for (Ficha ficha : jugadorActual.getFichas()) {
            if (ficha == null || ficha.getEstado() == EstadoFicha.EN_META) {
                continue;
            }

            switch (ficha.getEstado()) {
                case EN_BASE:
                    String estadoSalida = tablero.verificarCasillaSalida(ficha);
                    if (resultadoDado == tablero.getNumeroSalidaBase() && !"Bloqueada".equals(estadoSalida)) {
                        this.fichasDisponibles.add(ficha);
                    }
                    break;

                case EN_TABLERO:
                    if (tablero.verificarTrayectoriaLibre(ficha, resultadoDado)) {
                        this.fichasDisponibles.add(ficha);
                    }
                    break;

                case EN_PASILLO:
                    this.fichasDisponibles.add(ficha);
                    break;

                default:
                    break;
            }
        }

        if (this.fichasDisponibles.isEmpty()) {
            if (turnoActual.getMovimientosPendientes() != null && !turnoActual.getMovimientosPendientes().isEmpty()) {
                turnoActual.getMovimientosPendientes().remove(Integer.valueOf(resultadoDado));
            }
            if (!turnoActual.tieneAccionesPendientes()) {
                gestionarSiguienteTurno();
            }
        }
    }

    @Override
    public void registrarLlegadaMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null || turnoActual == null) {
            return;
        }

        if (tablero != null) {
            tablero.moverAMeta(fichaLlegada);
        }

        evaluarFinPartida();
        if (this.estadoPartida == EstadoPartida.FINALIZADA) {
            calcularPosiciones();
            calcularYGuardarEnMarcador();
            notificarObservers("PARTIDA_FINALIZADA");
            return;
        }

        aplicarBono(10);
        calcularMovimientosDisponibles(10);

        if (fichasDisponibles != null && !fichasDisponibles.isEmpty()) {
            notificarObservers("FICHA_EN_META");
        } else {
            if (!turnoActual.tieneAccionesPendientes()) {
                gestionarSiguienteTurno();
            }
            notificarObservers("FICHA_MOVIDA");
        }
    }

    @Override
    public void evaluarFinPartida() {
        if (turnoActual == null) return;
        Jugador actual = turnoActual.getJugadorActual();
        if (actual == null || actual.getFichas() == null) return;

        int fichasEnMeta = 0;
        for (Ficha f : actual.getFichas()) {
            if (f.getEstado() == EstadoFicha.EN_META) {
                fichasEnMeta++;
            }
        }

        if (fichasEnMeta == actual.getFichas().size()) {
            this.estadoPartida = EstadoPartida.FINALIZADA;
        }
    }

    @Override
    public void calcularYGuardarEnMarcador() {
        for (Jugador jugador : ordenJugadores) {
            int puntajeTotal = 0;
            if (jugador.getFichas() != null) {
                for (Ficha ficha : jugador.getFichas()) {
                    if (ficha.getEstado() == EstadoFicha.EN_META) {
                        puntajeTotal += 100;
                    } else if (ficha.getEstado() == EstadoFicha.EN_TABLERO) {
                        puntajeTotal += ficha.getPasosDados();
                    }
                }
            }
            marcador.put(jugador, puntajeTotal);
        }
    }

    @Override
    public void verificarPartidaFinalizada() {
        evaluarFinPartida();

        if (this.estadoPartida == EstadoPartida.FINALIZADA) {
            calcularPosiciones();
            calcularYGuardarEnMarcador();
            notificarObservers("PARTIDA_FINALIZADA");
        } else {
            if (!turnoActual.tieneAccionesPendientes()) {
                gestionarSiguienteTurno();
            }
        }
    }

    @Override
    public void incorporarFichaATablero(Long idFicha) {
        if (turnoActual == null || tablero == null) return;

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) return;

        Ficha fichaElegida = null;
        for (Ficha f : jugadorActual.getFichas()) {
            if (f.getIdFicha() != null && f.getIdFicha().equals(idFicha) && f.getEstado() == EstadoFicha.EN_BASE) {
                fichaElegida = f;
                break;
            }
        }

        if (fichaElegida != null) {
            String resultadoCasilla = tablero.verificarCasillaSalida(fichaElegida);

            if ("Bloqueada".equals(resultadoCasilla)) {
                notificarObservers("SALIDA_BLOQUEADA");
            } else {
                tablero.procesarAvance(fichaElegida, this.resultadoDado);
                turnoActual.aplicarMovimiento(this.resultadoDado);
                turnoActual.setUltimaFichaMovida(fichaElegida);

                Ficha fichaRival = null;
                if ("Rival".equals(resultadoCasilla) && fichaElegida.getCasillaActual() != null) {
                    fichaRival = tablero.obtenerFichaRivalEnCasilla(fichaElegida.getCasillaActual(), fichaElegida);
                }

                if (fichaRival != null) {
                    aplicarCaptura(fichaRival);
                } else {
                    if (!turnoActual.tieneAccionesPendientes()) {
                        gestionarSiguienteTurno();
                    }
                    notificarObservers("FICHA_INCORPORADA");
                }
            }
        }
    }

    public void calcularPosiciones() {
        if (this.estadoPartida == EstadoPartida.FINALIZADA) {
            for (Jugador j : ordenJugadores) {
                if (j.getFichas() != null) {
                    for (Ficha f : j.getFichas()) {
                        f.getPasosDados();
                    }
                }
            }
            return;
        }

        if (tablero != null && turnoActual != null && !turnoActual.getMovimientosPendientes().isEmpty()) {
            int valorDado = turnoActual.getMovimientosPendientes().get(0);
            calcularMovimientosDisponibles(valorDado);
        }
    }

    @Override
    public void solicitarMovimiento(int idFicha) {
        if (turnoActual == null || turnoActual.getMovimientosPendientes().isEmpty()) return;

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) return;

        Ficha fichaAMover = jugadorActual.getFichas().stream()
                .filter(f -> f.getIdFicha() != null && f.getIdFicha().equals((long) idFicha))
                .findFirst()
                .orElse(null);

        if (fichaAMover == null) {
            System.out.println("MODELO: Movimiento rechazado. La ficha con ID " + idFicha + " no existe o no le pertenece al jugador actual.");
            return;
        }
        if (tablero == null) {
            System.out.println("MODELO: Error crítico. El tablero no ha sido inicializado.");
            return;
        }

        int valorDado = turnoActual.getMovimientosPendientes().get(0);
        boolean trayectoriaLibre = tablero.verificarTrayectoriaLibre(fichaAMover, valorDado);

        if (trayectoriaLibre) {
            if (fichaAMover.getEstado() == EstadoFicha.EN_PASILLO) {
                tablero.aplicarMovimientoConRebote(fichaAMover, valorDado);
            } else {
                tablero.procesarAvance(fichaAMover, valorDado);
            }

            turnoActual.setUltimaFichaMovida(fichaAMover);
            turnoActual.aplicarMovimiento(valorDado);

            Casilla casillaDestino = fichaAMover.getCasillaActual();
            String estadoCasilla = tablero.evaluarCasillaDestino(casillaDestino, fichaAMover);

            if ("CAPTURA".equals(estadoCasilla)) {
                Ficha fichaRival = tablero.obtenerFichaRivalEnCasilla(casillaDestino, fichaAMover);
                aplicarCaptura(fichaRival);
            } else if ("CasillaMeta".equals(estadoCasilla)) {
                registrarLlegadaMeta(fichaAMover);
            } else {
                if (!turnoActual.tieneAccionesPendientes()) {
                    gestionarSiguienteTurno();
                }
                notificarObservers("FICHA_MOVIDA");
            }
        } else {
            notificarObservers("BLOQUEO_BARRERA");
        }
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public int getVotosInicio() {
        return votosInicio;
    }

    public void setVotosInicio(int votosInicio) {
        this.votosInicio = votosInicio;
    }

    public int getIndiceJugadorActual() {
        return indiceJugadorActual;
    }

    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }

    public List<Jugador> getOrdenJugadores() {
        return ordenJugadores;
    }

    public void setOrdenJugadores(List<Jugador> ordenJugadores) {
        this.ordenJugadores = ordenJugadores;
    }

    public Map<Jugador, Integer> getMarcador() {
        return marcador;
    }

    public void setMarcador(Map<Jugador, Integer> marcador) {
        this.marcador = marcador;
    }

    public List<Ficha> getFichasDisponibles() {
        return fichasDisponibles;
    }

    public void setFichasDisponibles(List<Ficha> fichas) {
        this.fichasDisponibles = fichas;
    }

    @Override
    public Turno getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Turno turnoActual) {
        this.turnoActual = turnoActual;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public ConfiguracionPartida getConfiguracionPartida() {
        return configuracionPartida;
    }

    public void setConfiguracionPartida(ConfiguracionPartida configuracionPartida) {
        this.configuracionPartida = configuracionPartida;
    }

    public boolean isTurnoExtra() {
        return turnoExtra;
    }

    public void setTurnoExtra(boolean turnoExtra) {
        this.turnoExtra = turnoExtra;
    }

    @Override
    public int getResultadoDado() {
        return resultadoDado;
    }

    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }
}