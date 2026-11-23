package com.eq2.parchis.comunes.bus;

import com.eq2.parchis.comunes.eventos.Evento;

/**
 * Contrato del bus de eventos. Lo implementan BusEventosServidor y BusEventosCliente.
 *
 * <p>La suscripción es POR TIPO de evento: un subscriptor solo recibe los
 * eventos de las clases a las que se suscribió (y sus subclases). Así el
 * ProxyServidor no recibe de vuelta las solicitudes que él mismo publica.</p>
 */
public interface IBusEventos {

    void suscribir(Class<? extends Evento> tipo, ISubscriptor subscriptor);

    void desuscribir(Class<? extends Evento> tipo, ISubscriptor subscriptor);

    void publicar(Evento evento);
}
