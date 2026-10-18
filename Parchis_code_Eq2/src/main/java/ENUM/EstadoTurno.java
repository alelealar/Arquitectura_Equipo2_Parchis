package ENUM;

/**
 * Representa los distintos estados en los que se puede encontrar el turno
 * de un jugador durante el desarrollo de una partida.
 * Permite controlar el flujo del juego y validar las acciones permitidas
 * en cada fase del turno.
 * 
 * @author Equipo 2
 */
public enum EstadoTurno {

    /**
     * El jugador se encuentra a la espera de lanzar el dado para iniciar sus acciones.
     */
    ESPERANDO_DADO,

    /**
     * El dado ya fue lanzado y el jugador debe elegir la ficha que desea mover.
     */
    SELECCIONANDO_FICHA,

    /**
     * Se está realizando o procesando el movimiento de la ficha seleccionada en el tablero.
     */
    EN_MOVIMIENTO,

    /**
     * El jugador ha concluido sus acciones permitidas y el turno ha finalizado.
     */
    TURNO_FINALIZADO
}