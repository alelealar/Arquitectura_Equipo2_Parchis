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
 * Gestiona la rotación de turnos, ejecución de lanzamientos de dados, aplicación de reglas
 * de negocio (bonos por captura o meta y penalizaciones por triples 6), cálculo del marcador
 * y determinación de victoria.
 *
 * Implementa la interfaz {@link IPartida}.
 *
 * @author Equipo 2
 */
public class Partida implements IPartida {

    /** Identificador único de la partida. */
    private Long idPartida;

    /** Estado actual en el ciclo de vida de la partida. */
    private EstadoPartida estadoPartida;

    /** Conteo de votos de los jugadores para dar inicio a la partida. */
    private int votosInicio;

    /** Índice del jugador en turno dentro de la lista ordenada. */
    private int indiceJugadorActual;

    /** Lista ordenada de jugadores participantes. */
    private List<Jugador> ordenJugadores;

    /** Marcador con la puntuación acumulada de cada jugador. */
    private Map<Jugador, Integer> marcador;

    /** Objeto que gestiona el turno del jugador activo. */
    private Turno turnoActual;

    /** Indica si se ha concedido un turno extra al jugador activo. */
    private boolean turnoExtra;

    /** Último valor de dado obtenido en la tirada. */
    private int resultadoDado;

    /**
     * Constructor para inicializar una nueva partida con los jugadores participantes.
     *
     * @param idPartida Identificador único de la partida.
     * @param jugadores Lista de jugadores que participarán.
     */
    public Partida(Long idPartida, List<Jugador> jugadores) {
        this.idPartida = idPartida;
        this.estadoPartida = EstadoPartida.EN_ESPERA;
        this.votosInicio = 0;
        this.indiceJugadorActual = 0;
        this.turnoExtra = false;
        this.resultadoDado = 1;

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
     * Asigna o actualiza el identificador de la partida.
     *
     * @param idPartida Nuevo ID a establecer.
     */
    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    /**
     * Obtiene el estado actual de la partida.
     *
     * @return El {@link EstadoPartida} actual.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Define el estado actual en el que se encuentra la partida.
     *
     * @param estadoPartida Nuevo estado de la partida.
     */
    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    /**
     * Obtiene la cantidad de votos registrados para iniciar la partida.
     *
     * @return Número de votos de inicio.
     */
    public int getVotosInicio() {
        return votosInicio;
    }

    /**
     * Asigna la cantidad de votos para el inicio de la partida.
     *
     * @param votosInicio Número de votos a establecer.
     */
    public void setVotosInicio(int votosInicio) {
        this.votosInicio = votosInicio;
    }

    /**
     * Obtiene el índice del jugador activo.
     *
     * @return Índice del jugador actual.
     */
    public int getIndiceJugadorActual() {
        return indiceJugadorActual;
    }

    /**
     * Asigna el índice del jugador en turno.
     *
     * @param indiceJugadorActual Nuevo índice en la lista de jugadores.
     */
    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }

    /**
     * Obtiene la lista ordenada de jugadores en la partida.
     *
     * @return Lista de objetos {@link Jugador}.
     */
    public List<Jugador> getOrdenJugadores() {
        return ordenJugadores;
    }

    /**
     * Establece la lista de jugadores que participan en la partida.
     *
     * @param ordenJugadores Lista de jugadores a asignar.
     */
    public void setOrdenJugadores(List<Jugador> ordenJugadores) {
        this.ordenJugadores = ordenJugadores;
    }

    /**
     * Obtiene el mapa del marcador con las puntuaciones de cada jugador.
     *
     * @return Mapa con claves de {@link Jugador} y valores de puntuación.
     */
    public Map<Jugador, Integer> getMarcador() {
        return marcador;
    }

    /**
     * Asigna el mapa del marcador de la partida.
     *
     * @param marcador Mapa con los puntajes de los jugadores.
     */
    public void setMarcador(Map<Jugador, Integer> marcador) {
        this.marcador = marcador;
    }

    /**
     * Obtiene la gestión del turno actual.
     *
     * @return Objeto {@link Turno} activo.
     */
    public Turno getTurnoActual() {
        return turnoActual;
    }

    /**
     * Asigna el objeto del turno activo.
     *
     * @param turnoActual Nuevo objeto de turno.
     */
    public void setTurnoActual(Turno turnoActual) {
        this.turnoActual = turnoActual;
    }

    /**
     * Indica si el jugador activo tiene concedido un turno extra.
     *
     * @return {@code true} si posee turno extra, {@code false} de lo contrario.
     */
    public boolean isTurnoExtra() {
        return turnoExtra;
    }

    /**
     * Define la disponibilidad de un turno extra para el jugador actual.
     *
     * @param turnoExtra Estado a establecer.
     */
    public void setTurnoExtra(boolean turnoExtra) {
        this.turnoExtra = turnoExtra;
    }

    /**
     * Obtiene el resultado del último tiro de dado.
     *
     * @return Valor del dado.
     */
    public int getResultadoDado() {
        return resultadoDado;
    }

    /**
     * Establece el valor del último dado lanzado.
     *
     * @param resultadoDado Resultado del dado a guardar.
     */
    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }

    // =========================================================================
    // MÉTODOS DE LA INTERFAZ IPARTIDA
    // =========================================================================

    /**
     * Notifica a los observadores suscritos sobre cambios en el estado de la partida.
     */
    @Override
    public void notificarObservers() {
        // Implementación según el patrón Observer definido en la arquitectura
    }

    /**
     * Concede un turno adicional al jugador en turno.
     */
    @Override
    public void otorgarTurnoExtra() {
        this.turnoExtra = true;
    }

    /**
     * Agrega una distancia dada como bono a la lista de movimientos pendientes del turno.
     *
     * @param distancia Puntos de avance a otorgar.
     */
    @Override
    public void calcularFichasBono(int distancia) {
        if (turnoActual != null && distancia > 0) {
            turnoActual.getMovimientosPendientes().add(distancia);
        }
    }

    /**
     * Aplica una cantidad determinada de pasos adicionales como bonificación.
     *
     * @param extra Cantidad de pasos de bono.
     */
    @Override
    public void aplicarBono(int extra) {
        calcularFichasBono(extra);
    }

    /**
     * Aplica la regla de captura sobre una ficha enemiga, enviándola a su base y otorgando 20 pasos de bono al capturador.
     *
     * @param fichaRival Ficha del rival capturada.
     */
    @Override
    public void aplicarCaptura(Ficha fichaRival) {
        if (fichaRival == null) {
            return;
        }
        fichaRival.cambiarEstado(EstadoFicha.EN_BASE);
        fichaRival.cambiarPosicion(null);

        aplicarBono(20);
        notificarObservers();
    }

    /**
     * Aplica la penalización a una ficha enviándola a la base sin otorgar bonificaciones.
     *
     * @param ficha Ficha a penalizar.
     */
    @Override
    public void aplicarPenalizacion(Ficha ficha) {
        if (ficha == null) {
            return;
        }
        ficha.cambiarEstado(EstadoFicha.EN_BASE);
        ficha.cambiarPosicion(null);
        notificarObservers();
    }

    /**
     * Simula el tiro de un dado de 6 caras, evalúa reglas de penalización por tres seises consecutivos
     * y registra el lanzamiento en el turno.
     *
     * @return El valor numérico del dado (entre 1 y 6).
     */
    @Override
    public int lanzarDado() {
        resultadoDado = (int) (Math.random() * 6) + 1;

        // Regla de penalización: 3 seises consecutivos envían la última ficha movida a la base
        if (turnoActual.getLanzamientosRealizados() == 2 && resultadoDado == 6) {
            Ficha fichaPenalizada = turnoActual.getUltimaFichaMovida();
            if (fichaPenalizada != null) {
                aplicarPenalizacion(fichaPenalizada);
            }

            turnoActual.getMovimientosPendientes().clear();
            turnoActual.setLanzamientosPendientes(0);
            gestionarSiguienteTurno();
            return resultadoDado;
        }

        turnoActual.registrarLanzamiento(resultadoDado);
        notificarObservers();
        return resultadoDado;
    }

    /**
     * Calcula qué fichas del jugador activo están habilitadas para realizar movimientos válidos según el dado.
     *
     * @param resultadoDado Resultado del dado lanzado.
     */
    @Override
    public void calcularMovimientosDispobibles(int resultadoDado) {
        // Consultas de validación delegadas a la lógica del Tablero
    }

    /**
     * Registra el arribo de una ficha a la meta, otorga puntos al jugador y acredita un bono de 10 pasos.
     *
     * @param fichaLlegada Ficha que alcanzó la meta.
     */
    @Override
    public void registrarLlegadaMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null) {
            return;
        }
        fichaLlegada.cambiarEstado(EstadoFicha.EN_META);

        Jugador actual = turnoActual.getJugadorActual();
        if (marcador.containsKey(actual)) {
            marcador.put(actual, marcador.get(actual) + 100);
        }

        aplicarBono(10);
        evaluarFinPartida();
        notificarObservers();
    }

    /**
     * Coordina el cambio de turno hacia el siguiente jugador o renueva el tiro si existe turno extra concedido.
     */
    @Override
    public void gestionarSiguienteTurno() {
        if (ordenJugadores == null || ordenJugadores.isEmpty()) {
            return;
        }

        if (turnoExtra) {
            turnoExtra = false;
            turnoActual.setLanzamientosPendientes(1);
            turnoActual.setEstadoTurno(EstadoTurno.ESPERANDO_DADO);
            notificarObservers();
            return;
        }

        // Si el jugador aún tiene tiradas o movimientos pendientes, mantiene su turno
        if (turnoActual.getLanzamientosPendientes() > 0 || !turnoActual.getMovimientosPendientes().isEmpty()) {
            return;
        }

        indiceJugadorActual = (indiceJugadorActual + 1) % ordenJugadores.size();
        Jugador siguienteJugador = ordenJugadores.get(indiceJugadorActual);

        this.turnoActual = new Turno(siguienteJugador);
        notificarObservers();
    }

    /**
     * Procesa la solicitud de movimiento enviada desde la interfaz para la ficha seleccionada.
     *
     * @param idFicha Identificador único de la ficha a mover.
     */
    @Override
    public void solicitarMovimiento(int idFicha) {
        if (turnoActual.getMovimientosPendientes().isEmpty()) {
            return;
        }

        int valorDado = turnoActual.getMovimientosPendientes().remove(0);

        if (turnoActual.getLanzamientosPendientes() == 0 && turnoActual.getMovimientosPendientes().isEmpty()) {
            gestionarSiguienteTurno();
        } else {
            notificarObservers();
        }
    }

    /**
     * Evalúa si el jugador actual ha ingresado sus 4 fichas a la meta para finalizar la partida.
     */
    @Override
    public void evaluarFinPartida() {
        Jugador actual = turnoActual.getJugadorActual();
        if (actual == null || actual.getFichas() == null) {
            return;
        }

        long fichasEnMeta = actual.getFichas().stream()
                .filter(f -> f.getEstado() == EstadoFicha.EN_META)
                .count();

        if (fichasEnMeta == 4) {
            this.estadoPartida = EstadoPartida.FINALIZADA;
            verificarFinalizarPartida();
        }
    }

    /**
     * Calcula la puntuación total acumulada por cada jugador a partir del estado y posición de sus fichas.
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
     * Verifica si la partida ha entrado en estado FINALIZADA para realizar el cálculo del marcador y notificar observadores.
     */
    @Override
    public void verificarFinalizarPartida() {
        if (this.estadoPartida == EstadoPartida.FINALIZADA) {
            calcularYGuardarEnMarcador();
            notificarObservers();
        }
    }
}