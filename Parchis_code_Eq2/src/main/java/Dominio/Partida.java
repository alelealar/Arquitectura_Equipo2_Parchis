package Dominio;

import ENUM.EstadoFicha;
import ENUM.EstadoPartida;
import ENUM.EstadoTurno;
import Modelo.IPartida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /** Indica si el jugador en turno ha obtenido un turno extra. */
    private boolean turnoExtra;

    /** Resultado del último lanzamiento de dado realizado. */
    private int resultadoDado;

    // =========================================================================
    // CONSTRUCTORES
    // =========================================================================

    /**
     * Constructor para inicializar una nueva partida con la lista de jugadores.
     * Instancia el tablero de juego y establece los valores por defecto del marcador.
     *
     * @param idPartida Identificador único asignado a la partida.
     * @param jugadores Lista de jugadores que integrarán la partida.
     */
    public Partida(Long idPartida, List<Jugador> jugadores) {
        this.idPartida = idPartida;
        this.estadoPartida = EstadoPartida.EN_ESPERA;
        this.votosInicio = 0;
        this.indiceJugadorActual = 0;
        this.turnoExtra = false;
        this.resultadoDado = 1;

        // Composición 1..1 con Tablero
        this.tablero = new Tablero();

        this.ordenJugadores = (jugadores != null) ? new ArrayList<>(jugadores) : new ArrayList<>();
        this.marcador = new HashMap<>();

        for (Jugador jugador : ordenJugadores) {
            marcador.put(jugador, 0);
        }

        if (!ordenJugadores.isEmpty()) {
            this.turnoActual = new Turno(ordenJugadores.get(0));
        }
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    /**
     * Obtiene el identificador único de la partida.
     *
     * @return ID de la partida.
     */
    public Long getIdPartida() {
        return idPartida;
    }

    /**
     * Asigna o actualiza el identificador único de la partida.
     *
     * @param idPartida Nuevo ID a establecer.
     */
    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    /**
     * Obtiene el estado actual en el ciclo de vida de la partida.
     *
     * @return El {@link EstadoPartida} actual.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Define el estado actual de la partida.
     *
     * @param estadoPartida Nuevo estado a asignar.
     */
    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    /**
     * Obtiene la cantidad de votos acumulados para dar inicio al juego.
     *
     * @return Número de votos de inicio.
     */
    public int getVotosInicio() {
        return votosInicio;
    }

    /**
     * Asigna el conteo de votos para iniciar la partida.
     *
     * @param votosInicio Cantidad de votos a establecer.
     */
    public void setVotosInicio(int votosInicio) {
        this.votosInicio = votosInicio;
    }

    /**
     * Obtiene el índice de la posición del jugador activo dentro de la lista.
     *
     * @return Índice del jugador actual.
     */
    public int getIndiceJugadorActual() {
        return indiceJugadorActual;
    }

    /**
     * Establece el índice del jugador que posee el turno.
     *
     * @param indiceJugadorActual Nuevo índice en la lista de orden.
     */
    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }

    /**
     * Obtiene la lista ordenada de jugadores participantes.
     *
     * @return Lista de objetos {@link Jugador}.
     */
    public List<Jugador> getOrdenJugadores() {
        return ordenJugadores;
    }

    /**
     * Asigna la lista con el orden de los jugadores de la partida.
     *
     * @param ordenJugadores Lista de jugadores a establecer.
     */
    public void setOrdenJugadores(List<Jugador> ordenJugadores) {
        this.ordenJugadores = ordenJugadores;
    }

    /**
     * Obtiene el mapa del marcador con las puntuaciones registradas.
     *
     * @return Mapa de {@link Jugador} e {@link Integer} con el puntaje.
     */
    public Map<Jugador, Integer> getMarcador() {
        return marcador;
    }

    /**
     * Asigna el mapa del marcador para la partida.
     *
     * @param marcador Mapa de puntuaciones a establecer.
     */
    public void setMarcador(Map<Jugador, Integer> marcador) {
        this.marcador = marcador;
    }

    /**
     * Obtiene la instancia del turno activo.
     *
     * @return Objeto {@link Turno}.
     */
    public Turno getTurnoActual() {
        return turnoActual;
    }

    /**
     * Asigna el objeto de gestión del turno activo.
     *
     * @param turnoActual Nuevo turno a establecer.
     */
    public void setTurnoActual(Turno turnoActual) {
        this.turnoActual = turnoActual;
    }

    /**
     * Obtiene la instancia del tablero de juego.
     *
     * @return Objeto {@link Tablero}.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Asigna el tablero de juego a la partida.
     *
     * @param tablero Objeto tablero a establecer.
     */
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     * Indica si se ha concedido un turno extra al jugador activo.
     *
     * @return {@code true} si el jugador posee turno extra, {@code false} de lo contrario.
     */
    public boolean isTurnoExtra() {
        return turnoExtra;
    }

    /**
     * Define la concesión de un turno extra para el jugador actual.
     *
     * @param turnoExtra Estado a establecer.
     */
    public void setTurnoExtra(boolean turnoExtra) {
        this.turnoExtra = turnoExtra;
    }

    /**
     * Obtiene el resultado numérico obtenido en el último tiro de dado.
     *
     * @return Puntos del dado.
     */
    public int getResultadoDado() {
        return resultadoDado;
    }

    /**
     * Asigna el resultado del dado en la tirada actual.
     *
     * @param resultadoDado Valor obtenido en el dado.
     */
    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }

    // =========================================================================
    // MÉTODOS DEL DIAGRAMA Y DE LA INTERFAZ IPARTIDA
    // =========================================================================

    /**
     * Notifica a las vistas u observadores suscritos sobre los cambios en la partida.
     */
    @Override
    public void notificarObservers() {
        // Notificación hacia la VistaJuego
    }

    /**
     * Concede un turno de lanzamiento adicional al jugador activo.
     */
    @Override
    public void otorgarTurnoExtra() {
        this.turnoExtra = true;
    }

    /**
     * Registra puntos de avance de bonificación en los movimientos pendientes del turno.
     *
     * @param distancia Cantidad de posiciones a agregar como bono.
     */
    @Override
    public void calcularFichasBono(int distancia) {
        if (turnoActual != null && distancia > 0) {
            turnoActual.getMovimientosPendientes().add(distancia);
        }
    }

    /**
     * Aplica la bonificación de pasos especificada.
     *
     * @param extra Cantidad de pasos extra por aplicar.
     */
    @Override
    public void aplicarBono(int extra) {
        calcularFichasBono(extra);
    }

    /**
     * Ejecuta la captura sobre una ficha rival, enviándola a su base y otorgando 20 pasos de bono.
     *
     * @param fichaRival Ficha del oponente capturada.
     */
    @Override
    public void aplicarCaptura(Ficha fichaRival) {
        if (fichaRival == null) {
            return;
        }

        if (tablero != null) {
            tablero.regresarFichaBase(fichaRival);
        }
        aplicarBono(20);

        notificarObservers();
    }

    /**
     * Aplica la penalización enviando la ficha indicada de regreso a su base.
     *
     * @param ficha Ficha a castigar.
     */
    @Override
    public void aplicarPenalizacion(Ficha ficha) {
        if (ficha == null) return;
        if (tablero != null) {
            tablero.regresarFichaBase(ficha);
        }
        notificarObservers();
    }

    /**
     * Simula el tiro del dado, valida la penalización por sacar tres seises consecutivos
     * y registra el valor en el turno.
     *
     * @return El número obtenido en el dado (entre 1 y 6).
     */
    @Override
    public int lanzarDado() {
        resultadoDado = (int) (Math.random() * 6) + 1;

        if (turnoActual.getLanzamientosRealizados() == 2 && resultadoDado == 6) {
            Ficha fichaPenalizada = turnoActual.getUltimaFichaMovida();
            if (fichaPenalizada != null && tablero != null) {
                tablero.regresarFichaBase(fichaPenalizada);
            }

            turnoActual.getMovimientosPendientes().clear();
            turnoActual.setLanzamientosPendientes(0);
            gestionarSiguienteTurno();
            return resultadoDado;
        }

        turnoActual.registrarLanzamiento(resultadoDado);

        if (resultadoDado == 6) {
            otorgarTurnoExtra();
        }

        notificarObservers();
        return resultadoDado;
    }

    /**
     * Evalúa cuáles fichas del jugador en turno tienen un movimiento válido
     * según el dado obtenido y las reglas físicas del Tablero (barreras, base, etc.).
     *
     * @param resultadoDado Puntos obtenidos en la tirada.
     */
    @Override
    public void calcularMovimientosDisponibles(int resultadoDado) {
        if (turnoActual == null || tablero == null) {
            return;
        }

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) {
            return;
        }

        List<Ficha> fichasHabilitadas = new ArrayList<>();

        for (Ficha ficha : jugadorActual.getFichas()) {
            if (ficha == null || ficha.getEstado() == EstadoFicha.EN_META) {
                continue; // Fichas nulas o que ya llegaron a la meta no participan
            }

            switch (ficha.getEstado()) {
                case EN_BASE:
                    if (resultadoDado == tablero.getNumeroSalidaBase() && tablero.verificarCasillaSalida(ficha)) {
                        fichasHabilitadas.add(ficha);
                    }
                    break;

                case EN_TABLERO:
                    if (tablero.verificarTrayectoriaLibre(ficha, resultadoDado)) {
                        fichasHabilitadas.add(ficha);
                    }
                    break;

                case EN_PASILLO:
                    fichasHabilitadas.add(ficha);
                    break;

                default:
                    break;
            }
        }

        if (fichasHabilitadas.isEmpty()) {
            System.out.println("El jugador " + jugadorActual.getNombre() + " no tiene movimientos válidos.");
            if (turnoActual.getMovimientosPendientes() != null && !turnoActual.getMovimientosPendientes().isEmpty()) {
                turnoActual.getMovimientosPendientes().remove(0); // Consume el dado atascado
            }

            if (!turnoActual.tieneAccionesPendientes()) {
                gestionarSiguienteTurno();
            }
        }
    }

    /**
     * Registra la llegada de una ficha a la meta, otorga 100 puntos en el marcador
     * y concede 10 pasos de bonificación.
     *
     * @param fichaLlegada Ficha que alcanzó el centro del tablero.
     */
    @Override
    public void registrarLlegadaMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null || turnoActual == null) {
            return;
        }

        if (tablero != null) {
            tablero.moverAMeta(fichaLlegada);
        }

        calcularYGuardarEnMarcador();
        aplicarBono(10);
        evaluarFinPartida();
        notificarObservers();
    }

    /**
     * Controla el pase de turno al siguiente jugador de la lista o la renovación del turno si se otorgó un turno extra.
     */
    @Override
    public void gestionarSiguienteTurno() {
        if (ordenJugadores == null || ordenJugadores.isEmpty()) return;

        if (turnoExtra) {
            turnoExtra = false;
            turnoActual.setLanzamientosPendientes(1);
            turnoActual.setEstadoTurno(EstadoTurno.ESPERANDO_DADO);
            notificarObservers();
            return;
        }

        if (turnoActual.tieneAccionesPendientes()) return;

        indiceJugadorActual = (indiceJugadorActual + 1) % ordenJugadores.size();
        Jugador siguienteJugador = ordenJugadores.get(indiceJugadorActual);

        this.turnoActual.reiniciarTurno(siguienteJugador);
        notificarObservers();
    }

    /**
     * Evalúa si las 4 fichas del jugador activo han alcanzado la meta para marcar la partida como finalizada.
     */
    @Override
    public void evaluarFinPartida() {
        Jugador actual = turnoActual.getJugadorActual();
        if (actual == null || actual.getFichas() == null) return;

        long fichasEnMeta = actual.getFichas().stream()
                .filter(f -> f.getEstado() == EstadoFicha.EN_META)
                .count();

        if (fichasEnMeta == 4) {
            this.estadoPartida = EstadoPartida.FINALIZADA;
            verificarPartidaFinalizada();
        }
    }

    /**
     * Recorre las fichas de todos los jugadores para calcular su puntuación total acumulada y guardarla en el marcador.
     */
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

    /**
     * Verifica si la partida ha concluido para efectuar el cierre del juego y la actualización del marcador final.
     */
    public void verificarPartidaFinalizada() {
        if (this.estadoPartida == EstadoPartida.FINALIZADA) {
            calcularYGuardarEnMarcador();
            notificarObservers();
        }
    }

    /**
     * Solicita la incorporación de una ficha del jugador activo desde la base hacia la casilla de salida del tablero.
     */
    public void incorporarFichaATablero() {
        if (turnoActual == null || tablero == null) return;

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) return;

        Ficha fichaEnBase = jugadorActual.getFichas().stream()
                .filter(f -> f.getEstado() == EstadoFicha.EN_BASE)
                .findFirst()
                .orElse(null);

        if (fichaEnBase != null) {
            tablero.incorporarFicha(fichaEnBase);
            notificarObservers();
        }
    }

    /**
     * Coordina la consulta y cálculo de trayectorias o posiciones relativas para las fichas en el tablero.
     */
    public void calcularPosiciones() {
        if (tablero != null && turnoActual != null && !turnoActual.getMovimientosPendientes().isEmpty()) {
            int valorDado = turnoActual.getMovimientosPendientes().get(0);
            calcularMovimientosDisponibles(valorDado);
        }
    }

    /**
     * Recibe la solicitud para desplazar la ficha indicada según el dado activo del turno.
     *
     * @param idFicha Identificador único de la ficha a mover.
     */
    @Override
    public void solicitarMovimiento(int idFicha) {
        if (turnoActual.getMovimientosPendientes().isEmpty()) return;

        Jugador jugadorActual = turnoActual.getJugadorActual();
        if (jugadorActual == null || jugadorActual.getFichas() == null) return;

        Ficha fichaAMover = jugadorActual.getFichas().stream()
                .filter(f -> f.getIdFicha() != null && f.getIdFicha().equals((long) idFicha))
                .findFirst()
                .orElse(null);

        if (fichaAMover == null) return;

        int valorDado = turnoActual.getMovimientosPendientes().get(0);

        if (tablero != null) {
            tablero.procesarAvance(fichaAMover, valorDado);
        }

        turnoActual.aplicarMovimiento(valorDado);

        if (!turnoActual.tieneAccionesPendientes()) {
            gestionarSiguienteTurno();
        } else {
            notificarObservers();
        }
    }
}