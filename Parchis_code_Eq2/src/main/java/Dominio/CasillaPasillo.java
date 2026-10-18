package Dominio;

import ENUM.ColorJugador;

/**
 * Representa una casilla perteneciente al pasillo o recta final del tablero.
 * Hereda de {@link Casilla} y está asociada a un color de jugador específico,
 * permitiendo el tránsito únicamente a las fichas que coincidan con dicho color.
 *
 * @author Equipo 2
 */
public class CasillaPasillo extends Casilla {

    /** Color del jugador al que está reservada esta casilla del pasillo. */
    private ColorJugador colorAsociado;

    /**
     * Constructor completo para inicializar una casilla de pasillo con su ID,
     * los atributos heredados de {@link Casilla} y su color asignado.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param maxFichas Límite de fichas permitidas en la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si la casilla inicia con barrera, {@code false} en caso contrario.
     * @param colorAsociado Color del jugador al que pertenece esta casilla del pasillo.
     */
    public CasillaPasillo(Long idCasilla, int maxFichas, int numeroCasilla, boolean hayBarrera, ColorJugador colorAsociado) {
        super(idCasilla, maxFichas, numeroCasilla, hayBarrera);
        this.colorAsociado = colorAsociado;
    }

    /**
     * Constructor para instanciar una casilla de pasillo asignando únicamente su color asociado.
     *
     * @param colorAsociado Color del jugador al que pertenece la casilla.
     */
    public CasillaPasillo(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }

    /**
     * Constructor por defecto para instanciar una casilla de pasillo vacía.
     */
    public CasillaPasillo() {
        super();
    }

    /**
     * Obtiene el color del jugador asociado a esta casilla del pasillo.
     *
     * @return El color asignado a la casilla.
     */
    public ColorJugador getColorAsociado() {
        return colorAsociado;
    }

    /**
     * Asigna o modifica el color del jugador asociado a esta casilla del pasillo.
     *
     * @param colorAsociado Nuevo color a asignar.
     */
    public void setColorAsociado(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }
}