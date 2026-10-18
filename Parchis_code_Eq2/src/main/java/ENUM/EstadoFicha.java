package ENUM;

/**
 * Define los posibles estados en los que puede encontrarse una ficha durante
 * el desarrollo de una partida de Parchís.
 * Sirve para determinar las reglas de desplazamiento y validar las acciones
 * permitidas para cada ficha según su posición actual.
 *
 * @author Equipo 2
 */
public enum EstadoFicha {

    /**
     * La ficha permanece en la cárcel/casa inicial a la espera de un lanzamiento que le permita salir.
     */
    EN_BASE,

    /**
     * La ficha está activa y desplazándose por el circuito de casillas comunes del tablero.
     */
    EN_TABLERO,

    /**
     * La ficha ha ingresado a la recta final o pasillo de seguridad exclusivo de su color.
     */
    EN_PASILLO,

    /**
     * La ficha ha alcanzado el centro del tablero (meta) y completado su recorrido.
     */
    EN_META
}