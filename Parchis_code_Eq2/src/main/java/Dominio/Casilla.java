package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una casilla individual dentro del tablero de juego.
 * Guarda la información referente a su identificación, su posición en el tablero,
 * el límite de fichas que se pueden colocar sobre ella, si existe una barrera activa
 * y la colección de fichas actualmente ubicadas sobre ella.
 *
 * @author Equipo 2
 */
public class Casilla {

    /** Identificador único de la casilla en la base de datos. */
    private Long idCasilla;

    /** Cantidad máxima de fichas que pueden ocupar la casilla al mismo tiempo. */
    private int maxFichas;

    /** Número o posición asignada a la casilla dentro del tablero. */
    private int numeroCasilla;

    /** Indica si actualmente hay una barrera formada en la casilla. */
    private boolean hayBarrera;

    /** Colección de fichas que se encuentran actualmente ocupando la casilla. */
    private List<Ficha> fichas = new ArrayList<>();

    /**
     * Constructor completo para inicializar una casilla con su ID y todos sus atributos.
     *
     * @param idCasilla Identificador único de la casilla.
     * @param maxFichas Límite de fichas permitidas en la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si la casilla inicia con barrera, {@code false} en caso contrario.
     */
    public Casilla(Long idCasilla, int maxFichas, int numeroCasilla, boolean hayBarrera) {
        this.idCasilla = idCasilla;
        this.maxFichas = maxFichas;
        this.numeroCasilla = numeroCasilla;
        this.hayBarrera = hayBarrera;
    }

    /**
     * Constructor para crear una casilla sin especificar un ID previa asignación o persistencia.
     *
     * @param maxFichas Límite de fichas permitidas en la casilla.
     * @param numeroCasilla Número o posición de la casilla en el tablero.
     * @param hayBarrera {@code true} si tiene barrera activa, {@code false} de lo contrario.
     */
    public Casilla(int maxFichas, int numeroCasilla, boolean hayBarrera) {
        this.maxFichas = maxFichas;
        this.numeroCasilla = numeroCasilla;
        this.hayBarrera = hayBarrera;
    }

    /**
     * Constructor por defecto para instanciar una casilla vacía.
     */
    public Casilla() {
    }

    /**
     * Obtiene el identificador único de la casilla.
     *
     * @return ID de la casilla.
     */
    public Long getIdCasilla() {
        return idCasilla;
    }

    /**
     * Asigna o actualiza el identificador único de la casilla.
     *
     * @param idCasilla Nuevo ID de la casilla.
     */
    public void setIdCasilla(Long idCasilla) {
        this.idCasilla = idCasilla;
    }

    /**
     * Obtiene la capacidad máxima de fichas que soporta la casilla.
     *
     * @return El número máximo de fichas permitidas.
     */
    public int getMaxFichas() {
        return maxFichas;
    }

    /**
     * Define el límite máximo de fichas que pueden estar en la casilla.
     *
     * @param maxFichas Cantidad máxima de fichas.
     */
    public void setMaxFichas(int maxFichas) {
        this.maxFichas = maxFichas;
    }

    /**
     * Obtiene el número de posición de la casilla en el tablero.
     *
     * @return El número de la casilla.
     */
    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    /**
     * Establece el número de casilla en el tablero.
     *
     * @param numeroCasilla Posición o número a asignar.
     */
    public void setNumeroCasilla(int numeroCasilla) {
        this.numeroCasilla = numeroCasilla;
    }

    /**
     * Indica si la casilla tiene una barrera activa actualmente.
     *
     * @return {@code true} si hay una barrera, {@code false} si no.
     */
    public boolean isHayBarrera() {
        return hayBarrera;
    }

    /**
     * Modifica el estado de barrera en la casilla.
     *
     * @param hayBarrera {@code true} para activar la barrera, {@code false} para desactivarla.
     */
    public void setHayBarrera(boolean hayBarrera) {
        this.hayBarrera = hayBarrera;
    }

    /**
     * Obtiene la lista de fichas que se encuentran actualmente en la casilla.
     *
     * @return Lista de objetos {@link Ficha}.
     */
    public List<Ficha> getFichas() {
        return fichas;
    }

    /**
     * Asigna o actualiza la lista de fichas presentes en la casilla.
     *
     * @param fichas Nueva lista de fichas.
     */
    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }
}