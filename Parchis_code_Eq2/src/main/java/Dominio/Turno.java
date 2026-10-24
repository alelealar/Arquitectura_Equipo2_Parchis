package Dominio;

import ENUM.EstadoTurno;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el turno que realiza un jugador durante el desarrollo de una partida de Parchís.
 * Controla la cantidad de lanzamientos ejecutados y pendientes, la lista de valores de dados
 * por mover, la última ficha desplazada y el cambio de estados en la jugada.
 *
 * @author Equipo 2
 */
public class Turno {

    /** Cantidad máxima de lanzamientos de dado permitidos consecutivamente en un mismo turno. */
    public static final int MAX_LANZAMIENTOS = 3;

    /** Lanzamientos que el jugador tiene pendientes por realizar. */
    private int lanzamientosPendientes;

    /** Lanzamientos que ya ha realizado el jugador en el turno actual. */
    private int lanzamientosRealizados;

    /** Lista con los valores de dados obtenidos pendientes por aplicar en el tablero. */
    private List<Integer> movimientosPendientes;

    /** Estado actual en el que se encuentra el turno del jugador. */
    private EstadoTurno estadoTurno;

    /** Referencia a la última ficha que el jugador desplazó durante el turno. */
    private Ficha ultimaFichaMovida;

    /** Jugador al que le corresponde el turno actual. */
    private Jugador jugadorActual;

    /**
     * Constructor que inicializa un turno con valores por defecto para un jugador.
     *
     * @param jugador Jugador al que se le asigna el turno.
     */
    public Turno(Jugador jugador) {
        this.lanzamientosPendientes = 1;
        this.lanzamientosRealizados = 0;
        this.movimientosPendientes = new ArrayList<>();
        this.estadoTurno = EstadoTurno.ESPERANDO_DADO;
        this.ultimaFichaMovida = null;
        this.jugadorActual = jugador;
    }

    /**
     * Reinicia el turno a sus valores por defecto y lo asigna al siguiente jugador.
     * @param nuevoJugador Jugador que toma el control del nuevo turno.
     */
    public void reiniciarTurno(Jugador nuevoJugador) {
        this.lanzamientosPendientes = 1;
        this.lanzamientosRealizados = 0;
        this.movimientosPendientes.clear();
        this.estadoTurno = EstadoTurno.ESPERANDO_DADO;
        this.ultimaFichaMovida = null;
        this.jugadorActual = nuevoJugador;
    }

    public void cancelarTurno() {
        this.lanzamientosPendientes = 0;
        this.lanzamientosRealizados = 0;
        this.estadoTurno = EstadoTurno.TURNO_FINALIZADO;
    }

    public void registrarLanzamiento() {
        this.lanzamientosRealizados++;
        this.lanzamientosPendientes--;
    }

    public void agregarMovimiento(int valorDado) {
        this.movimientosPendientes.add(valorDado);
    }

    public void bonificarLanzamiento(){
        this.lanzamientosPendientes++;
    }

    /**
     * Registra el valor obtenido tras un tiro de dado, incrementa el historial de tiradas
     * y evalúa si el jugador gana una tirada adicional (al obtener un 6 sin superar el límite).
     *
     * @param valorDado Puntos obtenidos al lanzar el dado.
     */
    public void registrarLanzamientoo(int valorDado) {
        if (lanzamientosRealizados < MAX_LANZAMIENTOS) {
            this.movimientosPendientes.add(valorDado);
            this.lanzamientosRealizados++;

            // Regla: Un 6 otorga un tiro extra siempre que no se haya superado el límite de 3
            if (valorDado == 6 && lanzamientosRealizados < MAX_LANZAMIENTOS) {
                this.lanzamientosPendientes = 1; // Tiro adicional concedido
                this.estadoTurno = EstadoTurno.ESPERANDO_DADO;
            } else {
                this.lanzamientosPendientes = 0; // Sin lanzamientos pendientes
                this.estadoTurno = EstadoTurno.EN_MOVIMIENTO;
            }
        }
    }


    /**
     * Remueve un valor de movimiento pendiente de la lista una vez ejecutado en el tablero.
     * @param valorDado Puntos consumidos por la ficha movida.
     * @return {@code true} si el valor existía y fue removido, {@code false} en caso contrario.
     */
    public boolean aplicarMovimiento(int valorDado) {
        boolean removido = this.movimientosPendientes.remove(Integer.valueOf(valorDado));
        if (this.movimientosPendientes.isEmpty() && this.lanzamientosPendientes == 0) {
            this.estadoTurno = EstadoTurno.TURNO_FINALIZADO;
        }
        return removido;
    }

    /**
     * Indica si el jugador ha agotado sus lanzamientos y no le quedan movimientos pendientes.
     * @return {@code true} si no hay acciones pendientes en el turno.
     */
    public boolean tieneAccionesPendientes() {
        return this.lanzamientosPendientes > 0 || !this.movimientosPendientes.isEmpty();
    }

    // GETTERS Y SETTERS

    /**
     * Obtiene la cantidad de tiradas pendientes que le quedan al jugador en este turno.
     *
     * @return Número de lanzamientos pendientes.
     */
    public int getLanzamientosPendientes() {
        return lanzamientosPendientes;
    }

    /**
     * Asigna la cantidad de lanzamientos pendientes disponibles.
     *
     * @param lanzamientosPendientes Nuevo número de tiradas pendientes.
     */
    public void setLanzamientosPendientes(int lanzamientosPendientes) {
        this.lanzamientosPendientes = lanzamientosPendientes;
    }

    /**
     * Obtiene el número de lanzamientos ejecutados en el turno actual.
     *
     * @return Cantidad de lanzamientos realizados.
     */
    public int getLanzamientosRealizados() {
        return lanzamientosRealizados;
    }

    /**
     * Define el conteo de lanzamientos que han sido realizados.
     *
     * @param lanzamientosRealizados Cantidad a establecer.
     */
    public void setLanzamientosRealizados(int lanzamientosRealizados) {
        this.lanzamientosRealizados = lanzamientosRealizados;
    }

    /**
     * Obtiene la lista de valores de dados que están pendientes por aplicar en movimientos.
     *
     * @return Lista de enteros con los puntos acumulados por mover.
     */
    public List<Integer> getMovimientosPendientes() {
        return movimientosPendientes;
    }

    /**
     * Asigna la lista de valores de dados pendientes por mover.
     *
     * @param movimientosPendientes Nueva lista de enteros con puntos por aplicar.
     */
    public void setMovimientosPendientes(List<Integer> movimientosPendientes) {
        this.movimientosPendientes = movimientosPendientes;
    }

    /**
     * Obtiene el estado actual del turno.
     *
     * @return El estado de tipo {@link EstadoTurno}.
     */
    public EstadoTurno getEstadoTurno() {
        return estadoTurno;
    }

    /**
     * Modifica el estado actual en el que se encuentra el turno.
     *
     * @param estadoTurno Nuevo estado del turno.
     */
    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    /**
     * Obtiene la última ficha que fue desplazada dentro de este turno.
     *
     * @return La última {@link Ficha} movida, o {@code null} si no se ha movido ninguna.
     */
    public Ficha getUltimaFichaMovida() {
        return ultimaFichaMovida;
    }

    /**
     * Guarda la referencia de la última ficha movida en el turno.
     *
     * @param ultimaFichaMovida Ficha recién desplazada.
     */
    public void setUltimaFichaMovida(Ficha ultimaFichaMovida) {
        this.ultimaFichaMovida = ultimaFichaMovida;
    }

    /**
     * Obtiene al jugador que posee el turno activo.
     *
     * @return El {@link Jugador} en turno.
     */
    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    /**
     * Asigna al jugador propietario del turno.
     *
     * @param jugadorActual Jugador a establecer.
     */
    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

}