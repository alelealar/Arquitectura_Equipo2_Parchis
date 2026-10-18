package ENUM;

/**
 * Define los estados generales del ciclo de vida de una partida de juego.
 * Permite gestionar la disponibilidad de la sala y controlar el flujo principal
 * del juego desde su creación hasta que concluye.
 *
 * @author Equipo 2
 */
public enum EstadoPartida {

    /**
     * La partida está creada y esperando a que se unan los jugadores o se confirme el inicio.
     */
    EN_ESPERA,

    /**
     * La partida se encuentra activa y los jugadores están realizando sus turnos.
     */
    EN_CURSO,

    /**
     * La partida ha concluido al determinarse un ganador o por cancelación.
     */
    FINALIZADA
}