
package Dominio;

import ENUM.EstadoTurno;

/**
 * Representa un turno que realiza cada jugador
 * dentro de la partida en parchis.
 * @author Julian
 */
public class Turno {
    
    /**Lanzamientos que el jugador tiene pendientes por realizar */
    private int lanzamientosPendientes;
    
    /*** Lanzamientos que ya ha realizado el jugador */
    private int lanzamientosRealizados;
    
    /**Movimientos que el jugador tiene pendientes de realizar */
    private int movimientosPendientes;
    
    /** Estado en el que se encuentra el turno del jugador */
    private EstadoTurno estadoTurno;
    
    /** Ultima ficha que el jugador ha movido en el tablero*/
    private Ficha ultimaFichaMovida;
    
    /** El jugador actual que tiene el turno */
    private Jugador jugadorActual;
    
    /** Constructor default que inicializa todo a sus valores por defecto.
     * Solamente el jugador actual es el que se recibe.
     * @param jugador actual con el turno
     */
    public Turno(Jugador jugador) {
        this.lanzamientosPendientes = 1;
        this.lanzamientosRealizados = 0;
        this.movimientosPendientes = 0;
        this.estadoTurno = EstadoTurno.ESPERANDO_DADO;
        this.ultimaFichaMovida = null;
        this.jugadorActual = jugador;
    }
    
    /**
     * Constructos completo para inicializar un Turno, con sus lanzamientos pendientes,
     * lanzamientos realizados, movimientos pendientes, su estado, la ultima ficha que se movio
     * y el jugador actual con el turno
     * @param lanzamientosPendientes Lanzamientos que el jugador tiene pendientes por realizar
     * @param lanzamientosRealizados Lanzamientos que ya ha realizado el jugador
     * @param movimientosPendientes Movimientos que el jugador tiene pendientes de realizar 
     * @param estadoTurno Estado en el que se encuentra el turno del jugador
     * @param ultimaFichaMovida Ultima ficha que el jugador ha movido en el tablero
     * @param jugador El jugador actual que tiene el turno 
     */
    public Turno(int lanzamientosPendientes, int lanzamientosRealizados, int movimientosPendientes, EstadoTurno estadoTurno, Ficha ultimaFichaMovida, Jugador jugador) {
        this.lanzamientosPendientes = lanzamientosPendientes;
        this.lanzamientosRealizados = lanzamientosRealizados;
        this.movimientosPendientes = movimientosPendientes;
        this.estadoTurno = estadoTurno;
        this.ultimaFichaMovida = ultimaFichaMovida;
        this.jugadorActual = jugador;
    }

    /**
     * Obtiene los lanzamientos pendientes en el turno.
     * @return el numero de los lanzamientos que estan pendientes
     */
    public int getLanzamientosPendientes() {
        return lanzamientosPendientes;
    }
    
    /**
     * Asigna los lanzamientos pendientes al turno 
     * @param lanzamientosPendientes numero de lanzamientos pendientes que se asignaran
     */
    public void setLanzamientosPendientes(int lanzamientosPendientes) {
        this.lanzamientosPendientes = lanzamientosPendientes;
    }

    /**
     * Obtiene los lanzamientos realizados en el turno
     * @return el numero de los lanzamientos que ya se han realizado
     */
    public int getLanzamientosRealizados() {
        return lanzamientosRealizados;
    }
    
    /**
     * Asigna el numero de los lanzamientos realizados en el turno.
     * @param lanzamientosRealizados numero de lanzamientos realizados que se asignaran
     */
    public void setLanzamientosRealizados(int lanzamientosRealizados) {
        this.lanzamientosRealizados = lanzamientosRealizados;
    }
    
    /**
     * Obtiene los movimientos pendientes en el turno
     * @return la cantidad de movimientos pendientes
     */
    public int getMovimientosPendientes() {
        return movimientosPendientes;
    }

    /**
     * Asigna los movimientos pendientes en el turno
     * @param movimientosPendientes numero de movimientos pendientes que se asignaran
     */
    public void setMovimientosPendientes(int movimientosPendientes) {
        this.movimientosPendientes = movimientosPendientes;
    }

    /**
     * Obtiene el estado en el que se encuentra el turno 
     * @return el estado del turno 
     */
    public EstadoTurno getEstadoTurno() {
        return estadoTurno;
    }

    /**
     * Asigna el estado a un turno 
     * @param estadoTurno el estado que se le asignara al turno 
     */
    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }
    
    /**
     * Obtiene la ultima ficha que se haya movido
     * @return la ultima ficha que se movio 
     */
    public Ficha getUltimaFichaMovida() {
        return ultimaFichaMovida;
    }

    /**
     * Asigna la ultima ficha que se movio a un turno 
     * @param ultimaFichaMovida la ultima ficha que se le desea asignar al turno
     */
    public void setUltimaFichaMovida(Ficha ultimaFichaMovida) {
        this.ultimaFichaMovida = ultimaFichaMovida;
    }

    /**
     * Obtiene al jugador actual que se encuentre en el turno
     * @return el jugador actual en el turno 
     */
    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    /**
     * Asigna al turno el jugador actual 
     * @param jugadorActual el jugador que se le asignara al turno 
     */
    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }
    
    /**
     * Metodo que reincia el turno a sus valores por defecto
     * para que otro jugador pueda iniciar su turno
     */
    public void reiniciarTurno(){
        lanzamientosPendientes = 1;
        lanzamientosRealizados = 0;
        movimientosPendientes = 0;
        ultimaFichaMovida = null;
    }
}
