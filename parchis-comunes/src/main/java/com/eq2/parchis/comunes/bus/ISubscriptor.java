package com.eq2.parchis.comunes.bus;

import com.eq2.parchis.comunes.eventos.Evento;

/**
 * Cualquier componente que quiera recibir eventos del bus
 * (ManejadorEventosServidor, ProxyServidor, ManejadorEventosCliente, ProxyCliente...).
 */
public interface ISubscriptor {

    void manejar(Evento evento);
}
