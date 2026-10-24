package Dominio;

import ENUM.EstadoFicha;

/**
 * Representa una ficha de juego dentro de la partida de Parchís.
 * Mantiene el registro de su identificador, su estado actual dentro del juego
 * y el conteo acumulado de casillas/pasos recorridos.
 *
 * @author Equipo 2
 */
public class Ficha {

    /** Identificador único de la ficha. */
    private Long idFicha;

    /** Estado actual de la ficha (EN_BASE, EN_TABLERO, EN_PASILLO, EN_META). */
    private EstadoFicha estadoFicha;

    /** Cantidad total de pasos o casillas que ha avanzado la ficha en la partida. */
    private int pasosDados;

    /** Referencia a la casilla actual donde se encuentra la ficha en el tablero. */
    private Casilla casillaActual;

    // Referencia a JUgador
    private Jugador jugador;

    /**
     * Constructor completo para inicializar una ficha con su ID, estado y pasos recorridos.
     *
     * @param idFicha Identificador único de la ficha.
     * @param estadoFicha Estado inicial de la ficha.
     * @param pasosDados Cantidad inicial de pasos dados.
     */
    public Ficha(Long idFicha, EstadoFicha estadoFicha, int pasosDados) {
        this.idFicha = idFicha;
        this.estadoFicha = estadoFicha;
        this.pasosDados = pasosDados;
    }

    public Ficha(Long idFicha, Jugador jugador) {
        this.idFicha = idFicha;
        this.jugador = jugador;
        this.estadoFicha = EstadoFicha.EN_BASE;
        this.pasosDados = 0;
    }

    /**
     * Constructor por defecto. Inicializa la ficha en la base con 0 pasos recorridos.
     */
    public Ficha() {
        this.estadoFicha = EstadoFicha.EN_BASE;
        this.pasosDados = 0;
    }

    /**
     * Obtiene el identificador único de la ficha.
     *
     * @return ID de la ficha.
     */
    public Long getIdFicha() {
        return idFicha;
    }

    /**
     * Asigna o modifica el identificador de la ficha.
     *
     * @param idFicha Nuevo ID a asignar.
     */
    public void setIdFicha(Long idFicha) {
        this.idFicha = idFicha;
    }

    /**
     * Obtiene el estado actual de la ficha.
     *
     * @return El estado de tipo {@link EstadoFicha}.
     */
    public EstadoFicha getEstado() {
        return estadoFicha;
    }

    /**
     * Asigna directamente el estado de la ficha.
     *
     * @param estadoFicha Estado a establecer.
     */
    public void setEstadoFicha(EstadoFicha estadoFicha) {
        this.estadoFicha = estadoFicha;
    }

    /**
     * Obtiene la cantidad de pasos que ha recorrido la ficha.
     *
     * @return El número de pasos acumulados.
     */
    public int getPasosDados() {
        return pasosDados;
    }

    /**
     * Modifica el contador de pasos dados por la ficha.
     *
     * @param pasosDados Nueva cantidad de pasos acumulados.
     */
    public void setPasosDados(int pasosDados) {
        this.pasosDados = pasosDados;
    }

    /**
     * Obtiene la casilla actual donde se ubica la ficha.
     *
     * @return La casilla ocupada.
     */
    public Casilla getCasillaActual() {
        return casillaActual;
    }

    /**
     * Asigna la casilla actual de la ficha.
     *
     * @param casillaActual Casilla a asignar.
     */
    public void setCasillaActual(Casilla casillaActual) {
        this.casillaActual = casillaActual;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Cambia la posición de la ficha a una nueva casilla dentro del tablero.
     *
     * @param casilla Nueva casilla a la que se desplaza la ficha.
     */
    public void cambiarPosicion(Casilla casilla) {
        this.casillaActual = casilla;
    }

    /**
     * Actualiza el estado actual en el que se encuentra la ficha.
     *
     * @param estadoFicha Nuevo estado a asignar a la ficha.
     */
    public void cambiarEstado(EstadoFicha estadoFicha) {
        this.estadoFicha = estadoFicha;
    }
}