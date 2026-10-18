package Dominio;

import ENUM.ColorJugador;

/**
 * Representa una casilla de salida en el tablero de juego.
 * Hereda de {@link Casilla} y está asociada a un color de jugador específico,
 * sirviendo como el punto inicial para las fichas de ese color al salir de la base.
 *
 * @author Equipo 2
 */
public class CasillaSalida extends Casilla {

    /** Color del jugador al que corresponde esta casilla de salida. */
    private ColorJugador colorAsociado;

    /**
     * Constructor completo para inicializar una casilla de salida con su ID,
     * los atributos de casilla base y su color asignado.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param maxFichas Límite de fichas permitidas en la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si la casilla inicia con barrera, {@code false} en caso contrario.
     * @param colorAsociado Color del jugador asociado a esta casilla de salida.
     */
    public CasillaSalida(Long idCasilla, int maxFichas, int numeroCasilla, boolean hayBarrera, ColorJugador colorAsociado) {
        super(idCasilla, maxFichas, numeroCasilla, hayBarrera);
        this.colorAsociado = colorAsociado;
    }

    /**
     * Constructor para crear una casilla de salida asignando únicamente su color asociado.
     *
     * @param colorAsociado Color del jugador al que pertenece la casilla de salida.
     */
    public CasillaSalida(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }

    /**
     * Obtiene el color del jugador asociado a la casilla de salida.
     *
     * @return El color asignado a la casilla.
     */
    public ColorJugador getColorAsociado() {
        return colorAsociado;
    }

    /**
     * Asigna o cambia el color del jugador asociado a la casilla de salida.
     *
     * @param colorAsociado Nuevo color a asignar.
     */
    public void setColorAsociado(ColorJugador colorAsociado) {
        this.colorAsociado = colorAsociado;
    }
}