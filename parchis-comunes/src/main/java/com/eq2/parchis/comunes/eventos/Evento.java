package com.eq2.parchis.comunes.eventos;

import java.io.Serializable;

/**
 * Clase base de TODOS los eventos que viajan por el bus y por la red.
 *
 * <p>Cada evento concreto (SolicitarLanzarDadoEvent, DadoLanzadoEvent, ...)
 * extiende esta clase y solo contiene datos simples o DTOs, nunca objetos
 * del dominio del servidor.</p>
 *
 * <ul>
 *   <li>{@code idConexionOrigen}: lo asigna el ProxyServidor al recibir el
 *       evento, para saber qué cliente lo envió. El cliente no lo llena.</li>
 *   <li>{@code idConexionDestino}: lo asigna el servidor al publicar una
 *       respuesta. {@code null} = se envía a todos los clientes conectados.</li>
 * </ul>
 */
public abstract class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idConexionOrigen;
    private Long idConexionDestino;

    public Long getIdConexionOrigen() {
        return idConexionOrigen;
    }

    public void setIdConexionOrigen(Long idConexionOrigen) {
        this.idConexionOrigen = idConexionOrigen;
    }

    public Long getIdConexionDestino() {
        return idConexionDestino;
    }

    public void setIdConexionDestino(Long idConexionDestino) {
        this.idConexionDestino = idConexionDestino;
    }

    /** @return true si el evento debe enviarse a todos los clientes. */
    public boolean esParaTodos() {
        return idConexionDestino == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[origen=" + idConexionOrigen + ", destino=" + idConexionDestino + "]";
    }
}
