package Dominio;

import ENUM.ColorJugador;

/**
 * Representa la casilla de meta o centro del tablero.
 * Hereda de {@link Casilla} y está asociada a un color de jugador específico,
 * siendo el objetivo final al que deben ingresar todas sus fichas para ganar la partida.
 *
 * @author Equipo 2
 */
public class CasillaMeta extends Casilla {

    /** Color del jugador al que pertenece esta casilla de meta. */
    private ColorJugador colorAsociado;

    /**
     * Constructor completo para inicializar la meta asignando capacidad para todas las fichas.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param numeroCasilla Posición de la casilla en el tablero.
     * @param colorAsociado Color del jugador asignado a esta meta.
     * @param maxFichas Límite máximo de fichas permitidas en la meta (ej. 4).
     */
    public CasillaMeta(Long idCasilla, int numeroCasilla, ColorJugador colorAsociado, int maxFichas) {
        super(idCasilla, maxFichas, numeroCasilla, false);
        this.colorAsociado = colorAsociado;
    }

    /**
     * Constructor para instanciar una casilla de meta asociando solo el color.
     *
     * @param colorAsociado Color del jugador al que pertenece la meta.
     */
    public CasillaMeta(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }

    /**
     * Constructor por defecto para instanciar una casilla de meta vacía.
     */
    public CasillaMeta() {
        super();
    }

    /**
     * Obtiene el color del jugador asociado a esta casilla de meta.
     *
     * @return El color asignado a la casilla.
     */
    public ColorJugador getColorAsociado() {
        return colorAsociado;
    }

    /**
     * Asigna o modifica el color del jugador asociado a la casilla de meta.
     *
     * @param colorAsociado Nuevo color a asignar.
     */
    public void setColorAsociado(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }
}