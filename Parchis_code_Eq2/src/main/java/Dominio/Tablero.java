package Dominio;

import ENUM.EstadoFicha;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero principal del juego de Parchís.
 * Mantiene la colección de casillas que componen el circuito y gestiona
 * la lógica de desplazamientos, detección de barreras, rebotes en la meta,
 * incorporación de fichas desde la base y evaluación de casillas de destino.
 *
 * @author Equipo 2
 */
public class Tablero {

    /** Identificador único del tablero en la base de datos. */
    private Long idTablero;

    /** Número obtenido en el dado requerido para sacar una ficha de la base. */
    private int numeroSalidaBase;

    /** Colección de casillas que forman la estructura completa del tablero. */
    private List<Casilla> casillas;

    /**
     * Constructor por defecto. Inicializa la lista de casillas y establece el
     * número de salida de base en 5 por defecto.
     */
    public Tablero() {
        this.casillas = new ArrayList<>();
        this.numeroSalidaBase = 5;
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
        this.numeroSalidaBase = 5;
    }

    /**
     * Obtiene el identificador único del tablero.
     *
     * @return ID del tablero.
     */
    public Long getIdTablero() {
        return idTablero;
    }

    /**
     * Asigna o actualiza el identificador único del tablero.
     *
     * @param idTablero Nuevo ID a establecer.
     */
    public void setIdTablero(Long idTablero) {
        this.idTablero = idTablero;
    }

    /**
     * Obtiene la lista de casillas que conforman el circuito del tablero.
     *
     * @return Lista de objetos {@link Casilla}.
     */
    public List<Casilla> getCasillas() {
        return casillas;
    }

    /**
     * Asigna o actualiza la lista de casillas del tablero.
     *
     * @param casillas Nueva lista de casillas a establecer.
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
     * Define el número necesario en el dado para incorporar una ficha al tablero.
     *
     * @param numeroSalidaBase Valor configurado para salir de la base.
     */
    public void setNumeroSalidaBase(int numeroSalidaBase) {
        this.numeroSalidaBase = numeroSalidaBase;
    }

    /**
     * Revisa casilla por casilla en el recorrido para asegurar que no exista ninguna barrera activa
     * en las casillas intermedias del avance.
     *
     * @param ficha Ficha que realizará el movimiento.
     * @param valorDado Cantidad de posiciones a avanzar según el dado.
     * @return {@code true} si la trayectoria está libre, {@code false} si se encuentra un bloqueo.
     */
    public boolean verificarTrayectoriaLibre(Ficha ficha, int valorDado) {
        if (casillas.isEmpty() || ficha.getCasillaActual() == null) {
            return true;
        }

        int posicionActual = casillas.indexOf(ficha.getCasillaActual());

        for (int i = 1; i < valorDado; i++) {
            int siguientePosicion = (posicionActual + i) % casillas.size();
            Casilla casillaIntermedia = casillas.get(siguientePosicion);

            if (casillaIntermedia.isHayBarrera()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Coordina el avance general de la ficha comprobando previamente que la trayectoria esté libre.
     *
     * @param ficha Ficha que se desea desplazar.
     * @param valorDado Cantidad de casillas a mover.
     */
    public void procesarAvance(Ficha ficha, int valorDado) {
        if (verificarTrayectoriaLibre(ficha, valorDado)) {
            moverFicha(ficha, valorDado);
        } else {
            System.out.println("El movimiento no es válido: hay una barrera bloqueando el paso.");
        }
    }

    /**
     * Aplica la regla de rebote cuando una ficha en el pasillo final supera las casillas exactas para la meta.
     *
     * @param ficha Ficha en el pasillo de meta.
     * @param valorDado Puntos del dado a aplicar.
     */
    public void aplicarMovimientoConRebote(Ficha ficha, int valorDado) {
        if (ficha.getEstado() == EstadoFicha.EN_PASILLO) {
            int limiteMeta = 7;
            int pasosFaltantes = limiteMeta - ficha.getPasosDados();

            if (valorDado > pasosFaltantes) {
                int exceso = valorDado - pasosFaltantes;
                int nuevaPosicion = limiteMeta - exceso;
                ficha.setPasosDados(nuevaPosicion);
            } else if (valorDado == pasosFaltantes) {
                ficha.setPasosDados(limiteMeta);
                moverAMeta(ficha);
            } else {
                ficha.setPasosDados(ficha.getPasosDados() + valorDado);
            }
        }
    }

    /**
     * Desplaza la ficha a lo largo del juego según su estado actual (EN_BASE, EN_TABLERO o EN_PASILLO).
     *
     * @param ficha Ficha que realizará el movimiento.
     * @param pasosAvanzar Cantidad de posiciones a desplazar.
     */
    public void moverFicha(Ficha ficha, int pasosAvanzar) {
        if (ficha == null || ficha.getEstado() == EstadoFicha.EN_META) {
            return;
        }

        switch (ficha.getEstado()) {
            case EN_BASE:
                if (pasosAvanzar == numeroSalidaBase) {
                    incorporarFichaAlTablero(ficha);
                }
                break;

            case EN_TABLERO:
                Casilla casillaDestino = calcularPosicion(ficha, pasosAvanzar);
                if (casillaDestino != null) {
                    actualizarPosicion(ficha, casillaDestino);
                    evaluarCasillaDestino(casillaDestino, ficha);
                }
                break;

            case EN_PASILLO:
                aplicarMovimientoConRebote(ficha, pasosAvanzar);
                break;

            default:
                break;
        }
    }

    /**
     * Saca la ficha de la base y la ubica en la casilla de salida correspondiente,
     * actualizando su estado a EN_TABLERO.
     *
     * @param ficha Ficha que abandona la base.
     */
    public void incorporarFichaAlTablero(Ficha ficha) {
        if (ficha == null) return;
        CasillaSalida casillaSalida = obtenerCasillaSalida(ficha);

        if (casillaSalida != null && verificarCasillaSalida(ficha)) {
            actualizarPosicion(ficha, casillaSalida);
            ficha.cambiarEstado(EstadoFicha.EN_TABLERO);
            casillaSalida.getFichas().add(ficha);
        }
    }

    /**
     * Registra el arribo de una ficha a la casilla de meta, cambiando su estado
     * y removiendo su asignación de casilla.
     *
     * @param fichaLlegada Ficha que alcanzó la meta.
     */
    public void moverAMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null) return;
        fichaLlegada.cambiarEstado(EstadoFicha.EN_META);
        fichaLlegada.cambiarPosicion(null);
    }

    /**
     * Envía la ficha indicada de regreso a la base inicial y reinicia su conteo de pasos.
     *
     * @param ficha Ficha que retornará a la base.
     */
    public void regresarFichaBase(Ficha ficha) {
        if (ficha == null) return;
        ficha.cambiarEstado(EstadoFicha.EN_BASE);
        ficha.cambiarPosicion(null);
        ficha.setPasosDados(0);
    }

    /**
     * Evalúa las reglas del juego en la casilla de llegada (capturas de fichas enemigas,
     * formación de barreras o ingreso a la meta).
     *
     * @param casillaDestino Casilla a la que llegó la ficha.
     * @param fichaMoviendose Ficha que realizó el desplazamiento.
     */
    public void evaluarCasillaDestino(Casilla casillaDestino, Ficha fichaMoviendose) {
        if (casillaDestino == null || fichaMoviendose == null) return;

        List<Ficha> fichasEnCasilla = casillaDestino.getFichas();

        if (casillaDestino instanceof CasillaMeta) {
            moverAMeta(fichaMoviendose);
            fichasEnCasilla.add(fichaMoviendose);
            return;
        }

        if (casillaDestino instanceof CasillaSegura || casillaDestino instanceof CasillaSalida) {
            if (!fichasEnCasilla.isEmpty()) {
                casillaDestino.setHayBarrera(true);
            }
            fichasEnCasilla.add(fichaMoviendose);
            return;
        }

        if (casillaDestino instanceof CasillaNormal) {
            if (fichasEnCasilla.isEmpty()) {
                fichasEnCasilla.add(fichaMoviendose);
                return;
            }

            Ficha fichaExistente = fichasEnCasilla.get(0);
            if (!fichaExistente.getIdFicha().equals(fichaMoviendose.getIdFicha())) {
                regresarFichaBase(fichaExistente);
                fichasEnCasilla.remove(fichaExistente);
                fichasEnCasilla.add(fichaMoviendose);
            } else {
                fichasEnCasilla.add(fichaMoviendose);
                casillaDestino.setHayBarrera(true);
            }
        }
    }

    /**
     * Actualiza la casilla asignada a la ficha e incrementa en uno el conteo de pasos recorridos.
     *
     * @param ficha Ficha a actualizar.
     * @param nuevaCasilla Casilla de destino asignada.
     */
    public void actualizarPosicion(Ficha ficha, Casilla nuevaCasilla) {
        if (ficha == null) return;
        ficha.cambiarPosicion(nuevaCasilla);
        ficha.setPasosDados(ficha.getPasosDados() + 1);
    }

    /**
     * Verifica si la casilla de salida correspondiente a la ficha está libre de barreras
     * para permitir su egreso desde la base.
     *
     * @param ficha Ficha que requiere salir de la base.
     * @return {@code true} si la casilla de salida no está bloqueada por una barrera, {@code false} de lo contrario.
     */
    public boolean verificarCasillaSalida(Ficha ficha) {
        CasillaSalida casillaSalida = obtenerCasillaSalida(ficha);
        if (casillaSalida == null) return false;
        return !casillaSalida.isHayBarrera();
    }

    /**
     * Calcula la casilla de destino correspondiente a partir de la posición actual de la ficha y los pasos a avanzar.
     *
     * @param ficha Ficha que se desplazará.
     * @param pasosAvanzar Puntos del dado a recorrer.
     * @return La objeto {@link Casilla} de destino calculada, o {@code null} si los parámetros son inválidos.
     */
    public Casilla calcularPosicion(Ficha ficha, int pasosAvanzar) {
        if (ficha == null || ficha.getCasillaActual() == null || casillas.isEmpty()) {
            return null;
        }
        int posicionActual = casillas.indexOf(ficha.getCasillaActual());
        int nuevaPosicion = (posicionActual + pasosAvanzar) % casillas.size();
        return casillas.get(nuevaPosicion);
    }


    /**
     * Metodo auxiliar privado que busca la casilla de salida en el tablero.
     *
     * @param ficha Ficha a ubicar.
     * @return La {@link CasillaSalida} encontrada, o {@code null} si no existe.
     */
    private CasillaSalida obtenerCasillaSalida(Ficha ficha) {
        for (Casilla casilla : casillas) {
            if (casilla instanceof CasillaSalida) {
                return (CasillaSalida) casilla;
            }
        }
        return null;
    }
}