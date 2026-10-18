package Dominio;

import ENUM.EstadoFicha;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero principal del juego de Parchís.
 * Mantiene la lista de casillas que componen el circuito y gestiona
 * la lógica de trayectorias, movimientos, rebotes en meta y capturas de fichas.
 *
 * @author Equipo 2
 */
public class Tablero {

    /** Identificador único del tablero. */
    private Long idTablero;

    /** Número obtenido en el dado requerido para poder sacar una ficha de la base. */
    private int numeroSalidaBase;

    /** Colección de casillas que forman la estructura completa del tablero. */
    private List<Casilla> casillas;


    /**
     * Constructor por defecto. Inicializa la lista de casillas del tablero.
     */
    public Tablero() {
        this.casillas = new ArrayList<>();
    }

    /**
     * Constructor completo para inicializar el tablero con su ID y sus casillas.
     *
     * @param idTablero Identificador único del tablero.
     * @param casillas Lista de casillas que componen el tablero.
     */
    public Tablero(Long idTablero, List<Casilla> casillas) {
        this.idTablero = idTablero;
        this.casillas = casillas;
    }


    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    /**
     * Obtiene el identificador único del tablero.
     *
     * @return ID del tablero.
     */
    public Long getIdTablero() {
        return idTablero;
    }

    /**
     * Asigna o modifica el identificador del tablero.
     *
     * @param idTablero Nuevo ID a establecer.
     */
    public void setIdTablero(Long idTablero) {
        this.idTablero = idTablero;
    }

    /**
     * Obtiene la lista de casillas que componen el tablero.
     *
     * @return Lista de casillas.
     */
    public List<Casilla> getCasillas() {
        return casillas;
    }

    /**
     * Asigna la lista de casillas que conforman el tablero.
     *
     * @param casillas Nueva lista de casillas.
     */
    public void setCasillas(List<Casilla> casillas) {
        this.casillas = casillas;
    }

    /**
     * Obtiene el valor del dado requerido para sacar una ficha de la base.
     *
     * @return Número de salida de base.
     */
    public int getNumeroSalidaBase() {
        return numeroSalidaBase;
    }

    /**
     * Define el número necesario en el dado para sacar una ficha de la base.
     *
     * @param numeroSalidaBase Valor configurado para salir de base.
     */
    public void setNumeroSalidaBase(int numeroSalidaBase) {
        this.numeroSalidaBase = numeroSalidaBase;
    }

    // =========================================================================
    // MÉTODOS DE LA CLASE
    // =========================================================================

    /**
     * Revisa casilla por casilla en el recorrido para asegurar que no haya ninguna barrera activa.
     *
     * @param ficha Ficha que se moverá.
     * @param valorDado Cantidad de casillas a consultar hacia adelante.
     * @return {@code true} si todas las casillas intermedias están libres, {@code false} si hay barrera.
     */
    public boolean verificarTrayectoriaLibre(Ficha ficha, int valorDado) {
        if (casillas.isEmpty() || ficha.getCasillaActual() == null) {
            return true;
        }

        int posicionActual = casillas.indexOf(ficha.getCasillaActual()); //guardamos la posicion actial de la ficha

        //revisando casilla por casilla
        for (int i = 1; i < valorDado; i++) { //ignoramos la casilla actual (0) y pasamos por cada casilla siguientes
            int siguientePosicion = posicionActual + i;

            // Si la posición se pasa del tamaño total del tablero, le restamos el total para volver a empezar desde 0
            if (siguientePosicion >= casillas.size()) {
                siguientePosicion = siguientePosicion - casillas.size();
            }
            Casilla casillaIntermedia = casillas.get(siguientePosicion);

            if (casillaIntermedia.isHayBarrera()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metodo principal que coordina el turno de avance de una ficha.
     * Verifica que el camino esté libre antes de mover la ficha.
     *
     * @param ficha Ficha que se desea mover.
     * @param valorDado Puntos obtenidos en el dado.
     */
    public void procesarAvance(Ficha ficha, int valorDado) {
        if (verificarTrayectoriaLibre(ficha, valorDado)) {
            moverFicha(ficha, valorDado);
        } else {
            System.out.println("El movimiento no es válido: hay una barrera bloqueando el paso.");
        }
    }

    /**
     * Aplica la regla de rebote cuando una ficha en el pasillo supera las casillas necesarias para la meta.
     *
     * @param ficha Ficha en movimiento.
     * @param valorDado Puntos del dado.
     */
    public void aplicarMovimientoConRebote(Ficha ficha, int valorDado) {
        if (ficha.getEstado() == EstadoFicha.EN_PASILLO) {
            int limiteMeta = 7; //NUMERO DE CASILLAS DE PASILLO
            int pasosFaltantes = limiteMeta - ficha.getPasosDados();

            if (valorDado > pasosFaltantes) { //aqui es cuando aplica el rebote
                int exceso = valorDado - pasosFaltantes; //calcula la cantidad de pasos que se paso
                int nuevaPosicion = limiteMeta - exceso; //se los resta al limite(rebote)
                ficha.setPasosDados(nuevaPosicion); //actualizas los pasos dados

            } else if (valorDado == pasosFaltantes) { //si da exacto el numero a la meta
                ficha.setPasosDados(limiteMeta);
                moverAMeta(ficha);

            } else {
                ficha.setPasosDados(ficha.getPasosDados() + valorDado);
            }
        }
    }

    /**
     * Calcula la nueva posición de la ficha en la lista de casillas y realiza el avance
     * según su estado actual (BASE, TABLERO, PASILLO).
     *
     * @param ficha Ficha a desplazar.
     * @param pasosAvanzar Puntos a recorrer obtenidos del dado.
     */
        public void moverFicha(Ficha ficha, int pasosAvanzar) {
        if (ficha == null || ficha.getEstado() == EstadoFicha.EN_META) {
            System.out.println("La ficha ya se encuentra en la meta o es nula.");
            return;
        }

        switch (ficha.getEstado()) {
            case EN_BASE:
                if (pasosAvanzar == numeroSalidaBase) {
                    salirDeBase(ficha);
                } else {
                    System.out.println("Se requiere un " + numeroSalidaBase + " en el dado para sacar una ficha de la base.");
                }
                break;

            case EN_TABLERO:
                avanzarEnTablero(ficha, pasosAvanzar);
                break;

            case EN_PASILLO:
                aplicarMovimientoConRebote(ficha, pasosAvanzar);
                break;

            default:
                break;
        }
    }

    /**
     * Marca la ficha con el estado EN_META.
     *
     * @param fichaLlegada Ficha que alcanza la meta.
     */
    public void moverAMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null) return;

        fichaLlegada.cambiarEstado(EstadoFicha.EN_META);
        fichaLlegada.cambiarPosicion(null);
    }

    /**
     * Devuelve la ficha a la base y reinicia sus contadores.
     *
     * @param ficha Ficha capturada.
     */
    public void regresarFichaBase(Ficha ficha) {
        ficha.cambiarEstado(EstadoFicha.EN_BASE);
        ficha.cambiarPosicion(null);
        ficha.setPasosDados(0);
    }

    /**
     * Evalúa las reglas del juego en la casilla de destino (capturas, barreras o meta).
     *
     * @param casillaDestino Casilla a la que llegó la ficha.
     * @param fichaMoviendose Ficha que realizó el avance.
     */
    public void evaluarCasillaDestino(Casilla casillaDestino, Ficha fichaMoviendose) {
        if (casillaDestino == null || fichaMoviendose == null) {
            return;
        }

        // Obtiene las fichas que se encuentran actualmente en la casilla
        List<Ficha> fichasEnCasilla = casillaDestino.getFichas();

        // CASO 1: Llegada a la Meta
        if (casillaDestino instanceof CasillaMeta) {
            moverAMeta(fichaMoviendose); // Se cambia el estado de la ficha a EN_META
            fichasEnCasilla.add(fichaMoviendose); // Se agrega la ficha a la casilla de meta
            System.out.println("¡La ficha ha alcanzado la meta!");
            return;
        }

        // CASO 2: Casilla Segura o Casilla de Salida (No se capturan fichas)
        if (casillaDestino instanceof CasillaSegura || casillaDestino instanceof CasillaSalida) {
            if (!fichasEnCasilla.isEmpty()) {
                casillaDestino.setHayBarrera(true); // Cambiar el estado de la casilla evaluada a que hay barrera
            }
            fichasEnCasilla.add(fichaMoviendose); // Agregamos la ficha a la lista de fichas de la casilla
            return;
        }

        // CASO 3: Casilla Normal
        if (casillaDestino instanceof CasillaNormal) {
            if (fichasEnCasilla.isEmpty()) {
                fichasEnCasilla.add(fichaMoviendose); // Si no hay fichas en la casilla, la agregamos directamente
                return;
            }

            Ficha fichaExistente = fichasEnCasilla.get(0); // Ficha que ya estaba en la casilla de destino

            if (esFichaEnemiga(fichaMoviendose, fichaExistente)) { // Si la ficha que está en el destino es enemiga
                regresarFichaBase(fichaExistente); // Nos comemos la ficha
                fichasEnCasilla.remove(fichaExistente); // Quitamos la ficha que nos comimos de la casilla

                fichasEnCasilla.add(fichaMoviendose); // Agregamos la ficha en turno a la casilla
                System.out.println("¡Se ha capturado una ficha enemiga! Regresa a la base.");
            } else {
                // Cuando la ficha existente es tuya
                fichasEnCasilla.add(fichaMoviendose);
                casillaDestino.setHayBarrera(true); // Aplicamos la barrera
                System.out.println("Se ha formado una barrera con tu ficha aliada.");
            }
        }
    }

    /**
     * Actualiza la casilla de la ficha e incrementa sus pasos recorridos.
     *
     * @param ficha Ficha a actualizar.
     * @param nuevaCasilla Casilla asignada.
     */
    public void actualizarPosicion(Ficha ficha, Casilla nuevaCasilla) {
        ficha.cambiarPosicion(nuevaCasilla);
        ficha.setPasosDados(ficha.getPasosDados() + 1);
    }



    // =========================================================================
    // MÉTODOS AUXILIARES
    // =========================================================================

    /**
     * Coloca la ficha en la casilla de salida y cambia su estado a EN_TABLERO.
     *
     * @param ficha Ficha que sale de la base.
     */
    private void salirDeBase(Ficha ficha) {
        Casilla casillaSalida = obtenerCasillaSalida(ficha);

        if (casillaSalida != null) {
            actualizarPosicion(ficha, casillaSalida);
            ficha.cambiarEstado(EstadoFicha.EN_TABLERO);
        }
    }

    /**
     * Maneja el desplazamiento estándar de la ficha en el circuito común del tablero.
     *
     * @param ficha Ficha a avanzar.
     * @param casillasAAvanzar Pasos a recorrer.
     */
    private void avanzarEnTablero(Ficha ficha, int casillasAAvanzar) {
        int posicionActual = casillas.indexOf(ficha.getCasillaActual()); //sacamos la posicion de la ficha
        int totalCasillas = casillas.size(); // tomamos la cantidad de casillas en el tablero

        int limiteCircuitoGeneral = 68;
        int nuevosPasosTotales = ficha.getPasosDados() + casillasAAvanzar; //guardamos el avance70

        //para doblar al pasillo
        if (nuevosPasosTotales > limiteCircuitoGeneral) { //verificamos que los pasos no se pasen del limite del circuito
            int pasosRestantesParaPasillo = nuevosPasosTotales - limiteCircuitoGeneral; //tomamos el valor que avanza la ficha en el pasillo meta

            ficha.cambiarEstado(EstadoFicha.EN_PASILLO);
            ficha.setPasosDados(0);

            aplicarMovimientoConRebote(ficha, pasosRestantesParaPasillo);

        //Evala que la ficha se mantenga en la pista comun
        //como no todas las fichas empiezan de la posicion 0, si no evaluamos esto arrojara indexOutOfBound
        } else {

            int nuevaPosicion = posicionActual + casillasAAvanzar; // guardamos la ppsicion nueva
            if (nuevaPosicion >= totalCasillas) { //si la posicion nueva sobrepasa la cantidad de casillas en el tablero
                nuevaPosicion = nuevaPosicion - totalCasillas; //
            }
            Casilla casillaDestino = casillas.get(nuevaPosicion);

            actualizarPosicion(ficha, casillaDestino);
            evaluarCasillaDestino(casillaDestino, ficha);
        }
    }

    /**
     * Busca la casilla de salida correspondiente en el tablero.
     *
     * @param ficha Ficha que requiere la casilla de salida.
     * @return La primera {@link CasillaSalida} encontrada.
     */
    private CasillaSalida obtenerCasillaSalida(Ficha ficha) {
        for (Casilla casilla : casillas) {
            if (casilla instanceof CasillaSalida) {
                return (CasillaSalida) casilla;
            }
        }
        return null;
    }


    /**
     * Comprueba si dos fichas pertenecen a jugadores distintos.
     *
     * @param f1 Primera ficha a comparar.
     * @param f2 Segunda ficha a comparar.
     * @return {@code true} si corresponden a jugadores diferentes.
     */
    private boolean esFichaEnemiga(Ficha f1, Ficha f2) {
        if (f1.getIdFicha() == null || f2.getIdFicha() == null) return false;
        return !f1.getIdFicha().equals(f2.getIdFicha());
    }
}