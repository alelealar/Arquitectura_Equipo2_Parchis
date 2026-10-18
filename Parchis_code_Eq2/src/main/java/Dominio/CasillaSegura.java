package Dominio;

/**
 * Representa una casilla especial de tipo seguro en el tablero de juego.
 * En estas casillas las fichas están protegidas, por lo que no pueden ser
 * capturadas por fichas de otros jugadores mientras permanezcan en ella.
 *
 * @author Equipo 2
 */
public class CasillaSegura extends Casilla {

    /**
     * Constructor completo para inicializar una casilla segura con su ID
     * y los atributos heredados de {@link Casilla}.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param maxFichas Límite de fichas permitidas en la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si la casilla inicia con barrera, {@code false} en caso contrario.
     */
    public CasillaSegura(Long idCasilla, int maxFichas, int numeroCasilla, boolean hayBarrera) {
        super(idCasilla, maxFichas, numeroCasilla, hayBarrera);
    }

    /**
     * Constructor por defecto para instanciar una casilla segura vacía.
     */
    public CasillaSegura() {
        super();
    }
}