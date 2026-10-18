package Dominio;

/**
 * Representa una casilla común o convencional en el tablero de juego.
 * A diferencia de las casillas especiales (como las de salida o seguras),
 * en las casillas normales las fichas pueden ser capturadas por fichas
 * de otros jugadores de acuerdo con las reglas de la partida.
 *
 * @author Equipo 2
 */
public class CasillaNormal extends Casilla {

    /**
     * Constructor para inicializar una casilla normal asignando un máximo de 2 fichas por defecto.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si inicia con barrera, {@code false} de lo contrario.
     */
    public CasillaNormal(Long idCasilla, int numeroCasilla, boolean hayBarrera) {
        super(idCasilla, 2, numeroCasilla, hayBarrera); // maxFichas fijado en 2
    }

    /**
     * Constructor por defecto para instanciar una casilla normal vacía.
     */
    public CasillaNormal() {
        super();
        this.setMaxFichas(2);
    }
}