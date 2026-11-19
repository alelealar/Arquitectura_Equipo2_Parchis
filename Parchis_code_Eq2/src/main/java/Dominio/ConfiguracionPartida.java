package Dominio;

/**
 * Representa los parámetros de configuración definidos por el anfitrión
 * antes de iniciar una partida de Parchís.
 *
 * @author Equipo 2
 */
public class ConfiguracionPartida {

    /** Cantidad de fichas asignadas a cada jugador (entre 1 y 4). */
    private int fichasPorJugador;

    /** Cantidad de casillas seguras distribuidas por cada ala del tablero. */
    private int casillasSegurasPorAla;

    /** Posición relativa de la casilla de salida dentro de cada ala del tablero. */
    private int numCasillaSalidaPorAla;

    /** Valor requerido en el dado para sacar una ficha desde la base. */
    private int valorParaSalirBase;

    /**
     * Constructor por defecto con las reglas estándar de Parchís.
     */
    public ConfiguracionPartida() {
        this.fichasPorJugador = 4;
        this.casillasSegurasPorAla = 4;
        this.numCasillaSalidaPorAla = 5;
        this.valorParaSalirBase = 5;
    }

    /**
     * Constructor completo para inicializar una configuración personalizada.
     *
     * @param fichasPorJugador Cantidad de fichas por jugador.
     * @param casillasSegurasPorAla Cantidad de casillas seguras por ala.
     * @param numCasillaSalidaPorAla Número de casilla de salida por ala.
     * @param valorParaSalirBase Valor del dado necesario para salir de la base.
     */
    public ConfiguracionPartida(int fichasPorJugador, int casillasSegurasPorAla, int numCasillaSalidaPorAla, int valorParaSalirBase) {
        this.fichasPorJugador = fichasPorJugador;
        this.casillasSegurasPorAla = casillasSegurasPorAla;
        this.numCasillaSalidaPorAla = numCasillaSalidaPorAla;
        this.valorParaSalirBase = valorParaSalirBase;
    }

    public int getFichasPorJugador() {
        return fichasPorJugador;
    }

    public void setFichasPorJugador(int fichasPorJugador) {
        this.fichasPorJugador = fichasPorJugador;
    }

    public int getCasillasSegurasPorAla() {
        return casillasSegurasPorAla;
    }

    public void setCasillasSegurasPorAla(int casillasSegurasPorAla) {
        this.casillasSegurasPorAla = casillasSegurasPorAla;
    }

    public int getNumCasillaSalidaPorAla() {
        return numCasillaSalidaPorAla;
    }

    public void setNumCasillaSalidaPorAla(int numCasillaSalidaPorAla) {
        this.numCasillaSalidaPorAla = numCasillaSalidaPorAla;
    }

    public int getValorParaSalirBase() {
        return valorParaSalirBase;
    }

    public void setValorParaSalirBase(int valorParaSalirBase) {
        this.valorParaSalirBase = valorParaSalirBase;
    }
}